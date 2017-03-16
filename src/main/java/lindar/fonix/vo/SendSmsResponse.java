package lindar.fonix.vo;

import lindar.fonix.vo.internal.InternalSendSmsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Steven on 10/03/2017.
 */
@Data
public class SendSmsResponse extends BaseSmsResponse{

    public SendSmsResponse(String txguid, String numbers, String smsparts, String encoding) {
        super(txguid, numbers, smsparts, encoding);
    }

    public static SendSmsResponse from(InternalSendSmsResponse internalSendSmsResponse){
        return new SendSmsResponse(
                internalSendSmsResponse.getSuccess().getTxguid(),
                internalSendSmsResponse.getSuccess().getNumbers(),
                internalSendSmsResponse.getSuccess().getSmsparts(),
                internalSendSmsResponse.getSuccess().getEncoding()
        );
    }
}
