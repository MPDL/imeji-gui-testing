package spot.util;

import java.sql.Timestamp;
import java.util.Date;

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
