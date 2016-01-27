package spot.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Provides a time stamp.
 * Format example: "2016-01-27 09:20:06" 
 * 
 * @author kocar
 *
 */
public class TimeStamp {

	public static String getTimeStamp() {
		// Date object
		Date date = new Date();
		
		// getTime() returns current time in milliseconds
		long time = date.getTime();
		
		// Passed the milliseconds to constructor of Timestamp class
		Timestamp ts = new Timestamp(time);
		
		return ts.toString();
	}
}
