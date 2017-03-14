/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*codigo 04/01/17*/

package mcdm;

import DAO.MongoDB;
import ELECTREII.ELECTREII;
import ELECTREII.SensorELECTREII;
//import NSGA_1.Paretto;
//import NSGA_1.SensorParetto;
import NSGA.Paretto;
import NSGA.SensorParetto;
import SAW.SAW;
import SAW.SensorSAW;
import TOPSIS.SensorTOPSIS;
import TOPSIS.TOPSIS;
import VIKOR.SensorVikor;
import VIKOR.VIKOR;
import WPM.SensorWPM;
import WPM.WPM;
import WSM.SensorWSM;
import WSM.WSM;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import commom.Attribute;
import commom.Experiment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author lhnunes
 */
public class MCDM {

    /**
     * @param args the command line arguments
     */
    public static Experiment exp = new Experiment();
    public static void main(String[] args) {

        int numberOfSensors = 0;
        double numberOfSelected = 0;
        int numberOfAttr = 0;
        int dbOrder = 1;
        int seed = 0;
        
        String method = "";

        if (args.length > 0) {
            method = args[0];
            numberOfSensors = Integer.parseInt(args[1]);
            numberOfSelected = Double.parseDouble(args[2]);
            numberOfAttr = Integer.parseInt(args[3]);
            seed = args.length > 4 ? Integer.parseInt(args[4]) : 0;
            dbOrder = args.length > 5 ? Integer.parseInt(args[5]) : 1;
        } else {
            method = "electreii";
            numberOfSensors = exp.getNumberOfSensors();
            numberOfSelected = exp.getNumberOfSelected();
            numberOfAttr = exp.getAttrSize();
            dbOrder = 1;
            seed = 101;
        }

        HashMap<String, Double> weights = exp.getWeights(seed, numberOfAttr);
        MongoDB db = new MongoDB();
        DBCursor queryResult = db.select(numberOfSensors, dbOrder);

        List<SensorParetto> lsp = new ArrayList<SensorParetto>();

        if ("saw".equals(method)) {
            lsp = saw(queryResult, numberOfAttr, weights);
        } else if ("topsis".equals(method)) {
            lsp = topsis(queryResult, numberOfAttr, weights);
        } else if ("vikor".equals(method)) {
            lsp = vikor(queryResult, numberOfAttr, weights);
        }else if("paretto".equals(method)){
            numberOfSelected = 1;
            lsp = paretto(queryResult, numberOfAttr);
        }/*Adriller*/ else if ("electreii".equals(method)) {
            lsp = electreii(queryResult, numberOfAttr, weights);
        } /*Adriller*/ else if ("wsm".equals(method)) {
            lsp = wsm(queryResult, numberOfAttr, weights);
        } /*Adriller*/ else if ("wpm".equals(method)) {
            lsp = wpm(queryResult, numberOfAttr, weights);
        }

        int size = (int) (lsp.size() * numberOfSelected);
        
        //list with n first sensors
        List<SensorParetto> selected = lsp.subList(0, size);
        
        
        
        int i = 0;
//        for(SensorParetto s1 : lsp){
//            
//            System.out.println(s1.getId() + "-" +s1.getRank());
//        }
        if("paretto".equals(method)){
            Paretto parettoFront = new Paretto(selected);
            System.out.println("Computing Pareto...");
            String s = parettoFront.start();
            System.out.println("\nStoring Pareto...");
            db.storePareto(selected, numberOfAttr,numberOfSensors);
            System.out.println("\nEnd");
        }
        else
            db.storeExperiments(selected, weights, method, seed, numberOfSelected, numberOfSensors);
        
//        String txt = method + ";" + numberOfSensors + ";" + size + ";" + numberOfAttr + ";"+ seed +";" + dbOrder + ";" + s +"\n";

        
//        write("log"+numberOfSensors+".csv",txt);
//        System.out.println(db.dropTempTable(numberOfAttr));
        db.disconect();
    }

