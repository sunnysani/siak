package com.ppl.siakngnewbe.pengecekanirs.result;

import com.ppl.siakngnewbe.matakuliah.MataKuliah;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrasyaratProxy {
    String namaMataKuliah;
    String idMataKuliah;
    boolean lulus;

    public PrasyaratProxy(MataKuliah mataKuliah) {
        this.namaMataKuliah = mataKuliah.getNama();
        this.idMataKuliah = mataKuliah.getId();
    }
}
