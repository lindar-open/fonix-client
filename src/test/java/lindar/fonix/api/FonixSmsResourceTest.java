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
    public void parseDeliveryReport_validDate_statusTimeResponseCorrect() {
        // given
        Map<String, String> parameters = getParseDeliveryReportParameters("20230306101252");

        // when
        DeliveryReport response = fonixClient.sms().parseDeliveryReport(parameters);

        // then
        assertThat(response, hasProperty("statusTime", is(new Date(1678093972000L))));
    }

    @Test
    public void parseDeliveryReport_validDateNearMidnight_statusTimeResponseCorrect() {
        // given
        Map<String, String> parameters = getParseDeliveryReportParameters("20230306235959");

        // when
        DeliveryReport response = fonixClient.sms().parseDeliveryReport(parameters);

        // then
        assertThat(response, hasProperty("statusTime", is(new Date(1678143599000L))));
    }

    @Test
    public void parseDeliveryReport_validDateAtDaylightSavingsSummerTime_statusTimeResponseCorrect() {
        // given
        Map<String, String> parameters = getParseDeliveryReportParameters("20230326020000");

        // when
        DeliveryReport response = fonixClient.sms().parseDeliveryReport(parameters);

        // then
        assertThat(response, hasProperty("statusTime", is(new Date(1679792400000L))));
    }

    @Test
    public void parseDeliveryReport_validDateAtDaylightSavingsWinterTime_statusTimeResponseCorrect() {
        // given
        Map<String, String> parameters = getParseDeliveryReportParameters("20231029030000");

        // when
        DeliveryReport response = fonixClient.sms().parseDeliveryReport(parameters);

        // then
        assertThat(response, hasProperty("statusTime", is(new Date(1698544800000L))));
    }

    @Test
    public void parseDeliveryReport_invalidDate_statusTimeResponseNull() {
        // given
        Map<String, String> parameters = getParseDeliveryReportParameters("2023032602");

        // when
        DeliveryReport response = fonixClient.sms().parseDeliveryReport(parameters);

        // then
        assertThat(response, hasProperty("statusTime", isEmptyOrNullString()));
    }

    private static Map<String, String> getParseDeliveryReportParameters(String statusTimeValue) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("IFVERSION","201001");
        parameters.put("OPERATOR","unknown");
        parameters.put("RETRYCOUNT","0");
        parameters.put("MONUMBER","94778978873");
        parameters.put("DESTINATION","MrQ");
        parameters.put("STATUSTIME", statusTimeValue);
        parameters.put("STATUSTEXT","Permanent+Error+%28network%2Fparameters%29");
        parameters.put("STATUSCODE","PERMANENT_OPERATOR_ERROR");
        parameters.put("CHARGESTATUS","false");
        parameters.put("GUID","fc7af6ee-2641-4f9c-82cc-2724f6cd4bf3");
        parameters.put("PRICE","0");
        parameters.put("DURATION","661");
        return parameters;
    }

}
