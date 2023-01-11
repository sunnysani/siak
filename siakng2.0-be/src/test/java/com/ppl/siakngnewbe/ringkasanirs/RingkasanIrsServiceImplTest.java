package com.ppl.siakngnewbe.ringkasanirs;

import java.util.ArrayList;
import java.util.List;

import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.kelasirs.KelasIrsRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RingkasanIrsServiceImplTest {
    @Mock
    private KelasIrsRepository kelasIrsRepository;

    @Mock
    private IrsMahasiswaRepository irsRepository;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @InjectMocks
    private RingkasanIrsServiceImpl ringkasanIrsService;

    private Mahasiswa mahasiswa;
    private List<IrsMahasiswa> listIrs;
    private List<Kelas> listKelas;
    private List<KelasIrs> listKelasIrs;

    @BeforeEach
    void setUp() {
        mahasiswa = new Mahasiswa();

        listIrs = new ArrayList<>();

        listIrs.add(new IrsMahasiswa());
        listIrs.get(0).setMahasiswa(mahasiswa);
        listIrs.get(0).setSemester(2021);

        listIrs.add(new IrsMahasiswa());
        listIrs.get(1).setMahasiswa(mahasiswa);
        listIrs.get(1).setSemester(2020);

        listKelas = new ArrayList<>();

        listKelas.add(new Kelas());
        listKelas.add(new Kelas());

        listKelasIrs = new ArrayList<>();

        listKelasIrs.add(new KelasIrs());
        listKelasIrs.get(0).setIrs(listIrs.get(0));
        listKelasIrs.get(0).setKelas(listKelas.get(0));
        
        listKelasIrs.add(new KelasIrs());
        listKelasIrs.get(1).setIrs(listIrs.get(0));
        listKelasIrs.get(1).setKelas(listKelas.get(1));
    }

    @Test
    void testServiceGetLatestDetailIrs() throws Exception {
        when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(listIrs);
        
        IrsMahasiswa irsResult = ringkasanIrsService.getLatestDetailIrs(mahasiswa);
        assertEquals(listIrs.get(0), irsResult);
    }

    @Test
    void testServiceGetLatestDetailIrsNoIrs() throws Exception {
        when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(null);
        
        IrsMahasiswa irsResult = ringkasanIrsService.getLatestDetailIrs(mahasiswa);
        assertNull(irsResult);
    }

    @Test
    void testServiceGetLatestMatkulDipilih() throws Exception {
        when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(listIrs);
        when(kelasIrsRepository.findByIrs(listIrs.get(0))).thenReturn(listKelasIrs);

        List<Kelas> listKelasResult = ringkasanIrsService.getLatestMatkulDipilih(mahasiswa);
        assertEquals(listKelas, listKelasResult);
    }

    @Test
    void testServiceGetLatestMatkulDipilihNoIrs() throws Exception {
        when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(null);

        List<Kelas> listKelasResult = ringkasanIrsService.getLatestMatkulDipilih(mahasiswa);
        assertTrue(listKelasResult.isEmpty());
    }

    @Test
    void testServiceGetLatestMatkulDipilihNoKelas() throws Exception {
        when(irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa)).thenReturn(listIrs);
        when(kelasIrsRepository.findByIrs(listIrs.get(0))).thenReturn(null);

        List<Kelas> listKelasResult = ringkasanIrsService.getLatestMatkulDipilih(mahasiswa);
        assertTrue(listKelasResult.isEmpty());
    }
}
