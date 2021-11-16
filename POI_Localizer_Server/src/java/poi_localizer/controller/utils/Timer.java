package poi_localizer.controller.utils;
import java.util.Calendar;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class Timer {
    
    private static Calendar cal = Calendar.getInstance();
    
    private Timer(){}
    
    public static Timestamp getTimestamp(int year, int month, int day, int hour,
            int minutes, int seconds)
    {
        Timestamp tmp = new Timestamp(year-1900, month, day, hour, minutes, seconds, 0);
        return tmp;
    }
    
    public static Date getDate(int year, int month, int day, int hour,
            int minutes, int seconds)
    {
        return new Date(getTimestamp(year, month, day, hour, minutes, seconds).getTime());
    }
    
    public static Timestamp getTimestamp()
    {
        long time = getDate().getTime();
        return new Timestamp(time);
    }
    
    public static Date getDate()
    {
        return new Date(cal.get(Calendar.YEAR)-1900, cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
    }
    
}
