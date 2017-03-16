package lindar.fonix.vo;

import lindar.fonix.vo.internal.InternalChargeSmsResponse;
import lindar.fonix.vo.internal.InternalSendSmsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Connection;

/**
 * Created by Steven on 10/03/2017.
 */
@Data

public class ChargeSmsResponse extends BaseSmsResponse{
    private Integer price;

    public ChargeSmsResponse(String txguid, String numbers, String smsparts, String encoding, Integer price) {
        super(txguid, numbers, smsparts, encoding);
        this.price = price;
    }

    public static ChargeSmsResponse from(InternalChargeSmsResponse internalChargeSmsResponse){
        return new ChargeSmsResponse(
                internalChargeSmsResponse.getSuccess().getTxguid(),
                internalChargeSmsResponse.getSuccess().getNumbers(),
                internalChargeSmsResponse.getSuccess().getSmsparts(),
                internalChargeSmsResponse.getSuccess().getEncoding(),
                internalChargeSmsResponse.getSuccess().getPrice()
        );
    }
}
