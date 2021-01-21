package com.yatra.commonworkflows;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Stopwatch;

/**
 * Stopwatch the elapsed/start time of the stop watch
 * 
 */
public class ExecutionTimer {
	private static Stopwatch sw = Stopwatch.createStarted();


	/**
	 * Returns the start time of the stop watch
	 * 
	 * @return time in seconds
	 */
	public static long startTime() {
		return sw.elapsed(TimeUnit.MILLISECONDS);
	}

	/**
	 * Returns the elapsed time of the stop watch.
	 * 
	 * @param startTime
	 *            - start time
	 * 
	 * @return elapsed time in seconds
	 */
	public static double elapsedTime(long startTime) {
		long timetaken = sw.elapsed(TimeUnit.MILLISECONDS) - startTime;
		Long l = new Long(timetaken);
		double d= l.doubleValue();
		return  d/1000;		
	}

}
