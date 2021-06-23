package lindar.fonix.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MobileOriginatedSms {
    private String  guid;
    private String  ifVersion;
    private String  operator;
    private String  mobileNumber;
    private String  destination;
    private Date    receiveTime;
    private Integer price;
    private Integer duration;
    private Integer retryCount;
    private String  body;
}
