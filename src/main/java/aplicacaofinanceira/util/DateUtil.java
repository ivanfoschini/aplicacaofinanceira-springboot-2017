package aplicacaofinanceira.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            return formatter.format(date);	
        } catch (Exception e) {
            return null;
        }
    }
}