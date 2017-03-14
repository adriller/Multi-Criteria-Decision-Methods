package VIKOR;

import java.util.Comparator;

public class SensorComparatorS implements Comparator<SensorVikor>{

	@Override
	public int compare(SensorVikor o1, SensorVikor o2) {
		
		return o1.getS() < o2.getS() ? 1
		         : o1.getS() > o2.getS() ? -1
		         : 0;
	}

}
