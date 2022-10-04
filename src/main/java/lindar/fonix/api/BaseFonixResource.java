package lindar.fonix.api;

import com.lindar.wellrested.WellRestedRequest;
import com.lindar.wellrested.vo.WellRestedResponse;
import lindar.acolyte.util.MapsAcolyte;
import lindar.acolyte.vo.Pair;
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
abstract class BaseFonixResource {

    private final String API_KEY_HEADER = "X-API-KEY";

    final boolean dummyMode;
    final String baseUrl;

    private Map<String, String> authenticationHeaders;

    BaseFonixResource(String baseUrl, String apiKey, boolean dummyMode) {
        this.baseUrl = baseUrl;
        this.dummyMode = dummyMode;
        this.authenticationHeaders = MapsAcolyte.mapOf(Pair.of(API_KEY_HEADER, apiKey));
    }

    final WellRestedResponse doRequest(List<NameValuePair> formParams, String endpoint) {
        return WellRestedRequest.builder().url(baseUrl + endpoint).globalHeaders(authenticationHeaders).build()
                .post().formParams(formParams).submit();
    }

    void validateAmount(int amountInPence) throws FonixBadRequestException {
        if (amountInPence <= 0) {
            throw new FonixBadRequestException("INVALID_AMOUNT", "Amount must be greater than 0");
        }

        if ((float) amountInPence % 100.f > 0) {
            log.warn("Not all providers support billing in penny increments");
        }
    }

    void throwExceptionFromResponse(WellRestedResponse responseVO) throws FonixException {

        if (responseVO.getStatusCode() == 400) {
            InternalFailureResponse internalFailureResponse = responseVO.fromJson().castTo(InternalFailureResponse.class);
            throw new FonixBadRequestException(internalFailureResponse.getFailure().getFailcode(), internalFailureResponse.getFailure().getParameter());
        }

        if (responseVO.getStatusCode() == 403) {
            throw new FonixNotAuthorizedException();
        }

        throw new FonixUnexpectedErrorException(responseVO.getStatusCode(), responseVO.getServerResponse());
    }
}
