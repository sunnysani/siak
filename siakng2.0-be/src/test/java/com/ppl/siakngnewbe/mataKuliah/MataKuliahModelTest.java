package com.ppl.siakngnewbe.mataKuliah;

import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class MataKuliahModelTest {

    private MataKuliah mataKuliah = new MataKuliah();
    private List<Kelas> kelass = new ArrayList<Kelas>();
    private Set<MataKuliah> mataKuliahs = new HashSet<MataKuliah>();
    private TahunAjaran tahunAjaran = new TahunAjaran();

    @BeforeEach
    public void setUp() {

        mataKuliah.setId("ID1");
        mataKuliah.setKelasSet(kelass);
        mataKuliah.setKurikulum("2020");
        mataKuliah.setNama("Proyek Perangkat Lunak");
        mataKuliah.setPrasyaratMataKuliahSet(mataKuliahs);
        mataKuliah.setSks(4);
        mataKuliah.setTerm("2020");
        mataKuliah.setTahunAjaran(tahunAjaran);

    }

    @Test
    void modelGetIdIrs() {
        assertEquals("ID1", mataKuliah.getId());
    }

    @Test
    void modelGetKelasSet() {
        assertEquals(kelass, mataKuliah.getKelasSet());
    }

    @Test
    void modelGetKurikulum() {
        assertEquals("2020", mataKuliah.getKurikulum());
    }

    @Test
    void modelGetNama() {
        assertEquals("Proyek Perangkat Lunak", mataKuliah.getNama());
    }

    @Test
    void modelGetPrasyarat() {
        assertEquals(mataKuliahs, mataKuliah.getPrasyaratMataKuliahSet());
    }

    @Test
    void modelGetSks() {
        assertEquals(4, mataKuliah.getSks());
    }

    @Test
    void modelGetTerm() {
        assertEquals("2020", mataKuliah.getTerm());
    }

    @Test
    void modelGetTahunAjaran() {
        assertEquals(tahunAjaran, mataKuliah.getTahunAjaran());
    }

}
