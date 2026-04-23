package com.tournasys.model;

import java.util.ArrayList;
import java.util.List;

public class Standings {
    private List<StandingRow> rows;

    public Standings() {
        this.rows = new ArrayList<>();
    }

    // Tüm puan durumunu maçlara göre baştan hesaplar
    public void recalculate() {
        // Algoritma buraya gelecek: Her galibiyet +3, beraberlik +1 puan.
    }
}