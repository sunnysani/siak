package com.ppl.siakngnewbe.mataKuliah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaService;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelas.KelasRepository;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.matakuliah.MataKuliahRepository;
import com.ppl.siakngnewbe.matakuliah.MataKuliahService;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaranService;

import com.ppl.siakngnewbe.tahunajaran.TahunAjaranStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MataKuliahServiceTest {
 
    @Mock
    private MataKuliahRepository mataKuliahRepository;

    @Mock
    private IrsMahasiswaRepository irsMahasiswaRepository;

    @Mock
    private KelasRepository kelasRepository;

    @Mock
    private IrsMahasiswaService irsMahasiswaService;

    @Mock
    private TahunAjaranService tahunAjaranService;

    @InjectMocks
    private MataKuliahService mataKuliahService;

    private IrsMahasiswa irs1;
    private IrsMahasiswa irs2;
    private IrsMahasiswa irs3;
    private String jsonWebToken;
    private TahunAjaran tahunAjaran;
    private TahunAjaran tahunAjaran1;
    private TahunAjaran tahunAjaran2;
    private String data;

    private List<MataKuliah> mataKuliahs;
    private List<IrsMahasiswa> irsMahasiswas;

    @BeforeEach
    public void SetUp() {

        MataKuliah mataKuliah = new MataKuliah();
        MataKuliah mataKuliah2 = new MataKuliah();
        MataKuliah mataKuliah3 = new MataKuliah();
        MataKuliah mataKuliah4 = new MataKuliah();

        Mahasiswa mahasiswa1 = new Mahasiswa();
        Mahasiswa mahasiswa2 = new Mahasiswa();
        KelasIrs kelasIrs = new KelasIrs();
        Kelas kelas = new Kelas();
        tahunAjaran = new TahunAjaran();
        tahunAjaran1 = new TahunAjaran();
        tahunAjaran2 = new TahunAjaran();

        tahunAjaran.setStatus(TahunAjaranStatus.IRS_ISI);
        tahunAjaran1.setNama("2019/2020-3");
        tahunAjaran1.setStatus(TahunAjaranStatus.IRS_ADD_DROP);
        tahunAjaran2.setNama("2018/2019-1");
        tahunAjaran2.setStatus(TahunAjaranStatus.IRS);

        mataKuliahs = new ArrayList<MataKuliah>();

        mataKuliah.setId("ID1");
        mataKuliah.setNama("Dasar Dasar Pemrograman");
        mataKuliah.setKurikulum("2016");
        mataKuliah.setSks(4);
        mataKuliah.setTerm("1");
        mataKuliah.setTahunAjaran(tahunAjaran);
        mataKuliah2.setId("ID2");
        mataKuliah2.setNama("Rekayasa Perangkat Lunak");
        mataKuliah2.setKurikulum("2020");
        mataKuliah2.setSks(4);
        mataKuliah2.setTerm("5");
        mataKuliah2.setTahunAjaran(tahunAjaran);
        mataKuliah3.setId("ID3");
        mataKuliah3.setNama("Proyek Perangkat Lunak");
        mataKuliah3.setKurikulum("2020");
        mataKuliah3.setSks(6);
        mataKuliah3.setTerm("6");
        mataKuliah3.setTahunAjaran(tahunAjaran);
        mataKuliah4.setId("ID4");
        mataKuliah4.setNama("Analisis Numerik");
        mataKuliah4.setKurikulum("2020");
        mataKuliah4.setSks(3);
        mataKuliah4.setTerm("6");
        mataKuliah4.setTahunAjaran(tahunAjaran1);

        Set<MataKuliah> prasyarat = new HashSet<>();
        prasyarat.add(mataKuliah);
        mataKuliah2.setPrasyaratMataKuliahSet(prasyarat);
        Set<MataKuliah> prasyarat2 = new HashSet<>();
        prasyarat2.add(mataKuliah2);
        mataKuliah3.setPrasyaratMataKuliahSet(prasyarat2);

        mataKuliahs.add(mataKuliah4);
        mataKuliahs.add(mataKuliah3);
        mataKuliahs.add(mataKuliah2);
        mataKuliahs.add(mataKuliah);

        kelas.setMataKuliah(mataKuliah);
        kelasIrs.setKelas(kelas);

        Set<KelasIrs> kelasIrss = new HashSet<KelasIrs>();
        kelasIrss.add(kelasIrs);

        irs1 = new IrsMahasiswa();
        irs2 = new IrsMahasiswa();
        irs3 = new IrsMahasiswa();

        irs1.setSemester(1);
        irs1.setMahasiswa(mahasiswa1);
        irs1.setKelasIrsSet(kelasIrss);
        irs2.setSemester(2);
        irs2.setMahasiswa(mahasiswa1);
        irs3.setSemester(1);
        irs3.setMahasiswa(mahasiswa2);

        irsMahasiswas = new ArrayList<IrsMahasiswa>();
        irsMahasiswas.add(irs1);
        irsMahasiswas.add(irs2);
        irsMahasiswas.add(irs3);

        helperPostLoginAuthWithJWT();

    }
    
    private void helperPostLoginAuthWithJWT() {
        jsonWebToken = "Bearer " + JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
	void testServiceGetMataKuliahSuccess() throws Exception {
        when(mataKuliahRepository.findAllByOrderByNamaAsc()).thenReturn(mataKuliahs);
        when(kelasRepository.findByMataKuliahOrderByNamaAsc(any())).thenReturn(null);
        when(irsMahasiswaRepository.findAll()).thenReturn(irsMahasiswas);
        when(irsMahasiswaService.getIrs(jsonWebToken)).thenReturn(irs2);
        when(tahunAjaranService.getTahunAjaran("latest")).thenReturn(tahunAjaran);
        data = "latest";
        String type = "isi";
        assertEquals("[{\"id\":\"ID3\",\"nama\":\"Proyek Perangkat Lunak\",\"sks\":6,\"term\":\"6\",\"kurikulum\":\"2020\",\"prasyaratMataKuliahSet\":[{\"id\":\"ID2\",\"nama\":\"Rekayasa Perangkat Lunak\",\"sks\":4,\"term\":\"5\",\"kurikulum\":\"2020\",\"prasyaratMataKuliahSet\":[{\"id\":\"ID1\",\"nama\":\"Dasar Dasar Pemrograman\",\"sks\":4,\"term\":\"1\",\"kurikulum\":\"2016\",\"prasyaratMataKuliahSet\":null,\"kelasSet\":null,\"tahunAjaran\":{\"nama\":null,\"term\":0,\"active\":0,\"status\":\"IRS_ISI\",\"isiIRSStart\":null,\"isiIRSEnd\":null,\"addDropIRSStart\":null,\"addDropIRSEnd\":null,\"pembayaranStart\":null,\"pembayaranEnd\":null,\"perkuliahanStart\":null,\"perkuliahanEnd\":null,\"isiNilaiDosenStart\":null,\"isiNilaiDosenEnd\":null,\"selesaiDate\":null}}],\"kelasSet\":null,\"tahunAjaran\":{\"nama\":null,\"term\":0,\"active\":0,\"status\":\"IRS_ISI\",\"isiIRSStart\":null,\"isiIRSEnd\":null,\"addDropIRSStart\":null,\"addDropIRSEnd\":null,\"pembayaranStart\":null,\"pembayaranEnd\":null,\"perkuliahanStart\":null,\"perkuliahanEnd\":null,\"isiNilaiDosenStart\":null,\"isiNilaiDosenEnd\":null,\"selesaiDate\":null}}],\"kelasSet\":null,\"tahunAjaran\":{\"nama\":null,\"term\":0,\"active\":0,\"status\":\"IRS_ISI\",\"isiIRSStart\":null,\"isiIRSEnd\":null,\"addDropIRSStart\":null,\"addDropIRSEnd\":null,\"pembayaranStart\":null,\"pembayaranEnd\":null,\"perkuliahanStart\":null,\"perkuliahanEnd\":null,\"isiNilaiDosenStart\":null,\"isiNilaiDosenEnd\":null,\"selesaiDate\":null},\"eligible\":0},{\"id\":\"ID2\",\"nama\":\"Rekayasa Perangkat Lunak\",\"sks\":4,\"term\":\"5\",\"kurikulum\":\"2020\",\"prasyaratMataKuliahSet\":[{\"id\":\"ID1\",\"nama\":\"Dasar Dasar Pemrograman\",\"sks\":4,\"term\":\"1\",\"kurikulum\":\"2016\",\"prasyaratMataKuliahSet\":null,\"kelasSet\":null,\"tahunAjaran\":{\"nama\":null,\"term\":0,\"active\":0,\"status\":\"IRS_ISI\",\"isiIRSStart\":null,\"isiIRSEnd\":null,\"addDropIRSStart\":null,\"addDropIRSEnd\":null,\"pembayaranStart\":null,\"pembayaranEnd\":null,\"perkuliahanStart\":null,\"perkuliahanEnd\":null,\"isiNilaiDosenStart\":null,\"isiNilaiDosenEnd\":null,\"selesaiDate\":null}}],\"kelasSet\":null,\"tahunAjaran\":{\"nama\":null,\"term\":0,\"active\":0,\"status\":\"IRS_ISI\",\"isiIRSStart\":null,\"isiIRSEnd\":null,\"addDropIRSStart\":null,\"addDropIRSEnd\":null,\"pembayaranStart\":null,\"pembayaranEnd\":null,\"perkuliahanStart\":null,\"perkuliahanEnd\":null,\"isiNilaiDosenStart\":null,\"isiNilaiDosenEnd\":null,\"selesaiDate\":null},\"eligible\":1},{\"id\":\"ID1\",\"nama\":\"Dasar Dasar Pemrograman\",\"sks\":4,\"term\":\"1\",\"kurikulum\":\"2016\",\"prasyaratMataKuliahSet\":null,\"kelasSet\":null,\"tahunAjaran\":{\"nama\":null,\"term\":0,\"active\":0,\"status\":\"IRS_ISI\",\"isiIRSStart\":null,\"isiIRSEnd\":null,\"addDropIRSStart\":null,\"addDropIRSEnd\":null,\"pembayaranStart\":null,\"pembayaranEnd\":null,\"perkuliahanStart\":null,\"perkuliahanEnd\":null,\"isiNilaiDosenStart\":null,\"isiNilaiDosenEnd\":null,\"selesaiDate\":null},\"eligible\":1}]", mataKuliahService.getMataKuliah(jsonWebToken, data, type));
    }

    @Test
    void testServiceGetMataKuliahNotOnStatusFail() throws Exception {
        when(tahunAjaranService.getTahunAjaran("latest")).thenReturn(tahunAjaran);
        data = "latest";
        String type = "add-drop";

        assertEquals("gagal", mataKuliahService.getMataKuliah(jsonWebToken, data, type));
    }

    @Test
    void testServiceGetMataKuliahNoMatkulOnTahunAjaran() throws Exception {
        when(tahunAjaranService.getTahunAjaran("2019/2020-3")).thenReturn(tahunAjaran1);
        data = "2019/2020-3";
        String type = "add-drop";
        assertEquals("[]", mataKuliahService.getMataKuliah(jsonWebToken, data, type));
    }

    @Test
    void testStatusNotOnAddDropOrIsi() throws Exception {
        when(tahunAjaranService.getTahunAjaran("2018/2019-1")).thenReturn(tahunAjaran2);
        data = "2018/2019-1";
        String type = "irs";
        assertEquals("gagal", mataKuliahService.getMataKuliah(jsonWebToken, data, type));
    }

    @Test
    void testStatusNotOnIsi() throws Exception {
        when(tahunAjaranService.getTahunAjaran("2019/2020-3")).thenReturn(tahunAjaran1);
        data = "2019/2020-3";
        String type = "isi";
        assertEquals("gagal", mataKuliahService.getMataKuliah(jsonWebToken, data, type));
    }

    @Test
    void testServiceGetMataKuliahFailTahunAjaranNotFound() throws Exception {
        when(tahunAjaranService.getTahunAjaran("2020/2021-3")).thenReturn(null);
        data = "2020/2021-3";
        String type = "isi";

        assertEquals("gagal", mataKuliahService.getMataKuliah(jsonWebToken, data, type));
    }

    @Test
	void testServiceGetMataKuliahTakenSuccess() throws Exception {
        when(irsMahasiswaRepository.findAll()).thenReturn(irsMahasiswas);
        when(irsMahasiswaService.getIrs(jsonWebToken)).thenReturn(irs2);
        assertEquals(1, mataKuliahService.getMataKuliahTaken(jsonWebToken).size());
    }

}