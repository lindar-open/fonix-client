package lindar.fonix;

import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.vo.KycResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by Steven on 09/03/2017.
 */
public class FonixFonixTest {

    private final String FAKE_VALID_MOBILE_NUMBER = "447777777777";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private FonixClient fonixClient;

    @Before
    public void setUp() {
        fonixClient = new FonixClient(System.getenv("fonix-api-key"), true);
    }

    @Test
    public void validSendSms() throws FonixException {
        KycResponse response = fonixClient.kyc().kycCheck(FAKE_VALID_MOBILE_NUMBER, "First", "Name", "11", "AL1 6PH", LocalDate.of(1990, 1, 25), "testrequest", true);
        assertThat(response, hasProperty("requestId", notNullValue()));
    }

    @Test
    public void invalidMobileNumber() throws FonixException {
        thrown.expect(FonixBadRequestException.class);
        thrown.expect(hasProperty("errorCode", is("INVALID")));
        thrown.expect(hasProperty("parameter", is("NUMBER")));

        fonixClient.kyc().kycCheck("hello", "First", "Name", "11", "AL1 6PH", LocalDate.of(1990, 1, 25), "testrequest", true);
    }

    @Test
    public void invalidAuth() throws FonixException {
        thrown.expect(FonixNotAuthorizedException.class);

        KycResponse response = new FonixClient("invalid-auth", true).kyc().kycCheck(FAKE_VALID_MOBILE_NUMBER, "First", "Name", "11", "AL1 6PH", LocalDate.of(1990, 1, 25), "testrequest", true);
    }
}
