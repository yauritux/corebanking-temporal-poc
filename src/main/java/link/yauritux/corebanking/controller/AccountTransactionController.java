package link.yauritux.corebanking.controller;

import link.yauritux.corebanking.service.command.AccountTransactionCommandService;
import link.yauritux.corebanking.service.dto.TransferDetails;
import link.yauritux.corebanking.shared.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountTransactionController {

    private final AccountTransactionCommandService accountTransactionCommandService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferDetails request) {
        request.setTransactionReferenceId(UUID.randomUUID());
        String transactionRefId = accountTransactionCommandService.doTransfer(request).toString();
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionRefId);
    }
}
