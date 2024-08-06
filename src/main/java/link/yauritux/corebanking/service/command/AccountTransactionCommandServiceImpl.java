package link.yauritux.corebanking.service.command;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import link.yauritux.corebanking.service.dto.TransferDetails;
import link.yauritux.corebanking.service.workflow.MoneyTransferWorkflow;
import link.yauritux.corebanking.shared.constants.Config;
import link.yauritux.corebanking.shared.exceptions.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
@Service
public class AccountTransactionCommandServiceImpl implements AccountTransactionCommandService {

    @Override
    public UUID doTransfer(TransferDetails transferDetails) throws BadRequestException {
        if (!StringUtils.hasText(transferDetails.getSourceAccountId())) {
            throw new BadRequestException("Source account id is required");
        }
        if (!StringUtils.hasText(transferDetails.getTargetAccountId())) {
            throw new BadRequestException("Target account id is required");
        }
        if (BigDecimal.ZERO.equals(transferDetails.getAmountToTransfer())) {
            throw new BadRequestException("Amount to transfer is required");
        }

        // A WorkflowServiceStubs represents a proxy service that
        // communicates with the Temporal front-end service through gRPC.
        WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

        // A WorkflowClient wraps the stub.
        // It can be used to start, signal, query, cancel, and terminate any Workflows.
        WorkflowClient client = WorkflowClient.newInstance(serviceStub);

        // Workflow options configure  Workflow stubs.
        // A WorkflowId prevents duplicate instances, which are removed.
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(Config.MONEY_TRANSFER_TASK_QUEUE)
                .setWorkflowId(String.format("money-transfer-workflow_%s", transferDetails.getTransactionReferenceId()))
                .build();

        // WorkflowStubs enable calls to methods as if the Workflow object is local
        // but actually perform a gRPC call to the Temporal Service.
        MoneyTransferWorkflow workflow = client.newWorkflowStub(MoneyTransferWorkflow.class, options);

        // execute money transfer workflow

        // return workflow.transfer(transferDetails); // this workflow call is a blocking version (synchronous call)

        // Perform asynchronous execution.
        // This process exits after making this call and printing details.
        WorkflowExecution we = WorkflowClient.start(workflow::transfer, transferDetails);

        System.out.printf("\nMONEY TRANSFER PROJECT\n\n");
        System.out.printf("Initiating transfer of %.2f from [Account %s] to [Account %s].\n\n",
                transferDetails.getAmountToTransfer(),
                transferDetails.getSourceAccountId(),
                transferDetails.getTargetAccountId());
        System.out.printf("[WorkflowID: %s]\n[RunID: %s]\n[Transaction Reference: %s]\n\n",
                we.getWorkflowId(), we.getRunId(),
                transferDetails.getTransactionReferenceId());
        return transferDetails.getTransactionReferenceId();
    }
}
