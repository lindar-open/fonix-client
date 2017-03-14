package lindar.fonix.vo;

import lindar.fonix.vo.internal.InternalCarrierBillingResponse;
import lindar.fonix.vo.internal.InternalSendSmsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Steven on 10/03/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarrierBillingResponse {
    private String txguid;
    private String numbers;
    private String smsparts;
    private String encoding;

    public static CarrierBillingResponse from(InternalCarrierBillingResponse internalCarrierBillingResponse){
        return new CarrierBillingResponse(
                internalCarrierBillingResponse.getSuccess().getTxguid(),
                internalCarrierBillingResponse.getSuccess().getNumbers(),
                internalCarrierBillingResponse.getSuccess().getSmsparts(),
                internalCarrierBillingResponse.getSuccess().getEncoding()
        );
    }
}
