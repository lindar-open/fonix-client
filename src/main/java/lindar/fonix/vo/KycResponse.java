package lindar.fonix.vo;

import lindar.fonix.util.FonixDateParser;
import lindar.fonix.vo.internal.InternalKycResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycResponse {
    private static final FonixDateParser FONIX_DATE_PARSER = new FonixDateParser();

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
        InternalKycResponse.Completed kycResponseCompleted = internalKycResponse.getCompleted();
        Date statusDateTime = FONIX_DATE_PARSER.getParsedDate("status time", kycResponseCompleted.getStatusTime(), kycResponseCompleted.getGuid());
        return new KycResponse(
                kycResponseCompleted.getIfversion(),
                kycResponseCompleted.getStatuscode(),
                kycResponseCompleted.getStatustext(),
                kycResponseCompleted.getGuid(),
                kycResponseCompleted.getRequestid(),
                statusDateTime,
                kycResponseCompleted.getFirstNameMatch(),
                kycResponseCompleted.getLastNameMatch(),
                kycResponseCompleted.getFullNameMatch(),
                kycResponseCompleted.getPostcodeMatch(),
                kycResponseCompleted.getHouseMatch(),
                kycResponseCompleted.getFullAddressMatch(),
                kycResponseCompleted.getBirthdayMatch(),
                kycResponseCompleted.getIsStolen(),
                kycResponseCompleted.getContractType()
        );
    }

}
