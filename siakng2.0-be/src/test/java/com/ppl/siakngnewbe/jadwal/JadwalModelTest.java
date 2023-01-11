package com.ppl.siakngnewbe.jadwal;

import java.util.Calendar;
import com.ppl.siakngnewbe.kelas.Kelas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MataKuliahModelTest {

    private Jadwal jadwal = new Jadwal();
    private Kelas kelas = new Kelas();
    private Calendar waktuMulai = Calendar.getInstance();
    private Calendar waktuSelesai = Calendar.getInstance();

    @BeforeEach
    public void setUp() {

        jadwal.setHari("Senin");
        jadwal.setId("ID1");
        jadwal.setKelas(kelas);
        jadwal.setRuang("A202");
        jadwal.setWaktuMulai(waktuMulai);
        jadwal.setWaktuSelesai(waktuSelesai);

    }

    @Test
    void modelGetHari() {
        assertEquals("Senin", jadwal.getHari());
    }

    @Test
    void modelGetId() {
        assertEquals("ID1", jadwal.getId());
    }

    @Test
    void modelGetKelas() {
        assertEquals(kelas, jadwal.getKelas());
    }

    @Test
    void modelGetRuang() {
        assertEquals("A202", jadwal.getRuang());
    }

    @Test
    void modelGetWaktuMulai() {
        assertEquals(waktuMulai, jadwal.getWaktuMulai());
    }

    @Test
    void modelGetWaktuSelesai() {
        assertEquals(waktuSelesai, jadwal.getWaktuSelesai());
    }

}
