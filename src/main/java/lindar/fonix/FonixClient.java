package lindar.fonix;

import lindar.fonix.api.FonixCarrierBillingResource;
import lindar.fonix.api.FonixKycResource;
import lindar.fonix.api.FonixSmsResource;
import lindar.fonix.util.impl.DefaultFonixTranslator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FonixClient {

    private final FonixSmsResource            fonixSmsResource;
    private final FonixCarrierBillingResource fonixCarrierBillingResource;
    private final FonixKycResource            fonixKycResource;

    public FonixClient(String apiKey) {
        this(StringUtils.EMPTY, StringUtils.EMPTY, apiKey, false);
    }

    public FonixClient(String apiKey, boolean dummyMode) {
        this(StringUtils.EMPTY, StringUtils.EMPTY, apiKey, dummyMode);
    }

    public FonixClient(String smsBaseUrl, String kycBaseUrl, String apiKey, boolean dummyMode) {
        fonixSmsResource = new FonixSmsResource(smsBaseUrl, apiKey, dummyMode);
        fonixCarrierBillingResource = new FonixCarrierBillingResource(smsBaseUrl, apiKey, new DefaultFonixTranslator(), dummyMode);
        fonixKycResource = new FonixKycResource(kycBaseUrl, apiKey, dummyMode);
    }

    public FonixSmsResource sms() {
        return fonixSmsResource;
    }

    public FonixCarrierBillingResource carrierBilling() {
        return fonixCarrierBillingResource;
    }

    public FonixKycResource kyc() {
        return fonixKycResource;
    }
}
