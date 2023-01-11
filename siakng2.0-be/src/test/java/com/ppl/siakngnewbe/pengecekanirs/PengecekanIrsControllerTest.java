package com.ppl.siakngnewbe.pengecekanirs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.ppl.siakngnewbe.security.utils.SecurityConstant.EXPIRATION_TIME;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaModelRepository;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.pengecekanirs.checker.IpsOutOfBoundException;
import com.ppl.siakngnewbe.pengecekanirs.result.JadwalResult;
import com.ppl.siakngnewbe.pengecekanirs.result.KapasitasResult;
import com.ppl.siakngnewbe.pengecekanirs.result.KelasProxy;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratProxy;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratResult;
import com.ppl.siakngnewbe.pengecekanirs.result.SksResult;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import com.ppl.siakngnewbe.user.UserModelRole;

@WebMvcTest(controllers = PengecekanIrsController.class)
class PengecekanIrsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    
    @MockBean
    private MahasiswaModelRepository mahasiswaRepository;

    @MockBean
    private PengecekanIrsService pengecekanIrsService;

    private String token;
    private Mahasiswa mahasiswa;
    private IrsMahasiswa irs;
    private List<Kelas> listKelas;
    private List<KelasIrs> listKelasIrs;
    private List<MataKuliah> listMataKuliah;

    private void initListMataKuliah() {
        listMataKuliah = List.of(new MataKuliah(), new MataKuliah(), new MataKuliah());

        listMataKuliah.get(0).setNama("Dummy");
        listMataKuliah.get(0).setId("DDDD000000");
        
        listMataKuliah.get(1).setNama("Spy");
        listMataKuliah.get(1).setId("SSSS000000");

        listMataKuliah.get(2).setNama("Mock");
        listMataKuliah.get(2).setId("MMMM000000");
    }

    private void initListKelas() {
        listKelas = List.of(new Kelas(), new Kelas(), new Kelas());

        listKelas.get(0).setNama("Kelas Dummy A");
        listKelas.get(0).setMataKuliah(listMataKuliah.get(0));
        
        listKelas.get(1).setNama("Kelas Spy A");
        listKelas.get(1).setMataKuliah(listMataKuliah.get(1));
        
        listKelas.get(2).setNama("Kelas Mock A");
        listKelas.get(2).setMataKuliah(listMataKuliah.get(2));
    }

    private void initListKelasIrs() {
        listKelasIrs = List.of(new KelasIrs(), new KelasIrs(), new KelasIrs());
        
        listKelasIrs.get(0).setIrs(irs);
        listKelasIrs.get(0).setKelas(listKelas.get(0));
        
        listKelasIrs.get(1).setIrs(irs);
        listKelasIrs.get(1).setKelas(listKelas.get(1));
        
        listKelasIrs.get(2).setIrs(irs);
        listKelasIrs.get(2).setKelas(listKelas.get(2));
    }

    @BeforeEach
    void setUp() {
        mahasiswa = new Mahasiswa();
        mahasiswa.setNpm("1234567890");
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);

        irs = new IrsMahasiswa();
        irs.setSemester(2021);
        irs.setMahasiswa(mahasiswa);

        initListMataKuliah();
        initListKelas();
        initListKelasIrs();

        token = "Bearer " + JWT.create()
                               .withSubject(mahasiswa.getUsername())
                               .withClaim("role",mahasiswa.getUserRole().name())
                               .withClaim("npm", mahasiswa.getNpm())
                               .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                               .sign(Algorithm.HMAC512(SECRET.getBytes()));

        when(mahasiswaRepository.findByNpm("1234567890")).thenReturn(mahasiswa);
    }

    @Test
    void testControllerPengecekanIrsWithNonMahasiswa() throws Exception {
        mockMvc.perform(get("/pengecekan-irs/cek-jadwal"))
               .andExpect(status().isForbidden());
    }

    @Nested
    class CekKapasitasTest {
        private List<KapasitasResult> listResult;

        void setUpResult() {
            listKelas.get(0).setKapasitasTotal(70);
            listKelas.get(1).setKapasitasTotal(32);

            listKelasIrs.get(0).setPosisi(75);
            listKelasIrs.get(1).setPosisi(32);

            listResult = new ArrayList<>();
            listResult.add(new KapasitasResult(listKelas.get(0)));
            listResult.add(new KapasitasResult(listKelas.get(1)));

            listResult.get(0).setPosisi(75);
            listResult.get(0).setKapasitasTotal(70);
            listResult.get(0).setOk(false);

            listResult.get(1).setPosisi(32);
            listResult.get(1).setKapasitasTotal(32);
            listResult.get(1).setOk(true);
        }

        @Test
        void testControllerCekKapasitasWithMahasiswaNoData() throws Exception {
            when(pengecekanIrsService.checkKapasitasOfLatestIrs(mahasiswa)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/pengecekan-irs/cek-kapasitas").header("Authorization", token))
                    .andExpect(jsonPath("$.status").value(204));
        }

        @Test
        void testControllerCekKapasitasWithMahasiswaDataExist() throws Exception {
            setUpResult();

            when(pengecekanIrsService.checkKapasitasOfLatestIrs(mahasiswa)).thenReturn(listResult);

            mockMvc.perform(get("/pengecekan-irs/cek-kapasitas").header("Authorization", token))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.result.*").isNotEmpty())
                   .andDo(print());
        }
    }

    @Nested
    class CekJadwalTest {
        private JadwalResult result;
        private List<JadwalResult> listResult;

        void setUpResult() {
            listResult = new ArrayList<>();
            
            result = new JadwalResult(listKelas.get(0));
            result.setKonflik(List.of(new KelasProxy(listKelas.get(1))));
            result.setOk(false);
            listResult.add(result);
            
            result = new JadwalResult(listKelas.get(1));
            result.setKonflik(List.of(new KelasProxy(listKelas.get(0))));
            result.setOk(false);
            listResult.add(result);

            result = new JadwalResult(listKelas.get(2));
            result.setKonflik(Collections.emptyList());
            result.setOk(true);
            listResult.add(result);
        }

        @Test
        void testControllerCekJadwalWithMahasiswaNoData() throws Exception {
            when(pengecekanIrsService.checkJadwalOfLatestIrs(mahasiswa)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/pengecekan-irs/cek-jadwal").header("Authorization", token))
                    .andExpect(jsonPath("$.status").value(204));
        }

        @Test
        void testControllerCekJadwalWithMahasiswaDataExist() throws Exception {
            setUpResult();

            when(pengecekanIrsService.checkJadwalOfLatestIrs(mahasiswa)).thenReturn(listResult);

            mockMvc.perform(get("/pengecekan-irs/cek-jadwal").header("Authorization", token))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.result.*").isNotEmpty())
                   .andDo(print());
        }
    }

    @Nested
    class CekSksTest {
        @Test
        void testControllerCekSksWithMahasiswaNoData() throws Exception {
            when(pengecekanIrsService.checkSksOfLatestIrs(mahasiswa)).thenReturn(null);

            mockMvc.perform(get("/pengecekan-irs/cek-sks").header("Authorization", token))
                   .andExpect(jsonPath("$.status").value(204));
        }

        @Test
        void testControllerCekSksWithMahasiswaIpsOutOfBound() throws Exception {
            when(pengecekanIrsService.checkSksOfLatestIrs(mahasiswa)).thenThrow(IpsOutOfBoundException.class);

            mockMvc.perform(get("/pengecekan-irs/cek-sks").header("Authorization", token))
                    .andExpect(jsonPath("$.status").value(500));
        }
        
        @Test
        void testControllerCekSksWithMahasiswaDataExist() throws Exception {
            var result = new SksResult(2023, 3.14, 15, 18, true);

            when(pengecekanIrsService.checkSksOfLatestIrs(mahasiswa)).thenReturn(result);

            mockMvc.perform(get("/pengecekan-irs/cek-sks").header("Authorization", token))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.result.*").isNotEmpty())
                   .andDo(print());
        }
    }

    @Nested
    class CekPrasyaratTest {
        private PrasyaratProxy proxy;
        private List<PrasyaratResult> listResult;

        void setUpResult() {
            listResult = new ArrayList<>();

            listResult.add(new PrasyaratResult(listMataKuliah.get(0)));
            listResult.get(0).setPrasyarat(new ArrayList<>());

            proxy = new PrasyaratProxy(listMataKuliah.get(2));
            proxy.setLulus(true);
            listResult.get(0).getPrasyarat().add(proxy);

            listResult.get(0).setOk(true);

            listResult.add(new PrasyaratResult(listMataKuliah.get(1)));
            listResult.get(1).setPrasyarat(new ArrayList<>());

            proxy = new PrasyaratProxy(listMataKuliah.get(0));
            proxy.setLulus(false);
            listResult.get(1).getPrasyarat().add(proxy);
            
            proxy = new PrasyaratProxy(listMataKuliah.get(2));
            proxy.setLulus(true);
            listResult.get(1).getPrasyarat().add(proxy);

            listResult.get(1).setOk(false);
        }

        @Test
        void testControllerCekPrasyaratWithMahasiswaNoData() throws Exception {
            when(pengecekanIrsService.checkPrasyaratOfLatestIrs(mahasiswa)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/pengecekan-irs/cek-prasyarat").header("Authorization", token))
                    .andExpect(jsonPath("$.status").value(204));
        }

        @Test
        void testControllerCekPrasyaratWithMahasiswaDataExist() throws Exception {
            setUpResult();

            when(pengecekanIrsService.checkPrasyaratOfLatestIrs(mahasiswa)).thenReturn(listResult);

            mockMvc.perform(get("/pengecekan-irs/cek-prasyarat").header("Authorization", token))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.result.*").isNotEmpty())
                   .andDo(print());
        }
    }
}
