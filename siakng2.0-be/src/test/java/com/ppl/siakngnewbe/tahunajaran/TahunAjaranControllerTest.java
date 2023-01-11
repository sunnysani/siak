package com.ppl.siakngnewbe.tahunajaran;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TahunAjaranController.class)
class TahunAjaranControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TahunAjaranService tahunAjaranService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private Mahasiswa mahasiswaModel;
    private Dosen dosenModel;
    private TahunAjaran tahunAjaran;
    private String jsonWebTokenMahasiswa;
    private String jsonWebTokenDosen;
    private String status;
    private MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", java.nio.charset.Charset.forName("UTF-8"));

    @BeforeEach
    public void SetUp() {

        Calendar testCalendar = Calendar.getInstance();
        tahunAjaran = new TahunAjaran();
        tahunAjaran.setNama("2021/2022-3");
        tahunAjaran.setStatus(TahunAjaranStatus.IRS_ISI);
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

        mahasiswaModel = new Mahasiswa();
        mahasiswaModel.setId(1L);
        mahasiswaModel.setNamaLengkap("Eren Yeager");
        mahasiswaModel.setUsername("eren.yeager");
        mahasiswaModel.setPassword("surveycorps");
        mahasiswaModel.setIpk(4);
        mahasiswaModel.setNpm("19062727231");
        mahasiswaModel.setStatus(StatusAkademik.AKTIF);
        mahasiswaModel.setUserRole(UserModelRole.MAHASISWA);

        dosenModel = new Dosen();
        dosenModel.setId(2);
        dosenModel.setNamaLengkap("Grisha Yeager");
        dosenModel.setNip("19091210901");
        dosenModel.setUsername("grisha.yeager");
        dosenModel.setUserRole(UserModelRole.DOSEN);
        dosenModel.setPassword("dummydummypassword");

        helperPostLoginAuthWithJWT();
        when(tahunAjaranService.getTahunAjaran("latest")).thenReturn(tahunAjaran);
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebTokenMahasiswa = "Bearer " + JWT.create()
                .withSubject(mahasiswaModel.getUsername())
                .withClaim("role",mahasiswaModel.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));

        jsonWebTokenDosen = "Bearer " + JWT.create()
                .withSubject(dosenModel.getUsername())
                .withClaim("role",dosenModel.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testControllerCreateTahunAjaran() throws Exception {
        status = "isi";
        mapToJson(tahunAjaran);
        when(tahunAjaranService.postTahunAjaran(jsonWebTokenDosen, tahunAjaran)).thenReturn(tahunAjaran);
        mvc.perform(post("/tahunAjaran/")
                .header("Authorization", jsonWebTokenDosen)
                .content(mapToJson(tahunAjaran))
                .contentType(MEDIA_TYPE_JSON_UTF8)
                .accept(MEDIA_TYPE_JSON_UTF8)
        )
                .andExpect(status().isOk());
    }
	
	@Test
    void testControllerGetLatestTahunAjaran() throws Exception {
        when(tahunAjaranService.getTahunAjaran("latest")).thenReturn(tahunAjaran);
        mvc.perform(get("/tahunAjaran/")
                .accept(MEDIA_TYPE_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    void testControllerSetTahunAjaranOnStatus() throws Exception {
        status = "isi";
        when(tahunAjaranService.setTahunAjaranStatus(jsonWebTokenDosen, tahunAjaran, status)).thenReturn(true);
        mvc.perform(post("/tahunAjaran/setStatus")
                        .header("Authorization", jsonWebTokenDosen)
                        .param("status", status)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testControllerGetTahunAjaran() throws Exception {
        status = "isi";
        when(tahunAjaranService.getTahunAjaranStatus(tahunAjaran)).thenReturn(TahunAjaranStatus.IRS_ISI.toString());
        mvc.perform(get("/tahunAjaran/getStatus")
                        .header("Authorization", jsonWebTokenDosen)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testControllerSetTahunAjaranNotOnStatus() throws Exception {
        status = "add-drop";
        when(tahunAjaranService.setTahunAjaranStatus(jsonWebTokenDosen, tahunAjaran, status)).thenReturn(false);
        mvc.perform(post("/tahunAjaran/setStatus")
                        .header("Authorization", jsonWebTokenDosen)
                        .param("status", status)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testControllerSetTahunAjaranRoleMahasiswa() throws Exception {
        status = "add-drop";
        when(tahunAjaranService.setTahunAjaranStatus(jsonWebTokenMahasiswa, tahunAjaran, status)).thenReturn(false);
        mvc.perform(post("/tahunAjaran/setStatus")
                        .header("Authorization", jsonWebTokenMahasiswa)
                        .param("status", status)
                )
                .andExpect(status().isOk());
    }

}
