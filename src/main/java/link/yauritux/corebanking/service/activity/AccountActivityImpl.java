package link.yauritux.corebanking.service.activity;

import io.temporal.activity.Activity;
import link.yauritux.corebanking.model.CustomerAccount;
import link.yauritux.corebanking.repository.CustomerAccountInMemRepository;

import java.math.BigDecimal;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
public class AccountActivityImpl implements AccountActivity {

    private final CustomerAccountInMemRepository repository = new CustomerAccountInMemRepository();

    @Override
    public CustomerAccount getCustomerAccount(String accountId) {
        var customerAccount = repository.getCustomerAccounts().get(accountId);
        if (customerAccount == null) {
            throw Activity.wrap(new RuntimeException(String.format("Cannot find account %s", accountId)));
        }
        return customerAccount;
    }

    @Override
    public void withdraw(String accountId, String referenceId, BigDecimal amount) {
        var customerAccount = getCustomerAccount(accountId);
        if (customerAccount.getBalance().compareTo(amount) < 0) {
            throw Activity.wrap(new RuntimeException("Insufficient funds"));
        }
        System.out.printf(
                "Withdrawing amount of %.2f from source account %s with transaction reference ID %s\n",
                amount, accountId, referenceId
        );
        customerAccount.setBalance(customerAccount.getBalance().subtract(amount));
    }

    @Override
    public void deposit(String accountId, String referenceId, BigDecimal amount) {
        var customerAccount = getCustomerAccount(accountId);
        customerAccount.setBalance(customerAccount.getBalance().add(amount));
        System.out.printf(
                "Successfully deposit %.2f of amount to target account %s with transaction reference ID %s\n",
                amount, accountId, referenceId
        );
    }

    @Override
    public void refund(String accountId, String referenceId, BigDecimal amount) {
        var customerAccount = getCustomerAccount(accountId);
        customerAccount.setBalance(customerAccount.getBalance().add(amount));
        System.out.printf(
                "Successfully refund %.2f of amount to source account %s with transaction reference ID %s\n",
                amount, accountId, referenceId
        );
    }
}
