/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics.euclidiandistance;

import DAO.MongoDB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author lhnunes
 */
public class Computation {

//    public static void compute(int expId, String algorithm, double nSelected, int nCriterion, DBCursor queryResult, DBCursor paretoResult, MongoDB db) {
//
//        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//        for (DBObject expObj : queryResult) {
//            //Verificar se não existe o cálculo da distância no objeto para tal atributo, se existir não realizar o cálculo
//            if (!expObj.containsField("minimumDistance")) {
//                EuclidianObject eo = new EuclidianObject();
//                eo.setExpId(expId);
//                eo.setAlgorithm(algorithm);
//                eo.setnSelected(nSelected);
//                eo.setnCriterion(nCriterion);
//                ComputationThread cpt = new ComputationThread(eo, expObj, paretoResult, db);
//                executor.execute(cpt);
//            }
//        }
//
//        executor.shutdown();
//        while (!executor.isTerminated()) {
//        }
//
//        System.out.println("Finished all threads");
//        System.out.println("Fim");
//    }
    public static void compute(int expId, String algorithm, double nSelected, int nCriterion, DBCursor queryResult, DBCursor paretoResult, MongoDB db, ConcurrentHashMap<Integer, EuclidianObject> map) {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (DBObject expObj : queryResult) {
            //Verificar se não existe o cálculo da distância no objeto para tal atributo, se existir não realizar o cálculo
            if (!map.containsKey((Integer) expObj.get("sid"))) {
                EuclidianObject eo = new EuclidianObject();
                eo.setExpId(expId);
                eo.setAlgorithm(algorithm);
                eo.setnSelected(nSelected);
                eo.setnCriterion(nCriterion);
                ComputationThread cpt = new ComputationThread(eo, expObj, paretoResult, db, map);
                executor.execute(cpt);
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        
        System.out.println("Fim");
    }

}
