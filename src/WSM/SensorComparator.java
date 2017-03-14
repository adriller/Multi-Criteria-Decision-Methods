/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WSM;
/**
 *
 * @author adriller
 */
import java.util.Comparator;
public class SensorComparator implements Comparator<SensorWSM>{
	@Override
	public int compare(SensorWSM o1, SensorWSM o2) {
		
		return o1.getWSMValue() < o2.getWSMValue() ? 1
		         : o1.getWSMValue() > o2.getWSMValue() ? -1
		         : 0;
	}
}