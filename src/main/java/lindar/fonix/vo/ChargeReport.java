package lindar.fonix.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ChargeReport {

    /*
    * The API version in use.
    */
    private String ifVersion;

    /*
    * The mobile number of the user who was charged. The format is MONUMBER=447111122222.
    */
    private String mobileNumber;

    /*
    * The mobile operator code of the user who was charged.
    */
    private String operator;

    /*
    * The method of charging that was used. Possible values: direct or psms.
    */
    private String chargeMethod;

    /*
    * The unique identifier assigned by us for this charge.
    */
    private String guid;

    /*
    * The duration in seconds it took to apply the charge or the duration it took to find out that it failed.
    */
    private Integer duration;

    /*
    * This parameter appears if a request is retried to be sent to you. It increases with every retry.
    */
    private Integer retryCount;

    /*
    * A code used to advise success or failure of your Charge Mobile request.
    */
    private String statusCode;

    /*
    * A description to provide more information about the status code.
    */
    private String statusText;

    /*
    * If you submitted your Charge Mobile request with a unique request id,
    * this field will contain it in return to help you identify the charge.
    */
    private String requestId;

    /*
    * Timestamp when the Charge Report was created
    */
    private Date statusTime;

    /*
     * The type of contract
     */
    private String contract;



    public boolean wasChargedSuccessfully(){
        return "DELIVERED".equalsIgnoreCase(statusCode);
    }

}
