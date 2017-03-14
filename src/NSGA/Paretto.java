package NSGA;

import commom.Attribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Paretto {

    List<SensorParetto> population;
    HashMap<Integer, List<SensorParetto>> frontMap = new HashMap<Integer, List<SensorParetto>>();

    public Paretto(List<SensorParetto> pop) {
        this.population = pop;
    }

    public String start() {

        HashMap<Integer, SensorParetto> dominatedSensors = convertListToMap(population);
        // NSGAII - foreach to determinate the first front
        HashMap<Integer, Integer> dominateTable = new HashMap<Integer, Integer>();
        System.out.println("Computing Fronts...");

        for (int i = 0; i < population.size(); i++) {
            SensorParetto p = population.get(i);
            List<Integer> dSensors = new ArrayList<Integer>();
            if (i % 1000 == 0) {
                System.out.println(i);
            }

//            System.out.println("Comparando: " + i);
            for (int j = 0; j < population.size(); j++) {

                if (i != j) {
                    SensorParetto q = population.get(j);

                    dominateTable.clear();
                    dominateTable.put(1, 0);
                    dominateTable.put(-1, 0);
                    dominateTable.put(0, 0);

                    for (int k = 0; k < p.getAttributes().size(); k++) {
                        Attribute pAttr = p.getAttributes().get(k);
                        Attribute qAttr = q.getAttributes().get(k);

                        int dominate = objectiveFunction(pAttr, qAttr);

                        dominateTable.put(dominate,
                                dominateTable.get(dominate) + 1);
                    }

                    // p dominates q
                    if (dominateTable.get(-1) == 0 && dominateTable.get(1) > 0) {
                        dSensors.add(q.getId());
//                        p.addToDominatedSensors(q);
                    }

                    // q dominates p
                    if (dominateTable.get(-1) > 0 && dominateTable.get(1) == 0) {
                        p.increaseNp();
                    }
                }
            }

            p.storeDominatedSensors(dSensors);

            if (p.getNp() == 0) {
                p.setRank(1);
                joinFront(p.getRank(), p);
            }
        }

        int k = 1;

        System.out.println("Computing dominance...");
        do {
            for (SensorParetto p : frontMap.get(k)) {

                List<Integer> lsDominated = p.getDominatedSensors();

                if (lsDominated != null
                        && lsDominated.size() > 0) {
                    for (Integer i : lsDominated) {
                        SensorParetto q = dominatedSensors.get(i);
                        q.decreaseNp();
                        if (q.getNp() == 0) {
                            q.setRank(k + 1);
                            joinFront(q.getRank(), q);
                        }
                    }

                }
            }
            k++;
        } while (frontMap.size() == k);
//
        String front = "";

//        for (Integer j : frontMap.keySet()) {
//            System.out.println("Front[" + j + "]: \t" + frontMap.get(j).size());
//            front += frontMap.get(j).size() +";";
//        }
        for (Integer j = 1; j <= frontMap.size(); j++) {
            System.out.println("Front[" + j + "]: \t" + frontMap.get(j).size());
            front += frontMap.get(j).size() + ";";
        }

        return front;

//        crowdingDistance(this.population);
        // for (Integer j : frontMap.keySet()) {
        // for (AbstractObject obj : frontMap.get(j)) {
        // System.out.println("Sensor: " + obj.getId() + " / Rank - "
        // + obj.getRank() + " / energy - "
        // + obj.getAttrs().get("energy").getT()
        // + " / performance - "
        // + obj.getAttrs().get("performance").getT()
        // + " / crowding distance - " + obj.getCrowdingDistance()
        // );
        // }
        // }
//        int print_size = (int) (this.population.size() * 0.1);
//        int count = 0;
//
//        for (int j = 1; j < frontMap.size(); j++) {
//
//            List<SensorParetto> ls = new ArrayList<SensorParetto>(frontMap.get(j));
//
//            Collections.sort(ls, new CrowdingComparator());
//
//            for (SensorParetto obj : ls) {
//
//                if (flag == false) {
//                    for (Attribute attr : obj.getAttributes()) {
//                        System.out.print("\t" + attr.getName());
//                    }
//                    System.out.print("\n");
//                    flag = true;
//                }
//
//                for (Attribute attr : obj.getAttributes()) {
//                    System.out.print("\t" + attr.getValue());
//                }
//                System.out.print("\n");
//
//                count++;
//
//                if (count > print_size) {
//                    break;
//                }
//            }
//            if (count > print_size) {
//                break;
//            }
//        }
        // for (int j = 1; j < 2; j++) {
        //
        //
        // for (SensorParetto obj : frontMap.get(j)) {
        //
        // if(flag==false){
        // for (Attribute attr : obj.getAttributes()){
        // System.out.print("\t\t"+attr.getName());
        // }
        // System.out.print("\n");
        // flag = true;
        // }
        //
        // for (Attribute attr : obj.getAttributes()){
        // System.out.print("\t\t"+attr.getValue());
        // }
        // System.out.print("\n");
        // }
        // }
    }

    public void joinFront(int i, SensorParetto object) {
        if (frontMap.size() < i) {
            List<SensorParetto> ls = new ArrayList<SensorParetto>();
            ls.add(object);
            frontMap.put(i, ls);
        } else {
            frontMap.get(i).add(object);
        }
    }

    public void crowdingDistance(List<SensorParetto> ls) {

        SensorParetto s = ls.get(0);

        for (int i = 0; i < s.getAttributes().size(); i++) {

            Collections.sort(ls, new SortByAttr(i));

            ls.get(0).setCrowdingDistance(Double.MAX_VALUE);
            double min = ls.get(0).getAttributes().get(i).getValue();
            ls.get(ls.size() - 1).setCrowdingDistance(Double.MAX_VALUE);
            double max = ls.get(ls.size() - 1).getAttributes().get(i)
                    .getValue();

            for (int j = 1; j < ls.size() - 1; j++) {
                double crowndist = ls.get(j).getCrowdingDistance();
                double prev = ls.get(j - 1).getAttributes().get(i).getValue();
                double next = ls.get(j + 1).getAttributes().get(i).getValue();
                crowndist += (next - prev) / (max - min);

                ls.get(j).setCrowdingDistance(crowndist);
            }

        }

    }

    public int objectiveFunction(Attribute attr1, Attribute attr2) {
        int mul = 0;

        if (attr1.getObjective() == "min" && attr1.getObjective() == "min") {
            mul = -1;
        } else if (attr1.getObjective() == "max"
                && attr1.getObjective() == "max") {
            mul = 1;
        }

        int value = 0;

        if (attr1.getValue() > attr2.getValue()) {
            value = 1 * mul;
        } else if (attr1.getValue() < attr2.getValue()) {
            value = -1 * mul;
        } else {
            value = 0;
        }

        return value;
    }

    public HashMap<Integer, SensorParetto> convertListToMap(List<SensorParetto> ls) {

        HashMap<Integer, SensorParetto> hm = new HashMap<>();

        for (SensorParetto s : ls) {
            hm.put(s.getId(), s);
        }

        return hm;
    }
}
