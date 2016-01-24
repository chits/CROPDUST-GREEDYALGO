
/*
 * Author: Chitrali Rai
 * Class for representing each field station or airport
 */
public class DustStation {
	boolean dusted;
	boolean base = false;
	int startTime;
	Point point;
	
	DustStation(boolean _dusted, Point _point)
	{
		dusted = _dusted;
		point = _point;
	}
	/*
	 * Set station as a base
	 */
	void setBase()
	{
		base = true;
	}
	
	/*
	 * Whether station is a base or not
	 */
	boolean IsBase(){
		return base;
	}
	
	/*
	 * Whether the station is visited or not
	 */
	boolean IsDusted(){
		return dusted;
	}
	
	void setDusted()
	{
		dusted = true;
	}
	
	void setTime(int time)
	{
		startTime = time;
	}
	
	int getTime()
	{
		return startTime;
	}
	
	/*
	 * Getting the coordinates of the station
	 */
	Point getCordinates(){
		return point;
	}

}
