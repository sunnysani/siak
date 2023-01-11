package com.ppl.siakngnewbe.riwayatakademik;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.kelasirs.KelasIrsRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaRepository;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.penilaian.KomponenPenilaian;
import com.ppl.siakngnewbe.penilaian.Nilai;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RiwayatAkademikServiceTest {

    @Mock private KelasIrsRepository kelasIrsRepository;

    @Mock private IrsMahasiswaRepository irsMahasiswaRepository;

    @Mock private MahasiswaRepository mahasiswaRepository;

    @InjectMocks private RiwayatAkademikServiceImpl riwayatAkademikService;

    private static Mahasiswa mahasiswa;
    private static List<IrsMahasiswa> irsMahasiswaList;
    private static Set<KelasIrs> kelasIrsSet = new HashSet<>();
    private static Set<Kelas> kelasSet = new HashSet<>();
    private static List<Nilai> nilaiList = new ArrayList<>();
    private static KelasIrs kelasIrs;
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

        Nilai nilai = new Nilai();
        KomponenPenilaian komponenPenilaian = new KomponenPenilaian();

        komponenPenilaian.setNama("TK1");
        komponenPenilaian.setBobot(100.0);

        nilai.setAngka(88.00);
        nilai.setKelasIrs(kelasIrs);
        nilai.setKomponenPenilaian(komponenPenilaian);
        nilaiList.add(nilai);

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
    }

    @Test
    void testGetMappingRiwayatIrs() throws Exception {
        lenient().when(mahasiswaRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        when(irsMahasiswaRepository.findByMahasiswaOrderBySemester(mahasiswa)).thenReturn(irsMahasiswaList);

        Map<Integer, Map<String, Object>> mapRiwayatIrs = riwayatAkademikService.getMappingRiwayatIrs(token);
        List<Integer> keyList = new ArrayList<>(mapRiwayatIrs.keySet());
        IrsMahasiswa irs;

        for (int i = 1; i <= mapRiwayatIrs.size(); i++) {
            irs = irsMahasiswaList.get(i-1);

            assertEquals(irs.getSemester(), keyList.get(i-1));
            assertEquals(irs.getTahunAjaran().getNama(), mapRiwayatIrs.get(i).get("Tahun Ajaran"));
            assertEquals(irs.getTahunAjaran().getTerm(), mapRiwayatIrs.get(i).get("Term"));
            assertEquals(irs.getSksa(), mapRiwayatIrs.get(i).get("SKSA"));

            System.out.println(mapRiwayatIrs.get(i).get("Mata Kuliah"));

        }
    }


    @Test
    void testGetMappingDetailNilai() throws Exception {
        long id = kelasIrs.getId();
        when(kelasIrsRepository.findById(id)).thenReturn(kelasIrs);

        Map<String, Map<String, Double>> mappingDetailNilai = riwayatAkademikService.getMappingDetailNilai(id);
        List<String> keyList = new ArrayList<>(mappingDetailNilai.keySet());

        for (int i = 0; i < mappingDetailNilai.size(); i++) {
            Nilai nilai = kelasIrs.getNilaiList().get(i);
            KomponenPenilaian komponenPenilaian = nilai.getKomponenPenilaian();

            assertEquals(komponenPenilaian.getNama(), keyList.get(i));
            assertEquals(komponenPenilaian.getBobot(), mappingDetailNilai.get(keyList.get(i)).get("Bobot"));
            assertEquals(nilai.getAngka(), mappingDetailNilai.get(keyList.get(i)).get("Nilai"));

        }
    }
}
