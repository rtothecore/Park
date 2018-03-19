package com.NTS.Utils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Time of the calculation process
 * @author liudy
 *
 */
public class TimerCountTools {
	private Date timestart = null;
	private Date timestop = null;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public TimerCountTools()
	{
		
	}
	
	public void start()
	{
		timestart = new Date(System.currentTimeMillis());
	}
	
	public void stop()
	{
		timestop = new Date(System.currentTimeMillis());
	}
	
	/**
	 * Returns the time difference,Unit: ms
	 * @return
	 */
	public long getProcessTime()
	{
		long mils = timestop.getTime() - timestart.getTime();
		return mils;
	}
}
