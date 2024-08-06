package link.yauritux.corebanking.model;

import link.yauritux.corebanking.shared.constants.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccount {

    private String accountId;
    private String accountName;
    private AccountType accountType;
    private BigDecimal balance;
}
