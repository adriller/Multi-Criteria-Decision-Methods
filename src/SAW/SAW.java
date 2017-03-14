package SAW;

import commom.Attribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SAW {

    public void rank(List<SensorSAW> sensors) {
        SensorSAW temp;
        Collections.sort(sensors, new SensorComparator());

//        boolean flag = false;
//        int print_size = (int) (sensors.size() * 0.1);
//
//        // for (SensorSAW obj : sensors) {
//        for (int i = 0; i < print_size; i++) {
//            SensorSAW obj = sensors.get(i);
//            // System.out.println("Id: " + sensor.getId() + "\t s_value: "
//            // + sensor.getSawValue());
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

    public void score(HashMap<String, Double> weights, List<SensorSAW> sensors) {

        for (SensorSAW s : sensors) {
            List<Attribute> lattr = s.getAttributes();
            for (Attribute attr : lattr) {
                double weight = weights.get(attr.getName());
                double svalue = s.getSawValue();
                svalue += attr.getNormalizedValue() * weight;
                s.setSawValue(svalue);
            }
        }
    }

    public void normalize(List<SensorSAW> sensors) {

        HashMap<String, Double> mMax = new HashMap<String, Double>();
        HashMap<String, Double> mMin = new HashMap<String, Double>();

        for (SensorSAW s : sensors) {
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

        for (SensorSAW s : sensors) {
            List<Attribute> lattr = s.getAttributes();

            for (Attribute attr : lattr) {
                if (attr.getObjective() == "min") {
                    Double normalized = (mMax.get(attr.getName()) - attr
                            .getValue())
                            / (mMax.get(attr.getName()) - mMin.get(attr
                            .getName()));
                    attr.setNormalizedValue(normalized);
                } else if (attr.getObjective() == "max") {
                    Double normalized = (attr.getValue() - mMin.get(attr
                            .getName()))
                            / (mMax.get(attr.getName()) - mMin.get(attr
                            .getName()));
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
