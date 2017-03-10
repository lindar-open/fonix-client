package lindar.fonix.exception;


/**
 * Created by Steven on 10/03/2017.
 */
public class FonixNotAuthorizedException extends FonixException {

    public FonixNotAuthorizedException(){
        super("Fonix request is not authorized. Double check your API key");
    }


}
