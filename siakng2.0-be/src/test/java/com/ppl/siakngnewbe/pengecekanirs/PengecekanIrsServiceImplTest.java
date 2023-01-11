package com.ppl.siakngnewbe.pengecekanirs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.jadwal.Jadwal;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.kelasirs.KelasIrsRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.pengecekanirs.result.JadwalResult;
import com.ppl.siakngnewbe.pengecekanirs.result.KapasitasResult;
import com.ppl.siakngnewbe.pengecekanirs.result.KelasProxy;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratProxy;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratResult;
import com.ppl.siakngnewbe.pengecekanirs.result.SksResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PengecekanIrsServiceImplTest {
    @Mock
    private KelasIrsRepository kelasIrsRepository;

    @Mock
    private IrsMahasiswaRepository irsRepository;

    @InjectMocks
    private PengecekanIrsServiceImpl pengecekanIrsService;

    private IrsMahasiswa irs;
    private Mahasiswa mahasiswa;
    private List<MataKuliah> listMataKuliah;
    private List<Kelas> listKelas;
    private List<KelasIrs> listKelasIrs;

    @BeforeEach
    void setUp() {
        mahasiswa = new Mahasiswa();

        irs = new IrsMahasiswa();
        irs.setMahasiswa(mahasiswa);

        listMataKuliah = new ArrayList<>();

        listMataKuliah.add(new MataKuliah());
        listMataKuliah.get(0).setNama("Test A");
        listMataKuliah.get(0).setId("CSGE000000");

        listMataKuliah.add(new MataKuliah());
        listMataKuliah.get(1).setNama("Test B");
        listMataKuliah.get(1).setId("CSGE000001");

        listKelas = new ArrayList<>();
        listKelas.add(new Kelas());
        listKelas.add(new Kelas());

        listKelas.get(0).setKapasitasTotal(50);
        listKelas.get(0).setNama("Kelas Test A");
        listKelas.get(0).setMataKuliah(listMataKuliah.get(0));

        listKelas.get(1).setKapasitasTotal(50);
        listKelas.get(1).setNama("Kelas Test B");
        listKelas.get(1).setMataKuliah(listMataKuliah.get(1));

        listKelasIrs = new ArrayList<>();
        listKelasIrs.add(new KelasIrs());
        listKelasIrs.add(new KelasIrs());

        listKelasIrs.get(0).setIrs(irs);
        listKelasIrs.get(0).setKelas(listKelas.get(0));
        listKelasIrs.get(0).setPosisi(25);

        listKelasIrs.get(1).setIrs(irs);
        listKelasIrs.get(1).setKelas(listKelas.get(1));
        listKelasIrs.get(1).setPosisi(25);
    }

    @Nested
    class CheckKapasitasTest {
        private List<KapasitasResult> listKapasitasResult;

        @BeforeEach
        void setUp() {
            listKelasIrs.get(0).setPosisi(25);

            listKapasitasResult = new ArrayList<>();
            listKapasitasResult.add(new KapasitasResult(listKelas.get(0)));
            listKapasitasResult.add(new KapasitasResult(listKelas.get(1)));

            listKapasitasResult.get(0).setPosisi(25);
            listKapasitasResult.get(0).setKapasitasTotal(50);
            listKapasitasResult.get(0).setOk(true);

            listKapasitasResult.get(1).setPosisi(25);
            listKapasitasResult.get(1).setKapasitasTotal(50);
            listKapasitasResult.get(1).setOk(true);
        }

        @Test
        void testCheckKapasitasOk() throws Exception {
            when(irsRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(irs);
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(listKelasIrs);

            assertThat(pengecekanIrsService.checkKapasitasOfLatestIrs(mahasiswa)).usingRecursiveComparison()
                    .isEqualTo(listKapasitasResult);
        }

        @Test
        void testCheckKapasitasNotOk() throws Exception {
            listKelasIrs.get(0).setPosisi(100);

            listKapasitasResult.get(0).setPosisi(100);
            listKapasitasResult.get(0).setOk(false);

            when(irsRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(irs);
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(listKelasIrs);

            assertThat(pengecekanIrsService.checkKapasitasOfLatestIrs(mahasiswa)).usingRecursiveComparison()
                    .isEqualTo(listKapasitasResult);
        }

        @Test
        void testCheckKapasitasNoIrs() throws Exception {
            when(irsRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(null);

            List<KapasitasResult> result = pengecekanIrsService.checkKapasitasOfLatestIrs(mahasiswa);
            assertTrue(result.isEmpty());
        }

        @Test
        void testCheckKapasitasNoKelasIrs() throws Exception {
            when(irsRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(irs);
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(null);

            List<KapasitasResult> result = pengecekanIrsService.checkKapasitasOfLatestIrs(mahasiswa);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class CheckJadwalTest {
        private List<Jadwal> listJadwal;
        private List<JadwalResult> listJadwalResult;

        @BeforeEach
        void setUp() {
            listJadwal = new ArrayList<>();
            listJadwal.add(new Jadwal());
            listJadwal.add(new Jadwal());

            listJadwal.get(0).setHari("Selasa");
            listJadwal.get(0).setWaktuMulai(new Calendar.Builder().setTimeOfDay(8, 0, 0).build());
            listJadwal.get(0).setWaktuSelesai(new Calendar.Builder().setTimeOfDay(9, 40, 0).build());

            listJadwal.get(1).setHari("Selasa");
            listJadwal.get(1).setWaktuMulai(new Calendar.Builder().setTimeOfDay(10, 0, 0).build());
            listJadwal.get(1).setWaktuSelesai(new Calendar.Builder().setTimeOfDay(11, 40, 0).build());

            listKelas.get(0).setJadwalSet(Set.of(listJadwal.get(0)));

            listKelas.get(1).setJadwalSet(Set.of(listJadwal.get(1)));

            listJadwalResult = new ArrayList<>();
            listJadwalResult.add(new JadwalResult(listKelas.get(0)));
            listJadwalResult.add(new JadwalResult(listKelas.get(1)));
        }

        @Test
        void testCheckJadwalOk() throws Exception {
            listJadwalResult.get(0).setKonflik(List.of());
            listJadwalResult.get(0).setOk(true);

            listJadwalResult.get(1).setKonflik(List.of());
            listJadwalResult.get(1).setOk(true);

            when(irsRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(irs);
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(listKelasIrs);

            assertThat(pengecekanIrsService.checkJadwalOfLatestIrs(mahasiswa)).usingRecursiveComparison()
                    .isEqualTo(listJadwalResult);
        }

        @Test
        void testCheckJadwalNotOk() throws Exception {
            listJadwal.get(1).getWaktuMulai().set(Calendar.HOUR_OF_DAY, 9);

            listJadwalResult.get(0).setKonflik(List.of(new KelasProxy(listKelas.get(1))));
            listJadwalResult.get(0).setOk(false);

            listJadwalResult.get(1).setKonflik(List.of(new KelasProxy(listKelas.get(0))));
            listJadwalResult.get(1).setOk(false);

            when(irsRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(irs);
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(listKelasIrs);

            assertThat(pengecekanIrsService.checkJadwalOfLatestIrs(mahasiswa)).usingRecursiveComparison()
                    .isEqualTo(listJadwalResult);
        }

        @Test
        void testCheckJadwalNoIrs() throws Exception {
            when(irsRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(null);

            List<JadwalResult> result = pengecekanIrsService.checkJadwalOfLatestIrs(mahasiswa);
            assertTrue(result.isEmpty());
        }

        @Test
        void testCheckJadwalNoKelasIrs() throws Exception {
            when(irsRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(irs);
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(null);

            List<JadwalResult> result = pengecekanIrsService.checkJadwalOfLatestIrs(mahasiswa);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class CheckSksTest {
        private IrsMahasiswa before;

        @BeforeEach
        void setUp() {
            irs.setSemester(2019);

            before = new IrsMahasiswa();
            before.setSemester(2018);
            before.setTotalMutu(1.4);
            before.setSksa(1);
        }

        @Test
        void testCheckSksNoIrs() throws Exception {
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(null);

            assertNull(pengecekanIrsService.checkSksOfLatestIrs(mahasiswa));
        }

        @Test
        void testCheckSksEmptyListIrs() throws Exception {
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(Collections.emptyList());

            assertNull(pengecekanIrsService.checkSksOfLatestIrs(mahasiswa));
        }

        @Test
        void testCheckSksOneIrsOk() throws Exception {
            irs.setSksa(15);
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(List.of(irs));

            var result = new SksResult(2019, 4.00, 15, 24, true);
            assertEquals(result, pengecekanIrsService.checkSksOfLatestIrs(mahasiswa));
        }

        @Test
        void testCheckSksOneIrsNotOk() throws Exception {
            irs.setSksa(28);
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(List.of(irs));

            var result = new SksResult(2019, 4.00, 28, 24, false);
            assertEquals(result, pengecekanIrsService.checkSksOfLatestIrs(mahasiswa));
        }

        @Test
        void testCheckSksMoreThanOneIrsOk() throws Exception {
            irs.setSksa(10);
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(List.of(irs, before));

            var result = new SksResult(2019, 1.4, 10, 12, true);
            assertEquals(result, pengecekanIrsService.checkSksOfLatestIrs(mahasiswa));
        }

        @Test
        void testCheckSksMoreThanOneIrsNotOk() throws Exception {
            irs.setSksa(15);
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(List.of(irs, before));

            var result = new SksResult(2019, 1.4, 15, 12, false);
            assertEquals(result, pengecekanIrsService.checkSksOfLatestIrs(mahasiswa));
        }
    }

    @Nested
    class CheckPrasyaratTest {
        private PrasyaratProxy proxy;
        private List<IrsMahasiswa> listIrs;
        private List<PrasyaratResult> results;

        @BeforeEach
        void setUp() {
            listIrs = new ArrayList<>();

            listIrs.add(irs);
            listIrs.get(0).setSemester(2021);

            listIrs.add(new IrsMahasiswa());
            listIrs.get(1).setMahasiswa(mahasiswa);
            listIrs.get(1).setSemester(2020);

            listKelasIrs.get(0).setIrs(listIrs.get(0));
            listKelasIrs.get(0).setKelas(listKelas.get(0));

            listKelasIrs.get(1).setIrs(listIrs.get(1));
            listKelasIrs.get(1).setKelas(listKelas.get(1));

            listMataKuliah.get(0).setPrasyaratMataKuliahSet(Set.of(listMataKuliah.get(1)));
        }

        @Test
        void testCheckPrasyaratNoIrs() throws Exception {
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(null);

            var result = pengecekanIrsService.checkPrasyaratOfLatestIrs(mahasiswa);
            assertTrue(result.isEmpty());
        }

        @Test
        void testCheckPrasyaratEmptyListIrs() throws Exception {
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(Collections.emptyList());

            var result = pengecekanIrsService.checkPrasyaratOfLatestIrs(mahasiswa);
            assertTrue(result.isEmpty());
        }

        @Test
        void testCheckPrasyaratNoKelasIrs() throws Exception {
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(List.of(irs));
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(null);

            var result = pengecekanIrsService.checkPrasyaratOfLatestIrs(mahasiswa);
            assertTrue(result.isEmpty());
        }

        @Test
        void testCheckPrasyaratEmptyListKelasIrs() throws Exception {
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(List.of(irs));
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(Collections.emptyList());

            var result = pengecekanIrsService.checkPrasyaratOfLatestIrs(mahasiswa);
            assertTrue(result.isEmpty());
        }

        @Test
        void testCheckPrasyaratOk() throws Exception {
            listKelasIrs.get(1).setNilaiAkhir(74.9);

            results = List.of(new PrasyaratResult(listMataKuliah.get(0)));
            results.get(0).setPrasyarat(new ArrayList<>());

            proxy = new PrasyaratProxy(listMataKuliah.get(1));
            proxy.setLulus(true);

            results.get(0).getPrasyarat().add(proxy);
            results.get(0).setOk(true);
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(listIrs);
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(listKelasIrs.subList(0, 1));
            when(kelasIrsRepository.findByIrsIn(listIrs.subList(1, 2))).thenReturn(listKelasIrs.subList(1, 2));

            assertThat(pengecekanIrsService.checkPrasyaratOfLatestIrs(mahasiswa)).usingRecursiveComparison().isEqualTo(results);
        }

        @Test
        void testCheckPrasyaratNotOk() throws Exception {
            listKelasIrs.get(1).setNilaiAkhir(47.9);

            results = List.of(new PrasyaratResult(listMataKuliah.get(0)));
            results.get(0).setPrasyarat(new ArrayList<>());

            proxy = new PrasyaratProxy(listMataKuliah.get(1));
            proxy.setLulus(false);

            results.get(0).getPrasyarat().add(proxy);
            results.get(0).setOk(false);
            when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(listIrs);
            when(kelasIrsRepository.findByIrs(irs)).thenReturn(listKelasIrs.subList(0, 1));
            when(kelasIrsRepository.findByIrsIn(listIrs.subList(1, 2))).thenReturn(listKelasIrs.subList(1, 2));

            assertThat(pengecekanIrsService.checkPrasyaratOfLatestIrs(mahasiswa)).usingRecursiveComparison().isEqualTo(results);
        }
    }
}
