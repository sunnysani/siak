package com.ppl.siakngnewbe.irsmahasiswa;

import static org.mockito.Mockito.when;

import java.util.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = IrsMahasiswaController.class)
class IrsMahasiswaControllerTest {
    
	@Autowired
    private MockMvc mvc;
    
    @MockBean
    private IrsMahasiswaService irsService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private IrsMahasiswa irs;
    private Set<KelasIrs> kelasIrss = new HashSet<KelasIrs>();
    private Mahasiswa mahasiswa = new Mahasiswa();
    private List<Kelas> kelass;
    private Mahasiswa mahasiswaModel;
    private String jsonWebToken;

    @BeforeEach
    public void SetUp() {

        kelass = new ArrayList<Kelas>();
        kelasIrss = new HashSet<KelasIrs>();

        Kelas kelas = new Kelas();
        KelasIrs kelasIrs = new KelasIrs();

        kelass.add(kelas);
        kelasIrss.add(kelasIrs);
        
        irs = new IrsMahasiswa();
        irs.setIdIrs("ID1");
        irs.setKelasIrsSet(kelasIrss);
        irs.setMahasiswa(mahasiswa);
        irs.setSemester(1);
        irs.setSksa(24);
        irs.setSksl(24);
        irs.setTotalMutu(96);

        mahasiswaModel = new Mahasiswa();
        mahasiswaModel.setId(1L);
        mahasiswaModel.setNamaLengkap("Eren Yeager");
        mahasiswaModel.setUsername("eren.yeager");
        mahasiswaModel.setPassword("surveycorps");
        mahasiswaModel.setIpk(4);
        mahasiswaModel.setNpm("123456789");
        mahasiswaModel.setStatus(StatusAkademik.AKTIF);
        mahasiswaModel.setUserRole(UserModelRole.MAHASISWA);
        helperPostLoginAuthWithJWT();
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebToken = "Bearer " + JWT.create()
                .withSubject(mahasiswaModel.getUsername())
                .withClaim("role",mahasiswaModel.getUserRole().name())
                .withClaim("npm", 123456789)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
	void testControllerGetIrsSuccess() throws Exception {
        when(irsService.getIrs(jsonWebToken)).thenReturn(irs);
        mvc.perform(get("/irsMahasiswa/get")
            .header("Authorization", jsonWebToken)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

    @Test
    void testControllerPostIrsSuccess() throws Exception {
        when(irsService.postIrs(kelass, jsonWebToken)).thenReturn(kelasIrss);
        mvc.perform(post("/irsMahasiswa/post")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", jsonWebToken)
        )
            .andExpect(status().isOk());
    }

}
