package link.yauritux.corebanking.controller;

import link.yauritux.corebanking.model.CustomerAccount;
import link.yauritux.corebanking.service.command.AccountTransactionCommandService;
import link.yauritux.corebanking.service.dto.request.TransferDetails;
import link.yauritux.corebanking.service.dto.response.MoneyTransferResponse;
import link.yauritux.corebanking.service.query.AccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final AccountQueryService accountQueryService;

    @PostMapping("/transfer")
    public ResponseEntity<MoneyTransferResponse> transferMoney(@RequestBody TransferDetails request) {
        request.setTransactionReferenceId(UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountTransactionCommandService.doTransfer(request)
        );
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<CustomerAccount> getAccount(@PathVariable String accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountQueryService.getCustomerAccount(accountId));
    }
}
