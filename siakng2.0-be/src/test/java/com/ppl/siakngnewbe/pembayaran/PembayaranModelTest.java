package com.ppl.siakngnewbe.pembayaran;

import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;

@ExtendWith(MockitoExtension.class)
class PembayaranModelTest {
    private Pembayaran pembayaran = new Pembayaran();
    private Mahasiswa mahasiswa = new Mahasiswa();
    private TahunAjaran tahunAjaran = new TahunAjaran();
    private Calendar deadlineDate = Calendar.getInstance();
    @BeforeEach
    public void setUp() {

        pembayaran.setId(1);
        pembayaran.setStatus(PembayaranStatus.BELUM_LUNAS);
        pembayaran.setTagihan(7500000);
        pembayaran.setMataUang("IDR");
        pembayaran.setDenda(0);
        pembayaran.setTunggakan(0);
        pembayaran.setTotalDibayar(0);
        pembayaran.setMahasiswa(mahasiswa);
        pembayaran.setTahunAjaran(tahunAjaran);
        pembayaran.setSemester(1);
        pembayaran.setDeadline(deadlineDate);

    }

    @Test
    void modelGetId() {
        assertEquals(1, pembayaran.getId());
    }

    @Test
    void modelGetStatus() {
        assertEquals(PembayaranStatus.BELUM_LUNAS, pembayaran.getStatus());
    }

    @Test
    void modelGetTagihan() {
        assertEquals(7500000, pembayaran.getTagihan());
    }

    @Test
    void modelGetMataUang() {
        assertEquals("IDR", pembayaran.getMataUang());
    }

    @Test
    void modelGetDenda() {
        assertEquals(0, pembayaran.getDenda());
    }

    @Test
    void modelGetTunggakan() {
        assertEquals(0, pembayaran.getTunggakan());
    }

    @Test
    void modelGetTotalDibayar() {
        assertEquals(0, pembayaran.getTotalDibayar());
    }

    @Test
    void modelGetMahasiswa() {
        assertEquals(mahasiswa, pembayaran.getMahasiswa());
    }

    @Test
    void modelGetTahunAjaran() {
        assertEquals(tahunAjaran, pembayaran.getTahunAjaran());
    }

    @Test
    void modelGetSemester() {
        assertEquals(1, pembayaran.getSemester());
    }

    @Test
    void modelGetDeadline() {
        assertEquals(deadlineDate, pembayaran.getDeadline());
    }
}
