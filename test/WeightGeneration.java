/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lhnunes
 */
public class WeightGeneration {

    public WeightGeneration() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
//    @Test
//    public void hello() {
//
//        int n = 6;
//
//        List<Integer> tempList = new ArrayList<Integer>();
//        List<Double> list = new ArrayList<Double>();
//        
//        Random r = new Random();
//        int temp = 0;
//        int sum = 1000000;
//        int max = 1000000;
//        
//        for (int i = 0; i < n; i++) {
//            temp = r.nextInt(sum);                                                          
//            if(i == (n-1)){
//                temp = sum;                
//            }            
//             sum -= temp;            
//            tempList.add(temp);                    
//        }
//        
//        sum  = 0;
//        
//        Collections.shuffle(tempList, r);
//                
//        for(int k : tempList){            
//            double x = (double) k / max;     
//            System.out.println(x);
//            list.add(x);
//        }
//        
//        
//
//        
//        
////        System.out.println(sum);
//    }
    
    @Test
    public void getWeights() {

        int seed = 1;
        int number = 5;
        
        HashMap<String, Double> weights = new HashMap<String, Double>();

        String[] attrs = {"battery", "price", "drift", "frequency", "energy_cons", "response_time"};

        List<Double> ls = randW(seed, number+1);

        if (seed != 0) {
            for (int i = 0; i <= number; i++) {
                weights.put(attrs[i], ls.get(i));
            }
        } else {
            for (int i = 0; i <= number; i++) {
                weights.put(attrs[i], 1.0);
            }
        }

        for(String key : weights.keySet())
            System.out.println(key + ": " + weights.get(key));
    }

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
