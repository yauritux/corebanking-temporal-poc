package link.yauritux.corebanking.service.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import link.yauritux.corebanking.model.CustomerAccount;

import java.math.BigDecimal;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
@ActivityInterface
public interface AccountActivity {

    @ActivityMethod
    CustomerAccount getCustomerAccount(String accountId);

    // Withdraw an amount of money from the source account
    @ActivityMethod
    void withdraw(String accountId, String referenceId, BigDecimal amount);

    // Deposit an amount of money into the destination account
    @ActivityMethod
    void deposit(String accountId, String referenceId, BigDecimal amount);

    // Compensate a failed deposit by refunding to the original account
    @ActivityMethod
    void refund(String accountId, String referenceId, BigDecimal amount);
}
