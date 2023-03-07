package lindar.fonix.api;

import com.lindar.wellrested.vo.WellRestedResponse;
import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.exception.FonixUnexpectedErrorException;
import lindar.fonix.util.FonixDateParser;
import lindar.fonix.util.FonixTranslator;
import lindar.fonix.vo.CarrierBilling;
import lindar.fonix.vo.CarrierBillingResponse;
import lindar.fonix.vo.ChargeReport;
import lindar.fonix.vo.internal.InternalCarrierBillingResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Api for dealing with SMS on the Fonix Platform
 */
@Slf4j
public class FonixCarrierBillingResource extends BaseFonixResource {

    private static final String BASE_URL = "https://sonar-fixed.fonix.io/v2/";

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
    private final String CR_CONTRACT = "CONTRACT";

    private final FonixDateParser fonixDateParser = new FonixDateParser();

    /**
     * Constructor
     *
     * @param apiKey    apikey provided by fonix to authenticate with their services
     * @param dummyMode when true api calls will run in dummy mode (no action taken) unless the dummy mode is otherwise specified
     */
    public FonixCarrierBillingResource(String baseUrl, String apiKey, FonixTranslator fonixTranslator, boolean dummyMode) {
        super(StringUtils.defaultIfBlank(baseUrl, BASE_URL), apiKey, dummyMode);
        this.translator = fonixTranslator;
    }

    /**
     * Get all ip addresses valid for the origin of a charge report request
     *
     * @return a list of valid ip addresses
     */
    public List<String> getChargeReportValidIpAddresses() {
        return Arrays.asList("54.194.146.155", "54.194.13.190");
    }

    /**
     * Parse map of values (from request) into a charge report
     *
     * @param mapParameters should contain the parameters sent from the fonix server
     *                      *
     * @return the fully build charge report
     */
    public ChargeReport parseChargeReport(Map<String, String> mapParameters) {
        ChargeReport chargeReport = new ChargeReport();

        chargeReport.setMobileNumber(mapParameters.get(CR_MOBILE_NUMBER));
        String guid = mapParameters.get(CR_GUID);
        chargeReport.setGuid(guid);
        chargeReport.setIfVersion(mapParameters.get(CR_IFVERSION));

        chargeReport.setOperator(mapParameters.get(CR_OPERATOR));
        chargeReport.setStatusCode(mapParameters.get(CR_STATUS_CODE));
        chargeReport.setChargeMethod(mapParameters.get(CR_CHARGE_METHOD));
        chargeReport.setRequestId(mapParameters.get(CR_REQUEST_ID));
        chargeReport.setContract(mapParameters.get(CR_CONTRACT));

        if (NumberUtils.isParsable(mapParameters.get(CR_DURATION))) {
            chargeReport.setDuration(Integer.parseInt(mapParameters.get(CR_DURATION)));
        }

        if (NumberUtils.isParsable(mapParameters.get(CR_RETRY_COUNT))) {
            chargeReport.setRetryCount(Integer.parseInt(mapParameters.get(CR_RETRY_COUNT)));
        }

        if (mapParameters.containsKey(CR_STATUS_TIME)) {
            chargeReport.setStatusTime(fonixDateParser.getParsedDate(CR_STATUS_TIME, mapParameters.get(CR_STATUS_TIME), guid));
        }

        chargeReport.setStatusText(translator.translateChargeReportStatusText(chargeReport.getOperator(), chargeReport.getStatusCode(), chargeReport.getChargeMethod(), mapParameters.get(CR_STATUS_TEXT)));

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
     * @param dummyMode      when dummyMode is true the message will not actually be sent
     * @return details of the carrier billing request
     * @throws FonixBadRequestException      if there is a problem with the values sent in the request
     * @throws FonixNotAuthorizedException   if authentication fails
     * @throws FonixUnexpectedErrorException if a unexpected error occurs on the Fonix platform
     * @see CarrierBilling
     */
    public CarrierBillingResponse chargeMobile(CarrierBilling carrierBilling, boolean dummyMode) throws FonixException {

        List<NameValuePair> formParams = buildFormParams(carrierBilling);

        if (dummyMode) {
            formParams.add(new BasicNameValuePair("DUMMY", "yes"));
        }

        log.info("chargeMobile: form params: {}", formParams);

        WellRestedResponse responseVO = doRequest(formParams, CHARGE_MOBILE_ENDPOINT);

        if (responseVO.getStatusCode() != 200) {
            throwExceptionFromResponse(responseVO);
        }

        return CarrierBillingResponse.from(responseVO.fromJson().castTo(InternalCarrierBillingResponse.class));
    }

    private List<NameValuePair> buildFormParams(CarrierBilling carrierBilling) throws FonixException {
        List<NameValuePair> formParams = new ArrayList<>();

        formParams.add(new BasicNameValuePair("NUMBERS", carrierBilling.getMobileNumber()));
        formParams.add(new BasicNameValuePair("ORIGINATOR", carrierBilling.getFrom()));
        formParams.add(new BasicNameValuePair("CHARGEDESCRIPTION", carrierBilling.getChargeDescription()));

        validateAmount(carrierBilling.getAmountInPence());

        formParams.add(new BasicNameValuePair("AMOUNT", String.valueOf(carrierBilling.getAmountInPence())));

        if (StringUtils.isNotBlank(carrierBilling.getBody())) {
            formParams.add(new BasicNameValuePair("BODY", carrierBilling.getBody()));
        }

        if (carrierBilling.getTtl() != null) {
            formParams.add(new BasicNameValuePair("TIMETOLIVE", String.valueOf(carrierBilling.getTtl())));
        }

        formParams.add(new BasicNameValuePair("CHARGESILENT", "no"));

        if (BooleanUtils.isTrue(carrierBilling.getSmsFallback())) {
            formParams.add(new BasicNameValuePair("SMSFALLBACK", "yes"));
        }

        if (BooleanUtils.isTrue(carrierBilling.getNoRetry())) {
            formParams.add(new BasicNameValuePair("NORETRY", "yes"));
        }

        // we explicitly check if this was set to false and not just null
        if (BooleanUtils.isFalse(carrierBilling.getNetworkRetry())) {
            formParams.add(new BasicNameValuePair("NETWORKRETRY", "no"));
        }

        if (StringUtils.isNotBlank(carrierBilling.getRequestId())) {
            formParams.add(new BasicNameValuePair("REQUESTID", carrierBilling.getRequestId()));
        }

        return formParams;
    }

}
