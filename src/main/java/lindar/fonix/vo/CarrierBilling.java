package lindar.fonix.vo;


import lombok.Getter;

/**
 * Created by Steven on 14/03/2017.
 */
public class CarrierBilling {
    @Getter
    private String mobileNumber;
    @Getter
    private int amountInPence;
    @Getter
    private String from;
    @Getter
    private String chargeDescription;
    @Getter
    private Boolean chargeSilently;
    @Getter
    private String body;
    @Getter
    private String requestId;
    @Getter
    private Integer ttl;
    @Getter
    private String currency;
    @Getter
    private Boolean smsFallback;

    private CarrierBilling(Builder builder){
        this.mobileNumber = builder.mobileNumber;
        this.amountInPence = builder.amountInPence;
        this.from = builder.from;
        this.chargeDescription = builder.chargeDescription;
        this.chargeSilently = builder.chargeSilently;
        this.body = builder.body;
        this.requestId = builder.requestId;
        this.ttl = builder.ttl;
        this.currency = builder.currency;
        this.smsFallback = builder.smsFallback;
    }

    public static class Builder {
        private String mobileNumber;
        private int amountInPence;
        private String from;
        private String chargeDescription;
        private Boolean chargeSilently;
        private String body;
        private String requestId;
        private Integer ttl;
        private String currency;
        private Boolean smsFallback;

        public Builder(String mobileNumber, int amountInPence, String from, String chargeDescription, String smsBody){
            this.mobileNumber = mobileNumber;
            this.amountInPence = amountInPence;
            this.from = from;
            this.chargeDescription = chargeDescription;
            this.body = smsBody;
        }


        /**
         * Set the expiration of the request.
         * If this period expires before a bill attempt was made, then a ‘failed Charge Report’ will be reported to you
         *
         * @param ttl minutes till request expires.
         *            Minimum value: 10 minutes, Maximum value: 4320 minutes, Default value: 90 minutes.
         *
         * @return the builder to keep on building
         */
        public Builder withTimeToLive(int ttl){
            this.ttl = ttl;
            return this;
        }


        /**
         * Fallback to premium sms charge if carrier billing fails
         *
         * @return the builder to keep on building
         */
        public Builder withSmsFallback(){
            this.smsFallback = true;
            return this;
        }

        /**
         * Set a request id
         *
         * @param requestId must be eternally unique for your service and up to 80 characters (0-9, a-z, A-Z)
         *
         * @return the builder to keep on building
         */
        public Builder withRequestId(String requestId){
            this.requestId = requestId;
            return this;
        }

        /**
         * Set currency
         *
         * @param currency currently only GBP is supported
         *
         * @return the builder to keep on building
         */
        public Builder withCurrency(String currency){
            this.currency = currency;
            return this;
        }

        /**
         * build a CarrierBilling from the builder configuration
         **
         * @return a fully built CarrierBilling for use with @See FonixCarrierBillingResource
         */
        public CarrierBilling build(){
            return new CarrierBilling(this);
        }

    }
}
