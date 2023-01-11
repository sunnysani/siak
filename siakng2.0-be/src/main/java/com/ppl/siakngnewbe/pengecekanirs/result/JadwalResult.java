package com.ppl.siakngnewbe.pengecekanirs.result;

import java.util.List;

import com.ppl.siakngnewbe.kelas.Kelas;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JadwalResult {
    String namaMataKuliah;
    String idMataKuliah;
    String namaKelas;
    List<KelasProxy> konflik;
    boolean ok;

    public JadwalResult(Kelas kelas) {
        this.namaMataKuliah = kelas.getMataKuliah().getNama();
        this.idMataKuliah = kelas.getMataKuliah().getId();
        this.namaKelas = kelas.getNama();
    }
}
