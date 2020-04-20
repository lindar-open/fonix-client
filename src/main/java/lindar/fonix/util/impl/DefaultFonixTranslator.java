package lindar.fonix.util.impl;

import lindar.acolyte.util.MapsAcolyte;
import lindar.acolyte.vo.Pair;
import lindar.fonix.util.FonixTranslator;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class DefaultFonixTranslator implements FonixTranslator {
    private static final String              O2_UK_OPERATOR = "o2-uk";
    private static final Map<String, String> OPERATOR_CODES = MapsAcolyte.mapOf(
            Pair.of("three-uk", "Three UK"),
            Pair.of("eeora-uk", "EE (Orange) UK"),
            Pair.of("eetmo-uk", "EE (T-Mobile) UK"),
            Pair.of("voda-uk", "Vodafone UK"),
            Pair.of("o2-uk", "O2 UK"),
            Pair.of("virgin-uk", "Virgin Media UK")
    );

    private static final String OPERATOR_UNKNOWN = "Operator unknown";

    private static final Map<String, String> O2_ERROR_MESSAGE = MapsAcolyte.mapOf(
            Pair.of("MAX_SPEND_MSISDN",
                    "Sorry you've reached your monthly spending limit on Charge to Mobile services, which O2 have to protect their new customers. You won’t be able to charge this type of service to your O2 bill again, until next month"),
            Pair.of("SECTOR_NOT_ALLOWED", "Service temporarily unavailable; try again later"),
            Pair.of("OPERATOR_SRV_DAILY_MAX_SPEND", "Sorry, you've spent all you can today, come back tomorrow"),
            Pair.of("OPERATOR_SRV_WEEKLY_MAX_SPEND", "Sorry, you've spent all you can this week, come back next week"),
            Pair.of("OPERATOR_SRV_MONTHLY_MAX_SPEND", "Sorry, you've spent all you can this month, come back next month"),
            Pair.of("OPERATOR_SRV_TYPE_DAILY_MAX_SPEND", "Sorry, you've spent all you can today on these things"),
            Pair.of("OPERATOR_SRV_TYPE_WEEKLY_MAX_SPEND", "Sorry, you've spent all you can this week on these things"),
            Pair.of("OPERATOR_SRV_TYPE_MONTHLY_MAX_SPEND", "Sorry, you've spent all you can this month, on these things")
    );

    @Override
    public String translateOperatorCode(String operatorCode) {
        return OPERATOR_CODES.getOrDefault(operatorCode, OPERATOR_UNKNOWN);
    }

    @Override
    public String translateChargeReportStatusText(String operatorCode, String statusCode, String defaultError) {
        if (O2_UK_OPERATOR.equalsIgnoreCase(operatorCode)) {
            return StringUtils.defaultIfBlank(O2_ERROR_MESSAGE.get(statusCode), defaultError);
        }
        return defaultError;
    }
}
