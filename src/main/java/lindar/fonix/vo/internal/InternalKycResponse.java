package lindar.fonix.vo.internal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class InternalKycResponse {

    private Completed completed;

    @Data
    public static class Completed {
        @SerializedName("ifversion")
        @Expose
        private String ifversion;
        @SerializedName("statuscode")
        @Expose
        private String statuscode;
        @SerializedName("statustext")
        @Expose
        private String statustext;
        @SerializedName("guid")
        @Expose
        private String guid;
        @SerializedName("requestid")
        @Expose
        private String requestid;
        @SerializedName("status_time")
        @Expose
        private String statusTime;
        @SerializedName("first_name_match")
        @Expose
        private Boolean firstNameMatch;
        @SerializedName("last_name_match")
        @Expose
        private Boolean lastNameMatch;
        @SerializedName("full_name_match")
        @Expose
        private Boolean fullNameMatch;
        @SerializedName("postcode_match")
        @Expose
        private Boolean postcodeMatch;
        @SerializedName("house_match")
        @Expose
        private Boolean houseMatch;
        @SerializedName("full_address_match")
        @Expose
        private Boolean fullAddressMatch;
        @SerializedName("birthday_match")
        @Expose
        private Boolean birthdayMatch;
        @SerializedName("is_stolen")
        @Expose
        private Boolean isStolen;
        @SerializedName("contract_type")
        @Expose
        private String contractType;
    }
}
