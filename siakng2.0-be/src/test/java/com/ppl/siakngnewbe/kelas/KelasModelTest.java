package com.ppl.siakngnewbe.kelas;

import java.util.HashSet;
import java.util.Set;

import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.jadwal.Jadwal;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KelasModelTest {

    private Kelas kelas = new Kelas();
    private Set<Dosen> dosens = new HashSet<Dosen>();
    private Set<Jadwal> jadwals = new HashSet<Jadwal>();
    private Set<KelasIrs> kelasIrss = new HashSet<KelasIrs>();
    private MataKuliah mataKuliah = new MataKuliah();

    @BeforeEach
    public void setUp() {

        kelas.setDosenSet(dosens);
        kelas.setId("ID1");
        kelas.setJadwalSet(jadwals);
        kelas.setKapasitasSaatIni(0);
        kelas.setKapasitasTotal(100);
        kelas.setKelasIrsSet(kelasIrss);
        kelas.setMataKuliah(mataKuliah);
        kelas.setNama("PPL-A");
        kelas.setStatus("Published");

    }

    @Test
    void modelGetDosen() {
        assertEquals(dosens, kelas.getDosenSet());
    }

    @Test
    void modelGetId() {
        assertEquals("ID1", kelas.getId());
    }

    @Test
    void modelGetJadwal() {
        assertEquals(jadwals, kelas.getJadwalSet());
    }

    @Test
    void modelGetKapasitasSaatIni() {
        assertEquals(0, kelas.getKapasitasSaatIni());
    }

    @Test
    void modelGetKapasitasTotal() {
        assertEquals(100, kelas.getKapasitasTotal());
    }

    @Test
    void modelGetKelasIrs() {
        assertEquals(kelasIrss, kelas.getKelasIrsSet());
    }

    @Test
    void modelGetMataKuliah() {
        assertEquals(mataKuliah, kelas.getMataKuliah());
    }

    @Test
    void modelGetNama() {
        assertEquals("PPL-A", kelas.getNama());
    }

    @Test
    void modelGetStatus() {
        assertEquals("Published", kelas.getStatus());
    }

}
