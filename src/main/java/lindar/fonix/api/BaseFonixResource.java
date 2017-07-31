package lindar.fonix.api;

import com.google.common.collect.ImmutableMap;
import com.lindar.wellrested.WellRestedRequest;
import com.lindar.wellrested.vo.WellRestedResponse;
import lindar.fonix.exception.FonixBadRequestException;
import lindar.fonix.exception.FonixException;
import lindar.fonix.exception.FonixNotAuthorizedException;
import lindar.fonix.exception.FonixUnexpectedErrorException;
import lindar.fonix.vo.internal.InternalFailureResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseFonixResource {

    private final String API_KEY_HEADER = "X-API-KEY";
    private final String BASE_URL = "https://sonar.fonix.io/v2/";

    protected final boolean dummyMode;

    protected Map<String, String> authenticationHeaders;

    public BaseFonixResource(String apiKey, boolean dummyMode) {
        this.dummyMode = dummyMode;
        this.authenticationHeaders = ImmutableMap.<String, String>builder().put(API_KEY_HEADER, apiKey).build();
    }

    protected final WellRestedResponse doRequest(List<NameValuePair> formParams, String endpoint){
        return WellRestedRequest.build(BASE_URL + endpoint).post(formParams, authenticationHeaders);
    }

    protected void validateAmount(int amountInPence) throws FonixBadRequestException {
        if(amountInPence <= 0){
            throw new FonixBadRequestException("INVALID_AMOUNT", "Amount must be greater than 0");
        }

        if(amountInPence > 0 && (float)amountInPence % 100.f > 0){
            log.warn("Not all providers support billing in penny increments");
        }
    }

    protected void throwExceptionFromResponse(WellRestedResponse responseVO) throws FonixException {

        if(responseVO.getStatusCode() == 400){
            InternalFailureResponse internalFailureResponse = responseVO.fromJson().castTo(InternalFailureResponse.class);
            throw new FonixBadRequestException(internalFailureResponse.getFailure().getFailcode(), internalFailureResponse.getFailure().getParameter());
        }

        if(responseVO.getStatusCode() == 403){
            throw new FonixNotAuthorizedException();
        }

        throw new FonixUnexpectedErrorException(responseVO.getStatusCode(), responseVO.getServerResponse());
    }
}
