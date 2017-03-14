package TOPSIS;

import commom.Attribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TOPSIS {

    public static SensorTOPSIS bestSolution;
    public static SensorTOPSIS worstSolution;

    public void rank(List<SensorTOPSIS> sensors) {
        SensorTOPSIS temp;
        Collections.sort(sensors, new SensorComparator());

        boolean flag = false;
        int print_size = (int) (sensors.size() * 0.1);

//        // for (SensorSAW obj : sensors) {
//        for (int i = 0; i < print_size; i++) {
//            SensorTOPSIS obj = sensors.get(i);
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
//
////            System.out.print("\t" + obj.getCloseness());
//            System.out.print("\n");
//
//        }
    }

    public void closeness(List<SensorTOPSIS> sensors) {
        for (SensorTOPSIS s : sensors) {
            double dBest = 0.0, dWorst = 0.0;
            List<Attribute> lattr = s.getAttributes();
            for (int i = 0; i < lattr.size(); i++) {
                dBest += Math.pow(
                        (lattr.get(i).getNormalizedValue() - bestSolution
                        .getAttributes().get(i).getNormalizedValue()),
                        2);

                dWorst += Math.pow(
                        (lattr.get(i).getNormalizedValue() - worstSolution
                        .getAttributes().get(i).getNormalizedValue()),
                        2);
            }

            dBest = Math.pow(dBest, 0.5);
            dWorst = Math.pow(dWorst, 0.5);

            s.setCloseness(dWorst / (dBest + dWorst));
        }
    }

    /**
     * Third step Calculates the best and worst solutions
     *
     * @param sensors
     */
    public void idealSolutions(List<SensorTOPSIS> sensors) {

        // retrieves the list of attributes
        List<Attribute> bestAttr = new ArrayList<Attribute>();
        List<Attribute> worstAttr = new ArrayList<Attribute>();

        // handles with the retrieved list establishing the best and worst
        // attributes
        for (SensorTOPSIS s : sensors) {
            List<Attribute> lattr = s.getAttributes();

            for (int i = 0; i < lattr.size(); i++) {
                Attribute attr = lattr.get(i);

                if (bestAttr.size() == i && worstAttr.size() == i) {
                    Attribute bAttr = new Attribute(attr.getName(), attr.getObjective(), attr.getNormalizedValue());
                    bAttr.setNormalizedValue(attr.getNormalizedValue());
                    Attribute wAttr = new Attribute(attr.getName(), attr.getObjective(), attr.getNormalizedValue());
                    wAttr.setNormalizedValue(attr.getNormalizedValue());

                    bestAttr.add(bAttr);
                    worstAttr.add(wAttr);
                } else {

                    // minimize objective criteria
                    if (attr.getObjective() == "min") {
                        if (bestAttr.get(i).getNormalizedValue() > attr
                                .getNormalizedValue()) {
                            bestAttr.get(i).setNormalizedValue(
                                    attr.getNormalizedValue());
                        } else if (worstAttr.get(i).getNormalizedValue() < attr
                                .getNormalizedValue()) {
                            worstAttr.get(i).setNormalizedValue(
                                    attr.getNormalizedValue());
                        }
                    }

                    // maximize objective criteria
                    if (attr.getObjective() == "max") {
                        if (bestAttr.get(i).getNormalizedValue() < attr
                                .getNormalizedValue()) {
                            bestAttr.get(i).setNormalizedValue(
                                    attr.getNormalizedValue());
                        } else if (worstAttr.get(i).getNormalizedValue() > attr
                                .getNormalizedValue()) {
                            worstAttr.get(i).setNormalizedValue(
                                    attr.getNormalizedValue());
                        }
                    }
                }
            }
        }

        bestSolution = new SensorTOPSIS(0, bestAttr);
        worstSolution = new SensorTOPSIS(0, worstAttr);

//        for (Attribute a : bestSolution.attributes) {
//            System.out.print("\t" + a.getNormalizedValue());
//        }
//
//        System.out.print("\n");
//
//        for (Attribute a : worstSolution.attributes) {
//            System.out.print("\t" + a.getNormalizedValue());
//        }
    }

    /**
     * Second step multiply the normalized values by the desired weights
     *
     * @param weights map with the weight of each attribute
     * @param sensors the target population
     */
    public void score(HashMap<String, Double> weights,
            List<SensorTOPSIS> sensors) {

        for (SensorTOPSIS s : sensors) {
            List<Attribute> lattr = s.getAttributes();
            for (Attribute attr : lattr) {
                double weight = weights.get(attr.getName());
                attr.setNormalizedValue(attr.getNormalizedValue() * weight);
            }
        }
    }

    /**
     * First step to TOPSIS
     *
     * @param sensors the target population
     */
    public void normalize(List<SensorTOPSIS> sensors) {

        HashMap<String, Double> normalizedDivisor = new HashMap<String, Double>();
        HashMap<String, Double> mMin = new HashMap<String, Double>();

        List<Attribute> lattr = new ArrayList<Attribute>();
        /**
         * Sum the squares of each attribute
         */
        for (SensorTOPSIS s : sensors) {
            lattr = s.getAttributes();

            if (normalizedDivisor.isEmpty()) {
                for (Attribute attr : lattr) {
                    normalizedDivisor.put(attr.getName(), 0.0);
                }
            }

            for (Attribute attr : lattr) {
                Double value = normalizedDivisor.get(attr.getName());
                value += Math.pow(attr.getValue(), 2);
                normalizedDivisor.put(attr.getName(), value);
            }
        }

        /**
         * power the sum to 0.5
         */
        for (Attribute attr : lattr) {
            Double value = normalizedDivisor.get(attr.getName());
            value = Math.pow(value, 0.5);
            normalizedDivisor.put(attr.getName(), value);
        }

        /**
         * Normalized values
         */
        for (SensorTOPSIS s : sensors) {

            for (Attribute attr : s.getAttributes()) {
                Double normalized = attr.getValue()/normalizedDivisor.get(attr.getName());
                attr.setNormalizedValue(normalized);
//                System.out.println(attr.getNormalizedValue());
            }

            // System.out.println("Name: " + attr.getName() +
            // "\tTrue Value: "
            // + attr.getValue() + "\tNormalized Value: "
            // + attr.getNormalizedValue());

        }
    }
}
