package com.ppl.siakngnewbe.riwayatakademik;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.penilaian.KomponenPenilaian;
import com.ppl.siakngnewbe.penilaian.Nilai;
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
@WebMvcTest(controllers = RiwayatAkademikController.class)
class RiwayatAkademikControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RiwayatAkademikServiceImpl riwayatAkademikService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private static Mahasiswa mahasiswa;
    private static List<IrsMahasiswa> irsMahasiswaList;
    private static Set<KelasIrs> kelasIrsSet = new HashSet<>();
    private static Set<Kelas> kelasSet = new HashSet<>();
    private static List<Nilai> nilaiList = new ArrayList<>();
    private static KelasIrs kelasIrs;
    private static Map<Integer, Map<String, Object>> mappingRiwayatIrs;
    private static Map<String, Map<String, Double>> mappingDetailNilai;
    private static String token;

    @BeforeAll
    public static void setUp() {
        mahasiswa = new Mahasiswa();
        mahasiswa.setId(1L);
        mahasiswa.setUsername("eren.yeager");
        mahasiswa.setPassword("dummyPassword");
        mahasiswa.setNamaLengkap("Eren Yeager");
        mahasiswa.setNpm("1906272788");
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);
        mahasiswa.setStatus(StatusAkademik.AKTIF);
        mahasiswa.setIpk(4.00f);

        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setId("1");
        mataKuliah.setNama("Dasar-Dasar Pemrograman 1");

        Kelas kelas = new Kelas();
        kelas.setId(Integer.toString(1));
        kelas.setNama("DDP1 - A");
        kelas.setSks(6);
        kelas.setMataKuliah(mataKuliah);
        kelasSet.add(kelas);

        kelasIrs = new KelasIrs();
        kelasIrs.setId(1);
        kelasIrs.setNilaiAkhir(88.00);
        kelasIrs.setNilaiHuruf("A");
        kelasIrs.setPosisi(1);
        kelasIrs.setKelas(kelas);
        kelasIrsSet.add(kelasIrs);

        for(int k = 0; k < 2; k++) {
            Nilai nilai = new Nilai();
            KomponenPenilaian komponenPenilaian = new KomponenPenilaian();

            komponenPenilaian.setNama("TK" + (k+1));
            komponenPenilaian.setBobot(50.0);

            nilai.setAngka(86.00 + 4*k);
            nilai.setKelasIrs(kelasIrs);
            nilai.setKomponenPenilaian(komponenPenilaian);
            nilaiList.add(nilai);
        }

        kelasIrs.setNilaiList(nilaiList);

        irsMahasiswaList = new ArrayList<>();
        irsMahasiswaList.add(new IrsMahasiswa());
        irsMahasiswaList.get(0).setMahasiswa(mahasiswa);
        irsMahasiswaList.get(0).setSemester(1);
        irsMahasiswaList.get(0).setSksa(6);
        irsMahasiswaList.get(0).setTahunAjaran(new TahunAjaran());
        irsMahasiswaList.get(0).getTahunAjaran().setNama("2021/2022");
        irsMahasiswaList.get(0).getTahunAjaran().setTerm(1);
        irsMahasiswaList.get(0).setKelasIrsSet(kelasIrsSet);

        token =  JWT.create()
                .withSubject(mahasiswa.getUsername())
                .withClaim("role", mahasiswa.getUserRole().name())
                .withClaim("npm", mahasiswa.getNpm())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));

        mappingRiwayatIrs = mappingRiwayatIrs();
        mappingDetailNilai = mappingDetailNilai();
    }

    private static Map<Integer, Map<String, Object>> mappingRiwayatIrs() {
        Map<Integer, Map<String, Object>> mappingRiwayatIrs = new HashMap<>();
        Map<String, Object> detailIrs;

        for (IrsMahasiswa irs : irsMahasiswaList) {
            detailIrs = new HashMap<>();
            int semester = irs.getSemester();

            detailIrs.put("Tahun Ajaran", irs.getTahunAjaran().getNama());
            detailIrs.put("Term", irs.getTahunAjaran().getTerm());
            detailIrs.put("SKSA", irs.getSksa());

            List<Map<String, Object>> listMataKuliah = new ArrayList<>();
            Map<String, Object> mappingMataKuliah;

            for (KelasIrs kelasIrs : irs.getKelasIrsSet()) {
                mappingMataKuliah = new HashMap<>();

                mappingMataKuliah.put("Nama Matkul", kelasIrs.getKelas().getMataKuliah().getNama());
                mappingMataKuliah.put("ID Matkul", kelasIrs.getKelas().getMataKuliah().getId());
                mappingMataKuliah.put("SKS", kelasIrs.getKelas().getSks());
                mappingMataKuliah.put("Nilai Akhir", kelasIrs.getNilaiAkhir());
                mappingMataKuliah.put("Nilai Huruf", kelasIrs.getNilaiHuruf());
                mappingMataKuliah.put("ID Kelas IRS", kelasIrs.getId());

                listMataKuliah.add(mappingMataKuliah);
            }

            detailIrs.put("Mata Kuliah", listMataKuliah);

            mappingRiwayatIrs.put(semester, detailIrs);
        }

        return mappingRiwayatIrs;
    }

    private static Map<String, Map<String, Double>> mappingDetailNilai() {
        long id = 1;
        List<Nilai> listNilai = kelasIrs.getNilaiList();
        Map<String, Map<String, Double>> mappingDetailNilai = new HashMap<>();
        Map<String, Double> mappingKomponenNilai;

        for (Nilai nilai : listNilai) {
            mappingKomponenNilai = new HashMap<>();
            KomponenPenilaian komponenPenilaian = nilai.getKomponenPenilaian();

            mappingKomponenNilai.put("Bobot", komponenPenilaian.getBobot());
            mappingKomponenNilai.put("Nilai", nilai.getAngka());

            mappingDetailNilai.put(komponenPenilaian.getNama(), mappingKomponenNilai);
        }

        return mappingDetailNilai;
    }

    @Test
    void testGetMappingRiwayatIrs() throws Exception {
        when(riwayatAkademikService.getMappingRiwayatIrs(token)).thenReturn(mappingRiwayatIrs);
        mockMvc.perform(get("/riwayat-akademik/irs").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void testGetMappingRiwayatIrsNull() throws Exception {
        when(riwayatAkademikService.getMappingRiwayatIrs(token)).thenReturn(null);
        mockMvc.perform(get("/riwayat-akademik/irs").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void testGetMappingDetailNilai() throws Exception {
        when(riwayatAkademikService.getMappingDetailNilai(2)).thenReturn(mappingDetailNilai);
        mockMvc.perform(get("/riwayat-akademik/nilai/2")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );

        when(riwayatAkademikService.getMappingDetailNilai(2)).thenReturn(null);
        mockMvc.perform(get("/riwayat-akademik/nilai/2")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );
    }
}
