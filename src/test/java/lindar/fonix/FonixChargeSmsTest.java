package lindar.fonix;

import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.vo.ChargeSmsResponse;
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
public class FonixChargeSmsTest {

    private final String FAKE_VALID_MOBILE_NUMBER = "447700900809";
    private final String ORIGINATOR = "84988";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private FonixClient fonixClient;

    @Before
    public void setUp(){
        fonixClient = new FonixClient(System.getenv("fonix-api-key"), true);
    }

    @Test
    public void validSendSms() throws FonixException {
        ChargeSmsResponse response = fonixClient.sms().chargeSms(FAKE_VALID_MOBILE_NUMBER, "Demo", "Hello, this is a test");
        assertThat(response, hasProperty("txguid", notNullValue()));
    }

    @Test
    public void invalidMobileNumber() throws FonixException {
        thrown.expect(FonixBadRequestException.class);
        thrown.expect(hasProperty("errorCode", is("INVALID_NUMBER")));
        thrown.expect(hasProperty("parameter", is("NUMBERS")));

        fonixClient.sms().chargeSms("hello", ORIGINATOR, "Hello, this is a test");
    }

    @Test
    public void invalidFrom() throws FonixException {
        thrown.expect(FonixBadRequestException.class);
        thrown.expect(hasProperty("errorCode", is("TOO_MANY_CHARACTERS")));
        thrown.expect(hasProperty("parameter", is("ORIGINATOR")));

        fonixClient.sms().chargeSms("hello", "invalid from", "Hello, this is a test");
    }

    @Test
    public void emptyContent() throws FonixException {
        thrown.expect(FonixBadRequestException.class);
        thrown.expect(hasProperty("errorCode", is("IS_EMPTY")));
        thrown.expect(hasProperty("parameter", is("BODY")));

        fonixClient.sms().chargeSms(FAKE_VALID_MOBILE_NUMBER, ORIGINATOR, "");
    }

    @Test
    public void contentTooLong() throws FonixException {
        thrown.expect(FonixBadRequestException.class);
        thrown.expect(hasProperty("errorCode", is("IS_EMPTY")));
        thrown.expect(hasProperty("parameter", is("BODY")));

        fonixClient.sms().chargeSms(FAKE_VALID_MOBILE_NUMBER, ORIGINATOR, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Nam in augue at lacus gravida finibus sed ac urna. Phasellus scelerisque est in justo commodo," +
                " id mattis urna consectetur. Proin sollicitudin velit nec velit venenatis, ut posuere ante blandit." +
                " Donec at vehicula sapien, at fringilla tellus. Nullam ac tortor in nunc congue facilisis." +
                " Phasellus semper quis diam a suscipit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos." +
                " Etiam commodo malesuada ipsum ac gravida. Proin nec enim commodo, tristique est vitae, convallis purus." +
                " Aliquam viverra purus at purus bibendum, vel ultricies justo sodales. Mauris non ante risus." +
                " Nullam id metus fermentum, commodo enim sed, accumsan tortor.");
    }

    @Test
    public void invalidAuth() throws FonixException {
        thrown.expect(FonixNotAuthorizedException.class);

        ChargeSmsResponse response = new FonixClient("invalid-auth", true).sms()
                .chargeSms(FAKE_VALID_MOBILE_NUMBER, ORIGINATOR, "Hello, this is a test");
    }
}
