package link.yauritux.corebanking.service.query;

import link.yauritux.corebanking.model.CustomerAccount;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
public interface AccountQueryService {

    CustomerAccount getCustomerAccount(String accountId);
}
