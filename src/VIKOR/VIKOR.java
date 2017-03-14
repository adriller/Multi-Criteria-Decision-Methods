package VIKOR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import commom.Attribute;

public class VIKOR {

    public static List<Attribute> bestfi;
    public static List<Attribute> worstfi;
    public static HashMap<String, Double> weights;

    public VIKOR(HashMap<String, Double> w) {
        bestfi = new ArrayList<Attribute>();
        worstfi = new ArrayList<Attribute>();
        weights = w;
    }

    public void calculateFi(List<SensorVikor> sensors) {

        for (SensorVikor sensor : sensors) {

            for (int i = 0; i < sensor.getAttributes().size(); i++) {

                if (bestfi.size() < sensor.getAttributes().size() && worstfi.size() < sensor.getAttributes().size()) {
                    Attribute bAttr = new Attribute(sensor.getAttributes().get(i).getName(), sensor.getAttributes().get(i).getObjective(), sensor.getAttributes().get(i).getValue());
                    Attribute wAttr = new Attribute(sensor.getAttributes().get(i).getName(), sensor.getAttributes().get(i).getObjective(), sensor.getAttributes().get(i).getValue());

                    bestfi.add(bAttr);
                    worstfi.add(wAttr);
                } else {

                    if (sensor.getAttributes().get(i).getObjective().equals("min")) {
                        if (bestfi.get(i).getValue() > sensor.getAttributes().get(i).getValue()) {
                            bestfi.get(i).setValue(sensor.getAttributes().get(i).getValue());
                        } else if (worstfi.get(i).getValue() < sensor.getAttributes().get(i).getValue()) {
                            worstfi.get(i).setValue(sensor.getAttributes().get(i).getValue());
                        }
                    }

                    if (sensor.getAttributes().get(i).getObjective().equals("max")) {
                        if (bestfi.get(i).getValue() < sensor.getAttributes().get(i).getValue()) {
                            bestfi.get(i).setValue(sensor.getAttributes().get(i).getValue());
                        } else if (worstfi.get(i).getValue() > sensor.getAttributes().get(i).getValue()) {
                            worstfi.get(i).setValue(sensor.getAttributes().get(i).getValue());
                        }
                    }

                }
            }
        }
//
//        for (int i = 0; i < bestfi.size(); i++) {
//            System.out.println(" BestFi: " + bestfi.get(i).getName() + " - " + bestfi.get(i).getValue());
//            System.out.println(" WorstFi: " + worstfi.get(i).getName() + " - " + worstfi.get(i).getValue());
//        }
    }

    public void calculateSR(List<SensorVikor> sensors) {
        Double s = 0.0;
        Double r = 0.0;
        Double temp = 0.0;

        for (SensorVikor sensor : sensors) {
            s = 0.0;
            for (int i = 0; i < sensor.getAttributes().size(); i++) {
                double w = weights.get(sensor.getAttributes().get(i).getName());
                temp = w * ((bestfi.get(i).getValue() - sensor.getAttributes().get(i).getValue()) / (bestfi.get(i).getValue() - worstfi.get(i).getValue()));

                s += temp;

                if (temp > r) {
                    r = temp;
                }
            }

//            System.out.println("id: " + sensor.getId() + "\t s: " + s + "\t r:" + r);
            sensor.setS(s);
            sensor.setR(r);
        }
    }

    public void calculateQ(List<SensorVikor> sensors) {
        double v = 0.5;

        Collections.sort(sensors, new SensorComparatorS());

        double maxS = sensors.get(0).getS();
        double minS = sensors.get(sensors.size() - 1).getS();

//        System.out.println("MaxS: " + maxS);
//        System.out.println("MinS: " + minS);

        Collections.sort(sensors, new SensorComparatorR());

        double maxR = sensors.get(0).getR();
        double minR = sensors.get(sensors.size() - 1).getR();

        if (minR == maxR) {
            maxR = 1;
            minR = 0;
        }

        System.out.println("maxR: " + maxR);
        System.out.println("minR: " + minR);

        for (SensorVikor sensor : sensors) {
            double q1 = (v * (sensor.getS() - minS)) / (maxS - minS);
//            System.out.println(q1);
            double q2 = ((1 - v) * (sensor.getR() - minR)) / (maxR - minR);
//            System.out.println(q2);
            sensor.setQ(q1 + q2);
        }

//        for (SensorVikor sensor : sensors) {
//            System.out.println("Sensor: " + sensor.getId() + " \t R: " + sensor.getR() + "\t S: " + sensor.getS() + "\t Q:" + sensor.getQ());
//        }
    }

    public void  alternatives(List<SensorVikor> sensors) {
        List<SensorVikor> lsS = new ArrayList<SensorVikor>(sensors);
        List<SensorVikor> lsR = new ArrayList<SensorVikor>(sensors);
        List<SensorVikor> lsQ = new ArrayList<SensorVikor>(sensors);


        Collections.sort(lsS, new SensorComparatorS());
        Collections.sort(lsR, new SensorComparatorR());
        Collections.sort(lsQ, new SensorComparatorQ());

        double DQ = 1.0 / (lsS.size() - 1);

        int rank = 1;

        for (int i = lsQ.size() - 1; i >= 0; i--) {

            boolean c1 ;
            boolean c2 ;

            if (i > 0) {
                c1 = criteria1(lsQ.get(i).getQ(), lsQ.get(i - 1).getQ(), DQ);

                c2 = criteria2(lsR, lsS, lsQ.get(i).getId());

                if (c1 && c2) {
                    lsQ.get(i).setRank(rank);
                } else if (!c2) {
                    lsQ.get(i).setRank(rank);
                    lsQ.get(i - 1).setRank(rank);
                    i--;
                } else if (!c1) {
                    int j = i - 1;
                    lsQ.get(i).setRank(rank);
                    while (((lsQ.get(j).getQ() - lsQ.get(i).getQ()) < DQ) && j > -1) {
                        lsQ.get(j).setRank(rank);
                        j--;
                    }
                    i = j + 1;
                }
                rank += 1;
            } else {
                lsQ.get(i).setRank(rank);
            }
        }
        
        sensors =  new ArrayList<SensorVikor>(lsQ);
    }

    public List<SensorVikor> rank(List<SensorVikor> sensors) {

        boolean flag = false;

//        int print_size = (int) (sensors.size() * size);

        Collections.sort(sensors, new SensorComparatorRank());

        // for (SensorSAW obj : sensors) {
//        for (int i = 0; i < print_size; i++) {
//            SensorVikor obj = sensors.get(i);
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
//        }
        
        return sensors;
    }

    public boolean criteria1(Double Qi, Double Qf, Double DQ) {
        if ((Qf - Qi) >= DQ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean criteria2(List<SensorVikor> listR, List<SensorVikor> listS, int id) {

        double minR = listR.get(listR.size() - 1).getR();
        double minS = listS.get(listS.size() - 1).getS();
        boolean cR = false, cS = false;

        for (SensorVikor sR : listR) {
            if (sR.getId() == id) {
                cR = sR.getR() <= minR ? true : false;
            }
        }

        for (SensorVikor sS : listS) {
            if (sS.getId() == id) {
                cS = sS.getR() <= minS ? true : false;
            }
        }

        return (cS || cR);
    }
}
