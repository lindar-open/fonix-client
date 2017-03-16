package lindar.fonix.util.impl;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import lindar.fonix.util.FonixTranslator;

import java.util.Map;

/**
 * Created by Steven on 16/03/2017.
 */
public class DefaultFonixTranslator implements FonixTranslator {
    private static final Map<String, String> OPERATOR_CODES = ImmutableMap.<String, String>builder()
            .put("three-uk", "Three UK")
            .put("eeora-uk", "EE (Orange) UK")
            .put("eetmo-uk", "EE (T-Mobile) UK")
            .put("voda-uk", "Vodafone UK")
            .put("o2-uk", "O2 UK")
            .put("virgin-uk", "Virgin Media UK")
            .build();

    private static final String OPERATOR_UNKNOWN = "Operator unknown";

    private static final ImmutableTable<String, String, String> OPERATOR_ERROR_MESSAGE = ImmutableTable.<String, String, String>builder()
            .put("o2-uk", "MAX_SPEND_MSISDN", "Sorry you've reached your monthly spending limit on Charge to Mobile services, which O2 have to protect their new customers. You won’t be able to charge this type of service to your O2 bill again, until next month")
            .put("o2-uk", "SECTOR_NOT_ALLOWED", "Service temporarily unavailable; try again later")
            .put("o2-uk", "OPERATOR_SRV_DAILY_MAX_SPEND", "Sorry, you've spent all you can today, come back tomorrow")
            .put("o2-uk", "OPERATOR_SRV_WEEKLY_MAX_SPEND", "Sorry, you've spent all you can this week, come back next week")
            .put("o2-uk", "OPERATOR_SRV_MONTHLY_MAX_SPEND", "Sorry, you've spent all you can this month, come back next month")
            .put("o2-uk", "OPERATOR_SRV_TYPE_DAILY_MAX_SPEND", "Sorry, you've spent all you can today on these things")
            .put("o2-uk", "OPERATOR_SRV_TYPE_WEEKLY_MAX_SPEND", "Sorry, you've spent all you can this week on these things")
            .put("o2-uk", "OPERATOR_SRV_TYPE_MONTHLY_MAX_SPEND", "Sorry, you've spent all you can this month, on these things")
            .build();

    @Override
    public String translateOperatorCode(String operatorCode){
        return OPERATOR_CODES.getOrDefault(operatorCode, OPERATOR_UNKNOWN);
    }

    @Override
    public String translateChargeReportStatusText(String operatorCode, String statusCode, String defaultError){
        return MoreObjects.firstNonNull(OPERATOR_ERROR_MESSAGE.get(operatorCode, statusCode), defaultError);
    }
}
