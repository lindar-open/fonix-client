package lindar.fonix.api;

/**
 * Created by Steven on 14/03/2017.
 */

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lindar.fonix.api.builder.CarrierBilling;
import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.exception.FonixUnexpectedErrorException;
import lindar.fonix.vo.CarrierBillingResponse;
import lindar.fonix.vo.internal.InternalCarrierBillingResponse;
import lindar.fonix.vo.internal.InternalFailureResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.spauny.joy.wellrested.fluid.WellRestedRequest;
import org.spauny.joy.wellrested.vo.ResponseVO;

import java.util.List;
import java.util.Map;

/**
 * Api for dealing with SMS on the Fonix Platform
 *
 * Created by Steven on 09/03/2017.
 *
 */
@Slf4j
public class FonixCarrierBillingResource {

    private final String API_KEY_HEADER = "X-API-KEY";
    private final String sendSmsUrl = "https://sonar.fonix.io/v2/sendsms";


    private final boolean dummyMode;

    private Map<String, String> authenticationHeaders;

    /**
     * Works just like {@link FonixSmsResource(String, boolean)} except by default dummyMode will be set to false
     *
     * @see FonixSmsResource(String, boolean)
     */
    public FonixCarrierBillingResource(String apiKey) {
        this(apiKey, false);
    }

    /**
     * Constructor
     *
     * @param apiKey apikey provided by fonix to authenticate with their services
     * @param dummyMode when true api calls will run in dummy mode (no action taken) unless the dummy mode is otherwise specified
     */
    public FonixCarrierBillingResource(String apiKey, boolean dummyMode) {
        this.dummyMode = dummyMode;
        this.authenticationHeaders = ImmutableMap.<String, String>builder().put(API_KEY_HEADER, apiKey).build();
    }


    /**
     * Works just like {@link FonixCarrierBillingResource#charge(CarrierBilling, boolean)} except dummyMode will be set
     * according to the default dummyMode preference
     *
     * @see FonixCarrierBillingResource#charge(CarrierBilling, boolean)
     */
    public CarrierBillingResponse charge(CarrierBilling carrierBilling) throws FonixException {
        return charge(carrierBilling, dummyMode);
    }

    /**
     * Send SMS
     *
     * @param carrierBilling details of the carrier billing
     *                       @see CarrierBilling
     * @param dummyMode when dummyMode is true the message will not actually be sent
     *
     * @throws FonixBadRequestException if there is a problem with the values sent in the request
     * @throws FonixNotAuthorizedException if authentication fails
     * @throws FonixUnexpectedErrorException if a unexpected error occurs on the Fonix platform
     *
     * @return details of the carrier billing request
     */
    public CarrierBillingResponse charge(CarrierBilling carrierBilling, boolean dummyMode) throws FonixException {

        List<NameValuePair> formParams = buildFormParams(carrierBilling);

        if(dummyMode){
            formParams.add(new BasicNameValuePair("DUMMY", "yes"));
        }

        ResponseVO responseVO = WellRestedRequest.build(sendSmsUrl).post(formParams, authenticationHeaders);

        if (responseVO.getStatusCode() != 200) {
            throwExceptionFromResponse(responseVO);
        }

        return CarrierBillingResponse.from(responseVO.castJsonResponse(InternalCarrierBillingResponse.class));
    }

    private List<NameValuePair> buildFormParams(CarrierBilling carrierBilling) throws FonixException {
        List<NameValuePair> formParams = Lists.newArrayList();

        formParams.add(new BasicNameValuePair("NUMBERS", carrierBilling.getMobileNumber()));
        formParams.add(new BasicNameValuePair("ORIGINATOR", carrierBilling.getFrom()));
        formParams.add(new BasicNameValuePair("CHARGEDESCRIPTION", carrierBilling.getChargeDescription()));

        validateAmountIfNeeded(carrierBilling.getAmountInPence());

        formParams.add(new BasicNameValuePair("AMOUNT", String.valueOf(carrierBilling.getAmountInPence())));

        if(StringUtils.isNotBlank(carrierBilling.getBody())){
            formParams.add(new BasicNameValuePair("BODY", carrierBilling.getBody()));
        }

        if(carrierBilling.getTtl() != null){
            formParams.add(new BasicNameValuePair("TIMETOLIVE", String.valueOf(carrierBilling.getTtl())));
        }

        if(carrierBilling.getChargeSilently() != null && carrierBilling.getChargeSilently() == true){
            formParams.add(new BasicNameValuePair("CHARGESILENT", "no"));
        }

        return formParams;
    }

    private void validateAmountIfNeeded(int amountInPence) throws FonixBadRequestException {
        if(amountInPence <= 0){
            throw new FonixBadRequestException("INVALID_AMOUNT", "Amount must be greater than 0");
        }

        if(amountInPence > 0 && (float)amountInPence % 100.f > 0){
            log.warn("Not all providers support billing in penny increments");
        }
    }

    private void throwExceptionFromResponse(ResponseVO responseVO) throws FonixException {

        if(responseVO.getStatusCode() == 400){
            InternalFailureResponse internalFailureResponse = responseVO.castJsonResponse(InternalFailureResponse.class);
            throw new FonixBadRequestException(internalFailureResponse.getFailure().getFailcode(), internalFailureResponse.getFailure().getParameter());
        }

        if(responseVO.getStatusCode() == 403){
            throw new FonixNotAuthorizedException();
        }

        throw new FonixUnexpectedErrorException(responseVO.getStatusCode(), responseVO.getServerResponse());
    }
}
