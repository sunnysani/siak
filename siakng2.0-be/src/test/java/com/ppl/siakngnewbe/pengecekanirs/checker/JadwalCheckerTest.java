package com.ppl.siakngnewbe.pengecekanirs.checker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.ppl.siakngnewbe.jadwal.Jadwal;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.pengecekanirs.result.KelasProxy;
import com.ppl.siakngnewbe.pengecekanirs.result.JadwalResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

class JadwalCheckerTest {
    private JadwalChecker checker;

    private List<Jadwal> listJadwal;

    private void initListJadwal() {
        listJadwal = new ArrayList<>();
        listJadwal.add(new Jadwal());
        listJadwal.add(new Jadwal());
        listJadwal.add(new Jadwal());
        listJadwal.add(new Jadwal());
        listJadwal.add(new Jadwal());
        listJadwal.add(new Jadwal());
        listJadwal.add(new Jadwal());

        listJadwal.get(0).setHari("Selasa");
        listJadwal.get(0).setWaktuMulai(new Calendar.Builder().setTimeOfDay(8, 0, 0).build());
        listJadwal.get(0).setWaktuSelesai(new Calendar.Builder().setTimeOfDay(9, 40, 0).build());
        
        listJadwal.get(1).setHari("Selasa");
        listJadwal.get(1).setWaktuMulai(new Calendar.Builder().setTimeOfDay(9, 0, 0).build());
        listJadwal.get(1).setWaktuSelesai(new Calendar.Builder().setTimeOfDay(11, 30, 0).build());
        
        listJadwal.get(2).setHari("Jumat");
        listJadwal.get(2).setWaktuMulai(new Calendar.Builder().setTimeOfDay(13, 0, 0).build());
        listJadwal.get(2).setWaktuSelesai(new Calendar.Builder().setTimeOfDay(13, 50, 0).build());

        listJadwal.get(3).setHari("Senin");
        listJadwal.get(3).setWaktuMulai(new Calendar.Builder().setTimeOfDay(16, 0, 0).build());
        listJadwal.get(3).setWaktuSelesai(new Calendar.Builder().setTimeOfDay(18, 30, 0).build());

        listJadwal.get(4).setHari("Kamis");
        listJadwal.get(4).setWaktuMulai(new Calendar.Builder().setTimeOfDay(14, 0, 0).build());
        listJadwal.get(4).setWaktuSelesai(new Calendar.Builder().setTimeOfDay(14, 50, 0).build());

        listJadwal.get(5).setHari("Rabu");
        listJadwal.get(5).setWaktuMulai(new Calendar.Builder().setTimeOfDay(10, 0, 0).build());
        listJadwal.get(5).setWaktuSelesai(new Calendar.Builder().setTimeOfDay(11, 40, 0).build());

        listJadwal.get(6).setHari("Jumat");
        listJadwal.get(6).setWaktuMulai(new Calendar.Builder().setTimeOfDay(13, 0, 0).build());
        listJadwal.get(6).setWaktuSelesai(new Calendar.Builder().setTimeOfDay(14, 40, 0).build());
    }

    @BeforeEach
    void setUp() {
        checker = new JadwalChecker();

        initListJadwal();
    }
    
    @Test
    void testToMinuteOfWeek() throws Exception {
        Calendar waktu = new Calendar.Builder().setTimeOfDay(9, 40, 0).build();
        assertEquals(1 * 24 * 60 + 9 * 60 + 40, checker.toMinuteOfWeek("Selasa", waktu));
    }

    @Test
    void testToMinuteOfWeekJadwal() throws Exception {
        Pair<Integer, Integer> expectedMinute = Pair.of(1 * 24 * 60 + 8 * 60, 1 * 24 * 60 + 9 * 60 + 40);
        assertEquals(expectedMinute, checker.toMinuteOfWeek(listJadwal.get(0)));
    }

    @Test
    void testIsConflicted() throws Exception {
        assertTrue(checker.isConflicted(listJadwal.get(0), listJadwal.get(1)));
    }

    @Test
    void testIsNotConflicted() throws Exception {
        assertFalse(checker.isConflicted(listJadwal.get(0), listJadwal.get(2)));
    }
    
    @Test
    void testIsConflictedList() throws Exception {
        assertTrue(checker.isConflicted(listJadwal.subList(0, 1), listJadwal.subList(1, 3)));
    }

    @Test
    void testIsNotConflictedList() throws Exception {
        assertFalse(checker.isConflicted(listJadwal.subList(0, 1), listJadwal.subList(2, 3)));
    }

