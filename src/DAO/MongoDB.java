package DAO;

//import NSGA_1.SensorParetto;
import NSGA.SensorParetto;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import commom.Attribute;
import commom.Experiment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bson.types.ObjectId;

public class MongoDB {

    public static MongoClient mongo = null;
    public static Experiment exp = new Experiment();

    public static void connect() {
        /**
         * ** Connect to MongoDB ***
         */
        // Since 2.10.0, uses MongoClient		
        if (mongo == null) {
            mongo = new MongoClient(exp.getDatabaseAddress(), 27017);
//              mongo = new MongoClient("10.0.0.100", 27017);
        }
    }

    public DBCursor select(int l, int order) {

        DBCursor ret;

        if (mongo == null) {
            connect();
        }

        /**
         * ** Get database ***
         */
        // if database doesn't exists, MongoDB will create it for you
        DB db = mongo.getDB(exp.getDataDB());
        DBCollection table = db.getCollection(exp.getDataTable());

        if (order == 0) {
            ret = table.find().limit(l);
        } else if (order == 1) {
            ret = table.find().sort(new BasicDBObject("_id", 1)).limit(l);
        } else if (order == 2) {
            ret = table.find().sort(new BasicDBObject("_id", -1)).limit(l);
        } else {
            ret = table.find().sort(new BasicDBObject("_id", 1)).limit(l);
        }

        /**
         * ** Get collection / table from 'testdb' ***
         */
        // if collection doesn't exists, MongoDB will create it for you
        return ret;
//        return table.find().sort(new BasicDBObject("natural", -1)).limit(l);
//        return table.find().limit(l);
    }

    /**
     * Selects the sensors of an specific experiment
     *
     * @param expId id of the experiment
     * @param algorithm desired algorithm
     * @param nSelected percentage of the selected sensors
     * @param nCriterion number of criteria
     * @return
     */
    public DBCursor selectExperiment(int expId, String algorithm, double nSelected, int nCriterion) {
        DBCursor ret;
        if (mongo == null) {
            connect();
        }
        DB db = mongo.getDB(exp.getExperimentDB());
        DBCollection table = db.getCollection(exp.getExperimentTable() + "-" + nCriterion + "-" + algorithm);
        BasicDBObject query = new BasicDBObject();
        query.put("expId", expId);
        query.put("algorithm", algorithm);
        query.put("nselected", nSelected);
        query.put("nCriterion", nCriterion);
        ret = table.find(query);
        return ret;
    }

    public DBCursor selectPareto(int nCriterion) {
        DBCursor ret;
        if (mongo == null) {
            connect();
        }
        DB db = mongo.getDB(exp.getExperimentDB());
        DBCollection table = db.getCollection(exp.getParetoTable() + nCriterion);
        BasicDBObject query = new BasicDBObject();
//        query.put("nCriterion", nCriterion);        
        ret = table.find(query);
        return ret;
    }

    public DBCursor selectParetoFront(int nCriterion, int front) {
        DBCursor ret;
        if (mongo == null) {
            connect();
        }
        DB db = mongo.getDB(exp.getExperimentDB());
        DBCollection table = db.getCollection(exp.getParetoTable() + nCriterion);
        BasicDBObject query = new BasicDBObject();
        query.put("front", front);
        query.put("nCriterion", nCriterion);
        ret = table.find(query);
        return ret;
    }

    public void storePareto(List<SensorParetto> lsp, int nCriterion, int databaseSize) {
        DBCursor ret;

        if (mongo == null) {
            connect();
        }

        DB db = mongo.getDB(exp.getExperimentDB());
        DBCollection table = db.getCollection(exp.getParetoTable() + nCriterion);

        /**
         * Compute the unique index for each entry Used to avoid duplicate
         * entries
         */
        BasicDBObject keys = new BasicDBObject();
        keys.append("sid", 1);
        keys.append("nCriterion", 1);
        keys.append("dbsize", 1);
        BasicDBObject options = new BasicDBObject("unique", true);

        table.createIndex(keys, options);

        for (SensorParetto s : lsp) {
            BasicDBObject sensor = new BasicDBObject();
            sensor.append("sid", s.getId());
            sensor.append("nCriterion", nCriterion);
            sensor.append("front", s.getRank());
            sensor.append("dbsize", databaseSize);
            HashMap<String, Double> mapAttr = new HashMap<String, Double>();
            for (Attribute a : s.getAttributes()) {
                mapAttr.put(a.getName(), a.getValue());
            }
            BasicDBObject attributes = new BasicDBObject(mapAttr);
            sensor.append("Criteria", attributes);

            try {
                table.insert(sensor);
            } catch (com.mongodb.DuplicateKeyException e) {
//                System.out.println(s.getId() + " - " + nAttr + " duplicado...");
            }

        }
    }

