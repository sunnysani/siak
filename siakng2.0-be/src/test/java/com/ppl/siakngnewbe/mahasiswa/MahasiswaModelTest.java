package com.ppl.siakngnewbe.mahasiswa;


import com.ppl.siakngnewbe.notifikasilonceng.NotifikasiLonceng;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)

class MahasiswaModelTest {

    private Mahasiswa mahasiswa = new Mahasiswa();
    private NotifikasiLonceng notifikasiLonceng;
    Set<NotifikasiLonceng> listNotifikasi = new HashSet<>();

    @BeforeEach
    public void setUp() {
        mahasiswa.setNamaLengkap("Eren Yeager");
        mahasiswa.setUsername("eren.yeager");
        mahasiswa.setPassword("dummyPassword");
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);
        mahasiswa.setNpm("19062828231");
        mahasiswa.setStatus(StatusAkademik.AKTIF);
        mahasiswa.setIpk(4);
        mahasiswa.setUrlFoto("www.google.com");

        notifikasiLonceng = new NotifikasiLonceng("Dummy notification", mahasiswa);
        notifikasiLonceng.setId(1L);

        listNotifikasi.add(notifikasiLonceng);
        mahasiswa.setNotifikasiLoncengSet(listNotifikasi);
    }

    @Test
    void getNameShouldReturnString() {
        assertEquals("Eren Yeager", mahasiswa.getNamaLengkap());
    }

    @Test
    void getUsernameShouldReturnString() {
        assertEquals("eren.yeager", mahasiswa.getUsername());
    }

    @Test
    void getPasswordShouldReturnString() {
        assertEquals("dummyPassword", mahasiswa.getPassword());
    }

    @Test
    void getNpmShouldReturngString() {
        assertEquals("19062828231", mahasiswa.getNpm());
    }

    @Test
    void getIpkShouldReturngFloat() {
        assertEquals(4, mahasiswa.getIpk());
    }

    @Test
    void getStatusShouldReturnStatus() {
        assertEquals(StatusAkademik.AKTIF, mahasiswa.getStatus());
    }

    @Test 
    void getListNotifikasiExpected() {
        assertEquals(listNotifikasi, mahasiswa.getNotifikasiLoncengSet());
    }

    @Test
    void getUrlFotoMahasiswaExpected() {
        assertEquals("www.google.com", mahasiswa.getUrlFoto());
    }
}
