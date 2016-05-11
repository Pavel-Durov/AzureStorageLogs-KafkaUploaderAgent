package Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String Parse(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return  dateFormat.format(date);
    }

    public static Date Parse(String str) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        Date result = null;
        try {
            result = df.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
