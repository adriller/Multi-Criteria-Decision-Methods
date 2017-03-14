/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcdm;

import DAO.MongoDB;
import com.mongodb.DBCursor;
import commom.Experiment;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import metrics.euclidiandistance.Computation;
import metrics.euclidiandistance.EuclidianObject;

/**
 *
 * @author lhnunes
 */
public class EuclidianDistance {

    public static Experiment exp = new Experiment();
    

    public static void main(String[] args) {
        MongoDB db = new MongoDB();
        int numberOfSensors = 0;
        double nSelected = 0;
        int nCriterion = 0;
        int dbOrder = 1;
        int limit = 1;

        String algorithm = "";

        if (args.length > 0) {
            algorithm = args[0];
            numberOfSensors = Integer.parseInt(args[1]);
            nSelected = Double.parseDouble(args[2]);
            nCriterion = Integer.parseInt(args[3]);
            limit = args.length > 4 ? Integer.parseInt(args[4]) : 0;
            dbOrder = args.length > 5 ? Integer.parseInt(args[5]) : 1;
        } else {
            algorithm = "topsis";
            numberOfSensors = exp.getNumberOfSensors();
            nSelected = exp.getNumberOfSelected();
            nCriterion = exp.getAttrSize();
            dbOrder = 1;
            limit = 2;
        }

        ConcurrentHashMap<Integer, EuclidianObject> distances = new ConcurrentHashMap<Integer, EuclidianObject>();
        DBCursor paretoResult = db.selectParetoFront(nCriterion, 1);

        //colocar o loop para o expId
        for (int expId = 1; expId < limit; expId++) {
            long time = System.currentTimeMillis();
            System.out.println("Computing: " + expId);
            DBCursor queryResult = db.selectExperiment(expId, algorithm, nSelected, nCriterion);
            Computation.compute(expId, algorithm, nSelected, nCriterion, queryResult, paretoResult, db, distances);
            System.out.println("Computed " + expId + " in " + (System.currentTimeMillis() - time) + " ms");
        }
        
        System.out.println("Updating db...");
        long time = System.currentTimeMillis();
        
        for(Integer key : distances.keySet()){
            EuclidianObject eo = distances.get(key);
            db.updateExperiment(eo.getExpId(), eo.getAlgorithm(), eo.getnSelected(), eo.getnCriterion(), eo.getSid(), eo.getObj());
        }
        
        System.out.println("Update all for " + nCriterion + " in " + (System.currentTimeMillis() - time));
        return;
    }

//     public static void main(String[] args) {
//        MongoDB db = new MongoDB();
//        int numberOfSensors = 0;
//        double nSelected = 0;
//        int nCriterion = 0;
//        int dbOrder = 1;
//        int expId = 1;
//        
//        String algorithm = "";
//
//        if (args.length > 0) {
//            algorithm = args[0];
//            numberOfSensors = Integer.parseInt(args[1]);
//            nSelected = Double.parseDouble(args[2]);
//            nCriterion = Integer.parseInt(args[3]);
//            expId = args.length > 4 ? Integer.parseInt(args[4]) : 0;
//            dbOrder = args.length > 5 ? Integer.parseInt(args[5]) : 1;
//        } else {
//            algorithm = "saw";
//            numberOfSensors = exp.getNumberOfSensors();
//            nSelected = exp.getNumberOfSelected();
//            nCriterion = exp.getAttrSize();
//            dbOrder = 1;
//            expId = 0;
//        }
//        
//        DBCursor queryResult     = db.selectExperiment(expId, algorithm, nSelected, nCriterion);
//        DBCursor paretoResult = db.selectParetoFront(nCriterion, 1);
//
//        Computation.compute(expId, algorithm, nSelected, nCriterion, queryResult, paretoResult, db);
//        
//        System.out.println("Fim");
//        return;
//}
}
