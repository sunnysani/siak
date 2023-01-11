package com.ppl.siakngnewbe.ringkasanakademik;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaRepository;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.pendidikan.Fakultas;
import com.ppl.siakngnewbe.pendidikan.ProgramPendidikan;
import com.ppl.siakngnewbe.pendidikan.ProgramStudi;
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

@ExtendWith(MockitoExtension.class)
class RingkasanAkademikServiceTest {
    @Mock
    private IrsMahasiswaRepository irsRepository;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @InjectMocks
    private RingkasanAkademikServiceImpl ringkasanAkademikService;

    private static Dosen dosen;
    private static Mahasiswa mahasiswa;
    private static List<IrsMahasiswa> listIrs;
    private static Set<KelasIrs> setKelasIrs1;
    private static Set<KelasIrs> setKelasIrs2;
    private static Fakultas fakultas;
    private static ProgramStudi programStudi;
    private static String token;

    @BeforeAll
    public static void setUp() {
        dosen = new Dosen();
        mahasiswa = new Mahasiswa();
        listIrs = new ArrayList<>();
        setKelasIrs1 = new HashSet<>();
        setKelasIrs2 = new HashSet<>();
        fakultas = new Fakultas();
        programStudi = new ProgramStudi();

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
        mahasiswa.setProgramStudi(programStudi);

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

        var kelasIrs5 = new KelasIrs();
        setKelasIrs2.add(kelasIrs5);

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
            listIrs.get(i).getTahunAjaran().setNama("2021/2022");
            listIrs.get(i).getTahunAjaran().setTerm(i+1);
            listIrs.get(i).setNilaiDefined(true);
        }

        listIrs.get(0).setKelasIrsSet(setKelasIrs1);
        listIrs.get(1).setKelasIrsSet(setKelasIrs2);

        fakultas.setId(1L);
        fakultas.setNama("Fakultas Ilmu Komputer");

        programStudi.setId(1L);
        programStudi.setNama("Ilmu Komputer");
        programStudi.setProgramPendidikan(ProgramPendidikan.S1_REGULER);
        programStudi.setFakultas(fakultas);

