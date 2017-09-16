package lindar.fonix.vo;

import lindar.fonix.vo.internal.InternalSendSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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
