package lindar.fonix.vo;

import lindar.fonix.vo.internal.InternalKycResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycResponse {
    private static final SimpleDateFormat STATUS_TIME_FORMAT = new SimpleDateFormat("yyyyMMddhhmmss");

    private String  ifVersion;
    private String  statusCode;
    private String  statusText;
    private String  guid;
    private String  requestId;
    private Date    statusTime;
    private Boolean firstNameMatch;
    private Boolean lastNameMatch;
    private Boolean fullNameMatch;
    private Boolean postcodeMatch;
    private Boolean houseMatch;
    private Boolean fullAddressMatch;
    private Boolean birthdayMatch;
    private Boolean isStolen;
    private String  contractType;

    public static KycResponse from(InternalKycResponse internalKycResponse) {
        return new KycResponse(
                internalKycResponse.getCompleted().getIfversion(),
                internalKycResponse.getCompleted().getStatuscode(),
                internalKycResponse.getCompleted().getStatustext(),
                internalKycResponse.getCompleted().getGuid(),
                internalKycResponse.getCompleted().getRequestid(),
                parseDate(internalKycResponse.getCompleted().getStatusTime()),
                internalKycResponse.getCompleted().getFirstNameMatch(),
                internalKycResponse.getCompleted().getLastNameMatch(),
                internalKycResponse.getCompleted().getFullNameMatch(),
                internalKycResponse.getCompleted().getPostcodeMatch(),
                internalKycResponse.getCompleted().getHouseMatch(),
                internalKycResponse.getCompleted().getFullAddressMatch(),
                internalKycResponse.getCompleted().getBirthdayMatch(),
                internalKycResponse.getCompleted().getIsStolen(),
                internalKycResponse.getCompleted().getContractType()
        );
    }

    private static Date parseDate(String date) {
        try {
            return STATUS_TIME_FORMAT.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
