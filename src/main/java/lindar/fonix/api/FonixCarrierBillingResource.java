package lindar.fonix.api;

/**
 * Created by Steven on 14/03/2017.
 */

import com.google.common.collect.Lists;
import com.lindar.wellrested.vo.WellRestedResponse;
import lindar.fonix.util.FonixTranslator;
import lindar.fonix.vo.CarrierBilling;
import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.exception.FonixUnexpectedErrorException;
import lindar.fonix.vo.CarrierBillingResponse;
import lindar.fonix.vo.ChargeReport;
import lindar.fonix.vo.internal.InternalCarrierBillingResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Api for dealing with SMS on the Fonix Platform
 *
 * Created by Steven on 09/03/2017.
 *
 */
@Slf4j
public class FonixCarrierBillingResource extends BaseFonixResource{

    private final FonixTranslator translator;

    private final String CHARGE_MOBILE_ENDPOINT = "chargemobile";

    private final String CR_IFVERSION = "IFVERSION";
    private final String CR_CHARGE_METHOD = "CHARGEMETHOD";
    private final String CR_MOBILE_NUMBER = "MONUMBER";
    private final String CR_OPERATOR = "OPERATOR";
    private final String CR_GUID = "GUID";
    private final String CR_DURATION = "DURATION";
    private final String CR_RETRY_COUNT = "RETRYCOUNT";
    private final String CR_STATUS_CODE = "STATUSCODE";
    private final String CR_STATUS_TEXT = "STATUSTEXT";
    private final String CR_REQUEST_ID = "REQUESTID";
    private final String CR_STATUS_TIME = "STATUSTIME";

    private final SimpleDateFormat parseStatusTime = new SimpleDateFormat("yyyyMMddhhmmss");


    private Map<String, String> authenticationHeaders;

    /**
     * Works just like {@link FonixSmsResource(String, boolean)} except by default dummyMode will be set to false
     *
     * @see FonixSmsResource(String, boolean)
     */
    public FonixCarrierBillingResource(String apiKey, FonixTranslator fonixTranslator) {
        this(apiKey, fonixTranslator, false);
    }

    /**
     * Constructor
     *
     * @param apiKey apikey provided by fonix to authenticate with their services
     * @param dummyMode when true api calls will run in dummy mode (no action taken) unless the dummy mode is otherwise specified
     */
    public FonixCarrierBillingResource(String apiKey, FonixTranslator fonixTranslator, boolean dummyMode) {
        super(apiKey, dummyMode);
        this.translator = fonixTranslator;
    }

    /**
     * Get all ip addresses valid for the origin of a charge report request
     *
     * @return a list of valid ip addresses
     */
    public List<String> getChargeReportValidIpAddresses(){
        return Arrays.asList("54.194.146.155", "54.194.13.190");
    }

    /**
     * Parse map of values (from request) into a charge report
     *
     * @param mapParameters should contain the parameters sent from the fonix server
     **
     * @return the fully build charge report
     */
    public ChargeReport parseChargeReport(Map<String, String> mapParameters){
        ChargeReport chargeReport = new ChargeReport();

        chargeReport.setMobileNumber(mapParameters.get(CR_MOBILE_NUMBER));
        chargeReport.setGuid(mapParameters.get(CR_GUID));
        chargeReport.setIfVersion(mapParameters.get(CR_IFVERSION));

        chargeReport.setOperator(mapParameters.get(CR_OPERATOR));
        chargeReport.setStatusCode(mapParameters.get(CR_STATUS_CODE));
        chargeReport.setChargeMethod(mapParameters.get(CR_CHARGE_METHOD));
        chargeReport.setRequestId(mapParameters.get(CR_REQUEST_ID));

        if(NumberUtils.isParsable(mapParameters.get(CR_DURATION))){
            chargeReport.setDuration(Integer.parseInt(mapParameters.get(CR_DURATION)));
        }

        if(NumberUtils.isParsable(mapParameters.get(CR_RETRY_COUNT))){
            chargeReport.setRetryCount(Integer.parseInt(mapParameters.get(CR_RETRY_COUNT)));
        }

        try {
            chargeReport.setStatusTime(parseStatusTime.parse(mapParameters.get(CR_STATUS_TIME)));
        } catch (ParseException e) {
            log.error("unable to parse status time from string {}", mapParameters.get(CR_STATUS_TIME));
        }

        chargeReport.setStatusText(translator.translateChargeReportStatusText(chargeReport.getOperator(), chargeReport.getStatusCode(), mapParameters.get(CR_STATUS_TEXT)));

        return chargeReport;
    }


    /**
     * Works just like {@link FonixCarrierBillingResource#chargeMobile(CarrierBilling, boolean)} except dummyMode will be set
     * according to the default dummyMode preference
     *
     * @see FonixCarrierBillingResource#chargeMobile(CarrierBilling, boolean)
     */
    public CarrierBillingResponse chargeMobile(CarrierBilling carrierBilling) throws FonixException {
        return chargeMobile(carrierBilling, dummyMode);
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
    public CarrierBillingResponse chargeMobile(CarrierBilling carrierBilling, boolean dummyMode) throws FonixException {

        List<NameValuePair> formParams = buildFormParams(carrierBilling);

        if(dummyMode){
            formParams.add(new BasicNameValuePair("DUMMY", "yes"));
        }

        WellRestedResponse responseVO = doRequest(formParams, CHARGE_MOBILE_ENDPOINT);

        if (responseVO.getStatusCode() != 200) {
            throwExceptionFromResponse(responseVO);
        }

        return CarrierBillingResponse.from(responseVO.fromJson().castTo(InternalCarrierBillingResponse.class));
    }

    private List<NameValuePair> buildFormParams(CarrierBilling carrierBilling) throws FonixException {
        List<NameValuePair> formParams = Lists.newArrayList();

        formParams.add(new BasicNameValuePair("NUMBERS", carrierBilling.getMobileNumber()));
        formParams.add(new BasicNameValuePair("ORIGINATOR", carrierBilling.getFrom()));
        formParams.add(new BasicNameValuePair("CHARGEDESCRIPTION", carrierBilling.getChargeDescription()));

        validateAmount(carrierBilling.getAmountInPence());

        formParams.add(new BasicNameValuePair("AMOUNT", String.valueOf(carrierBilling.getAmountInPence())));

        if(StringUtils.isNotBlank(carrierBilling.getBody())){
            formParams.add(new BasicNameValuePair("BODY", carrierBilling.getBody()));
        }

        if(carrierBilling.getTtl() != null){
            formParams.add(new BasicNameValuePair("TIMETOLIVE", String.valueOf(carrierBilling.getTtl())));
        }

        formParams.add(new BasicNameValuePair("CHARGESILENT", "no"));

        if(BooleanUtils.isTrue(carrierBilling.getSmsFallback())){
            formParams.add(new BasicNameValuePair("SMSFALLBACK", "yes"));
        }

        if(StringUtils.isNotBlank(carrierBilling.getRequestId())){
            formParams.add(new BasicNameValuePair("REQUESTID", carrierBilling.getRequestId()));

        }

        return formParams;
    }

}
