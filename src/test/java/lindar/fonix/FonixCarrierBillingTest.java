package lindar.fonix;

import lindar.fonix.api.builder.CarrierBilling;
import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.vo.CarrierBillingResponse;
import lindar.fonix.vo.SendSmsResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by Steven on 09/03/2017.
 */
public class FonixCarrierBillingTest {

    private final String FAKE_VALID_MOBILE_NUMBER = "447700900809";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private FonixClient fonixClient;

    @Before
    public void setUp(){
        fonixClient = new FonixClient(System.getenv("fonix-api-key"), true);
    }

    @Test
    public void validChargeWithSms() throws FonixException {
        CarrierBilling carrierBilling = new CarrierBilling
                .Builder(FAKE_VALID_MOBILE_NUMBER, 100, "Demo", "Demo charge from demo")
                .withSms("You have just been charged by Demo App")
                .build();

        CarrierBillingResponse charge = fonixClient.carrierBilling().charge(carrierBilling);
        assertThat(charge, hasProperty("txguid", notNullValue()));
    }

}
