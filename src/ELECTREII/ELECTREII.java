/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELECTREII;

import java.util.List;
import commom.Attribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Adriller Ferreira
 */
public final class ELECTREII {

    HashMap<String, Double> weights;
    Double p = 0.0, q = 0.0;
    List<Double> sigma = new ArrayList<Double>();

    public ELECTREII(HashMap<String, Double> w) {
        weights = w;
    }

    public void normalizarPeso(List<SensorELECTREII> sensors) {
        /*Soma todos os pesos e divide cada um pelo total*/
        double soma = 0.0, max = 0;
        SensorELECTREII aux = sensors.get(1);
        List<Attribute> lattr = aux.getAttributes();
        for (Attribute attr : lattr) {
            soma += weights.get(attr.getName());
        }
        for (Attribute attr : lattr) {
            double oldValue = weights.get(attr.getName());
            double newValue = weights.get(attr.getName()) / soma;
            weights.replace(attr.getName(), oldValue, newValue);
        }
        for (Attribute attr : lattr) {
            if ("min".equals(attr.getObjective())) {
                int index = lattr.indexOf(attr);
                for (SensorELECTREII s : sensors) {
                    double value = s.getAttributes().get(index).getValue();
                    if (value > max) {
                        max = value;
                    }
                }
                for (SensorELECTREII s : sensors) {
                    double value = s.getAttributes().get(index).getValue();
                    s.getAttributes().get(index).setValue(max - value);
                }

            }
        }
    }

    public void Score(List<SensorELECTREII> sensors) {
        double soma = 0;
        double maior = 0, aux = 0;
        this.indiceConcordancia(sensors);
        this.sigmaCalculator(sensors);
        this.indiceDiscordancia(sensors);

        /*No loop abaixo, somo todos os pesos em que o item s1 e melhor do que o item s2 dado cada criterio*/
        for (SensorELECTREII s1 : sensors) {
            List<Attribute> lattr1 = s1.getAttributes();
            for (SensorELECTREII s2 : sensors) {
                if (!s1.equals(s2)) {
                    List<Attribute> lattr2 = s2.getAttributes();
                    for (Attribute a : lattr1) {
                        int index = lattr1.indexOf(a);
                        if (a.getValue() >= lattr2.get(index).getValue()) {
                            soma += weights.get(a.getName());
                        }
                        aux = (lattr2.get(index).getValue() - a.getValue()) / sigma.get(index);
                        if (aux > maior) {
                            maior = aux;
                        }
                    }
                    if ((soma > p) && (maior < q)) {
                        Double auxVal = s1.getElectreValue();
                        s1.setElectreValue(auxVal + 1);
                        auxVal = s2.getElectreValue();
                        s2.setElectreValue(auxVal - 1);
                    }

                    maior = 0;
                    soma = 0;
                }
            }
        }
    }

    public void Rank(List<SensorELECTREII> sensors) {
        SensorELECTREII temp;
        Collections.sort(sensors, new SensorComparator());
    }

    public void indiceConcordancia(List<SensorELECTREII> sensors) {
        //Concordance = new double[tam][tam];
        double soma = 0;
        /*No loop abaixo, somo todos os pesos em que o item s1 e melhor do que o item s2 dado cada criterio*/
        for (SensorELECTREII s1 : sensors) {
            List<Attribute> lattr1 = s1.getAttributes();
            for (SensorELECTREII s2 : sensors) {
                if (!s1.equals(s2)) {
                    List<Attribute> lattr2 = s2.getAttributes();
                    for (Attribute a : lattr1) {
                        int index = lattr1.indexOf(a);
                        if (a.getValue() >= lattr2.get(index).getValue()) {
                            soma += weights.get(a.getName());
                        }
                    }
                    p += soma;
                    soma = 0;
                }
            }
        }
        p = p / (sensors.size() * (sensors.size() - 1));
    }

    public void sigmaCalculator(List<SensorELECTREII> sensors) {
        /*O sigma e o maior valor que um item pode ter no criterio I, menos o menor valor que um item pode ter no mesmo criterio*/
        Double maior = 0.0, menor = Double.POSITIVE_INFINITY;

        SensorELECTREII aux = sensors.get(1);
        List<Attribute> lattr = aux.getAttributes();
        for (Attribute attr : lattr) {
            int index = lattr.indexOf(attr);
            for (SensorELECTREII s : sensors) {
                Double value = s.getAttributes().get(index).getValue();
                if (value > maior) {
                    /*Encontramos qual item tem o maior valor nesse criterio*/
                    maior = value;
                }
                if (value < menor) {
                    /*Encontramos qual o item tem o menor valor nesse criterio*/
                    menor = value;
                }
            }
            /*Assim calculamos o Sigma*/
            Double valorSigma = maior - menor;
            sigma.add(valorSigma);
            maior = 0.0;
            menor = Double.POSITIVE_INFINITY;
        }
    }

    public void indiceDiscordancia(List<SensorELECTREII> sensors) {
        double maior = 0, aux = 0;
        /*No loop abaixo, pego em qual criterio ha a maior discrepancia entre os itens K e I, dividindo-o por sigma*/

        for (SensorELECTREII s1 : sensors) {
            List<Attribute> lattr1 = s1.getAttributes();
            for (SensorELECTREII s2 : sensors) {
                if (!s1.equals(s2)) {
                    List<Attribute> lattr2 = s2.getAttributes();
                    for (Attribute a : lattr1) {
                        int index = lattr1.indexOf(a);
                        aux = (lattr2.get(index).getValue() - a.getValue()) / sigma.get(index);
                        if (aux > maior) {
                            maior = aux;
                        }

                    }
                    q += maior;
                    maior = 0;
                }
            }
        }

        q = q / (sensors.size() * (sensors.size() - 1));

    }

    void setP(double p) {
        this.p = p;
    }

    double getP() {
        return p;
    }

    void setQ(double q) {
        this.q = q;
    }

    double getQ() {
        return q;
    }

}
