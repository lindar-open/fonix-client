package lindar.fonix.vo.internal;

import lombok.Data;

/**
 * Created by Steven on 09/03/2017.
 */
@Data
public class InternalCarrierBillingResponse {

    private Success success;

    @Data
    public class Success {
        private String txguid;
        private String numbers;
        private String smsparts;
        private String encoding;
    }
}
