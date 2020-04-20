package lindar.fonix.api;

import com.lindar.wellrested.vo.WellRestedResponse;
import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.exception.FonixUnexpectedErrorException;
import lindar.fonix.vo.ChargeSmsResponse;
import lindar.fonix.vo.DeliveryReport;
import lindar.fonix.vo.SendSmsResponse;
import lindar.fonix.vo.internal.InternalChargeSmsResponse;
import lindar.fonix.vo.internal.InternalSendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

    private final String SEND_SMS_ENDPOINT   = "sendsms";
    private final String CHARGE_SMS_ENDPOINT = "chargesms";

    private final SimpleDateFormat parseStatusTime = new SimpleDateFormat("yyyyMMddhhmmss");


    private final String DR_IFVERSION     = "IFVERSION";
    private final String DR_MOBILE_NUMBER = "MONUMBER";
    private final String DR_DESTINATION   = "DESTINATION";
    private final String DR_OPERATOR      = "OPERATOR";
    private final String DR_GUID          = "GUID";
    private final String DR_DURATION      = "DURATION";
    private final String DR_RETRY_COUNT   = "RETRYCOUNT";
    private final String DR_STATUS_CODE   = "STATUSCODE";
    private final String DR_STATUS_TEXT   = "STATUSTEXT";
    private final String DR_STATUS_TIME   = "STATUSTIME";
    private final String DR_RECEIVE_TIME  = "RECEIVETIME";


    /**
     * Constructor
     *
     * @param apiKey    apikey provided by fonix to authenticate with their services
     * @param dummyMode when true api calls will run in dummy mode (no action taken) unless the dummy mode is otherwise specified
     */
    public FonixSmsResource(String baseUrl, String apiKey, boolean dummyMode) {
        super(baseUrl, apiKey, dummyMode);
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

        deliveryReport.setMobileNumber(mapParameters.get(DR_MOBILE_NUMBER));
        deliveryReport.setGuid(mapParameters.get(DR_GUID));
        deliveryReport.setIfVersion(mapParameters.get(DR_IFVERSION));

        deliveryReport.setOperator(mapParameters.get(DR_OPERATOR));
        deliveryReport.setStatusCode(mapParameters.get(DR_STATUS_CODE));


        if (NumberUtils.isParsable(mapParameters.get(DR_DURATION))) {
            deliveryReport.setDuration(Integer.parseInt(mapParameters.get(DR_DURATION)));
        }

        if (NumberUtils.isParsable(mapParameters.get(DR_RETRY_COUNT))) {
            deliveryReport.setRetryCount(Integer.parseInt(mapParameters.get(DR_RETRY_COUNT)));
        }

        try {
            if (mapParameters.containsKey(DR_STATUS_TIME)) {
                deliveryReport.setStatusTime(parseStatusTime.parse(mapParameters.get(DR_STATUS_TIME)));
            }
        } catch (ParseException e) {
            log.error("unable to parse status time from string {}", mapParameters.get(DR_STATUS_TIME));
        }

        try {
            if (mapParameters.containsKey(DR_RECEIVE_TIME)) {
                deliveryReport.setReceiveTime(parseStatusTime.parse(mapParameters.get(DR_RECEIVE_TIME)));
            }
        } catch (ParseException e) {
            log.error("unable to parse receive time from string {}", mapParameters.get(DR_RECEIVE_TIME));
        }

        deliveryReport.setStatusText(mapParameters.get(DR_STATUS_TEXT));

        return deliveryReport;
    }

}
