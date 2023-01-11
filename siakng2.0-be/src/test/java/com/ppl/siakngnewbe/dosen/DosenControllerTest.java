package com.ppl.siakngnewbe.dosen;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppl.siakngnewbe.chat.ChatServiceImpl;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.PersetujuanIRSStatus;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

@WebMvcTest(controllers = DosenController.class)
class DosenControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private DosenServiceImpl dosenService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Dosen dosenModel;
    private Mahasiswa mahasiswaModel;
    private IrsMahasiswa irsMahasiswa;
    private String jsonWebTokenDosen;

    @BeforeEach
    public  void setUp() {
        Set<Mahasiswa> listMahasiswa = new HashSet<>();

        dosenModel = new Dosen();
        dosenModel.setId(2);
        dosenModel.setNamaLengkap("Grisha Yeager");
        dosenModel.setNip("19091210901");
        dosenModel.setUsername("grisha.yeager");
        dosenModel.setUserRole(UserModelRole.DOSEN);
        dosenModel.setPassword("dummydummypassword");

        mahasiswaModel = new Mahasiswa();
        mahasiswaModel.setId(1L);
        mahasiswaModel.setUsername("eren.yeager");
        mahasiswaModel.setPassword("dummyPassword");
        mahasiswaModel.setNamaLengkap("Eren Yeager");
        mahasiswaModel.setNpm("1906282831");
        mahasiswaModel.setUserRole(UserModelRole.MAHASISWA);
        mahasiswaModel.setStatus(StatusAkademik.AKTIF);
        mahasiswaModel.setIpk(4);

        irsMahasiswa = new IrsMahasiswa();
        irsMahasiswa.setStatusPersetujuan(PersetujuanIRSStatus.DISETUJUI);

        listMahasiswa.add(mahasiswaModel);
        dosenModel.setMahasiswaModelSet(listMahasiswa);

    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebTokenDosen =  JWT.create()
                .withSubject(dosenModel.getUsername())
                .withClaim("role",dosenModel.getUserRole().name())
                .withClaim("nip", dosenModel.getNip())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testControllerSetPA() throws Exception {
        when(dosenService.addPembimbingAkademik(jsonWebTokenDosen, "1906282831")).thenReturn(true);
        helperPostLoginAuthWithJWT();
        mvc.perform(post("/dosen/pa/1906282831").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebTokenDosen))
                .andExpect(status().isOk());
    }

    @Test
    void testControllerGetAllDosen() throws Exception {
        List<Dosen> listDosen = new ArrayList<>();
        listDosen.add(dosenModel);
        when(dosenService.getAllDosen()).thenReturn(listDosen);
        helperPostLoginAuthWithJWT();
        mvc.perform(get("/dosen/all").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebTokenDosen))
                .andExpect(status().isOk());
    }

    @Test
    void testControllerGetProfileDosen() throws Exception {
        when(dosenService.getDosenById(jsonWebTokenDosen)).thenReturn(dosenModel);
        helperPostLoginAuthWithJWT();
        mvc.perform(get("/dosen/profile").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebTokenDosen))
                .andExpect(status().isOk());
    }

    @Test
    void testControllerGetMahasiswaBimbingan() throws Exception {
        when(dosenService.getMahasiswaBimbingan(jsonWebTokenDosen)).thenReturn(dosenModel.getMahasiswaModelSet());
        helperPostLoginAuthWithJWT();
        mvc.perform(get("/dosen/bimbingan").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebTokenDosen))
                .andExpect(status().isOk());
    }

    @Test
    void testSetPersetujuanIRSMahasiswa() throws Exception {
        when(dosenService.setPersetujuanIrsMahasiswa(jsonWebTokenDosen, mahasiswaModel.getNpm(), irsMahasiswa)).thenReturn(true);
        helperPostLoginAuthWithJWT();
        mvc.perform(post("/dosen/setuju-irs/1906282831").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebTokenDosen)
                .content(mapToJson(irsMahasiswa)))
                .andExpect(status().isOk());
    }
}
