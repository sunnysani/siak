package com.ppl.siakngnewbe.irsmahasiswa;

import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class IrsMahasiswaModelTest {

    private IrsMahasiswa irs = new IrsMahasiswa();
    private Set<KelasIrs> kelasIrss = new HashSet<KelasIrs>();
    private Mahasiswa mahasiswa = new Mahasiswa();
    private TahunAjaran tahunAjaran = new TahunAjaran();

    @BeforeEach
    public void setUp() {

        irs.setIdIrs("ID1");
        irs.setKelasIrsSet(kelasIrss);
        irs.setMahasiswa(mahasiswa);
        irs.setSemester(1);
        irs.setSksa(24);
        irs.setSksl(24);
        irs.setTotalMutu(96);
        irs.setTahunAjaran(tahunAjaran);

    }

    @Test
    void modelGetIdIrs() {
        assertEquals("ID1", irs.getIdIrs());
    }

    @Test
    void modelGetIps() {
        assertEquals(4.0, irs.getIps());
    }

    @Test
    void modelGetKelasIrs() {
        assertEquals(kelasIrss, irs.getKelasIrsSet());
    }

    @Test
    void modelGetMahasiswa() {
        assertEquals(mahasiswa, irs.getMahasiswa());
    }

    @Test
    void modelgetSemester() {
        assertEquals(1, irs.getSemester());
    }

    @Test
    void modelGetSksa() {
        assertEquals(24, irs.getSksa());
    }

    @Test
    void modelGetSksl() {
        assertEquals(24, irs.getSksl());
    }

    @Test
    void modelGetTotalMutu() {
        assertEquals(96, irs.getTotalMutu());
    }

    @Test
    void modelGetTahunAjaran() {
        assertEquals(tahunAjaran, irs.getTahunAjaran());
    }

}
