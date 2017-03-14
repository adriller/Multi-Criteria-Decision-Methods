package VIKOR;

import java.util.Comparator;

public class SensorComparatorR implements Comparator<SensorVikor>{

	@Override
	public int compare(SensorVikor o1, SensorVikor o2) {
		
		return o1.getR() < o2.getR() ? 1
		         : o1.getR() > o2.getR() ? -1
		         : 0;
	}

}