        token =  JWT.create()
                .withSubject(mahasiswa.getUsername())
                .withClaim("role", mahasiswa.getUserRole().name())
                .withClaim("npm", mahasiswa.getNpm())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testGetMappingStatistikIrs() throws Exception {
        lenient().when(mahasiswaRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        lenient().when(irsRepository.findByMahasiswaOrderBySemester(mahasiswa)).thenReturn(listIrs);

        Map<Integer, Map<String, Object>> mappingStatistikIrs = ringkasanAkademikService.getMappingStatistikIrs(token);
        List<Integer> keyList = new ArrayList<>(mappingStatistikIrs.keySet());
        IrsMahasiswa irsSemester;

        for (int i = 1; i <= mappingStatistikIrs.size(); i++) {
            irsSemester = listIrs.get(i-1);

            assertEquals(irsSemester.getSemester(), keyList.get(i - 1));
            assertEquals(irsSemester.getTahunAjaran().getNama(), mappingStatistikIrs.get(i).get("Periode Tahun"));
            assertEquals(irsSemester.getTahunAjaran().getTerm(), mappingStatistikIrs.get(i).get("Term"));
            assertEquals(irsSemester.isNilaiDefined(), mappingStatistikIrs.get(i).get("Nilai Defined"));
            assertEquals(irsSemester.getKelasIrsSet().size(), mappingStatistikIrs.get(i).get("MK"));
            assertEquals(irsSemester.getSksa(), mappingStatistikIrs.get(i).get("SKSA Semester"));
            assertEquals(irsSemester.getSksl(), mappingStatistikIrs.get(i).get("SKSL Semester"));
            assertEquals(irsSemester.getTotalMutu(), mappingStatistikIrs.get(i).get("Total Mutu"));
            assertEquals(irsSemester.getIps(), mappingStatistikIrs.get(i).get("IP"));
        }
    }

    @Test
    void testGetMappingDetailMahasiswa() throws Exception {
        var irsTerbaru = listIrs.get(listIrs.size()-1);
        lenient().when(mahasiswaRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        lenient().when(irsRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(irsTerbaru);

        Map<String, Object> mappingDetailMahasiswa = ringkasanAkademikService.getMappingDetailMahasiswa(token);
        System.out.println(mappingDetailMahasiswa);

        assertEquals(mahasiswa.getNamaLengkap(), mappingDetailMahasiswa.get("Nama"));
        assertEquals(mahasiswa.getProgramStudi().toString(), mappingDetailMahasiswa.get("Program Studi"));
        assertEquals(mahasiswa.getNpm(), mappingDetailMahasiswa.get("NPM"));
        assertEquals(irsTerbaru.getIps(), mappingDetailMahasiswa.get("IPS"));
        assertEquals(irsTerbaru.getTahunAjaran().getNama(), mappingDetailMahasiswa.get("Tahun Ajaran"));
        assertEquals(irsTerbaru.getSemester(), mappingDetailMahasiswa.get("Semester"));
        assertEquals(mahasiswa.getStatus(), mappingDetailMahasiswa.get("Status Akademik"));
        assertEquals(mahasiswa.getPembimbingAkademik().getNamaLengkap(), mappingDetailMahasiswa.get("Pembimbing Akademik"));
        assertEquals(dosen.getNamaLengkap(), mappingDetailMahasiswa.get("Pembimbing Akademik"));
        assertEquals(mahasiswa.getPembimbingAkademik().getNip(), mappingDetailMahasiswa.get("NIP Pembimbing Akademik"));
        assertEquals(dosen.getNip(), mappingDetailMahasiswa.get("NIP Pembimbing Akademik"));
    }

    @Test
    void testGetMappingNilai() throws Exception {
        lenient().when(mahasiswaRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        lenient().when(irsRepository.findByMahasiswaOrderBySemester(mahasiswa)).thenReturn(listIrs);

        Map<String, Integer> mappingNilai = ringkasanAkademikService.getMappingNilai(token);
        System.out.println(mappingNilai);

        for (IrsMahasiswa irs : listIrs) {
            Map<String, Integer> tempMapNilai = new HashMap<>();

            for (KelasIrs kelasIrs : irs.getKelasIrsSet()) {
                var nilaiHuruf = kelasIrs.getNilaiHuruf();
                nilaiHuruf = nilaiHuruf == null ? "U" : nilaiHuruf;

                if (!mappingNilai.containsKey(nilaiHuruf)) {
                    tempMapNilai.put(nilaiHuruf, 1);
                } else {
                    int newcount = mappingNilai.get(nilaiHuruf) + 1;
                    tempMapNilai.replace(nilaiHuruf, newcount);
                }
            }
            List<String> listNilai = new ArrayList<>(tempMapNilai.keySet());

            for (String nilai : listNilai) {
                assertEquals(tempMapNilai.get(nilai), mappingNilai.get(nilai));
            }
        }
    }

    @Test
    void testGetMappingIp() throws Exception {
        lenient().when(mahasiswaRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        lenient().when(irsRepository.findByMahasiswaOrderBySemester(mahasiswa)).thenReturn(listIrs);

        Map<String, List<Double>> mappingIp = ringkasanAkademikService.getMappingIP(token);
        System.out.println(mappingIp);

        for (int i = 0; i < listIrs.size(); i++) {
            assertEquals(listIrs.get(i).getIps(), mappingIp.get("IP").get(i));
        }
    }

    @Test
    void testGetIpk() throws Exception {
        lenient().when(mahasiswaRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        lenient().when(irsRepository.findByMahasiswaOrderBySemester(mahasiswa)).thenReturn(listIrs);

        assertEquals(ringkasanAkademikService.getIpk(mahasiswa), 4);
    }

    @Test
    void testGetTotalMutu() throws Exception {
        lenient().when(mahasiswaRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        lenient().when(irsRepository.findByMahasiswaOrderBySemester(mahasiswa)).thenReturn(listIrs);

        assertEquals(ringkasanAkademikService.getTotalMutu(mahasiswa), 192);
    }

    @Test
    void testGetSksL() throws Exception {
        lenient().when(mahasiswaRepository.findByNpm(mahasiswa.getNpm())).thenReturn(mahasiswa);
        lenient().when(irsRepository.findByMahasiswaOrderBySemester(mahasiswa)).thenReturn(listIrs);

        assertEquals(ringkasanAkademikService.getSksL(mahasiswa), 48);
    }
}
