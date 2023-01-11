package com.ppl.siakngnewbe.ringkasanirs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaService;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import com.ppl.siakngnewbe.user.UserModelRole;

@WebMvcTest(controllers = RingkasanIrsController.class)
class RingkasanIrsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RingkasanIrsService ringkasanIrsService;

    @MockBean
    private MahasiswaService mahasiswaService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private Mahasiswa mahasiswa;
    private MataKuliah mataKuliah;
    private IrsMahasiswa irs;
    private List<Kelas> listKelas;

    @BeforeEach
    void setUp() {
        mahasiswa = new Mahasiswa();
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);

        mataKuliah = new MataKuliah();
        mataKuliah.setNama("Test");

        irs = new IrsMahasiswa();
        irs.setSemester(2021);
        
        listKelas = new ArrayList<Kelas>();

        listKelas.add(new Kelas());
        listKelas.get(0).setNama("Test A");
        listKelas.get(0).setMataKuliah(mataKuliah);

        listKelas.add(new Kelas());
    }

    @Test
    void testControllerGetDetailIrsWithNonMahasiswa() throws Exception {
        mockMvc.perform(get("/ringkasan-irs/detail-irs"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testControllerGetDetailIrsWithMahasiswa() throws Exception {
        var data = irs;

        when(ringkasanIrsService.getLatestDetailIrs(mahasiswa)).thenReturn(irs);
        
        mockMvc.perform(get("/ringkasan-irs/detail-irs").with(user(mahasiswa)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.result.*").isNotEmpty());

        verify(ringkasanIrsService, times(1)).getLatestDetailIrs(mahasiswa);
    }

    @Test
    void testControllerGetDetailIrsWithMahasiswaNoIrs() throws Exception {
        when(ringkasanIrsService.getLatestDetailIrs(mahasiswa)).thenReturn(null);
        
        mockMvc.perform(get("/ringkasan-irs/detail-irs").with(user(mahasiswa)))
               .andExpect(jsonPath("$.status").value(204));

        verify(ringkasanIrsService, times(1)).getLatestDetailIrs(mahasiswa);
    }

    @Test
    void testControllerGetMatkulDipilihWithNonMahasiswa() throws Exception {
        mockMvc.perform(get("/ringkasan-irs/matkul-dipilih"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testControllerGetMatkulDipilihWithMahasiswa() throws Exception {
        var data =  listKelas;

        when(ringkasanIrsService.getLatestMatkulDipilih(mahasiswa)).thenReturn(listKelas);

        mockMvc.perform(get("/ringkasan-irs/matkul-dipilih").with(user(mahasiswa)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.result.*").isNotEmpty());
         
        verify(ringkasanIrsService, times(1)).getLatestMatkulDipilih(mahasiswa);
    }

    @Test
    void testControllerGetMatkulDipilihWithMahasiswaNoKelas() throws Exception {
        when(ringkasanIrsService.getLatestMatkulDipilih(mahasiswa)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ringkasan-irs/matkul-dipilih").with(user(mahasiswa)))
               .andExpect(jsonPath("$.status").value(204));
        
        verify(ringkasanIrsService, times(1)).getLatestMatkulDipilih(mahasiswa);
    }
}
