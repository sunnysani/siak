package com.ppl.siakngnewbe.mataKuliah;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.mahasiswa.*;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.matakuliah.MataKuliahController;
import com.ppl.siakngnewbe.matakuliah.MataKuliahService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MataKuliahController.class)
class MataKuliahControllerTest {

	@Autowired
    private MockMvc mvc;
    
    @MockBean
    private MataKuliahService mataKuliahService;
    
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private List<MataKuliah> mataKuliahs;
    private Mahasiswa mahasiswaModel;
    private String jsonWebToken;
    private String data;

    @BeforeEach
    public void SetUp() {

        mataKuliahs = new ArrayList<MataKuliah>();
        MataKuliah mataKuliah = new MataKuliah();
        mataKuliahs.add(mataKuliah);
        
        mahasiswaModel = new Mahasiswa();
        mahasiswaModel.setId(1L);
        mahasiswaModel.setNamaLengkap("Eren Yeager");
        mahasiswaModel.setUsername("eren.yeager");
        mahasiswaModel.setPassword("surveycorps");
        mahasiswaModel.setIpk(4);
        mahasiswaModel.setNpm("19062727231");
        mahasiswaModel.setStatus(StatusAkademik.AKTIF);
        mahasiswaModel.setUserRole(UserModelRole.MAHASISWA);
        helperPostLoginAuthWithJWT();
    }
    
    private void helperPostLoginAuthWithJWT() {
        jsonWebToken = "Bearer " + JWT.create()
                .withSubject(mahasiswaModel.getUsername())
                .withClaim("role",mahasiswaModel.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
	void testControllerGetMataKuliahSuccess() throws Exception {
        data = "latest";
        String irsType = "isi";
        when(mataKuliahService.getMataKuliah(jsonWebToken, data, irsType)).thenReturn("{}");
        mvc.perform(get("/mataKuliah/getAll")
            .header("Authorization", jsonWebToken)
            .param("periode", "latest").param("type", irsType)
        )
            .andExpect(status().isOk());
	}

    @Test
    void testControllerGetMataKuliahNotOnStatus() throws Exception {
        data = "latest";
        String irsType = "add-drop";
        when(mataKuliahService.getMataKuliah(jsonWebToken, data, irsType)).thenReturn("gagal");
        mvc.perform(get("/mataKuliah/getAll")
                        .header("Authorization", jsonWebToken)
                        .param("periode", "latest").param("type", irsType)
                )
                .andExpect(status().isOk());
    }

    @Test
	void testControllerGetMataKuliahTakenSuccess() throws Exception {
        when(mataKuliahService.getMataKuliahTaken(jsonWebToken)).thenReturn(mataKuliahs);
        mvc.perform(get("/mataKuliah/getTaken")
            .header("Authorization", jsonWebToken)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

}
