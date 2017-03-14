/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WSM;
import WSM.SensorWSM;
import commom.Attribute;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author adriller
 */
public class WSM {
    public void rank(List<SensorWSM> sensors) {
        SensorWSM temp;
        Collections.sort(sensors, new SensorComparator());
//        boolean flag = false;
//        int print_size = (int) (sensors.size() * 0.1);
//
//        // for (SensorWSM obj : sensors) {
//        for (int i = 0; i < print_size; i++) {
//            SensorWSM obj = sensors.get(i);
//            // System.out.println("Id: " + sensor.getId() + "\t s_value: "
//            // + sensor.getWSMValue());
//
//            // System.out.printf("%d\t%.2f\t%.2f\n",sensor.getId(),
//            // sensor.getAttributes().get(0).getValue(),sensor.getAttributes().get(1).getValue());
//
//            if (flag == false) {
//                for (Attribute attr : obj.getAttributes()) {
//                    System.out.print("\t" + attr.getName());
//                }
//                System.out.print("\n");
//                flag = true;
//            }
//
//            for (Attribute attr : obj.getAttributes()) {
//                System.out.print("\t" + attr.getValue());
//            }
//            System.out.print("\n");
//
//        }
    }
    public void score(HashMap<String, Double> weights, List<SensorWSM> sensors) {
        for (SensorWSM s : sensors) {
            List<Attribute> lattr = s.getAttributes();
            for (Attribute attr : lattr) {
                double weight = weights.get(attr.getName());
                double svalue = s.getWSMValue();
                svalue += attr.getNormalizedValue() * weight;
                s.setWSMValue(svalue);
            }
        }
    }
    public void normalize(List<SensorWSM> sensors) {
        HashMap<String, Double> mMax = new HashMap<String, Double>();
        HashMap<String, Double> mMin = new HashMap<String, Double>();
        for (SensorWSM s : sensors) {
            List<Attribute> lattr = s.getAttributes();
            if (mMax.isEmpty() && mMin.isEmpty()) {
                for (Attribute attr : lattr) {
                    mMax.put(attr.getName(), Double.MIN_VALUE);
                    mMin.put(attr.getName(), Double.MAX_VALUE);
                }
            }
            for (Attribute attr : lattr) {
                if (attr.getValue() > mMax.get(attr.getName())) {
                    mMax.put(attr.getName(), attr.getValue());
                }
                if (attr.getValue() < mMin.get(attr.getName())) {
                    mMin.put(attr.getName(), attr.getValue());
                }
            }
        }
        for (SensorWSM s : sensors) {
            List<Attribute> lattr = s.getAttributes();
            for (Attribute attr : lattr) {
                if (attr.getObjective() == "min") {
                    Double normalized = (mMax.get(attr.getName()) - attr
                            .getValue());
                    attr.setNormalizedValue(normalized);
                } else if (attr.getObjective() == "max") {
                    Double normalized = attr.getValue();
                    attr.setNormalizedValue(normalized);
                }
                // System.out.println("Name: " + attr.getName() +
                // "\tTrue Value: "
                // + attr.getValue() + "\tNormalized Value: "
                // + attr.getNormalizedValue());
            }
        }
    }
}