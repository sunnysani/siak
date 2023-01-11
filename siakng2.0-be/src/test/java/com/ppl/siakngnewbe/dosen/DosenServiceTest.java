package com.ppl.siakngnewbe.dosen;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.email.EmailService;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.irsmahasiswa.PersetujuanIRSStatus;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaModelRepository;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.notifikasilonceng.NotifikasiLonceng;
import com.ppl.siakngnewbe.notifikasilonceng.NotifikasiLoncengRepository;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class DosenServiceTest {
    @Mock
    private MahasiswaModelRepository mahasiswaRepository;

    @Mock
    private DosenModelRepository dosenRepository;

    @Mock
    private IrsMahasiswaRepository irsMahasiswaRepository;

    @Mock
    private NotifikasiLoncengRepository notifikasiLoncengRepository;

    @InjectMocks
    private DosenServiceImpl dosenService;
	
	@Mock
	private EmailService emailService;

    private Mahasiswa mahasiswaModel;
    private Dosen dosenModel;
    private IrsMahasiswa irsMahasiswa;
    private NotifikasiLonceng notifikasiLonceng;
    private String jsonWebTokenDosen;
    private Set<NotifikasiLonceng> listNotifikasiMahasiswa;

    @BeforeEach
    public void setUp() {
        Set<Mahasiswa> listMahasiswa = new HashSet<>();
        mahasiswaModel = new Mahasiswa();
        mahasiswaModel.setId(1L);
        mahasiswaModel.setUsername("eren.yeager");
        mahasiswaModel.setPassword("dummyPassword");
        mahasiswaModel.setNamaLengkap("Eren Yeager");
        mahasiswaModel.setNpm("1906282831");
        mahasiswaModel.setUserRole(UserModelRole.MAHASISWA);
        mahasiswaModel.setStatus(StatusAkademik.AKTIF);
        mahasiswaModel.setIpk(4);

        dosenModel = new Dosen();
        dosenModel.setId(2);
        dosenModel.setNamaLengkap("Grisha Yeager");
        dosenModel.setNip("19091210901");
        dosenModel.setUsername("grisha.yeager");
        dosenModel.setUserRole(UserModelRole.DOSEN);
        dosenModel.setPassword("dummydummypassword");
        dosenModel.setDiChatOleh(listMahasiswa);

        listMahasiswa.add(mahasiswaModel);
        dosenModel.setMahasiswaModelSet(listMahasiswa);
        dosenModel.setDiChatOleh(listMahasiswa);

        irsMahasiswa = new IrsMahasiswa();
        irsMahasiswa.setIdIrs("ID1");
        irsMahasiswa.setKelasIrsSet(null);
        irsMahasiswa.setMahasiswa(mahasiswaModel);
        irsMahasiswa.setSemester(1);
        irsMahasiswa.setSksa(24);
        irsMahasiswa.setSksl(24);
        irsMahasiswa.setTotalMutu(96);
        irsMahasiswa.setStatusPersetujuan(PersetujuanIRSStatus.DISETUJUI);

        mahasiswaModel.setPembimbingAkademik(dosenModel);

        notifikasiLonceng = new NotifikasiLonceng("Dummy notification", mahasiswaModel);
        notifikasiLonceng.setId(1L);

        listNotifikasiMahasiswa = new HashSet<>();
        listNotifikasiMahasiswa.add(notifikasiLonceng);

        mahasiswaModel.setNotifikasiLoncengSet(listNotifikasiMahasiswa);
        jsonWebTokenDosen = JWT.create()
                .withSubject(dosenModel.getUsername())
                .withClaim("role", dosenModel.getUserRole().name())
                .withClaim("nip", dosenModel.getNip())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testServiceAddPASuccess() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        Boolean result = dosenService.addPembimbingAkademik(jsonWebTokenDosen, mahasiswaModel.getNpm());
        assertTrue(result);
    }

    @Test
    void testServiceAddPANoMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        Boolean result = dosenService.addPembimbingAkademik(jsonWebTokenDosen, mahasiswaModel.getNpm());
        assertFalse(result);
    }

    @Test
    void testServiceAddPANoDosen() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        Boolean result = dosenService.addPembimbingAkademik(jsonWebTokenDosen, mahasiswaModel.getNpm());
        assertFalse(result);
    }

    @Test
    void testServiceAddPAFail() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        Boolean result = dosenService.addPembimbingAkademik(jsonWebTokenDosen, mahasiswaModel.getNpm());
        assertFalse(result);
    }

    @Test
    void testServiceGetAllDosen() {
        List<Dosen> listDosen = new ArrayList<>();
        listDosen.add(dosenModel);
        when(dosenRepository.findAll()).thenReturn(listDosen);
        List<Dosen> res = dosenService.getAllDosen();
        assertEquals(listDosen, res);
    }

    @Test
    void testServiceGetDosenByToken() {
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        Dosen res = dosenService.getDosenById(jsonWebTokenDosen);
        assertEquals(dosenModel, res);
    }

    @Test
    void testServiceGetSetMahasiswa() {
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        Set<Mahasiswa> res = dosenService.getMahasiswaBimbingan(jsonWebTokenDosen);
        assertEquals(dosenModel.getMahasiswaModelSet(), res);
    }

    @Test
    void testSetPersetujuanMahasiswaAndDosenExistAndDisetujui() {
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(irsMahasiswaRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswaModel)).thenReturn(irsMahasiswa);
        assertTrue(dosenService.setPersetujuanIrsMahasiswa(jsonWebTokenDosen, mahasiswaModel.getNpm(), irsMahasiswa));
    }

    @Test
    void testSetPersetujuanMahasiswaAndDosenExistAndTidakDisetujui() {
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        irsMahasiswa.setStatusPersetujuan(PersetujuanIRSStatus.TIDAK_DISETUJUI);
        when(irsMahasiswaRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswaModel)).thenReturn(irsMahasiswa);
        assertTrue(dosenService.setPersetujuanIrsMahasiswa(jsonWebTokenDosen, mahasiswaModel.getNpm(), irsMahasiswa));
    }

    @Test
    void testSetPersetujuanMahasiswaAndDosenExistButWrongPA() {
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        Dosen dosenLain = new Dosen();
        dosenLain.setNamaLengkap("Unknown Dosen");
        mahasiswaModel.setPembimbingAkademik(dosenLain);
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        assertFalse(dosenService.setPersetujuanIrsMahasiswa(jsonWebTokenDosen, mahasiswaModel.getNpm(), irsMahasiswa));
    }

    @Test
    void testSetPersetujuanMahasiswaNullAndDosenExist() {
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        assertFalse(dosenService.setPersetujuanIrsMahasiswa(jsonWebTokenDosen, mahasiswaModel.getNpm(), irsMahasiswa));
    }

    @Test
    void testSetPersetujuanMahasiswaExistAndDosenNull() {
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        assertFalse(dosenService.setPersetujuanIrsMahasiswa(jsonWebTokenDosen, mahasiswaModel.getNpm(), irsMahasiswa));
    }

    @Test
    void testSetPersetujuanMahasiswaAndDosenNull() {
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        assertFalse(dosenService.setPersetujuanIrsMahasiswa(jsonWebTokenDosen, mahasiswaModel.getNpm(), irsMahasiswa));
    }
}
