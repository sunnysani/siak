package com.ppl.siakngnewbe.mahasiswa;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.user.UserModelRole;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MahasiswaController.class)
class MahasiswaControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private  MahasiswaServiceImpl mahasiswaService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Mahasiswa mahasiswaModel;
    private String jsonWebToken;

    @BeforeEach
    public void setUp() {
        mahasiswaModel = new Mahasiswa();
        mahasiswaModel.setId(1L);
        mahasiswaModel.setNamaLengkap("Eren Yeager");
        mahasiswaModel.setUsername("eren.yeager");
        mahasiswaModel.setPassword("surveycorps");
        mahasiswaModel.setIpk(4);
        mahasiswaModel.setNpm("19062727231");
        mahasiswaModel.setStatus(StatusAkademik.AKTIF);
        mahasiswaModel.setUserRole(UserModelRole.MAHASISWA);
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebToken =  JWT.create()
                .withSubject(mahasiswaModel.getUsername())
                .withClaim("role",mahasiswaModel.getUserRole().name())
                .withClaim("npm", mahasiswaModel.getNpm())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testControllerGetMahasiswaByUsername()  {
        when(mahasiswaService.getMahasiswaByUsername("eren.yeager")).thenReturn(mahasiswaModel);
        helperPostLoginAuthWithJWT();
        try {
            mvc.perform(get("/mahasiswa/detail").contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization","Bearer " + jsonWebToken)
            )
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testControllerGetMahasiswaByUsernameFailed()  {
        when(mahasiswaService.getMahasiswaByUsername("eren.yeager")).thenReturn(mahasiswaModel);
        helperPostLoginAuthWithJWT();
        try {
            mvc.perform(get("/mahasiswa/detail").contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testControllerGetMahasiswaByUsernameFailed2()  {
        when(mahasiswaService.getMahasiswaByUsername("eren.yeager")).thenReturn(mahasiswaModel);
        helperPostLoginAuthWithJWT();
        try {
            mvc.perform(get("/mahasiswa/detail").contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization","" + jsonWebToken)
            )
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testControllerGetMahasiswaByUsernameFailed3()  {
        when(mahasiswaService.getMahasiswaByUsername("eren.yeager")).thenReturn(mahasiswaModel);
        helperPostLoginAuthWithJWT();
        try {
            mvc.perform(get("/mahasiswa/detail").contentType(MediaType.APPLICATION_JSON)
                    .header("" + jsonWebToken)
            )
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
