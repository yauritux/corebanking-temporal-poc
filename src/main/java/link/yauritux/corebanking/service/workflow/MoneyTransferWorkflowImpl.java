package link.yauritux.corebanking.service.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import link.yauritux.corebanking.service.activity.AccountActivity;
import link.yauritux.corebanking.service.dto.TransferDetails;
import link.yauritux.corebanking.shared.exceptions.BadRequestException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {

    // RetryOptions specify how to automatically handle retries when Activities fail
    private final RetryOptions noRetryOptions = RetryOptions.newBuilder()
            .setMaximumAttempts(1) // no retry
            .build();
    private final RetryOptions retryOptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(2)) // Wait 1 second before first retry
            .setMaximumInterval(Duration.ofSeconds(20)) // Do not exceed 20 seconds between retries
            .setBackoffCoefficient(2) // Wait 2 second, then 4, then 6, etc
            .setMaximumAttempts(3) // Fail after 3 attempts
            .build();

    // ActivityOptions specify the limits on how long an Activity can execute before
    // being interrupted by the Orchestration service
    private final ActivityOptions noRetryActivityOptions = ActivityOptions.newBuilder()
            .setRetryOptions(noRetryOptions)
            .setStartToCloseTimeout(Duration.ofSeconds(2)) // Max execution time for single Activity
            .setScheduleToCloseTimeout(Duration.ofSeconds(5000)) // Entire duration from scheduling to completion including queue time
            .build();
    private final ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
            .setRetryOptions(retryOptions) // Apply the RetryOptions defined above
            .setStartToCloseTimeout(Duration.ofSeconds(2)) // Max execution time for single Activity
            .setScheduleToCloseTimeout(Duration.ofSeconds(5000)) // Entire duration from scheduling to completion including queue time
            .build();

    private final Map<String, ActivityOptions> perActivityMethodOptions = new HashMap<>() {{
        // A heartbeat time-out is a proof-of life indicator that an activity is still working.
        // The 5 second duration used here waits for up to 5 seconds to hear a heartbeat.
        // If one is not heard, the Activity fails.
        // Use heartbeats for long-lived event-driven applications.
        put("GetAccountDetails", ActivityOptions.newBuilder().setHeartbeatTimeout(Duration.ofSeconds(5)).build());
        put("Withdraw", ActivityOptions.newBuilder().setHeartbeatTimeout(Duration.ofSeconds(5)).build());
        put("Deposit", ActivityOptions.newBuilder().setHeartbeatTimeout(Duration.ofSeconds(5)).build());
    }};

    // ActivityStubs enable calls to methods as if the Activity object is local but actually perform an RPC invocation
    private final AccountActivity noRetryAccountActivityStub = Workflow.newActivityStub(AccountActivity.class, noRetryActivityOptions, perActivityMethodOptions);
    private final AccountActivity accountActivityStub = Workflow.newActivityStub(AccountActivity.class, defaultActivityOptions, perActivityMethodOptions);

    /**
     * The transfer method is the entry point to the Workflow
     * Activity method executions can be orchestrated here or from within other Activity methods
     *
     * @param transferDetails transfer request payload
     * @return transaction reference id
     */
    @Override
    public UUID transfer(TransferDetails transferDetails) {
        // Step 1: Get Account Details
        var sourceAccount = noRetryAccountActivityStub.getCustomerAccount(transferDetails.getSourceAccountId());
        var targetAccount = noRetryAccountActivityStub.getCustomerAccount(transferDetails.getTargetAccountId());

        // Step 2: Validate source account balance
        if (sourceAccount.getBalance().compareTo(transferDetails.getAmountToTransfer()) < 0) {
            throw new BadRequestException("Insufficient funds");
        }

        // Step 3: Withdraw funds from source
        try {
            // Launch `withdrawal` Activity
            accountActivityStub.withdraw(sourceAccount.getAccountId(),
                    transferDetails.getTransactionReferenceId().toString(),
                    transferDetails.getAmountToTransfer()
            );
        } catch (Exception e) {
            // If the withdrawal fails, for any exception, it's caught here
            System.out.printf("[%s] Withdrawal of %.2f from account %s failed", transferDetails.getTransactionReferenceId(),
                    transferDetails.getAmountToTransfer(), sourceAccount.getAccountId());
            System.out.flush();

            // Transaction ends here
            return null;
        }

        // Step 4: Deposit funds to destination
        try {
            // Perform `deposit` Activity
            accountActivityStub.deposit(targetAccount.getAccountId(),
                    transferDetails.getTransactionReferenceId().toString(),
                    transferDetails.getAmountToTransfer()
            );
        } catch (Exception e) {
            // If the deposit fails, for any exception, it's caught here
            System.out.printf("[%s] Deposit of %.2f to account %s failed.\n",
                    transferDetails.getTransactionReferenceId(), transferDetails.getAmountToTransfer(),
                    targetAccount.getAccountId());
            System.out.flush();
        }

        // Step 5: Perform refund activity
        try {
            System.out.printf("[%s] Refunding %.2f to account %s.\n",
                    transferDetails.getTransactionReferenceId(), transferDetails.getAmountToTransfer(),
                    sourceAccount.getAccountId());
            System.out.flush();
            accountActivityStub.refund(sourceAccount.getAccountId(),
                    transferDetails.getTransactionReferenceId().toString(),
                    transferDetails.getAmountToTransfer());
        } catch (Exception e) {
            // A recovery mechanism can fail too. Handle any exception here
            System.out.printf("[%s] Deposit of %.2f to account %s failed. Did not compensate withdrawal.\n",
                    transferDetails.getTransactionReferenceId(),
                    transferDetails.getAmountToTransfer(), targetAccount.getAccountId());
            System.out.printf("[%s] Workflow failed.", transferDetails.getTransactionReferenceId());
            System.out.flush();

            // Rethrowing the exception causes a Workflow Task failure
            throw(e);
        }

        return transferDetails.getTransactionReferenceId();
    }
}
