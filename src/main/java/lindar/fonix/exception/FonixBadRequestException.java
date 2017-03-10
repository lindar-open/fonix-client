package lindar.fonix.exception;

import lombok.Getter;

/**
 * Created by Steven on 10/03/2017.
 */
public class FonixBadRequestException extends FonixException {
    @Getter
    private final String errorCode;
    @Getter
    private final String parameter;

    public FonixBadRequestException(String errorCode){
        super("Fonix request failed with code [" + errorCode + "]");
        this.errorCode = errorCode;
        this.parameter = "";
    }

    public FonixBadRequestException(String errorCode, String parameter){
        super("Fonix request failed with code [" + errorCode + "] and parameter [" + parameter + "]");
        this.errorCode = errorCode;
        this.parameter = parameter;
    }

}
