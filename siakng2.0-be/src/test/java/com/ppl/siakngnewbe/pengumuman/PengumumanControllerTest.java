package com.ppl.siakngnewbe.pengumuman;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = PengumumanController.class)
class PengumumanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PengumumanServiceImpl pengumumanService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private PengumumanModel pengumumanModel;
    private PengumumanModel pengumumanModel2;
    private String jsonWebToken;
    private Mahasiswa mahasiswa;

    @BeforeEach
    void setUp() {
        pengumumanModel = new PengumumanModel();
        pengumumanModel.setId(1L);
        pengumumanModel.setJudul("Test Pengumuman");
        pengumumanModel.setIsi("Test");
        pengumumanModel.setPenulis("Tester");
        pengumumanModel.setWaktu(new Date());

        pengumumanModel2 = new PengumumanModel();
        pengumumanModel.setId(2L);
        pengumumanModel.setJudul("Test Pengumuman 2");
        pengumumanModel.setIsi("Test 2");
        pengumumanModel.setPenulis("Tester 2");
        pengumumanModel.setWaktu(new Date());

        mahasiswa = new Mahasiswa();
        mahasiswa.setUsername("eren.yeager");
        mahasiswa.setNpm("1907282822");
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebToken =  JWT.create()
                .withSubject(mahasiswa.getUsername())
                .withClaim("role",mahasiswa.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testControllerGetPengumumanById() throws Exception {
        doReturn(pengumumanModel).when(pengumumanService).getPengumumanById(2);
        helperPostLoginAuthWithJWT();
        mockMvc.perform(get("/pengumuman/2")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jsonWebToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.result.id").value("2")
                );
    }

    @Test
    void testControllerGetPengumumanAll() throws Exception {
        List<PengumumanModel> listPengumuman = new ArrayList<>();
        listPengumuman.add(pengumumanModel);
        listPengumuman.add(pengumumanModel2);
        helperPostLoginAuthWithJWT();
        doReturn(listPengumuman).when(pengumumanService).getPengumumanAll();

        mockMvc.perform(get("/pengumuman")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jsonWebToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.result.*", hasSize(2))
                );
    }
}
