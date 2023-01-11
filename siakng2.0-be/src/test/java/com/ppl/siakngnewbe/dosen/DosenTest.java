package com.ppl.siakngnewbe.dosen;


import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

class DosenTest {

    private Mahasiswa mahasiswaModel = new Mahasiswa();
    private Dosen dosen = new Dosen();
    Set<Mahasiswa> listMahasiswa = new HashSet<>();

    @BeforeEach
    public void setUp() {


        dosen.setNamaLengkap("Mikasa Ackerman");
        dosen.setUsername("mikasa.yeager");
        dosen.setPassword("dummyPassword");
        dosen.setUserRole(UserModelRole.DOSEN);
        dosen.setNip("190628282311312312");

        mahasiswaModel.setId(1L);
        mahasiswaModel.setUsername("eren.yeager");
        mahasiswaModel.setPassword("dummyPassword");
        mahasiswaModel.setNamaLengkap("Eren Yeager");
        mahasiswaModel.setNpm("1906282831");
        mahasiswaModel.setUserRole(UserModelRole.MAHASISWA);
        mahasiswaModel.setStatus(StatusAkademik.AKTIF);
        mahasiswaModel.setIpk(4);

        listMahasiswa.add(mahasiswaModel);
        dosen.setMahasiswaModelSet(listMahasiswa);
        dosen.setDiChatOleh(listMahasiswa);

    }
    @Test
    void getNameShouldReturnString() {
        assertEquals("Mikasa Ackerman", dosen.getNamaLengkap());
    }

    @Test
    void getUsernameShouldReturnString() {
        assertEquals("mikasa.yeager", dosen.getUsername());
    }

    @Test
    void getPasswordShouldReturnString() {
        assertEquals("dummyPassword", dosen.getPassword());
    }

    @Test
    void getNIPShouldReturnString() {
        assertEquals("190628282311312312", dosen.getNip());
    }

    @Test
    void getAllMahasiswaBimbingan() {
        assertEquals(listMahasiswa,dosen.getMahasiswaModelSet());
    }

    @Test
    void getAllMahasiswaChat() {
        assertEquals(listMahasiswa, dosen.getDiChatOleh());
    }
}

