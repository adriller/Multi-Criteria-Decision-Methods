/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELECTREII;

import java.util.Comparator;

/**
 *
 * @author adriller
 */
public class SensorComparator implements Comparator<SensorELECTREII>{
        @Override
        public int compare(SensorELECTREII o1, SensorELECTREII o2) {

            return o1.getElectreValue() < o2.getElectreValue() ? 1
                    : o1.getElectreValue() > o2.getElectreValue() ? -1
                    : 0;
        }
    
}
