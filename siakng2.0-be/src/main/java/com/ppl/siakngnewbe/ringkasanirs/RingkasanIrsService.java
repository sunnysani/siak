package com.ppl.siakngnewbe.ringkasanirs;

import java.util.List;

import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;

public interface RingkasanIrsService {
    public IrsMahasiswa getLatestDetailIrs(Mahasiswa mahasiswa);
    public List<Kelas> getLatestMatkulDipilih(Mahasiswa mahasiswa);
}
