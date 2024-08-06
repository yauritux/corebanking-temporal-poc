package link.yauritux.corebanking.service.dto.response;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
public record MoneyTransferResponse(String workflowId, String runId, String transactionReference, String description) {
}