    public static List<SensorParetto> paretto(DBCursor queryResult, int numberOfAttr) {
        List<SensorParetto> lsparetto = new ArrayList<SensorParetto>();

        for (DBObject obj : queryResult) {
            List<Attribute> lAttr = exp.getAttributes(obj, numberOfAttr);
            SensorParetto sp = new SensorParetto((int) obj.get("id"),lAttr);
            lsparetto.add(sp);
        }
        
        return lsparetto;
    }
    
    public static List<SensorParetto> saw(DBCursor queryResult, int numberOfAttr, HashMap<String, Double> weights) {
        SAW saw = new SAW();
        List<SensorSAW> ls = new ArrayList<SensorSAW>();
        List<SensorParetto> lsparetto = new ArrayList<SensorParetto>();

        for (DBObject obj : queryResult) {
            List<Attribute> lAttr = exp.getAttributes(obj, numberOfAttr);
//            SensorSAW s = new SensorSAW((Integer) obj.get("id"), lAttr);
            SensorSAW s = new SensorSAW((int) obj.get("id"), lAttr);
            ls.add(s);
        }

        saw.normalize(ls);
        saw.score(weights, ls);
        saw.rank(ls);

        for(int i=0; i < ls.size(); i++){
//        for (SensorSAW s : ls) {
            SensorSAW s = ls.get(i);
            // i + 1 representa a classificação do sensor na lista
            SensorParetto sp = new SensorParetto(s.getId(), s.getAttributes(), i+1);
            lsparetto.add(sp);
        }

        return lsparetto;
    }

    public static List<SensorParetto> topsis(DBCursor queryResult, int numberOfAttr, HashMap<String, Double> weights) {
        List<SensorTOPSIS> ls = new ArrayList<SensorTOPSIS>();
        List<SensorParetto> lsparetto = new ArrayList<SensorParetto>();

        TOPSIS topsis = new TOPSIS();

        for (DBObject obj : queryResult) {
            List<Attribute> lAttr = exp.getAttributes(obj, numberOfAttr);
            SensorTOPSIS s = new SensorTOPSIS((int) obj.get("id"), lAttr);
            ls.add(s);
        }

        topsis.normalize(ls);
        topsis.score(weights, ls);
        topsis.idealSolutions(ls);
        topsis.closeness(ls);
        topsis.rank(ls);

        for(int i=0; i < ls.size(); i++){
//        for (SensorSAW s : ls) {
            SensorTOPSIS s = ls.get(i);
            // i + 1 representa a classificação do sensor na lista
            SensorParetto sp = new SensorParetto(s.getId(), s.getAttributes(), i+1);
            lsparetto.add(sp);
        }
        
//        for (SensorTOPSIS s : ls) {
//            SensorParetto sp = new SensorParetto(s.getId(), s.getAttributes());
//            lsparetto.add(sp);
//        }

        return lsparetto;
    }

    public static List<SensorParetto> vikor(DBCursor queryResult, int numberOfAttr, HashMap<String, Double> weights) {
        VIKOR vikor = new VIKOR(weights);

        List<SensorVikor> ls = new ArrayList<SensorVikor>();
        List<SensorParetto> lsparetto = new ArrayList<SensorParetto>();

        for (DBObject obj : queryResult) {
            List<Attribute> lAttr = exp.getAttributes(obj, numberOfAttr);
            SensorVikor s = new SensorVikor((int) obj.get("id"), lAttr);
            ls.add(s);
        }

        vikor.calculateFi(ls);
        vikor.calculateSR(ls);
        vikor.calculateQ(ls);
        vikor.alternatives(ls);
        vikor.rank(ls);

        for(int i=0; i < ls.size(); i++){
//        for (SensorSAW s : ls) {
            SensorVikor s = ls.get(i);
            // i + 1 representa a classificação do sensor na lista
            SensorParetto sp = new SensorParetto(s.getId(), s.getAttributes(), i+1);
            lsparetto.add(sp);
        }
        
//        for (SensorVikor s : ls) {
//            SensorParetto sp = new SensorParetto(s.getId(), s.getAttributes());
//            lsparetto.add(sp);
//        }

        return lsparetto;
    }