    @Nested
    class CheckTest {
        private List<MataKuliah> listMataKuliah;
        private List<Kelas> listKelas;
        private List<KelasIrs> listKelasIrs;
        private List<KelasProxy> listKelasProxy;

        private void initListMataKuliah() {
            listMataKuliah = new ArrayList<>();
            listMataKuliah.add(new MataKuliah());
            listMataKuliah.add(new MataKuliah());
            listMataKuliah.add(new MataKuliah());
    
            listMataKuliah.get(0).setNama("Dummy");
            listMataKuliah.get(0).setId("CSGE000000");
            
            listMataKuliah.get(1).setNama("Stub");
            listMataKuliah.get(1).setId("CSCM600000");
    
            listMataKuliah.get(2).setNama("Test Double");
            listMataKuliah.get(2).setId("CSCE604244");
        }
    
        private void initListKelas() {
            listKelas = new ArrayList<>();
            listKelas.add(new Kelas());
            listKelas.add(new Kelas());
            listKelas.add(new Kelas());
            listKelas.add(new Kelas());
    
            listKelas.get(0).setNama("Kelas Dummy A");
            listKelas.get(0).setMataKuliah(listMataKuliah.get(0));
            listKelas.get(0).setJadwalSet(Set.of(listJadwal.get(0)));
            
            listKelas.get(1).setNama("Kelas Stub B");
            listKelas.get(1).setMataKuliah(listMataKuliah.get(1));
            listKelas.get(1).setJadwalSet(Set.of(listJadwal.get(1), listJadwal.get(2)));
            
            listKelas.get(2).setNama("Kelas Test Double A");
            listKelas.get(2).setMataKuliah(listMataKuliah.get(2));
            listKelas.get(2).setJadwalSet(Set.of(listJadwal.get(3), listJadwal.get(4)));
            
            listKelas.get(3).setNama("Kelas Test Double D");
            listKelas.get(3).setMataKuliah(listMataKuliah.get(2));
            listKelas.get(3).setJadwalSet(Set.of(listJadwal.get(5), listJadwal.get(6)));

            listKelasProxy = new ArrayList<>();
            listKelasProxy.add(new KelasProxy(listKelas.get(0)));
            listKelasProxy.add(new KelasProxy(listKelas.get(1)));
            listKelasProxy.add(new KelasProxy(listKelas.get(2)));
            listKelasProxy.add(new KelasProxy(listKelas.get(3)));
        }
    
        private void initListKelasIrs() {
            listKelasIrs = new ArrayList<>();
            listKelasIrs.add(new KelasIrs());
            listKelasIrs.add(new KelasIrs());
            listKelasIrs.add(new KelasIrs());
            listKelasIrs.add(new KelasIrs());
    
            listKelasIrs.get(0).setKelas(listKelas.get(0));
            listKelasIrs.get(1).setKelas(listKelas.get(1));
            listKelasIrs.get(2).setKelas(listKelas.get(2));
            listKelasIrs.get(3).setKelas(listKelas.get(3));
        }

        @BeforeEach
        void setUp() {
            initListMataKuliah();
            initListKelas();
            initListKelasIrs();
        }

        @Test
        void testCheckKelas0ConflictWithKelas1() throws Exception {
            JadwalResult result = new JadwalResult(listKelas.get(0));
            result.setKonflik(List.of(listKelasProxy.get(1)));
            result.setOk(false);
            
            assertThat(checker.check(listKelasIrs.get(0), listKelasIrs)).usingRecursiveComparison().isEqualTo(result);
        }
        
        @Test
        void testCheckKelas1ConflictWithKelas0AndKelas3() throws Exception {
            JadwalResult result = new JadwalResult(listKelas.get(1));
            result.setKonflik(List.of(listKelasProxy.get(0), listKelasProxy.get(3)));
            result.setOk(false);
            
            assertThat(checker.check(listKelasIrs.get(1), listKelasIrs)).usingRecursiveComparison().isEqualTo(result);
        }
        
        @Test
        void testCheckKelas2NoConflict() throws Exception {
            JadwalResult result = new JadwalResult(listKelas.get(2));
            result.setKonflik(List.of());
            result.setOk(true);
            
            assertThat(checker.check(listKelasIrs.get(2), listKelasIrs)).usingRecursiveComparison().isEqualTo(result);
        }
        
        @Test
        void testCheckKelas3ConflictWithKelas1() throws Exception {
            JadwalResult result = new JadwalResult(listKelas.get(3));
            result.setKonflik(List.of(listKelasProxy.get(1)));
            result.setOk(false);
            
            assertThat(checker.check(listKelasIrs.get(3), listKelasIrs)).usingRecursiveComparison().isEqualTo(result);
        }
    }
}
