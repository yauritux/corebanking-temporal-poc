package link.yauritux.corebanking.repository;

import link.yauritux.corebanking.model.CustomerAccount;
import link.yauritux.corebanking.shared.constants.AccountType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
@Getter
public class CustomerAccountInMemRepository {

    private final HashMap<String, CustomerAccount> customerAccounts = new HashMap<>();

    public CustomerAccountInMemRepository() {
        customerAccounts.put("3376884385", new CustomerAccount(
                "3376884385", "Ichigo Kurosaki", AccountType.RETAIL,
                BigDecimal.valueOf(10_000_000)
        ));
        customerAccounts.put("3376884397", new CustomerAccount(
                "3376884397", "Natsu Dragneel", AccountType.RETAIL,
                BigDecimal.valueOf(15_000_000)
        ));
        customerAccounts.put("3376884199", new CustomerAccount(
                "3376884199", "Tanjiro Kamado", AccountType.RETAIL,
                BigDecimal.valueOf(25_000_000)
        ));
        customerAccounts.put("9133775999", new CustomerAccount(
                "9133775999", "X Company", AccountType.CORPORATE,
                BigDecimal.valueOf(100_000_000)
        ));
        customerAccounts.put("9133775911", new CustomerAccount(
                "9133775911", "XYZ Company", AccountType.CORPORATE,
                BigDecimal.valueOf(500_000_000)
        ));
    }
}
