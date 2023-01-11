package com.ppl.siakngnewbe.notifikasilonceng;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaRepository;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.user.UserModelRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotifikasiLoncengServiceTest {
    @Mock
    private MahasiswaRepository mahasiswaModelRepository;

    @Mock
    private NotifikasiLoncengRepository notifikasiLoncengRepository;

    @InjectMocks
    private NotifikasiLoncengService notifikasiLoncengService;

    private Mahasiswa mahasiswa;
    private NotifikasiLonceng notifikasiLonceng;
    private NotifikasiLonceng notifikasiLonceng2;
    private String jsonWebTokenMahasiswa;

    @BeforeEach
    public void setUp() {
        mahasiswa = new Mahasiswa();
        mahasiswa.setId(1L);
        mahasiswa.setUsername("eren.yeager");
        mahasiswa.setPassword("dummyPassword");
        mahasiswa.setNamaLengkap("Eren Yeager");
        mahasiswa.setNpm("1906282831");
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);
        mahasiswa.setStatus(StatusAkademik.AKTIF);
        mahasiswa.setIpk(4);

        notifikasiLonceng = new NotifikasiLonceng("Dummy notification", mahasiswa);
        notifikasiLonceng.setId(1L);

        notifikasiLonceng2 = new NotifikasiLonceng();
        notifikasiLonceng2.setId(2L);
        notifikasiLonceng2.setCreatedAt("2022-21-12");
        notifikasiLonceng2.setIsiNotifikasi("Dummy notif");
        notifikasiLonceng2.setNotifikasiMahasiswa(mahasiswa);
        notifikasiLonceng2.setRead(false);

        jsonWebTokenMahasiswa =  JWT.create()
        .withSubject(mahasiswa.getUsername())
        .withClaim("role",mahasiswa.getUserRole().name())
        .withClaim("npm", mahasiswa.getNpm())
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testGetAllNotifikasiForMahasiswaMahasiswaExist() {
        List<NotifikasiLonceng> listNotifikasi = new ArrayList<>();
        listNotifikasi.add(notifikasiLonceng);
        when(mahasiswaModelRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        when(notifikasiLoncengRepository.getAllNotifikasiMahasiswa(mahasiswa.getId())).thenReturn(listNotifikasi);
        List<NotifikasiLonceng> actual = notifikasiLoncengService.getNotifikasiForMahasiswa(jsonWebTokenMahasiswa);
        assertEquals(listNotifikasi, actual);
    }

    @Test
    void testGetAllNotifikasiForMahasiswaMahasiswaNotExist() {
        when(mahasiswaModelRepository.findByNpm(mahasiswa.getNpm())).thenReturn(null);
        List<NotifikasiLonceng> actual = notifikasiLoncengService.getNotifikasiForMahasiswa(jsonWebTokenMahasiswa);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void testSetReadAllNotifikasiByMahasiswaReturnTrue() {
        when(mahasiswaModelRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        List<NotifikasiLonceng> listNotifikasi = new ArrayList<>();
        listNotifikasi.add(notifikasiLonceng);
        listNotifikasi.add(notifikasiLonceng2);
        when(notifikasiLoncengRepository.getAllNotifikasiMahasiswa(mahasiswa.getId())).thenReturn(listNotifikasi);
        assertTrue(notifikasiLoncengService.readAllNotifikasiByMahasiswa(jsonWebTokenMahasiswa));
    }

    @Test
    void testSetReadAllNotifikasiByMahasiswaReturnFalse() {
        when(mahasiswaModelRepository.findByNpm(mahasiswa.getNpm())).thenReturn(null);
        assertFalse(notifikasiLoncengService.readAllNotifikasiByMahasiswa(jsonWebTokenMahasiswa));
    }
}
