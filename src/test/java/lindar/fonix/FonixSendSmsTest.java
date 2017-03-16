package lindar.fonix;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.vo.SendSmsResponse;
import lindar.fonix.vo.internal.InternalSendSmsResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by Steven on 09/03/2017.
 */
public class FonixSendSmsTest {

    private final String FAKE_VALID_MOBILE_NUMBER = "447700900809";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private FonixClient fonixClient;

    @Before
    public void setUp(){
        fonixClient = new FonixClient(System.getenv("fonix-api-key"), true);
    }

    @Test
    public void validSendSms() throws FonixException {
        SendSmsResponse response = fonixClient.sms().sendSms(FAKE_VALID_MOBILE_NUMBER, "Demo", "Hello, this is a test");
        assertThat(response, hasProperty("txguid", notNullValue()));
    }

    @Test
    public void invalidMobileNumber() throws FonixException {
        thrown.expect(FonixBadRequestException.class);
        thrown.expect(hasProperty("errorCode", is("INVALID_NUMBER")));
        thrown.expect(hasProperty("parameter", is("NUMBERS")));

        fonixClient.sms().sendSms("hello", "Demo", "Hello, this is a test");
    }

    @Test
    public void invalidFrom() throws FonixException {
        thrown.expect(FonixBadRequestException.class);
        thrown.expect(hasProperty("errorCode", is("TOO_MANY_CHARACTERS")));
        thrown.expect(hasProperty("parameter", is("ORIGINATOR")));

        fonixClient.sms().sendSms("hello", "thisisaverylongfromname", "Hello, this is a test");
    }

    @Test
    public void emptyContent() throws FonixException {
        thrown.expect(FonixBadRequestException.class);
        thrown.expect(hasProperty("errorCode", is("IS_EMPTY")));
        thrown.expect(hasProperty("parameter", is("BODY")));

        fonixClient.sms().sendSms(FAKE_VALID_MOBILE_NUMBER, "Demo", "");
    }

    @Test
    public void invalidAuth() throws FonixException {
        thrown.expect(FonixNotAuthorizedException.class);

        SendSmsResponse response = new FonixClient("invalid-auth", true).sms().sendSms(FAKE_VALID_MOBILE_NUMBER, "Demo", "Hello, this is a test");
    }
}
