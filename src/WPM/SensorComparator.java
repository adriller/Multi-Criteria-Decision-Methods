/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WPM;
import WPM.SensorWPM;
import java.util.Comparator;
/**
 *
 * @author adriller
 */
public class SensorComparator implements Comparator<SensorWPM>{
	@Override
	public int compare(SensorWPM o1, SensorWPM o2) {
		
		return o1.getWPMValue() < o2.getWPMValue() ? 1
		         : o1.getWPMValue() > o2.getWPMValue() ? -1
		         : 0;
	}
}