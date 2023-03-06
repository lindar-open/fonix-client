package lindar.fonix.api;

import com.lindar.wellrested.vo.WellRestedResponse;
import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.exception.FonixUnexpectedErrorException;
import lindar.fonix.vo.ChargeSmsResponse;
import lindar.fonix.vo.DeliveryReport;
import lindar.fonix.vo.MobileOriginatedSms;
import lindar.fonix.vo.SendSmsResponse;
import lindar.fonix.vo.internal.InternalChargeSmsResponse;
import lindar.fonix.vo.internal.InternalSendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Api for dealing with SMS on the Fonix Platform
 */
@Slf4j
public class FonixSmsResource extends BaseFonixResource {
    private static final String BASE_URL = "https://sonar-fixed.fonix.io/v2/";

    private final String SEND_SMS_ENDPOINT   = "sendsms";
    private final String CHARGE_SMS_ENDPOINT = "chargesms";

    private final SimpleDateFormat parseStatusTime = new SimpleDateFormat("yyyyMMddhhmmss");


    private final String IFVERSION     = "IFVERSION";
    private final String MOBILE_NUMBER = "MONUMBER";
    private final String DESTINATION   = "DESTINATION";
    private final String OPERATOR      = "OPERATOR";
    private final String GUID          = "GUID";
    private final String DURATION      = "DURATION";
    private final String PRICE         = "PRICE";
    private final String RETRY_COUNT   = "RETRYCOUNT";
    private final String RECEIVE_TIME  = "RECEIVETIME";

    private final String DR_STATUS_CODE = "STATUSCODE";
    private final String DR_STATUS_TEXT = "STATUSTEXT";
    private final String DR_STATUS_TIME = "STATUSTIME";

    private final String MO_BODY = "BODY";


    /**
     * Constructor
     *
     * @param apiKey    apikey provided by fonix to authenticate with their services
     * @param dummyMode when true api calls will run in dummy mode (no action taken) unless the dummy mode is otherwise specified
     */
    public FonixSmsResource(String baseUrl, String apiKey, boolean dummyMode) {
        super(StringUtils.defaultIfBlank(baseUrl, BASE_URL), apiKey, dummyMode);
    }

    /**
     * Works just like {@link FonixSmsResource#sendSms(String, String, String, boolean)} except dummyMode will be set
     * according to the default dummyMode preference
     *
     * @see FonixSmsResource#sendSms(String, String, String, boolean)
     */
    public SendSmsResponse sendSms(String to, String from, String body) throws FonixException {
        return sendSms(to, from, body, dummyMode);
    }


    /**
     * Send SMS
     *
     * @param to        mobile number to send sms to in E.164 format (International format)
     * @param from      the originator the sms is from, must either be a pre-registered shortcode
     *                  or a alphanumeric sender ID (max 11 alphanumeric characters)
     * @param body      the content of the sms to be sent
     * @param dummyMode when dummyMode is true the message will not actually be sent
     * @return details of the sms sent
     * @throws FonixBadRequestException      if there is a problem with the values sent in the request
     * @throws FonixNotAuthorizedException   if authentication fails
     * @throws FonixUnexpectedErrorException if a unexpected error occurs on the Fonix platform
     */
    public SendSmsResponse sendSms(String to, String from, String body, boolean dummyMode) throws FonixException {

        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("BODY", body));
        formParams.add(new BasicNameValuePair("ORIGINATOR", from));
        formParams.add(new BasicNameValuePair("NUMBERS", to));

        if (dummyMode) {
            formParams.add(new BasicNameValuePair("DUMMY", "yes"));
        }

        WellRestedResponse responseVO = doRequest(formParams, SEND_SMS_ENDPOINT);

        if (responseVO.getStatusCode() != 200) {
            throwExceptionFromResponse(responseVO);
        }

