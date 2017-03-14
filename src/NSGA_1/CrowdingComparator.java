package NSGA_1;

import java.util.Comparator;

public class CrowdingComparator implements Comparator<SensorParetto> {

    @Override
    public int compare(SensorParetto o1, SensorParetto o2) {
        return o1.getCrowdingDistance() < o2.getCrowdingDistance() ? -1
                : o1.getCrowdingDistance() > o2.getCrowdingDistance() ? 1
                : 0;
    }
}
