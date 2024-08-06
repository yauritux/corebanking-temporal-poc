package link.yauritux.corebanking.service.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
@Data
public class TransferDetails {

    @NotNull(message = "{cms.transfer.sourceAccountId.not.null}")
    private String sourceAccountId;
    @NotNull(message = "{cms.transfer.targetAccountId.not.null}")
    private String targetAccountId;
    private UUID transactionReferenceId;
    @NotNull(message = "{cms.transfer.amountToTransfer.not.null}")
    private BigDecimal amountToTransfer;
    private String note;
}
