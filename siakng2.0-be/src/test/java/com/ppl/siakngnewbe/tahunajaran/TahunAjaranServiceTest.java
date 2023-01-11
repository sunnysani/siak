package com.ppl.siakngnewbe.tahunajaran;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.dosen.DosenService;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.notifikasilonceng.NotifikasiLoncengRepository;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class TahunAjaranServiceTest {

    @Mock
    private TahunAjaranRepository tahunAjaranRepository;

    @Mock
    private NotifikasiLoncengRepository notifikasiLoncengRepository;

    @Mock
    private DosenService dosenService;

    @Mock
    private Scheduler scheduler;

    @Mock
    private TahunAjaranJob tahunAjaranJob;

    @InjectMocks
    private TahunAjaranService tahunAjaranService;

    private List<TahunAjaran> tahunAjarans;
    private TahunAjaran tahunAjaran;
    private TahunAjaran tahunAjaran2;
    private TahunAjaran tahunAjaran3;
    private Dosen dosenModel;
    private String jsonWebTokenDosen;

    private String jsonWebToken;

    @BeforeEach
    public void SetUp() {

        tahunAjaran = new TahunAjaran();
        tahunAjaran2 = new TahunAjaran();
        tahunAjaran3 = new TahunAjaran();

        Calendar testCalendar = Calendar.getInstance();

        tahunAjaran.setNama("2021/2022");
        tahunAjaran.setTerm(1);
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
        tahunAjaran2.setNama("2021/2022");
        tahunAjaran2.setTerm(2);
        tahunAjaran3.setNama("2020/2021");
        tahunAjaran3.setTerm(2);

        tahunAjaran2.setStatus(TahunAjaranStatus.IRS_ISI);

        tahunAjarans = new ArrayList<>();
        tahunAjarans.add(tahunAjaran);
        tahunAjarans.add(tahunAjaran2);
        tahunAjarans.add(tahunAjaran3);

        dosenModel = new Dosen();
        dosenModel.setId(2);
        dosenModel.setNamaLengkap("Grisha Yeager");
        dosenModel.setNip("19091210901");
        dosenModel.setUsername("grisha.yeager");
        dosenModel.setUserRole(UserModelRole.DOSEN);
        dosenModel.setPassword("dummydummypassword");

        helperPostLoginAuthWithJWT();
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebToken = "Bearer " + JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testPostTahunAjaranByDosen() throws Exception {
        when(dosenService.getDosenById(jsonWebToken)).thenReturn(dosenModel);
        assertEquals(tahunAjaran, tahunAjaranService.postTahunAjaran(jsonWebToken, tahunAjaran));

        // When status is predefiened
        tahunAjaran.setStatus(TahunAjaranStatus.IRS_ADD_DROP);
        assertEquals(tahunAjaran, tahunAjaranService.postTahunAjaran(jsonWebToken, tahunAjaran));
    }

    @Test
    void testPostTahunAjaranByNotDosen() throws Exception {
        assertNull(tahunAjaranService.postTahunAjaran("wrong-token", tahunAjaran));
    }

    @Test
    void testServiceGetTahunAjaranByPeriodeSuccess() throws Exception {
        when(tahunAjaranRepository.findTopByNamaAndTerm("2021/2022", 1)).thenReturn(tahunAjaran);
        assertEquals(tahunAjaran, tahunAjaranService.getTahunAjaran("2021/2022-1"));
    }

    @Test
    void testServiceGetTahunAjaranLatestSuccess() throws Exception {
        when(tahunAjaranRepository.findTopByOrderByNamaDescTermDesc()).thenReturn(tahunAjaran2);
        assertEquals(tahunAjaran2, tahunAjaranService.getTahunAjaran("latest"));
    }

    @Test
    void testServiceGetTahunAjaranByPeriodeFail() throws Exception {
        when(tahunAjaranRepository.findTopByNamaAndTerm("2021/2022", 3)).thenReturn(null);
        assertNull(tahunAjaranService.getTahunAjaran("2021/2022-3"));
    }

    @Test
    void testServiceSetTahunAjaranStatusIsi() throws Exception {
        String status = "isi";
        when(dosenService.getDosenById(jsonWebToken)).thenReturn(dosenModel);
        assertEquals(true, tahunAjaranService.setTahunAjaranStatus(jsonWebToken, tahunAjaran2, status));
    }

    @Test
    void testServiceSetTahunAjaranStatusAddDrop() throws Exception {
        String status = "add-drop";
        when(dosenService.getDosenById(jsonWebToken)).thenReturn(dosenModel);
        assertEquals(true, tahunAjaranService.setTahunAjaranStatus(jsonWebToken, tahunAjaran2, status));
    }

    void testServiceSetTahunAjaranStatusDosen() throws Exception {
        String status = "dosen";
        when(dosenService.getDosenById(jsonWebToken)).thenReturn(dosenModel);
        assertEquals(true, tahunAjaranService.setTahunAjaranStatus(jsonWebToken, tahunAjaran2, status));
    }

    void testServiceSetTahunAjaranStatusClosed() throws Exception {
        String status = "closed";
        when(dosenService.getDosenById(jsonWebToken)).thenReturn(dosenModel);
        assertEquals(true, tahunAjaranService.setTahunAjaranStatus(jsonWebToken, tahunAjaran2, status));
    }

    @Test
    void testServiceSetTahunAjaranStatusWrongStatus() throws Exception {
        String status = "wrong-status";
        when(dosenService.getDosenById(jsonWebToken)).thenReturn(dosenModel);
        assertEquals(false, tahunAjaranService.setTahunAjaranStatus(jsonWebToken, tahunAjaran2, status));
    }

    @Test
    void testServiceGetTahunAjaran() throws Exception {
        assertEquals("IRS_ISI", tahunAjaranService.getTahunAjaranStatus(tahunAjaran2));
    }

    @Test
    void testServiceSetTahunAjaranRoleMahasiswa() throws Exception {
        String status = "isi";
        when(dosenService.getDosenById(jsonWebToken)).thenReturn(null);
        assertEquals(false, tahunAjaranService.setTahunAjaranStatus(jsonWebToken, tahunAjaran2, status));
    }
}