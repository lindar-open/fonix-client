package lindar.fonix;

import lindar.fonix.api.FonixCarrierBillingResource;
import lindar.fonix.api.FonixSmsResource;
import lindar.fonix.util.impl.DefaultFonixTranslator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FonixClient {

    private final FonixSmsResource            fonixSmsResource;
    private final FonixCarrierBillingResource fonixCarrierBillingResource;

    public FonixClient(String apiKey) {
        this(StringUtils.EMPTY, apiKey, false);
    }

    public FonixClient(String apiKey, boolean dummyMode) {
        this(StringUtils.EMPTY, apiKey, dummyMode);
    }

    public FonixClient(String baseUrl, String apiKey, boolean dummyMode) {
        fonixSmsResource = new FonixSmsResource(baseUrl, apiKey, dummyMode);
        fonixCarrierBillingResource = new FonixCarrierBillingResource(baseUrl, apiKey, new DefaultFonixTranslator(), dummyMode);
    }

    public FonixSmsResource sms() {
        return fonixSmsResource;
    }

    public FonixCarrierBillingResource carrierBilling() {
        return fonixCarrierBillingResource;
    }
}
