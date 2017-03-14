package NSGA;

import java.util.Comparator;

public class SortByAttr implements Comparator<SensorParetto> {

    private int index;

    public SortByAttr(int indx) {
        this.index = indx;
    }

    @Override
    public int compare(SensorParetto o1, SensorParetto o2) {

        return o1.getAttributes().get(index).getValue() < o2.getAttributes().get(index).getValue() ? -1
                : o1.getAttributes().get(index).getValue() > o2.getAttributes().get(index).getValue() ? 1 : 0;
    }
}
