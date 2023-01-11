package com.ppl.siakngnewbe.pengecekanirs.result;

import java.util.List;

import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrasyaratResult {
    String namaMataKuliah;
    String idMataKuliah;
    List<PrasyaratProxy> prasyarat;
    boolean ok;

    public PrasyaratResult(MataKuliah mataKuliah) {
        this.namaMataKuliah = mataKuliah.getNama();
        this.idMataKuliah = mataKuliah.getId();
    }
}
