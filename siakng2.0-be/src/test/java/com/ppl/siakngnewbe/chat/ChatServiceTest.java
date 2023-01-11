package com.ppl.siakngnewbe.chat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.dosen.DosenModelRepository;
import com.ppl.siakngnewbe.dosen.DosenServiceImpl;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaModelRepository;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaServiceImpl;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    @Mock
    private MahasiswaModelRepository mahasiswaRepository;

    @Mock
    private DosenModelRepository dosenRepository;

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    private Mahasiswa mahasiswaModel;
    private Dosen dosenModel;
    private Chat chatModel;
    private Chat chatModelDosen;
    String jsonWebTokenMahasiswa;
    String jsonWebTokenDosen;
    @BeforeEach
    public void setUp() {
        Set<Chat> listChat = new HashSet<>();
        Set<Mahasiswa> listMahasiswa = new HashSet<>();

        mahasiswaModel = new Mahasiswa();
        mahasiswaModel.setId(1L);
        mahasiswaModel.setUsername("eren.yeager");
        mahasiswaModel.setPassword("dummyPassword");
        mahasiswaModel.setNamaLengkap("Eren Yeager");
        mahasiswaModel.setNpm("1906282831");
        mahasiswaModel.setUserRole(UserModelRole.MAHASISWA);
        mahasiswaModel.setStatus(StatusAkademik.AKTIF);
        mahasiswaModel.setIpk(4);

        dosenModel = new Dosen();
        dosenModel.setId(2);
        dosenModel.setNamaLengkap("Grisha Yeager");
        dosenModel.setNip("19091210901");
        dosenModel.setUsername("grisha.yeager");
        dosenModel.setUserRole(UserModelRole.DOSEN);
        dosenModel.setPassword("dummydummypassword");
        dosenModel.setDiChatOleh(listMahasiswa);

        chatModel = new Chat();
        chatModel.setId(3);
        chatModel.setIsiChat("Hallo World");
        chatModel.setSender("MAHASISWA");
        chatModel.setMahasiswaModel(mahasiswaModel);
        chatModel.setDosenModel(dosenModel);

        chatModelDosen = new Chat();
        chatModelDosen.setId(3);
        chatModelDosen.setIsiChat("Hallo World");
        chatModelDosen.setSender("DOSEN");
        chatModelDosen.setMahasiswaModel(mahasiswaModel);
        chatModelDosen.setDosenModel(dosenModel);

        listChat.add(chatModel);
        listChat.add(chatModelDosen);
        mahasiswaModel.setChatSet(listChat);
        mahasiswaModel.setPembimbingAkademik(dosenModel);

        jsonWebTokenMahasiswa =  JWT.create()
                .withSubject(mahasiswaModel.getUsername())
                .withClaim("role",mahasiswaModel.getUserRole().name())
                .withClaim("npm", mahasiswaModel.getNpm())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));

        jsonWebTokenDosen =  JWT.create()
                .withSubject(dosenModel.getUsername())
                .withClaim("role",dosenModel.getUserRole().name())
                .withClaim("nip", dosenModel.getNip())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));
        }

        @Test
        void testServicePostChatByMahasiswa() {
            when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
            when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);

            Chat resultPostChat = chatService.postChatMahasiswa(jsonWebTokenMahasiswa,chatModel);
            assertEquals(resultPostChat.getIsiChat(),chatModel.getIsiChat());
        }

        @Test
        void testServicePostChatByDosen() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        Chat resultPostChat = chatService.postChatDosen(jsonWebTokenDosen,chatModel, mahasiswaModel.getNpm());
        assertEquals(resultPostChat.getIsiChat(),chatModel.getIsiChat());
        }

    @Test
    void testServicePostChatByDosenNoMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        Chat resultPostChat = chatService.postChatDosen(jsonWebTokenDosen,chatModel, mahasiswaModel.getNpm());
        assertNull(resultPostChat);
    }

    @Test
    void testServicePostChatByDosenNoDosen() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        Chat resultPostChat = chatService.postChatDosen(jsonWebTokenDosen,chatModel, mahasiswaModel.getNpm());
        assertNull(resultPostChat);
    }

    @Test
    void testServicePostChatByDosenNoDosenNoMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        Chat resultPostChat = chatService.postChatDosen(jsonWebTokenDosen,chatModel, mahasiswaModel.getNpm());
        assertNull(resultPostChat);
    }

    @Test
    void testServiceGetChatForMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        List<Chat> listChat = new ArrayList<>();
        List<Chat> result = chatService.getChatForMahasiswa(jsonWebTokenMahasiswa);
        assertEquals(listChat, result);
    }

    @Test
    void testServiceGetChatForMahasiswaReturnEmpty() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        List<Chat> listChat = new ArrayList<>();
        List<Chat> result = chatService.getChatForMahasiswa(jsonWebTokenMahasiswa);
        assertEquals(listChat, result);
    }

    @Test
    void testServiceGetChatForDosen() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        List<Chat> listChat = new ArrayList<>();
        List<Chat> result = chatService.getChatForDosen(jsonWebTokenDosen, mahasiswaModel.getNpm());
        assertEquals(listChat, result);
    }

    @Test
    void testServiceGetChatForDosenReturnNoMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        List<Chat> listChat = new ArrayList<>();
        List<Chat> result = chatService.getChatForDosen(jsonWebTokenDosen, mahasiswaModel.getNpm());
        assertEquals(listChat, result);
    }

    @Test
    void testServiceGetChatForDosenReturnNoDosen() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        List<Chat> listChat = new ArrayList<>();
        List<Chat> result = chatService.getChatForDosen(jsonWebTokenDosen, mahasiswaModel.getNpm());
        assertEquals(listChat, result);
    }

    @Test
    void testServiceGetChatForDosenReturnNoDosenMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        List<Chat> listChat = new ArrayList<>();
        List<Chat> result = chatService.getChatForDosen(jsonWebTokenDosen, mahasiswaModel.getNpm());
        assertEquals(listChat, result);
    }

    @Test
    void testServiceReadChatMahasiswaByDosen() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        List<Chat> listChat = new ArrayList<>();
        listChat.add(chatModel);
        when(chatRepository.findChatByMahasiswaAndDosen(dosenModel.getId(),mahasiswaModel.getId())).thenReturn(listChat);
        Boolean result = chatService.readChatMahasiswaByDosen(jsonWebTokenDosen,mahasiswaModel.getNpm());
        assertTrue(result);
    }

    @Test
    void testServiceReadChatMahasiswaByDosenNoMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        Boolean result = chatService.readChatMahasiswaByDosen(jsonWebTokenDosen,mahasiswaModel.getNpm());
        assertFalse(result);
    }

    @Test
    void testServiceReadChatMahasiswaByDosenNoDosen() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        Boolean result = chatService.readChatMahasiswaByDosen(jsonWebTokenDosen,mahasiswaModel.getNpm());
        assertFalse(result);
    }

    @Test
    void testServiceReadChatMahasiswaByDosenNoDosenNoMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(null);
        Boolean result = chatService.readChatMahasiswaByDosen(jsonWebTokenDosen,mahasiswaModel.getNpm());
        assertFalse(result);
    }

    @Test
    void testServiceReadChatDosenByMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(mahasiswaModel);
        when(dosenRepository.findByNip(dosenModel.getNip())).thenReturn(dosenModel);
        List<Chat> listChat = new ArrayList<>();
        listChat.add(chatModelDosen);
        when(chatRepository.findChatByMahasiswaAndDosen(dosenModel.getId(),mahasiswaModel.getId())).thenReturn(listChat);
        Boolean result = chatService.readChatDosenByMahasiswa(jsonWebTokenMahasiswa);
        assertTrue(result);
    }

    @Test
    void testServiceReadChatDosenByMahasiswaNoMahasiswa() {
        when(mahasiswaRepository.findByNpm(mahasiswaModel.getNpm())).thenReturn(null);
        Boolean result = chatService.readChatDosenByMahasiswa(jsonWebTokenMahasiswa);
        assertFalse(result);
    }

}
