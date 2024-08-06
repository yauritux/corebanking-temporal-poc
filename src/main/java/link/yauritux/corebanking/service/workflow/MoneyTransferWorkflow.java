package link.yauritux.corebanking.service.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import link.yauritux.corebanking.service.dto.request.TransferDetails;

import java.util.UUID;

/**
 * @author M. Yauri M. Attamimi (yauritux@gmail.com)
 * @version 1.0
 */
@WorkflowInterface
public interface MoneyTransferWorkflow {

    @WorkflowMethod
    UUID transfer(TransferDetails transferDetails);
}