        return SendSmsResponse.from(responseVO.fromJson().castTo(InternalSendSmsResponse.class));
    }

    /**
     * Works just like {@link FonixSmsResource#chargeSms(String, String, String, boolean)} except dummyMode will be set
     * according to the default dummyMode preference
     *
     * @see FonixSmsResource#chargeSms(String, String, String, boolean)
     */
    public ChargeSmsResponse chargeSms(String to, String from, String body) throws FonixException {
        return chargeSms(to, from, body, dummyMode);
    }

    /**
     * Charge SMS
     *
     * @param to        mobile number to send sms to in E.164 format (International format)
     * @param from      the originator the sms is from, must be a pre-registered shortcode for the specific charge amount
     * @param body      the content of the sms to be sent, content must not be more then 1 sms (aka 160 characters)
     * @param dummyMode when dummyMode is true the message will not actually be sent
     * @return details of the sms sent
     * @throws FonixBadRequestException      if there is a problem with the values sent in the request
     * @throws FonixNotAuthorizedException   if authentication fails
     * @throws FonixUnexpectedErrorException if a unexpected error occurs on the Fonix platform
     */
    public ChargeSmsResponse chargeSms(String to, String from, String body, boolean dummyMode) throws FonixException {

        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("BODY", body));
        formParams.add(new BasicNameValuePair("ORIGINATOR", from));
        formParams.add(new BasicNameValuePair("NUMBERS", to));

        if (dummyMode) {
            formParams.add(new BasicNameValuePair("DUMMY", "yes"));
        }

        WellRestedResponse responseVO = doRequest(formParams, CHARGE_SMS_ENDPOINT);

        if (responseVO.getStatusCode() != 200) {
            throwExceptionFromResponse(responseVO);
        }

        return ChargeSmsResponse.from(responseVO.fromJson().castTo(InternalChargeSmsResponse.class));
    }


    /**
     * Parse map of values (from request) into a delivery report
     *
     * @param mapParameters should contain the parameters sent from the fonix server
     *                      *
     * @return the fully build delivery report
     */
    public DeliveryReport parseDeliveryReport(Map<String, String> mapParameters) {
        DeliveryReport deliveryReport = new DeliveryReport();

        deliveryReport.setMobileNumber(mapParameters.get(MOBILE_NUMBER));
        deliveryReport.setGuid(mapParameters.get(GUID));
        deliveryReport.setIfVersion(mapParameters.get(IFVERSION));

        deliveryReport.setOperator(mapParameters.get(OPERATOR));
        deliveryReport.setStatusCode(mapParameters.get(DR_STATUS_CODE));


        if (NumberUtils.isParsable(mapParameters.get(DURATION))) {
            deliveryReport.setDuration(Integer.parseInt(mapParameters.get(DURATION)));
        }

        if (NumberUtils.isParsable(mapParameters.get(RETRY_COUNT))) {
            deliveryReport.setRetryCount(Integer.parseInt(mapParameters.get(RETRY_COUNT)));
        }

        try {
            if (mapParameters.containsKey(DR_STATUS_TIME)) {
                deliveryReport.setStatusTime(parseStatusTime.parse(mapParameters.get(DR_STATUS_TIME)));
            }
        } catch (ParseException | NumberFormatException e) {
            log.error("unable to parse status time from string {}", mapParameters.get(DR_STATUS_TIME));
        }

        try {
            if (mapParameters.containsKey(RECEIVE_TIME)) {
                deliveryReport.setReceiveTime(parseStatusTime.parse(mapParameters.get(RECEIVE_TIME)));
            }
        } catch (ParseException e) {
            log.error("unable to parse receive time from string {}", mapParameters.get(RECEIVE_TIME));
        }

        deliveryReport.setStatusText(mapParameters.get(DR_STATUS_TEXT));

        return deliveryReport;
    }

    /**
     * Parse map of values (from request) into a Mobile Originated Sms
     * @param mapParameters should contain the parameters sent from the Fonix server
     * @return the fully built Mobile Originated Sms
     */
    public MobileOriginatedSms parseMobileOriginatedSms(Map<String, String> mapParameters) {
        MobileOriginatedSms moSms = new MobileOriginatedSms();

        moSms.setGuid(mapParameters.get(GUID));
        moSms.setIfVersion(mapParameters.get(IFVERSION));
        moSms.setOperator(mapParameters.get(OPERATOR));
        moSms.setMobileNumber(mapParameters.get(MOBILE_NUMBER));
        moSms.setDestination(mapParameters.get(DESTINATION));

        try {
            if (mapParameters.containsKey(RECEIVE_TIME)) {
                moSms.setReceiveTime(parseStatusTime.parse(mapParameters.get(RECEIVE_TIME)));
            }
        } catch (ParseException e) {
            log.error("unable to parse receive time from string {}", mapParameters.get(RECEIVE_TIME));
        }

        if (NumberUtils.isParsable(mapParameters.get(PRICE))) {
            moSms.setPrice(Integer.parseInt(mapParameters.get(PRICE)));
        }

        if (NumberUtils.isParsable(mapParameters.get(DURATION))) {
            moSms.setDuration(Integer.parseInt(mapParameters.get(DURATION)));
        }

        if (NumberUtils.isParsable(mapParameters.get(RETRY_COUNT))) {
            moSms.setRetryCount(Integer.parseInt(mapParameters.get(RETRY_COUNT)));
        }

        moSms.setBody(mapParameters.get(MO_BODY));

        return moSms;
    }

}
