package SAW;

import java.util.Comparator;

public class SensorComparator implements Comparator<SensorSAW>{

	@Override
	public int compare(SensorSAW o1, SensorSAW o2) {
		
		return o1.getSawValue() < o2.getSawValue() ? 1
		         : o1.getSawValue() > o2.getSawValue() ? -1
		         : 0;
	}
}
