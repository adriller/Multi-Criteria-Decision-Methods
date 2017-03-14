package TOPSIS;

import java.util.Comparator;

public class SensorComparator implements Comparator<SensorTOPSIS>{

	@Override
	public int compare(SensorTOPSIS o1, SensorTOPSIS o2) {
		
		return o1.getCloseness() < o2.getCloseness() ? 1
		         : o1.getCloseness() > o2.getCloseness() ? -1
		         : 0;
	}

}
