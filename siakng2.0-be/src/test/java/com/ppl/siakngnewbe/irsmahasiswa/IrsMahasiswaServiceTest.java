package com.ppl.siakngnewbe.irsmahasiswa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelas.KelasRepository;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.kelasirs.KelasIrsRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.user.UserModelRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class IrsMahasiswaServiceTest {
 
    @Mock
    private IrsMahasiswaRepository irsMahasiswaRepository;

    @Mock
    private KelasIrsRepository kelasIrsRepository;

    @Mock
    private KelasRepository kelasRepository;

    @InjectMocks
    private IrsMahasiswaService irsService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Kelas kelas;
    private KelasIrs kelasIrs;
    private KelasIrs kelasIrs2;
    private IrsMahasiswa irsMahasiswa;
    private IrsMahasiswa irsMahasiswa2;
    private IrsMahasiswa irsMahasiswa3;
    private Mahasiswa mahasiswa;
    private Mahasiswa mahasiswa2;
    private MataKuliah mataKuliah;
    private String jsonWebToken;
    private String jsonWebToken2;
    private String jsonWebToken3;

    private List<Kelas> kelass;
    private List<KelasIrs> kelasIrss;
    private List<IrsMahasiswa> irsMahasiswas;

    @BeforeEach
    public void SetUp() {
        kelas = new Kelas();
        kelasIrs = new KelasIrs();
        kelasIrs2 = new KelasIrs();
        irsMahasiswa = new IrsMahasiswa();
        irsMahasiswa2 = new IrsMahasiswa();
        irsMahasiswa3 = new IrsMahasiswa();
        mahasiswa = new Mahasiswa();
        mahasiswa2 = new Mahasiswa();
        mataKuliah = new MataKuliah();
        
        mahasiswa.setId(1L);
        mahasiswa.setNamaLengkap("Eren Yeager");
        mahasiswa.setUsername("eren.yeager");
        mahasiswa.setPassword("surveycorps");
        mahasiswa.setIpk(4);
        mahasiswa.setNpm("123456789");
        mahasiswa.setStatus(StatusAkademik.AKTIF);
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);

        mahasiswa2.setId(2L);
        mahasiswa2.setNamaLengkap("Eren Yeager A");
        mahasiswa2.setUsername("eren.yeagera");
        mahasiswa2.setPassword("surveycorpsa");
        mahasiswa2.setIpk(3);
        mahasiswa2.setNpm("123454321");
        mahasiswa2.setStatus(StatusAkademik.AKTIF);
        mahasiswa2.setUserRole(UserModelRole.MAHASISWA);

        kelas.setId("ID1");
        kelas.setKapasitasSaatIni(0);
        kelas.setMataKuliah(mataKuliah);

        irsMahasiswa.setIdIrs("ID1");
        irsMahasiswa.setSemester(1);
        irsMahasiswa.setMahasiswa(mahasiswa);

        irsMahasiswa2.setIdIrs("ID2");
        irsMahasiswa2.setSemester(1);
        irsMahasiswa2.setMahasiswa(mahasiswa2);

        irsMahasiswa3.setIdIrs("ID3");
        irsMahasiswa3.setSemester(0);
        irsMahasiswa3.setMahasiswa(mahasiswa);

        kelasIrs.setKelas(kelas);
        kelasIrs.setPosisi(1);
        kelasIrs2.setKelas(kelas);
        kelasIrs2.setPosisi(2);

        irsMahasiswas = new ArrayList<IrsMahasiswa>();
        irsMahasiswas.add(irsMahasiswa);
        irsMahasiswas.add(irsMahasiswa2);
        irsMahasiswas.add(irsMahasiswa3);

        kelasIrss = new ArrayList<KelasIrs>();
        kelasIrss.add(kelasIrs);
        kelasIrss.add(kelasIrs2);

        kelass = new ArrayList<Kelas>();
        kelass.add(kelas);
        helperPostLoginAuthWithJWT();
    }
    
    private void helperPostLoginAuthWithJWT() {
        jsonWebToken = "Bearer " + JWT.create()
                .withSubject(mahasiswa.getUsername())
                .withClaim("npm", "123456789")
                .withClaim("role",mahasiswa.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));

        jsonWebToken2 = "Bearer " + JWT.create()
                .withSubject(mahasiswa2.getUsername())
                .withClaim("npm", "123454321")
                .withClaim("role",mahasiswa2.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));

        jsonWebToken3 = "Bearer " + JWT.create()
                .withSubject(mahasiswa2.getUsername())
                .withClaim("npm", "987654321")
                .withClaim("role",mahasiswa2.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
	void testServiceGetIrsFail() throws Exception {
        when(irsMahasiswaRepository.findAll()).thenReturn(irsMahasiswas);
        assertNull(irsService.getIrs(jsonWebToken3));
	}

    @Test
	void testServiceGetIrsSuccess() throws Exception {
        when(irsMahasiswaRepository.findAll()).thenReturn(irsMahasiswas);
        assertEquals(irsMahasiswa, irsService.getIrs(jsonWebToken));
	}

    @Test
	void testServiceGetIrsSamePeriodeSuccess() throws Exception {

        IrsMahasiswa irsPeriodeTwo = new IrsMahasiswa();
        irsPeriodeTwo.setIdIrs("ID1");
        irsPeriodeTwo.setSemester(2);
        irsPeriodeTwo.setMahasiswa(mahasiswa);
        irsMahasiswas.add(irsPeriodeTwo);

        when(irsMahasiswaRepository.findAll()).thenReturn(irsMahasiswas);
        assertEquals(irsPeriodeTwo, irsService.getIrs(jsonWebToken));
	}

    @Test
    void testServicePostIrsAdd() throws Exception {        
        when(irsMahasiswaRepository.findAll()).thenReturn(irsMahasiswas);
        when(kelasRepository.getById("ID1")).thenReturn(kelas);
        Set<KelasIrs> resultKelasIrs = irsService.postIrs(kelass, jsonWebToken);

        assertEquals(1, resultKelasIrs.size());
        assertEquals(1, kelas.getKapasitasSaatIni());
        assertEquals(kelas, resultKelasIrs.iterator().next().getKelas());

        Kelas kelasAdded = new Kelas();
        kelasAdded.setId("ID2");
        kelasAdded.setKapasitasSaatIni(10);
        kelasAdded.setMataKuliah(mataKuliah);
        kelass.add(kelasAdded);
        when(kelasRepository.getById("ID2")).thenReturn(kelasAdded);

        resultKelasIrs = irsService.postIrs(kelass, jsonWebToken);
        assertEquals(2, resultKelasIrs.size());
        assertEquals(11, kelasAdded.getKapasitasSaatIni());
    }

    @Test
    void testServicePostIrsDrop() throws Exception {
        when(irsMahasiswaRepository.findAll()).thenReturn(irsMahasiswas);
        when(kelasIrsRepository.findAll()).thenReturn(kelasIrss);
        when(kelasRepository.getById("ID1")).thenReturn(kelas);

        Kelas kelasDropped = new Kelas();
        KelasIrs kelasIrsDropped = new KelasIrs();
        kelasDropped.setId("ID2");
        kelasDropped.setKapasitasSaatIni(10);
        kelasDropped.setMataKuliah(mataKuliah);
        kelass.add(kelasDropped);
        kelasIrsDropped.setKelas(kelasDropped);
        kelasIrsDropped.setPosisi(5);

        when(kelasRepository.getById("ID2")).thenReturn(kelasDropped);
        irsService.postIrs(kelass, jsonWebToken);
        irsService.postIrs(kelass, jsonWebToken2);
        assertEquals(2, kelas.getKapasitasSaatIni());

        Set<KelasIrs> resultKelasIrs = irsService.postIrs(new ArrayList<Kelas>(), jsonWebToken);

        assertEquals(0, resultKelasIrs.size());
        assertEquals(1, kelas.getKapasitasSaatIni());
    }

    @Test
    void testServicePostIrsDoNothing() throws Exception {
        when(irsMahasiswaRepository.findAll()).thenReturn(irsMahasiswas);
        when(kelasRepository.getById("ID1")).thenReturn(kelas);

        irsService.postIrs(kelass, jsonWebToken);
        assertEquals(1, kelas.getKapasitasSaatIni());

        Set<KelasIrs> resultKelasIrs = irsService.postIrs(kelass, jsonWebToken);

        assertEquals(1, resultKelasIrs.size());
        assertEquals(1, kelas.getKapasitasSaatIni());
        assertEquals(kelas, resultKelasIrs.iterator().next().getKelas());
    }
}