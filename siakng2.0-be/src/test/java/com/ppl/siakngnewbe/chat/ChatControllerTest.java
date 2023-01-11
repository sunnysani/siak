package com.ppl.siakngnewbe.chat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaServiceImpl;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import com.ppl.siakngnewbe.security.UserDetailsServiceImpl;
import com.ppl.siakngnewbe.security.utils.SecurityConstant;
import com.ppl.siakngnewbe.user.UserModelRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private ChatServiceImpl chatService;

    @MockBean
    private  PasswordEncoder passwordEncoder;

    private Mahasiswa mahasiswaModel;
    private Dosen dosenModel;
    private Chat chatModel;
    private Chat chatModelDosen;

    private String jsonWebToken;
    private String jsonWebTokenDosen;

    @BeforeEach
    public  void setUp() {
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

        chatModel = new Chat();
        chatModel.setIsiChat("Hallo World");
        chatModel.setSender("MAHASISWA");
        chatModel.setMahasiswaModel(mahasiswaModel);
        chatModel.setDosenModel(dosenModel);

        chatModelDosen = new Chat();
        chatModelDosen.setIsiChat("Hai");
        chatModelDosen.setSender("DOSEN");
        chatModelDosen.setMahasiswaModel(mahasiswaModel);
        chatModelDosen.setDosenModel(dosenModel);
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private void helperPostLoginAuthWithJWT() {
        jsonWebToken =  JWT.create()
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
    void testControllerPostChatMahasiswa() throws Exception{
        when(chatService.postChatMahasiswa(jsonWebToken, chatModel)).thenReturn(chatModel);
        helperPostLoginAuthWithJWT();
        mvc.perform(post("/chat/post/mahasiswa").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebToken)
                .content(mapToJson(chatModel)))
                .andExpect(status().isOk());
    }

    @Test
    void testControllerPostChatDosen() throws Exception {
        when(chatService.postChatDosen(jsonWebTokenDosen,chatModel,"19091210901")).thenReturn(chatModelDosen);
        helperPostLoginAuthWithJWT();
        mvc.perform(post("/chat/post/dosen/19091210901").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebToken)
                .content(mapToJson(chatModel)))
                .andExpect(status().isOk());
    }

    @Test
    void testControllerGetChatFromMahasiswa() throws  Exception {
        List<Chat> listChat = new ArrayList<>();
        listChat.add(chatModel);
        listChat.add(chatModelDosen);
        when(chatService.getChatForMahasiswa(jsonWebToken)).thenReturn(listChat);
        helperPostLoginAuthWithJWT();
        mvc.perform(get("/chat/mahasiswa").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebToken))
                .andExpect(status().isOk());
    }

    @Test
    void testControllerGetChatFromDosen() throws  Exception {
        List<Chat> listChat = new ArrayList<>();
        listChat.add(chatModel);
        listChat.add(chatModelDosen);
        when(chatService.getChatForDosen(jsonWebTokenDosen,mahasiswaModel.getNpm())).thenReturn(listChat);
        helperPostLoginAuthWithJWT();
        mvc.perform(get("/chat/dosen/19091210901").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebToken))
                .andExpect(status().isOk());
    }

    @Test
    void testControllerReadChatByDosen() throws  Exception {
        when(chatService.readChatMahasiswaByDosen(jsonWebTokenDosen, "19091210901")).thenReturn(true);
        helperPostLoginAuthWithJWT();
        mvc.perform(post("/chat/dosen/read/19091210901").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebToken))
                .andExpect(status().isOk());
    }

    @Test
    void testControllerReadChatByMahasiswa() throws  Exception {
        when(chatService.readChatDosenByMahasiswa(jsonWebToken)).thenReturn(true);
        helperPostLoginAuthWithJWT();
        mvc.perform(post("/chat/mahasiswa/read").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + jsonWebToken))
                .andExpect(status().isOk());
    }

}
