package lindar.fonix.api;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.lindar.wellrested.vo.ResponseVO;
import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.exception.FonixUnexpectedErrorException;
import lindar.fonix.vo.ChargeSmsResponse;
import lindar.fonix.vo.SendSmsResponse;
import lindar.fonix.vo.internal.InternalChargeSmsResponse;
import lindar.fonix.vo.internal.InternalFailureResponse;
import lindar.fonix.vo.internal.InternalSendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Map;

/**
 * Api for dealing with SMS on the Fonix Platform
 *
 * Created by Steven on 09/03/2017.
 *
 */
@Slf4j
public class FonixSmsResource extends BaseFonixResource {

    private final String SEND_SMS_ENDPOINT = "sendsms";
    private final String CHARGE_SMS_ENDPOINT = "chargesms";


    /**
     * Works just like {@link FonixSmsResource(String, boolean)} except by default dummyMode will be set to false
     *
     * @see FonixSmsResource(String, boolean)
     */
    public FonixSmsResource(String apiKey) {
        this(apiKey, false);
    }

    /**
     * Constructor
     *
     * @param apiKey apikey provided by fonix to authenticate with their services
     * @param dummyMode when true api calls will run in dummy mode (no action taken) unless the dummy mode is otherwise specified
     */
    public FonixSmsResource(String apiKey, boolean dummyMode) {
        super(apiKey, dummyMode);
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
     * @param to mobile number to send sms to in E.164 format (International format)
     * @param from the originator the sms is from, must either be a pre-registered shortcode
     *             or a alphanumeric sender ID (max 11 alphanumeric characters)
     * @param body the content of the sms to be sent
     * @param dummyMode when dummyMode is true the message will not actually be sent
     *
     * @throws FonixBadRequestException if there is a problem with the values sent in the request
     * @throws FonixNotAuthorizedException if authentication fails
     * @throws FonixUnexpectedErrorException if a unexpected error occurs on the Fonix platform
     *
     * @return details of the sms sent
     */
    public SendSmsResponse sendSms(String to, String from, String body, boolean dummyMode) throws FonixException {

        List<NameValuePair> formParams = Lists.newArrayList();
        formParams.add(new BasicNameValuePair("BODY", body));
        formParams.add(new BasicNameValuePair("ORIGINATOR", from));
        formParams.add(new BasicNameValuePair("NUMBERS", to));

        if(dummyMode){
            formParams.add(new BasicNameValuePair("DUMMY", "yes"));
        }

        ResponseVO responseVO = doRequest(formParams, SEND_SMS_ENDPOINT);

        if (responseVO.getStatusCode() != 200) {
            throwExceptionFromResponse(responseVO);
        }

        return SendSmsResponse.from(responseVO.castJsonResponse(InternalSendSmsResponse.class));
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
     * @param to mobile number to send sms to in E.164 format (International format)
     * @param from the originator the sms is from, must be a pre-registered shortcode for the specific charge amount
     * @param body the content of the sms to be sent, content must not be more then 1 sms (aka 160 characters)
     * @param dummyMode when dummyMode is true the message will not actually be sent
     *
     * @throws FonixBadRequestException if there is a problem with the values sent in the request
     * @throws FonixNotAuthorizedException if authentication fails
     * @throws FonixUnexpectedErrorException if a unexpected error occurs on the Fonix platform
     *
     * @return details of the sms sent
     */
    public ChargeSmsResponse chargeSms(String to, String from, String body, boolean dummyMode) throws FonixException {

        List<NameValuePair> formParams = Lists.newArrayList();
        formParams.add(new BasicNameValuePair("BODY", body));
        formParams.add(new BasicNameValuePair("ORIGINATOR", from));
        formParams.add(new BasicNameValuePair("NUMBERS", to));

        if(dummyMode){
            formParams.add(new BasicNameValuePair("DUMMY", "yes"));
        }

        ResponseVO responseVO = doRequest(formParams, CHARGE_SMS_ENDPOINT);

        if (responseVO.getStatusCode() != 200) {
            throwExceptionFromResponse(responseVO);
        }

        return ChargeSmsResponse.from(responseVO.castJsonResponse(InternalChargeSmsResponse.class));
    }

}
