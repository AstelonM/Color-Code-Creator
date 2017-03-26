package com.mihai.colorcodecreator;

import android.support.annotation.NonNull;

import java.io.Serializable;

class CuloareSalvata implements Comparable<CuloareSalvata>, Serializable {

    private int cod;
    private String nume;

    CuloareSalvata(int cod, String nume)
    {
        this.cod = cod;
        this.nume = nume;
    }

    @Override
    public int compareTo(@NonNull CuloareSalvata o) {
        if(this.getCod() < o.getCod())
            return -1;
        if(this.getCod() == o.getCod())
            return 0;
        return 1;
    }

    int getCod() {
        return cod;
    }

    String getNume() {
        return nume;
    }
}
