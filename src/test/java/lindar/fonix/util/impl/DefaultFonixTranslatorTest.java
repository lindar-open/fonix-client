package lindar.fonix.util.impl;

import lindar.fonix.util.FonixTranslator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultFonixTranslatorTest {

    @Test
    public void testEE_errorMapping(){
        FonixTranslator translator = new DefaultFonixTranslator();
        assertEquals("Sorry you've reached your monthly spending limit on Charge to Mobile services, which O2 have to protect their new customers. You won’t be able to charge this type of service to your O2 bill again, until next month", translator.translateChargeReportStatusText("o2-uk", "MAX_SPEND_MSISDN", "psms", "default"));
        assertEquals("default", translator.translateChargeReportStatusText("o2-uk", "SOME_OTHER_CODE", "direct", "default"));
    }

    @Test
    public void testO2_errorMapping(){
        FonixTranslator translator = new DefaultFonixTranslator();
        assertEquals("You are unable to use this service, please contact your mobile provider.", translator.translateChargeReportStatusText("eeora-uk", "TEMPORARY_BARRED", "psms", "default"));
        assertEquals("You are unable to use this service; you have a 3rd party bar. To remove the bar text UNBAR to 150.", translator.translateChargeReportStatusText("eeora-uk", "TEMPORARY_BARRED", "direct", "default"));
        assertEquals("You have reached your charge to bill limit. Further attempts to add charges to your bill will fail. The limit will reset on your next bill date.", translator.translateChargeReportStatusText("eeora-uk", "OPERATOR_SRV_MONTHLY_MAX_SPEND", "direct", "default"));
        assertEquals("default", translator.translateChargeReportStatusText("eeora-uk", "SOME_OTHER_CODE", "direct", "default"));
    }

}