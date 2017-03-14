/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commom;

import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author lhnunes
 */
public class Experiment {

    private static int numberOfSensors;
    private static double numberOfSelected;
    private static int attrSize;
    private static String experimentDB;
    private static String experimentTable;
    private static String databaseAddress;
    private static String paretoTable;
    private static String dataDB;
    private static String dataTable;
    private static String tempTable;

    /**
     * @return the tempTable
     */
    public static String getTempTable() {
        return tempTable;
    }

    /**
     * @param aTempTable the tempTable to set
     */
    public static void setTempTable(String aTempTable) {
        tempTable = aTempTable;
    }

    /**
     * @return the dataDB
     */
    public String getDataDB() {
        return dataDB;
    }

    /**
     * @param aDataDB the dataDB to set
     */
    public void setDataDB(String aDataDB) {
        dataDB = aDataDB;
    }

    /**
     * @return the dataTable
     */
    public String getDataTable() {
        return dataTable;
    }

    /**
     * @param aDataTable the dataTable to set
     */
    public void setDataTable(String aDataTable) {
        dataTable = aDataTable;
    }

    /**
     * @return the paretoTable
     */
    public String getParetoTable() {
        return paretoTable;
    }

    /**
     * @param aParetoTable the paretoTable to set
     */
    public static void setParetoTable(String aParetoTable) {
        paretoTable = aParetoTable;
    }

    /**
     * @return the databaseAddress
     */
    public String getDatabaseAddress() {
        return databaseAddress;
    }

    /**
     * @param aDatabaseAddress the databaseAddress to set
     */
    public static void setDatabaseAddress(String aDatabaseAddress) {
        databaseAddress = aDatabaseAddress;
    }

    /**
     * @return the experimentDB
     */
    public String getExperimentDB() {
        return experimentDB;
    }

    /**
     * @return the experimentTable
     */
    public String getExperimentTable() {
        return experimentTable;
    }

    /**
     * @return the numberOfSelected
     */
    public double getNumberOfSelected() {
        return numberOfSelected;
    }

    /**
     * @param aNumberOfSelected the numberOfSelected to set
     */
    public static void setNumberOfSelected(double aNumberOfSelected) {
        numberOfSelected = aNumberOfSelected;
    }

    /**
     * @return the attrSize
     */
    public int getAttrSize() {
        return attrSize;
    }

    /**
     * @param aAttrSize the attrSize to set
     */
    public static void setAttrSize(int aAttrSize) {
        attrSize = aAttrSize;
    }

    /**
     * @param aNumberOfSensors the numberOfSensors to set
     */
    public static void setNumberOfSensors(int aNumberOfSensors) {
        numberOfSensors = aNumberOfSensors;
    }

    public Experiment() {
        numberOfSensors = 10000;
   //    numberOfSensors = 209555;
        numberOfSelected = 1;
        attrSize = 4;
        experimentDB = "testeBD";
//        experimentDB = "testeBD-Full";
        experimentTable = "experiment";
        paretoTable = "pareto-";
        dataDB = "weather_db";
        dataTable = "day_0";
//        dataTable = "day_1";
//        databaseAddress = "10.0.3.246";
        databaseAddress = "localhost";
        tempTable = "tempSensorTable";
    }

    /**
     * @return the numberOfSensors
     */
    public int getNumberOfSensors() {
        return numberOfSensors;
    }

    public HashMap<String, Double> getWeights(int seed, int number) {

        HashMap<String, Double> weights = new HashMap<String, Double>();

//        String[] attrs = {"battery", "price", "drift", "frequency","energy_cons", "response_time"};
        String[] attrs = {"temp_day_norm", "humidity", "clouds", "speed", "rain_norm", "dt"};
        List<Double> ls = randW(seed, number);

        if (seed != 0) {
            for (int i = 0; i < number; i++) {
                System.out.println(attrs[i]);
                weights.put(attrs[i], ls.get(i));
            }
        } else {
            for (int i = 0; i < number; i++) {
                weights.put(attrs[i], 1.0);
            }
        }

//        weights.put("battery", 1.0);
//        weights.put("price", 1.0);
//        weights.put("drift", 1.0);
//        weights.put("frequency", 1.0);
//        weights.put("energy_cons", 1.0);
//        weights.put("response_time", 1.0);
        return weights;
    }

//    public HashMap<String, Double> getWeights() {
//
//        HashMap<String, Double> weights = new HashMap<String, Double>();
//        weights.put("battery", 1.0);
//        weights.put("price", 1.0);
//        weights.put("drift", 1.0);
//        weights.put("frequency", 1.0);
//        weights.put("energy_cons", 1.0);
//        weights.put("response_time", 1.0);
//
//        return weights;
//    }
    public List<Attribute> getAttributes(DBObject obj, int numberOfAttr) {
        Attribute attr0 = new Attribute("temp_day_norm", "max",
                (Double) obj.get("temp_day_norm"));
        Attribute attr1 = new Attribute("humidity", "max",
                (Double) obj.get("humidity"));
        Attribute attr2 = new Attribute("clouds", "max",
                (Double) obj.get("clouds"));
        Attribute attr3 = new Attribute("speed", "min",
                (Double) obj.get("speed"));
        Attribute attr4 = new Attribute("rain_norm", "max",
                (Double) obj.get("rain_norm"));
        Attribute attr5 = new Attribute("dt", "min",
                (Double) obj.get("dt"));

        List<Attribute> lAttr = new ArrayList<Attribute>();

        lAttr.add(attr0);
        lAttr.add(attr1);
        lAttr.add(attr2);
        lAttr.add(attr3);
        lAttr.add(attr4);
        lAttr.add(attr5);

        return lAttr.subList(0, numberOfAttr);
    }

//    public List<Attribute> getAttributes(DBObject obj, int numberOfAttr) {
//        Attribute attr0 = new Attribute("battery", "max",
//                (Double) obj.get("battery"));
//        Attribute attr1 = new Attribute("price", "min",
//                (Double) obj.get("price"));
//        Attribute attr2 = new Attribute("drift", "min",
//                (Double) obj.get("drift"));
//        Attribute attr3 = new Attribute("frequency", "max",
//                (Double) obj.get("frequency"));
//        Attribute attr4 = new Attribute("energy_cons", "min",
//                (Double) obj.get("energy_cons"));
//        Attribute attr5 = new Attribute("response_time", "min",
//                (Double) obj.get("response_time"));
//
//        List<Attribute> lAttr = new ArrayList<Attribute>();
//
//        lAttr.add(attr0);
//        lAttr.add(attr1);
//        lAttr.add(attr2);
//        lAttr.add(attr3);
//        lAttr.add(attr4);
//        lAttr.add(attr5);
//        
//        return lAttr.subList(0, numberOfAttr);
//    }
    public static List<Double> randW(int seed, int n) {

        List<Integer> tempList = new ArrayList<Integer>();
        List<Double> list = new ArrayList<Double>();

        Random r = new Random(seed);
        int temp = 0;
        int sum = 1000000;
        int max = 1000000;

        for (int i = 0; i < n; i++) {
            temp = r.nextInt(sum);
            if (i == (n - 1)) {
                temp = sum;
            }
            sum -= temp;
            tempList.add(temp);
        }

        sum = 0;

        Collections.shuffle(tempList, r);

        for (int k : tempList) {
            double x = (double) k / max;
            list.add(x);
        }

        return list;
    }
}
