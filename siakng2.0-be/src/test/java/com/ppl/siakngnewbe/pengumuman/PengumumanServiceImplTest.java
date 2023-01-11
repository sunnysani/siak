package com.ppl.siakngnewbe.pengumuman;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PengumumanServiceImplTest {
    private Class<?> pengumumanServiceClass;

    @Mock
    private PengumumanRepository pengumumanRepository;

    @InjectMocks
    private PengumumanServiceImpl pengumumanService;

    private PengumumanModel pengumumanModel;
    private PengumumanModel pengumumanModel2;
    private Mahasiswa mahasiswa;
    private String jsonWebToken;

    @BeforeEach
    public void setUp() throws Exception {
        pengumumanServiceClass = Class.forName("com.ppl.siakngnewbe.pengumuman.PengumumanServiceImpl");

        pengumumanModel = new PengumumanModel();
        pengumumanModel.setId(1L);
        pengumumanModel.setJudul("Test Pengumuman");
        pengumumanModel.setIsi("Test");
        pengumumanModel.setPenulis("Tester");

        pengumumanModel2 = new PengumumanModel();
        pengumumanModel2.setId(2L);
        pengumumanModel2.setJudul("Test Pengumuman 2");
        pengumumanModel2.setIsi("Test 2");
        pengumumanModel2.setPenulis("Tester 2");

        pengumumanRepository.save(pengumumanModel);
        pengumumanRepository.save(pengumumanModel2);
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebToken =  JWT.create()
                .withSubject(mahasiswa.getUsername())
                .withClaim("role",mahasiswa.getUserRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
    }


    @Test
    void testPengumumanServiceHasGetAllMethod() throws Exception {
        Method getPengumumanAll = pengumumanServiceClass.getDeclaredMethod("getPengumumanAll");
        int methodModifiers = getPengumumanAll.getModifiers();
        assertTrue(Modifier.isPublic(methodModifiers));

        ParameterizedType pt = (ParameterizedType) getPengumumanAll.getGenericReturnType();
        assertEquals(List.class, pt.getRawType());
    }


    @Test
    void testGetPengumumanById() throws Exception {
        when(pengumumanRepository.findById(1)).thenReturn(Optional.of(pengumumanModel));
        PengumumanModel result = pengumumanService.getPengumumanById(pengumumanModel.getId());
        assertEquals(pengumumanModel.getId(), result.getId());
    }

    @Test
    void testGetNullPengumumanById() throws Exception {
        assertNull(pengumumanService.getPengumumanById(pengumumanModel.getId()));
    }

    @Test
    void testGetAllPengumuman() throws Exception {
        List<PengumumanModel> listPengumuman = new ArrayList<>();
        listPengumuman.add(pengumumanModel);
        listPengumuman.add(pengumumanModel2);

        doReturn(listPengumuman).when(pengumumanRepository).findAll();
        assertEquals(2, pengumumanService.getPengumumanAll().size());
    }
}