    /*Adriller*/
    public static List<SensorParetto> wpm(DBCursor queryResult, int numberOfAttr, HashMap<String, Double> weights) {
        WPM WPM = new WPM();
        List<SensorWPM> ls = new ArrayList<SensorWPM>();
        List<SensorParetto> lsparetto = new ArrayList<SensorParetto>();
        for (DBObject obj : queryResult) {
            List<Attribute> lAttr = exp.getAttributes(obj, numberOfAttr);
            //SensorWPM s = new SensorWPM((Integer) obj.get("id"), lAttr);
            SensorWPM s = new SensorWPM((int) obj.get("id"), lAttr);
            ls.add(s);
        }
        WPM.normalize(ls);
        WPM.score(weights, ls);
        WPM.rank(ls);
        
        for(int i=0; i < ls.size(); i++){
        //for (SensorWPM s : ls) {
            SensorWPM s = ls.get(i);
            SensorParetto sp = new SensorParetto(s.getId(), s.getAttributes(), i+1);
            lsparetto.add(sp);
        }
        return lsparetto;
    }
   

    /*Adriller*/
    public static List<SensorParetto> wsm(DBCursor queryResult, int numberOfAttr, HashMap<String, Double> weights) {
        WSM WSM = new WSM();
        List<SensorWSM> ls = new ArrayList<SensorWSM>();
        List<SensorParetto> lsparetto = new ArrayList<SensorParetto>();
        for (DBObject obj : queryResult) {
            List<Attribute> lAttr = exp.getAttributes(obj, numberOfAttr);
            //SensorWSM s = new SensorWSM((Integer) obj.get("id"), lAttr);
            SensorWSM s = new SensorWSM((int) obj.get("id"), lAttr);
            ls.add(s);
        }
        WSM.normalize(ls);
        WSM.score(weights, ls);
        WSM.rank(ls);
        
        for(int i=0; i < ls.size(); i++){
        //for (SensorWSM s : ls) {
            SensorWSM s = ls.get(i);
            SensorParetto sp = new SensorParetto(s.getId(), s.getAttributes(), i+1);
            lsparetto.add(sp);
        }
        return lsparetto;
    }

    /*Adriller*/
    public static List<SensorParetto> electreii(DBCursor queryResult, int numberOfAttr, HashMap<String, Double> weights) {
        ELECTREII electreii = new ELECTREII(weights);
        List<SensorELECTREII> ls = new ArrayList<>();
        List<SensorParetto> lsparetto = new ArrayList<SensorParetto>();
        
        for (DBObject obj : queryResult) {
            List<Attribute> lAttr = exp.getAttributes(obj, numberOfAttr);
            //SensorELECTREII s = new SensorELECTREII((Integer) obj.get("id"), lAttr);
            SensorELECTREII s = new SensorELECTREII((int) obj.get("id"), lAttr);
            ls.add(s);
        }
        electreii.normalizarPeso(ls);
        electreii.Score(ls);
        electreii.Rank(ls);
        
        for(int i=0; i < ls.size(); i++){
        //for (SensorELECTREII s : ls) {
            SensorELECTREII s = ls.get(i);
            SensorParetto sp = new SensorParetto(s.getId(), s.getAttributes(), i+1);
            lsparetto.add(sp);
        }
        return lsparetto;
    }

    public static void write(String fileName, String s) {

        File arquivo = new File(fileName);

        try {

            if (!arquivo.exists()) {
                //cria um arquivo (vazio)
                arquivo.createNewFile();
            }

            //escreve no arquivo
            FileWriter fw = new FileWriter(arquivo, true);

            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(s);

            bw.close();
            fw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
}
