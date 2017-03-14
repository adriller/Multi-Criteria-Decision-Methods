package VIKOR;

import java.util.Comparator;

public class SensorComparatorQ implements Comparator<SensorVikor>{

	@Override
	public int compare(SensorVikor o1, SensorVikor o2) {
		
		return o1.getQ() < o2.getQ() ? 1
		         : o1.getQ() > o2.getQ() ? -1
		         : 0;
	}

}
