package com.mihai.colorcodecreator;

import java.util.Comparator;

public class ComparatorCuloare implements Comparator<CuloareSalvata> {
    @Override
    public int compare(CuloareSalvata o1, CuloareSalvata o2) {
        if(o1.getCod() < o2.getCod())
            return -1;
        if(o1.getCod() == o2.getCod())
            return 0;
        return 1;
    }
}
