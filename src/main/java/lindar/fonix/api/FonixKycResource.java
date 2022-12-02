package lindar.fonix.api;

import com.lindar.wellrested.vo.WellRestedResponse;
import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.exception.FonixUnexpectedErrorException;
import lindar.fonix.vo.KycResponse;
import lindar.fonix.vo.internal.InternalKycResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Api for dealing with SMS on the Fonix Platform
 */
@Slf4j
public class FonixKycResource extends BaseFonixResource {

    private static final DateTimeFormatter DOB_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-YYYY");

    private static final String BASE_URL = "https://kycsolo.fonix.io/v2/";

    private final String KYC_ENDPOINT = "kyc";

    /**
     * Constructor
     *
     * @param apiKey    apikey provided by fonix to authenticate with their services
     * @param dummyMode when true api calls will run in dummy mode (no action taken) unless the dummy mode is otherwise specified
     */
    public FonixKycResource(String baseUrl, String apiKey, boolean dummyMode) {
        super(StringUtils.defaultIfBlank(baseUrl, BASE_URL), apiKey, dummyMode);
    }

    /**
     * Works just like {@link FonixKycResource#kycCheck(String, String, String, String, String, LocalDate, String, boolean)} except dummyMode will be set
     * according to the default dummyMode preference
     *
     * @see FonixKycResource#kycCheck(String, String, String, String, String, LocalDate, String, boolean)
     */
    public KycResponse kycCheck(String number, String name, String surname, String houseNumber, String postCode, LocalDate dob, String requestId) throws FonixException {
        return kycCheck(number, name, surname, houseNumber, postCode, dob, requestId, dummyMode);
    }


    /**
     * Send SMS
     *
     * @param number      mobile number to send sms to in E.164 format (International format)
     * @param name        First name (varchar 255)
     * @param surname     Surname (varchar 255).
     * @param houseNumber House Number (varchar 255).
     * @param postCode    Postcode of the customer’s billing address (varchar 255).
     * @param dob         Date of Birthday with format DD-MM-YYYY.
     * @param requestId   string of up to 50 characters (0-9, a-z, A-Z) used to identfy a request that must be eternally unique for your service.
     *                    For 7 days any subsequent request with the same REQUESTID and customer details will not be forwarded to the Networks and
     *                    a response with the result of the first request would be returned instead.
     * @param dummyMode   when dummyMode is true the message will not actually be sent
     * @return details of the sms sent
     * @throws FonixBadRequestException      if there is a problem with the values sent in the request
     * @throws FonixNotAuthorizedException   if authentication fails
     * @throws FonixUnexpectedErrorException if a unexpected error occurs on the Fonix platform
     */
    public KycResponse kycCheck(String number, String name, String surname, String houseNumber, String postCode, LocalDate dob, String requestId, boolean dummyMode) throws FonixException {

        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("NUMBER", number));
        formParams.add(new BasicNameValuePair("NAME", name));
        formParams.add(new BasicNameValuePair("SURNAME", surname));
        formParams.add(new BasicNameValuePair("HOUSE_NUMBER", houseNumber));
        formParams.add(new BasicNameValuePair("POSTCODE", postCode));
        formParams.add(new BasicNameValuePair("DOB", dob.format(DOB_FORMATTER)));
        formParams.add(new BasicNameValuePair("REQUESTID", requestId));

        if (dummyMode) {
            formParams.add(new BasicNameValuePair("DUMMY", "yes"));
        }

        WellRestedResponse responseVO = doRequest(formParams, KYC_ENDPOINT);

        if (responseVO.getStatusCode() != 200) {
            throwExceptionFromResponse(responseVO);
        }

        return KycResponse.from(responseVO.fromJson().castTo(InternalKycResponse.class));
    }
}
