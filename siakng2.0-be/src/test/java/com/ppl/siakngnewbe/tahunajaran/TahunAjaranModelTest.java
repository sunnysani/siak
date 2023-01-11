package com.ppl.siakngnewbe.tahunajaran;

import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)

class TahunAjaranModelTest {

    private TahunAjaran tahunAjaran = new TahunAjaran();
    private Set<IrsMahasiswa> irss = new HashSet<>();
    private Set<MataKuliah> mataKuliahs = new HashSet<>();
    private Calendar testCalendar = Calendar.getInstance();

    @BeforeEach
    public void setUp() {

        tahunAjaran.setId(1);
        tahunAjaran.setNama("2000/2001-1");
        tahunAjaran.setActive(1);
        tahunAjaran.setStatus(TahunAjaranStatus.IRS);
        tahunAjaran.setIrsSet(irss);
        tahunAjaran.setMataKuliahSet(mataKuliahs);
        tahunAjaran.setPembayaranStart(testCalendar);
        tahunAjaran.setPembayaranEnd(testCalendar);
        tahunAjaran.setIsiIRSStart(testCalendar);
        tahunAjaran.setIsiIRSEnd(testCalendar);
        tahunAjaran.setAddDropIRSStart(testCalendar);
        tahunAjaran.setAddDropIRSEnd(testCalendar);
        tahunAjaran.setPerkuliahanStart(testCalendar);
        tahunAjaran.setPerkuliahanEnd(testCalendar);
        tahunAjaran.setIsiNilaiDosenStart(testCalendar);
        tahunAjaran.setIsiNilaiDosenEnd(testCalendar);
        tahunAjaran.setSelesaiDate(testCalendar);

    }

    @Test
    void modelGetId() {
        assertEquals(1, tahunAjaran.getId());
    }

    @Test
    void modelGetNama() {
        assertEquals("2000/2001-1", tahunAjaran.getNama());
    }

    @Test
    void modelGetActive() {
        assertEquals(1, tahunAjaran.getActive());
    }

    @Test
    void modelGetStatus() {
        assertEquals(TahunAjaranStatus.IRS, tahunAjaran.getStatus());
    }

    @Test
    void modelGetIrsSet() {
        assertEquals(irss, tahunAjaran.getIrsSet());
    }

    @Test
    void modelGetMataKuliahSet() {
        assertEquals(mataKuliahs, tahunAjaran.getMataKuliahSet());
    }

    @Test
    void modelGetIsiIRSStart() {
        assertEquals(testCalendar, tahunAjaran.getIsiIRSStart());
    }

    @Test
    void modelGetIsiIRSEnd() {
        assertEquals(testCalendar, tahunAjaran.getIsiIRSEnd());
    }

    @Test
    void modelGetAddDropIRSStart() {
        assertEquals(testCalendar, tahunAjaran.getAddDropIRSStart());
    }

    @Test
    void modelGetAddDropIRSEnd() {
        assertEquals(testCalendar, tahunAjaran.getAddDropIRSEnd());
    }

    @Test
    void modelGetPembayaranStart() {
        assertEquals(testCalendar, tahunAjaran.getPembayaranStart());
    }

    @Test
    void modelGetPembayaranEnd() {
        assertEquals(testCalendar, tahunAjaran.getPembayaranEnd());
    }

    @Test
    void modelGetPerkuliahanStart() {
        assertEquals(testCalendar, tahunAjaran.getPerkuliahanStart());
    }

    @Test
    void modelGetPerkuliahanEnd() {
        assertEquals(testCalendar, tahunAjaran.getPerkuliahanEnd());
    }

    @Test
    void modelGetIsiNilaiDosenStart() {
        assertEquals(testCalendar, tahunAjaran.getIsiNilaiDosenStart());
    }

    @Test
    void modelGetIsiNilaiDosenEnd() {
        assertEquals(testCalendar, tahunAjaran.getIsiNilaiDosenEnd());
    }

    @Test
    void modelGetSelesaiDate() {
        assertEquals(testCalendar, tahunAjaran.getSelesaiDate());
    }

}
