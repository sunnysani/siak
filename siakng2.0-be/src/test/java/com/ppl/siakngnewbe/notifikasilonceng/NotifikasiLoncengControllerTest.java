package com.ppl.siakngnewbe.notifikasilonceng;

import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(controllers = NotifikasiLoncengController.class)
class NotifikasiLoncengControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private NotifikasiLoncengService notifikasiLoncengService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Mahasiswa mahasiswa;
    private NotifikasiLonceng notifikasiLonceng;

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
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebTokenMahasiswa =  JWT.create()
                .withSubject(mahasiswa.getUsername())
                .withClaim("role",mahasiswa.getUserRole().name())
                .withClaim("npm", mahasiswa.getNpm())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testControllerGetNotifikasiForMahasiswa() throws Exception {
        List<NotifikasiLonceng> listNotifikasi = new ArrayList<>();
        listNotifikasi.add(notifikasiLonceng);
        when(notifikasiLoncengService.getNotifikasiForMahasiswa(jsonWebTokenMahasiswa)).thenReturn(listNotifikasi);
        helperPostLoginAuthWithJWT();
        mvc.perform(get("/notifikasi-lonceng").contentType(MediaType.APPLICATION_JSON)
        .header("Authorization","Bearer " + jsonWebTokenMahasiswa))
        .andExpect(status().isOk());
    }
    
    @Test
    void testControllerSetReadNotifByMahasiswa() throws Exception {
        when(notifikasiLoncengService.readAllNotifikasiByMahasiswa(jsonWebTokenMahasiswa)).thenReturn(true);
        helperPostLoginAuthWithJWT();
        mvc.perform(post("/notifikasi-lonceng/read").contentType(MediaType.APPLICATION_JSON)
        .header("Authorization","Bearer " + jsonWebTokenMahasiswa))
        .andExpect(status().isOk());
    }
}
