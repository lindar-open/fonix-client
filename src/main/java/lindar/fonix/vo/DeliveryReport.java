package lindar.fonix.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by Steven on 16/03/2017.
 */
@Data
public class DeliveryReport {
    private String ifVersion;
    private String operator;
    private String mobileNumber;
    private String destination;
    private String guid;
    private Date receiveTime;
    private Integer price;
    private Integer duration;
    private Integer retryCount;
    private Date statusTime;
    private String statusCode;
    private String statusText;
}
