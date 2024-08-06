package link.yauritux.corebanking.service.query;

import link.yauritux.corebanking.model.CustomerAccount;
import link.yauritux.corebanking.repository.CustomerAccountInMemRepository;
import link.yauritux.corebanking.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
@Service
public class AccountQueryServiceImpl implements AccountQueryService {

    private final CustomerAccountInMemRepository repository = new CustomerAccountInMemRepository();

    @Override
    public CustomerAccount getCustomerAccount(String accountId) {
        var customerAccount = repository.getCustomerAccounts().get(accountId);
        if (customerAccount == null) {
            throw new NotFoundException(String.format("Cannot find customer account %s", accountId));
        }

        return customerAccount;
    }
}
