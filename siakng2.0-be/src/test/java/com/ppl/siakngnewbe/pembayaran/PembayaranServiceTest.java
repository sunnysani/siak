package com.ppl.siakngnewbe.pembayaran;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaRepository;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PembayaranServiceTest {

    @Mock
    private PembayaranRepository pembayaranRepository;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @InjectMocks
    private PembayaranService pembayaranService;

    private static Mahasiswa mahasiswa;

    private static TahunAjaran tahunAjaran;
    private static TahunAjaran tahunAjaran1;

    private static Pembayaran pembayaran;
    private static Pembayaran pembayaran1;

    Map<String, Object> dataPembayaran;
    Map<String, Object> dataPembayaran1;

    private List<Pembayaran> pembayarans;

    private String jsonWebToken;

    @BeforeEach
    public void SetUp() {
        mahasiswa = new Mahasiswa();
        mahasiswa.setId(1L);
        mahasiswa.setNamaLengkap("Eren Yeager");
        mahasiswa.setUsername("eren.yeager");
        mahasiswa.setPassword("surveycorps");
        mahasiswa.setIpk(4);
        mahasiswa.setNpm("123456789");
        mahasiswa.setStatus(StatusAkademik.AKTIF);
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);

        tahunAjaran = new TahunAjaran();
        tahunAjaran.setNama("2020/2021");
        tahunAjaran.setTerm(1);

        tahunAjaran1 = new TahunAjaran();
        tahunAjaran1.setNama("2020/2021");
        tahunAjaran1.setTerm(2);

        pembayaran = new Pembayaran();
        pembayaran.setId(1);
        pembayaran.setTahunAjaran(tahunAjaran);
        pembayaran.setMahasiswa(mahasiswa);
        pembayaran.setSemester(1);
        pembayaran.setTunggakan(0);
        pembayaran.setDenda(0);
        pembayaran.setMataUang("IDR");
        pembayaran.setTagihan(7500000);
        pembayaran.setTotalDibayar(7500000);
        pembayaran.setStatus(PembayaranStatus.LUNAS);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.YEAR, 2022);

        pembayaran.setDeadline(calendar);

        pembayaran1 = new Pembayaran();
        pembayaran1.setTahunAjaran(tahunAjaran1);
        pembayaran1.setMahasiswa(mahasiswa);
        pembayaran1.setSemester(2);
        pembayaran1.setTunggakan(0);
        pembayaran1.setDenda(0);
        pembayaran1.setMataUang("IDR");
        pembayaran1.setTagihan(7500000);
        pembayaran1.setTotalDibayar(0);
        pembayaran1.setStatus(PembayaranStatus.LUNAS);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DATE, 1);
        calendar1.set(Calendar.MONTH, 11);
        calendar1.set(Calendar.YEAR, 2022);

        pembayaran1.setDeadline(calendar1);

        pembayaran.setDeadline(calendar);

        pembayarans = new ArrayList<Pembayaran>();
        pembayarans.add(pembayaran);
        pembayarans.add(pembayaran1);

        dataPembayaran = new HashMap<>();
        dataPembayaran1 = new HashMap<>();

        dataPembayaran.put("Tahun Ajaran", pembayaran.getTahunAjaran().getNama());
        dataPembayaran.put("Term", pembayaran.getTahunAjaran().getTerm());
        dataPembayaran.put("Mata Uang", pembayaran.getMataUang());
        dataPembayaran.put("Tagihan", pembayaran.getTagihan());
        dataPembayaran.put("Tunggakan", pembayaran.getTunggakan());
        dataPembayaran.put("Denda", pembayaran.getDenda());

        int totalTagihan = pembayaran.getTagihan() + pembayaran.getTunggakan() + pembayaran.getDenda();

        dataPembayaran.put("Total Tagihan", totalTagihan);
        dataPembayaran.put("Total Pembayaran", pembayaran.getTotalDibayar());

        int sisaTagihan = totalTagihan - pembayaran.getTotalDibayar();

        dataPembayaran.put("Sisa Tagihan", sisaTagihan);
        dataPembayaran.put("Status", pembayaran.getStatus());
        dataPembayaran.put("Deadline", pembayaran.getDeadline());

        dataPembayaran1.put("Tahun Ajaran", pembayaran1.getTahunAjaran().getNama());
        dataPembayaran1.put("Term", pembayaran1.getTahunAjaran().getTerm());
        dataPembayaran1.put("Mata Uang", pembayaran1.getMataUang());
        dataPembayaran1.put("Tagihan", pembayaran1.getTagihan());
        dataPembayaran1.put("Tunggakan", pembayaran1.getTunggakan());
        dataPembayaran1.put("Denda", pembayaran1.getDenda());

        totalTagihan = pembayaran1.getTagihan() + pembayaran1.getTunggakan() + pembayaran1.getDenda();

        dataPembayaran1.put("Total Tagihan", totalTagihan);
        dataPembayaran1.put("Total Pembayaran", pembayaran1.getTotalDibayar());

        sisaTagihan = totalTagihan - pembayaran1.getTotalDibayar();

        dataPembayaran1.put("Sisa Tagihan", sisaTagihan);
        dataPembayaran1.put("Status", pembayaran1.getStatus());
        dataPembayaran1.put("Deadline", pembayaran1.getDeadline());

        helperPostLoginAuthWithJWT();
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebToken = "Bearer " + JWT.create()
                .withSubject(mahasiswa.getUsername())
                .withClaim("npm", "123456789")
                .withClaim("role", mahasiswa.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testServiceGetPembayaran() throws Exception {
        lenient().when(mahasiswaRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        when(pembayaranRepository.findByMahasiswaOrderBySemester(mahasiswa)).thenReturn(pembayarans);
        assertEquals(dataPembayaran, pembayaranService.getPembayaran(jsonWebToken).get(0));
        assertEquals(dataPembayaran1, pembayaranService.getPembayaran(jsonWebToken).get(1));
    }

}