    public void storeExperiments(List<SensorParetto> lsp, HashMap<String, Double> weights, String algorithm, int expId, double numberOfSelected, int databaseSize) {
        DBCursor ret;

        if (mongo == null) {
            connect();
        }

        DB db = mongo.getDB(exp.getExperimentDB());
        //Linha alterada para o armazenamento dos experimentos em mais tabelas
        DBCollection table = db.getCollection(exp.getExperimentTable() + "-" + weights.size() + "-" + algorithm);

        /**
         * Compute the unique index for each entry Used to avoid duplicate
         * entries
         */
        BasicDBObject keys = new BasicDBObject();
        keys.append("sid", 1);
        keys.append("nCriterion", 1);
        keys.append("dbsize", 1);
        keys.append("algorithm", 1);
        keys.append("expId", 1);
        keys.append("nselected", 1);
        BasicDBObject options = new BasicDBObject("unique", true);

        table.createIndex(keys, options);

        for (SensorParetto s : lsp) {

            BasicDBObject sensor = new BasicDBObject();
            sensor.append("sid", s.getId());
            sensor.append("nCriterion", weights.size());
            sensor.append("rank", s.getRank());
            sensor.append("dbsize", databaseSize);
            sensor.append("algorithm", algorithm);
            sensor.append("expId", expId);
            sensor.append("nselected", numberOfSelected);
            HashMap<String, Double> mapAttr = new HashMap<String, Double>();

            for (Attribute a : s.getAttributes()) {
                mapAttr.put(a.getName(), a.getValue());
            }

            BasicDBObject attributes = new BasicDBObject(mapAttr);
            BasicDBObject criterionWeight = new BasicDBObject(weights);
            sensor.append("criteria", attributes);
            sensor.append("cweight", criterionWeight);

            try {
                table.insert(sensor);
            } catch (com.mongodb.DuplicateKeyException e) {
//                System.out.println(s.getId() + " - " + weights.size() + " duplicado...");
            }

        }
    }

    public void updateExperiment(int expId, String algorithm, double nSelected, int nCriterion, long sid, BasicDBObject obj) {
        DBCursor ret;
        if (mongo == null) {
            connect();
        }

        DB db = mongo.getDB(exp.getExperimentDB());
        DBCollection table = db.getCollection(exp.getExperimentTable() + "-" + nCriterion + "-" + algorithm);
        BasicDBObject query = new BasicDBObject();
        //Remover da atualização os campos expId, algorithm e nselected pois se os pontos 
        //e a fronteira de pareto são os mesmos as distâncias serão as mesmas para qualquer experimento
//        query.put("expId", expId);
        query.put("algorithm", algorithm);
//        query.put("nselected", nSelected);
        query.put("nCriterion", nCriterion);
        query.put("sid", sid);

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", obj);
//        table.update(query, newDocument);
        table.update(query, newDocument, false, true);
        
    }

    public void updateExperimentFront(String algorithm, int nCriterion, long sid, BasicDBObject obj) {
        DBCursor ret;
        if (mongo == null) {
            connect();
        }

        DB db = mongo.getDB(exp.getExperimentDB());
        DBCollection table = db.getCollection(exp.getExperimentTable() + "-" + nCriterion + "-" + algorithm);
        BasicDBObject query = new BasicDBObject();
        //Remover da atualização os campos expId, algorithm e nselected pois se os pontos 
        //e a fronteira de pareto são os mesmos as distâncias serão as mesmas para qualquer experimento
//        query.put("expId", expId);
//        query.put("algorithm", algorithm);
//        query.put("nselected", nSelected);
//        query.put("nCriterion", nCriterion);
        query.put("sid", sid);

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", obj);
//        table.update(query, newDocument);
//        table.update(query, newDocument, false, true);
        table.updateMulti(query, newDocument);
    }

    public void insert() {

        DBCursor ret;

        if (mongo == null) {
            connect();
        }

        DB db = mongo.getDB("testCities");
        DBCollection table = db.getCollection("experiment");

        BasicDBObject obj = new BasicDBObject();
        obj.append("teste", 1);
        table.insert(obj);
    }

    public void disconect() {
        mongo.close();
    }

    public void storeTempDominatedSensors(int id, List<Integer> dominatedSensors, int nCriterion) {
        DBCursor ret;

        if (mongo == null) {
            connect();
        }

        DB db = mongo.getDB(exp.getExperimentDB()+"-temp");
        DBCollection table = db.getCollection(exp.getTempTable() + "-" + nCriterion);

        /**
         * Compute the unique index for each entry Used to avoid duplicate
         * entries
         */
        BasicDBObject keys = new BasicDBObject();
        keys.append("sid", 1);        
        BasicDBObject options = new BasicDBObject("unique", true);

        table.createIndex(keys, options);

        BasicDBObject sensor = new BasicDBObject();
        sensor.append("sid", id);
        
        BasicDBList list = new BasicDBList();
        
        for(Integer i : dominatedSensors){
            list.add(i);
        }
        
        sensor.append("dominated", list);

        try {
            table.insert(sensor);
        } catch (com.mongodb.DuplicateKeyException e) {
//                System.out.println(s.getId() + " - " + nAttr + " duplicado...");
        }

    }
    
    public DBCursor selectTempDominatedSensors(int sid, int nCriterion) {
        DBCursor ret;
        if (mongo == null) {
            connect();
        }
        DB db = mongo.getDB(exp.getExperimentDB()+"-temp");
        DBCollection table = db.getCollection(exp.getTempTable() + "-" + nCriterion);
        BasicDBObject query = new BasicDBObject();
        query.put("sid", sid);        
        ret = table.find(query);
        return ret;
    }
    
    public String dropTempTable(int nCriterion) {
        DBCursor ret;
        if (mongo == null) {
            connect();
        }
        DB db = mongo.getDB(exp.getExperimentDB()+"-temp");
        DBCollection table = db.getCollection(exp.getTempTable() + "-" + nCriterion);
        table.drop();
        return "Temporary table droped";
    }
}
