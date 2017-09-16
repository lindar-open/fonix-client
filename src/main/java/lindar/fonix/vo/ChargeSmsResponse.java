package lindar.fonix.vo;

import lindar.fonix.vo.internal.InternalChargeSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChargeSmsResponse extends BaseSmsResponse {
    private Integer price;

    public ChargeSmsResponse(String txguid, String numbers, String smsparts, String encoding, Integer price) {
        super(txguid, numbers, smsparts, encoding);
        this.price = price;
    }

    public static ChargeSmsResponse from(InternalChargeSmsResponse internalChargeSmsResponse) {
        return new ChargeSmsResponse(
                internalChargeSmsResponse.getSuccess().getTxguid(),
                internalChargeSmsResponse.getSuccess().getNumbers(),
                internalChargeSmsResponse.getSuccess().getSmsparts(),
                internalChargeSmsResponse.getSuccess().getEncoding(),
                internalChargeSmsResponse.getSuccess().getPrice()
        );
    }
}
