package com.ppl.siakngnewbe.mahasiswa;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MahasiswaServiceTest {
    @Mock
    private MahasiswaModelRepository mahasiswaModelRepository;

    @InjectMocks
    private MahasiswaServiceImpl mahasiswaService;

    private Mahasiswa mahasiswaModel;

    String jsonWebToken;
    @BeforeEach
    public void setUp() {
        mahasiswaModel = new Mahasiswa();
        mahasiswaModel.setId(1L);
        mahasiswaModel.setNamaLengkap("Eren Yeager");
        mahasiswaModel.setUsername("eren.yeager");
        mahasiswaModel.setPassword("surveycorps");
        mahasiswaModel.setIpk(4);
        mahasiswaModel.setNpm("19062727231");
        mahasiswaModel.setStatus(StatusAkademik.AKTIF);
        mahasiswaModel.setUserRole(UserModelRole.MAHASISWA);

        jsonWebToken =  JWT.create()
                .withSubject(mahasiswaModel.getUsername())
                .withClaim("role",mahasiswaModel.getUserRole().name())
                .withClaim("npm", mahasiswaModel.getNpm())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }

    @Test
    void testServiceGetMahasiswaByUsernameExist() {
        when(mahasiswaModelRepository.findByUsername("eren.yeager")).thenReturn(Optional.of(mahasiswaModel));
        Mahasiswa mahasiswa = mahasiswaService.getMahasiswaByUsername(jsonWebToken);
        assertEquals(mahasiswa.getNpm(), mahasiswaModel.getNpm());
    }
}
