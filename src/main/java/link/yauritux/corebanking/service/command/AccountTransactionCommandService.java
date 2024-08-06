package link.yauritux.corebanking.service.command;

import link.yauritux.corebanking.service.dto.request.TransferDetails;
import link.yauritux.corebanking.service.dto.response.MoneyTransferResponse;
import link.yauritux.corebanking.shared.exceptions.BadRequestException;

import java.util.UUID;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
public interface AccountTransactionCommandService {

    MoneyTransferResponse doTransfer(TransferDetails transferDetails) throws BadRequestException;
}
