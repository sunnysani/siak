package com.ppl.siakngnewbe.auth;


import com.ppl.siakngnewbe.dosen.DosenDTO;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.dosen.DosenModelRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaDTO;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaModelRepository;
import com.ppl.siakngnewbe.user.*;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserModelRepository userModelRepository;

    @Mock
    private MahasiswaModelRepository mahasiswaModelRepository;

    @Mock
    private DosenModelRepository dosenModelRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private MahasiswaDTO mahasiswaDTO;
    private DosenDTO dosenDTO;

    @BeforeEach
    void setUp() {
        authService.setUserModelRepository(userModelRepository);
        authService.setMahasiswaModelRepository(mahasiswaModelRepository);
        authService.setDosenModelRepository(dosenModelRepository);
        authService.setPasswordEncoder(passwordEncoder);
        mahasiswaDTO = new MahasiswaDTO();
        mahasiswaDTO.setUsername("yeager.eren");
        mahasiswaDTO.setPassword("dummypassword");
        mahasiswaDTO.setNamaLengkap("EREN YEAGER");
        mahasiswaDTO.setIpk(4);
        mahasiswaDTO.setNpm("2006190699");

        dosenDTO = new DosenDTO();
        dosenDTO.setUsername("keith.sadies");
        dosenDTO.setPassword("aot-ending-sucks");
        dosenDTO.setNamaLengkap("Keith Sadies");
        dosenDTO.setNip("190908283012801");
    }

    @Test
    void testRegisterMahasiswa() throws IllegalAccessException {
        when(userModelRepository.findByUsername("yeager.eren")).thenReturn(Optional.empty());
        mahasiswaDTO.setStatusAkademik("AKTIF");
        Mahasiswa result = authService.registerMahasiswa(mahasiswaDTO);
        assertEquals(StatusAkademik.AKTIF, result.getStatus());
    }

    @Test
    void testRegisterMahasiswaTidakAktif() throws IllegalAccessException {
        mahasiswaDTO.setStatusAkademik("TIDAK_AKTIF");
        Mahasiswa result = authService.registerMahasiswa(mahasiswaDTO);
        assertEquals(StatusAkademik.TIDAK_AKTIF, result.getStatus());
    }

    @Test
    void testRegisterMahasiswaDO() throws IllegalAccessException {
        mahasiswaDTO.setStatusAkademik("DROP_OUT");
        Mahasiswa result = authService.registerMahasiswa(mahasiswaDTO);
        assertEquals(StatusAkademik.DROP_OUT, result.getStatus());
    }

    @Test
    void testRegisterMahasiswaCuti() throws IllegalAccessException {
        mahasiswaDTO.setStatusAkademik("CUTI");
        Mahasiswa result = authService.registerMahasiswa(mahasiswaDTO);
        assertEquals(StatusAkademik.CUTI, result.getStatus());
    }

    @Test
    void testRegisterMahasiswaRandom() throws IllegalAccessException {
        mahasiswaDTO.setStatusAkademik("ASAL");
        Mahasiswa result = authService.registerMahasiswa(mahasiswaDTO);
        assertNull(result.getStatus());
    }

    @Test
    void testRegisterDosen() throws IllegalAccessException {
        when(userModelRepository.findByUsername("keith.sadies")).thenReturn(Optional.empty());
        Dosen result = authService.registerDosen(dosenDTO);
        assertEquals("keith.sadies", result.getUsername());
    }

    @Test
    void testRegisterButEmailExist() {
        Dosen dosen = new Dosen();
        dosen.setUsername("keith.sadies");
        dosen.setPassword("aot-ending-sucks");
        dosen.setUserRole(UserModelRole.DOSEN);
        dosen.setId(1L);
        dosen.setNamaLengkap("Keith Sadies");
        dosen.setNip("190908283012801");

        when(userModelRepository.findByUsername("keith.sadies")).thenReturn(Optional.of(dosen));

        assertThrows(IllegalAccessException.class, () -> authService.registerDosen(dosenDTO));
    }

}

