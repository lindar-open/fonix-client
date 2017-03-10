package lindar.fonix.vo.internal;

import lombok.Data;

/**
 * Created by Steven on 09/03/2017.
 */
@Data
public class InternalFailureResponse {

    private Failure failure;

    @Data
    public class Failure {
        private String parameter;
        private String failcode;
    }
}
