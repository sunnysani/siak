package com.ppl.siakngnewbe.pembayaran;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PembayaranController.class)
public class PembayaranControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PembayaranService pembayaranService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private Mahasiswa mahasiswaModel;
    private String jsonWebToken;
    private Pembayaran pembayaran;
    private TahunAjaran tahunAjaran;
    private List<Map<String, Object>> pembayarans;
    Map<String, Object> dataPembayaran;

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

        pembayaran = new Pembayaran();
        tahunAjaran = new TahunAjaran();
        pembayaran.setId(1);
        pembayaran.setTahunAjaran(tahunAjaran);
        pembayaran.setMahasiswa(mahasiswaModel);
        pembayaran.setSemester(1);
        pembayaran.setTunggakan(0);
        pembayaran.setDenda(0);
        pembayaran.setMataUang("IDR");
        pembayaran.setTagihan(7500000);
        pembayaran.setTotalDibayar(7500000);
        pembayaran.setStatus(PembayaranStatus.LUNAS);

        dataPembayaran = new HashMap<>();

        dataPembayaran.put("Tahun Ajaran", pembayaran.getTahunAjaran().getNama());
        dataPembayaran.put("Term", pembayaran.getTahunAjaran().getTerm());
        dataPembayaran.put("Mata Uang", pembayaran.getMataUang());
        dataPembayaran.put("Tagihan", pembayaran.getTagihan());
        dataPembayaran.put("Tunggakan", pembayaran.getTunggakan());
        dataPembayaran.put("Denda", pembayaran.getDenda());

        int totalTagihan = pembayaran.getTunggakan() + pembayaran.getDenda();

        dataPembayaran.put("Total Tagihan", totalTagihan);
        dataPembayaran.put("Total Pembayaran", pembayaran.getTotalDibayar());

        int sisaTagihan = totalTagihan - pembayaran.getTotalDibayar();

        dataPembayaran.put("Sisa Tagihan", sisaTagihan);
        dataPembayaran.put("Status", pembayaran.getStatus());

        pembayarans = new ArrayList<>();
        pembayarans.add(dataPembayaran);

        helperPostLoginAuthWithJWT();
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebToken = "Bearer " + JWT.create()
                .withSubject(mahasiswaModel.getUsername())
                .withClaim("role", mahasiswaModel.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testControllerGetPembayaranSuccess() throws Exception {
        when(pembayaranService.getPembayaran(jsonWebToken)).thenReturn(pembayarans);
        mvc.perform(get("/pembayaran/getPembayaran")
                        .header("Authorization", jsonWebToken)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testControllerGetPembayaranFail() throws Exception {
        when(pembayaranService.getPembayaran(jsonWebToken)).thenReturn(null);
        mvc.perform(get("/pembayaran/getPembayaran")
                        .header("Authorization", jsonWebToken)
                )
                .andExpect(status().isOk());
    }
}
