package com.ppl.siakngnewbe.notifikasilonceng;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Calendar;

import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.user.UserModelRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotifikasiLoncengModelTest {

    private NotifikasiLonceng notifikasiLonceng;
    private Mahasiswa mahasiswa;
    
    @BeforeEach
    public void setUp() throws Exception {
        mahasiswa = new Mahasiswa();
        mahasiswa.setNamaLengkap("Eren Yeager");
        mahasiswa.setUsername("eren.yeager");
        mahasiswa.setPassword("dummyPassword");
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);
        mahasiswa.setNpm("19062828231");
        mahasiswa.setStatus(StatusAkademik.AKTIF);
        mahasiswa.setIpk(4);

        notifikasiLonceng = new NotifikasiLonceng("Dummy notification", mahasiswa);
        notifikasiLonceng.setId(1L);
    }

    @Test
    void getIsiNotifikasiReturnExpected() {
        assertEquals("Dummy notification", notifikasiLonceng.getIsiNotifikasi());
    }

    @Test
    void getMahasiswaReturnExpectedMahasiswa() {
        assertEquals(mahasiswa, notifikasiLonceng.getNotifikasiMahasiswa());
    }

    @Test
    void getBooleanReadReturnFalse() {
        assertFalse(notifikasiLonceng.getRead());
    }

    @Test
    void getCreatedAtReturnNow() {
        assertEquals(Calendar.getInstance().getTime().toString(), notifikasiLonceng.getCreatedAt());
    }

    @Test
    void getNotifikasiId() {
        assertEquals(1L, notifikasiLonceng.getId());
    }
}
