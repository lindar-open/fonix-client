package lindar.fonix;

import lindar.acolyte.vo.AccessCredentials;
import lindar.fonix.api.FonixSmsResource;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Steven on 09/03/2017.
 */
@Slf4j
public class FonixClient {

    private final FonixSmsResource fonixSmsResource;

    public FonixClient(String apiKey) {
        fonixSmsResource = new FonixSmsResource(apiKey);
    }


    public FonixClient(String apiKey, boolean dummyMode) {
        fonixSmsResource = new FonixSmsResource(apiKey, dummyMode);
    }

    public FonixSmsResource sms(){
        return fonixSmsResource;
    }
}
