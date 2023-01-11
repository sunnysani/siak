package com.ppl.siakngnewbe.ringkasanakademik;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = RingkasanAkademikController.class)
class RingkasanAkademikControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RingkasanAkademikServiceImpl ringkasanAkademikService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private static Dosen dosen;
    private static Mahasiswa mahasiswa;
    private static List<IrsMahasiswa> listIrs;
    private static Set<KelasIrs> setKelasIrs1;
    private static Set<KelasIrs> setKelasIrs2;
    private static String token;
    private static Map<Integer, Map<String, Object>> mappingStatistikIrs;
    private static Map<String, Object> mappingDetailMahasiswa;
    private static Map<String, Integer> mappingNilai;
    private static Map<String, List<Double>> mappingIp;

    @BeforeAll
    public static void setUp() {
        dosen = new Dosen();
        mahasiswa = new Mahasiswa();
        listIrs = new ArrayList<>();
        setKelasIrs1 = new HashSet<>();
        setKelasIrs2 = new HashSet<>();

        dosen.setNamaLengkap("Grisha Yeager");
        dosen.setNip("19610123123112312001");

        mahasiswa.setId(1L);
        mahasiswa.setUsername("eren.yeager");
        mahasiswa.setPassword("dummyPassword");
        mahasiswa.setNamaLengkap("Eren Yeager");
        mahasiswa.setNpm("1906272788");
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);
        mahasiswa.setStatus(StatusAkademik.AKTIF);
        mahasiswa.setIpk(4.00f);
        mahasiswa.setPembimbingAkademik(dosen);

        for(int j = 0; j < 3; j++) {
            var kelasIrs1 = new KelasIrs();
            kelasIrs1.setNilaiHuruf("A");
            setKelasIrs1.add(kelasIrs1);

            var kelasIrs2 = new KelasIrs();
            kelasIrs2.setNilaiHuruf("B");
            setKelasIrs1.add(kelasIrs2);

            var kelasIrs3 = new KelasIrs();
            kelasIrs3.setNilaiHuruf("A-");
            setKelasIrs2.add(kelasIrs3);

            var kelasIrs4 = new KelasIrs();
            kelasIrs4.setNilaiHuruf("B+");
            setKelasIrs2.add(kelasIrs4);
        }

        for(int i = 0; i < 2; i++) {
            listIrs.add(new IrsMahasiswa());
        }

        for(int i = 0; i < 2; i++) {
            listIrs.get(i).setMahasiswa(mahasiswa);
            listIrs.get(i).setSemester(1+i);
            listIrs.get(i).setTotalMutu(96.00);
            listIrs.get(i).setSksa(24);
            listIrs.get(i).setSksl(24);
            listIrs.get(i).setTahunAjaran(new TahunAjaran());
            listIrs.get(i).getTahunAjaran().setNama("2021/2022 - " + (i+1));
        }

        listIrs.get(0).setKelasIrsSet(setKelasIrs1);
        listIrs.get(1).setKelasIrsSet(setKelasIrs2);

        token =  JWT.create()
                .withSubject(mahasiswa.getUsername())
                .withClaim("role",mahasiswa.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));

        mappingStatistikIrs = mappingStatistikIrs();
        mappingDetailMahasiswa = mappingDetailMahasiswa();
        mappingNilai = mappingNilai();
        mappingIp = mappingIp();
    }

    private static Map<Integer, Map<String, Object>> mappingStatistikIrs() {
        Map<Integer, Map<String, Object>> mappingStatistikIrs = new HashMap<>();
        Map<String, Object> detailIrs = new HashMap<>();

        for (IrsMahasiswa irs : listIrs) {
            int semester = irs.getSemester();

            detailIrs.put("Periode Tahun", irs.getTahunAjaran().getNama());
            detailIrs.put("MK", irs.getKelasIrsSet().size());
            detailIrs.put("SKSA Semester", irs.getSksa());
            detailIrs.put("SKSL Semester", irs.getSksl());
            detailIrs.put("Total Mutu", irs.getTotalMutu());
            detailIrs.put("IP", irs.getIps());

            mappingStatistikIrs.put(semester, detailIrs);
        }

        return mappingStatistikIrs;
    }

    private static Map<String, Object> mappingDetailMahasiswa() {
        Map<String, Object> mappingDetailMahasiswa = new HashMap<>();
        IrsMahasiswa irsTerbaru = listIrs.get(listIrs.size()-1);

        mappingDetailMahasiswa.put("Nama", mahasiswa.getNamaLengkap());
        mappingDetailMahasiswa.put("NPM", mahasiswa.getNpm());
        mappingDetailMahasiswa.put("IPS", irsTerbaru.getIps());
        mappingDetailMahasiswa.put("IPK", mahasiswa.getIpk());
        mappingDetailMahasiswa.put("SKS Lulus", irsTerbaru.getSksl());
        mappingDetailMahasiswa.put("Total Mutu", irsTerbaru.getTotalMutu());
        mappingDetailMahasiswa.put("Tahun Ajaran", irsTerbaru.getTahunAjaran().getNama());
        mappingDetailMahasiswa.put("Semester", irsTerbaru.getSemester());
        mappingDetailMahasiswa.put("Status Akademik", mahasiswa.getStatus());
        mappingDetailMahasiswa.put("Pembimbing Akademik", mahasiswa.getPembimbingAkademik().getNamaLengkap());
        mappingDetailMahasiswa.put("NIP Pembimbing Akademik", mahasiswa.getPembimbingAkademik().getNip());

        return mappingDetailMahasiswa;
    }

    private static Map<String, Integer> mappingNilai() {
        Map<String, Integer> mappingNilai = new HashMap<>();

        for (IrsMahasiswa irs : listIrs) {
            Set<KelasIrs> setKelas = irs.getKelasIrsSet();

            for (KelasIrs kelasIrs : setKelas) {
                var nilaiHuruf = kelasIrs.getNilaiHuruf();

                if (!mappingNilai.containsKey(nilaiHuruf)) {
                    mappingNilai.put(nilaiHuruf, 1);
                } else {
                    int newcount = mappingNilai.get(nilaiHuruf) + 1;
                    mappingNilai.replace(nilaiHuruf, newcount);
                }
            }
        }
        return mappingNilai;
    }

    private static Map<String, List<Double>> mappingIp() {
        Map<String, List<Double>> mappingIP = new HashMap<>();
        List<Double> ipsList = new ArrayList<>();
        List<Double> ipkList = new ArrayList<>();

        for (IrsMahasiswa irs : listIrs) {
            ipsList.add(irs.getIps());
        }
        mappingIP.put("IP", ipsList);
        mappingIP.put("IPK", ipkList);

        return mappingIP;
    }

    @Test
    void testGetMappingStatistikIrs() throws Exception {
        when(ringkasanAkademikService.getMappingStatistikIrs(token)).thenReturn(mappingStatistikIrs);
        mockMvc.perform(get("/ringkasan-akademik/irs").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );

        when(ringkasanAkademikService.getMappingStatistikIrs(token)).thenReturn(null);
        mockMvc.perform(get("/ringkasan-akademik/irs").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void testGetMappingDetailMahasiswa() throws Exception {
        when(ringkasanAkademikService.getMappingDetailMahasiswa(token)).thenReturn(mappingDetailMahasiswa);
        mockMvc.perform(get("/ringkasan-akademik/mahasiswa").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );

        when(ringkasanAkademikService.getMappingDetailMahasiswa(token)).thenReturn(null);
        mockMvc.perform(get("/ringkasan-akademik/mahasiswa").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void testGetMappingNilai() throws Exception {
        when(ringkasanAkademikService.getMappingNilai(token)).thenReturn(mappingNilai);
        mockMvc.perform(get("/ringkasan-akademik/nilai").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpect(status().isOk());

        when(ringkasanAkademikService.getMappingNilai(token)).thenReturn(null);
        mockMvc.perform(get("/ringkasan-akademik/nilai").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMappingIp() throws Exception {
        when(ringkasanAkademikService.getMappingIP(token)).thenReturn(mappingIp);
        mockMvc.perform(get("/ringkasan-akademik/ip").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );

        when(ringkasanAkademikService.getMappingIP(token)).thenReturn(null);
        mockMvc.perform(get("/ringkasan-akademik/ip").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );
    }
}
