package lindar.fonix.util;


/**
 * Created by Steven on 15/03/2017.
 */
public interface FonixTranslator {
    String translateOperatorCode(String operatorCode);
    String translateChargeReportStatusText(String operatorCode, String statusCode, String defaultError);
}
