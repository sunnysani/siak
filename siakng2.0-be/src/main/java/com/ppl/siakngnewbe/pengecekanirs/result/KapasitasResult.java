package com.ppl.siakngnewbe.pengecekanirs.result;

import com.ppl.siakngnewbe.kelas.Kelas;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KapasitasResult {
    String namaMataKuliah;
    String idMataKuliah;
    String namaKelas;
    int posisi;
    int kapasitasTotal;
    boolean ok;

    public KapasitasResult(Kelas kelas) {
        this.namaMataKuliah = kelas.getMataKuliah().getNama();
        this.idMataKuliah = kelas.getMataKuliah().getId();
        this.namaKelas = kelas.getNama();
    }
}
