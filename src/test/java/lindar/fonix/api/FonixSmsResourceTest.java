package lindar.fonix.api;

import lindar.fonix.FonixClient;
import lindar.fonix.vo.DeliveryReport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;

public class FonixSmsResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private FonixClient fonixClient;

    @Before
    public void setUp(){
        fonixClient = new FonixClient(System.getenv("fonix-api-key"), true);
    }

    @Test
    public void parseDeliveryReport_validParameters_responseCorrect() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("IFVERSION","201001");
        parameters.put("OPERATOR","unknown");
        parameters.put("RETRYCOUNT","0");
        parameters.put("MONUMBER","94778378773");
        parameters.put("DESTINATION","MrQ");
        parameters.put("STATUSTIME", "20230306101252");
        parameters.put("STATUSTEXT","Permanent+Error+%28network%2Fparameters%29");
        parameters.put("STATUSCODE","PERMANENT_OPERATOR_ERROR");
        parameters.put("CHARGESTATUS","false");
        parameters.put("GUID","fc7af6ee-2641-4f9c-82cc-2724f6cd4bf3");
        parameters.put("PRICE","0");
        parameters.put("DURATION","661");

        // when
        DeliveryReport response = fonixClient.sms().parseDeliveryReport(parameters);

        // then
        assertThat(response, hasProperty("ifVersion", is("201001")));
        assertThat(response, hasProperty("operator", is("unknown")));
        assertThat(response, hasProperty("mobileNumber", is("94778378773")));
        assertThat(response, hasProperty("destination", isEmptyOrNullString()));
        assertThat(response, hasProperty("guid", is("fc7af6ee-2641-4f9c-82cc-2724f6cd4bf3")));
        assertThat(response, hasProperty("receiveTime", isEmptyOrNullString()));
        assertThat(response, hasProperty("price", isEmptyOrNullString()));
        assertThat(response, hasProperty("duration", is(661)));
        assertThat(response, hasProperty("retryCount", is(0)));
        assertThat(response, hasProperty("statusTime", is(new Date(1678093972000L))));
        assertThat(response, hasProperty("statusCode", is("PERMANENT_OPERATOR_ERROR")));
        assertThat(response, hasProperty("statusText", is("Permanent+Error+%28network%2Fparameters%29")));
    }

}
