package lindar.fonix.exception;

import lombok.Getter;

/**
 * Created by Steven on 10/03/2017.
 */
public class FonixUnexpectedErrorException extends FonixException {

    @Getter
    private final int statusCode;
    @Getter
    private final String serverResponse;

    public FonixUnexpectedErrorException(int statusCode, String serverResponse){
        super("Fonix unexpected error occured with status code ["+statusCode+"] and response ["+serverResponse+"]");
        this.serverResponse = serverResponse;
        this.statusCode = statusCode;
    }


}
