package lindar.fonix.vo;


import lombok.Getter;
import lombok.ToString;

@ToString
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
    private String body;
    @Getter
    private String requestId;
    @Getter
    private Integer ttl;
    @Getter
    private String currency;
    @Getter
    private Boolean smsFallback;
    @Getter
    private Boolean noRetry;
    @Getter
    private Boolean networkRetry;


    private CarrierBilling(Builder builder) {
        this.mobileNumber = builder.mobileNumber;
        this.amountInPence = builder.amountInPence;
        this.from = builder.from;
        this.chargeDescription = builder.chargeDescription;
        this.body = builder.body;
        this.requestId = builder.requestId;
        this.ttl = builder.ttl;
        this.currency = builder.currency;
        this.smsFallback = builder.smsFallback;
        this.noRetry = builder.noRetry;
        this.networkRetry = builder.networkRetry;
    }

    public static class Builder {
        private String mobileNumber;
        private int amountInPence;
        private String from;
        private String chargeDescription;
        private String body;
        private String requestId;
        private Integer ttl;
        private String currency;
        private Boolean smsFallback;
        private Boolean noRetry;
        private Boolean networkRetry;

        public Builder(String mobileNumber, int amountInPence, String from, String chargeDescription, String smsBody) {
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
         * @return the builder to keep on building
         */
        public Builder withTimeToLive(int ttl) {
            this.ttl = ttl;
            return this;
        }


        /**
         * Fallback to premium sms charge if carrier billing fails
         *
         * @return the builder to keep on building
         */
        public Builder withSmsFallback() {
            this.smsFallback = true;
            return this;
        }

        /**
         * If NORETRY=yes is requested in combination with SMSFALLBACK=yes, then we will only attempt to charge the user through a premium SMS message ONCE.
         * This is useful where you require a quick turn-around of your billing requests, but still want to use SMS fallback.
         * Default is NORETRY=no, meaning we retry to bill the customer through SMS for a period of time.
         *
         * @return the builder to keep on building
         */
        public Builder withNoRetry() {
            this.noRetry = true;
            return this;
        }

        /**
         * Default is NETWORKRETRY=yes. If set to NO, please make sure that the operator is specified in the request i.e. NUMBERS=eeora-uk.447123456789.
         * When you send a request to us, we first check if we had successful premium transaction from the Msisdn that you are trying to bill and if we do,
         * we’ll first try to bill the msisdn through the Operator on which we had the successful transaction and than,
         * if that Operator doesn’t recognise the Msisdn, through the other Operators.
         * If you are sure about to which Operator the Msisdn belongs to (i.e. you get the operator from header enrichment or from an MO to a shortcode),
         * you should submit NETWORKRETRY=NO to have a quicker response.
         *
         * @return the builder to keep on building
         */
        public Builder withNoNetworkRetry() {
            this.networkRetry = false;
            return this;
        }

        /**
         * Set a request id
         *
         * @param requestId must be eternally unique for your service and up to 80 characters (0-9, a-z, A-Z)
         * @return the builder to keep on building
         */
        public Builder withRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        /**
         * Set currency
         *
         * @param currency currently only GBP is supported
         * @return the builder to keep on building
         */
        public Builder withCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        /**
         * build a CarrierBilling from the builder configuration
         * *
         *
         * @return a fully built CarrierBilling for use with @See FonixCarrierBillingResource
         */
        public CarrierBilling build() {
            return new CarrierBilling(this);
        }

    }
}
