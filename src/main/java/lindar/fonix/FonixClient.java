package lindar.fonix;

import lindar.fonix.api.FonixCarrierBillingResource;
import lindar.fonix.api.FonixSmsResource;
import lindar.fonix.util.impl.DefaultFonixTranslator;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Steven on 09/03/2017.
 */
@Slf4j
public class FonixClient {

    private final FonixSmsResource fonixSmsResource;
    private final FonixCarrierBillingResource fonixCarrierBillingResource;

    public FonixClient(String apiKey) {
        this(apiKey, false);
    }

    public FonixClient(String apiKey, boolean dummyMode) {
        fonixSmsResource = new FonixSmsResource(apiKey, dummyMode);
        fonixCarrierBillingResource = new FonixCarrierBillingResource(apiKey, new DefaultFonixTranslator(), dummyMode);

    }

    public FonixSmsResource sms(){
        return fonixSmsResource;
    }

    public FonixCarrierBillingResource carrierBilling(){
        return fonixCarrierBillingResource;
    }
}
