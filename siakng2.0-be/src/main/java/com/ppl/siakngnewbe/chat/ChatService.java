package com.ppl.siakngnewbe.chat;

import java.util.List;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface ChatService {
    public DecodedJWT decodeJwt(String token);
    Chat postChatMahasiswa(String token, Chat chat);
    Chat postChatDosen(String token, Chat chat, String npm);
    List<Chat> getChatForMahasiswa(String token);
    List<Chat> getChatForDosen(String token, String npm);
    Boolean readChatMahasiswaByDosen(String token, String npm);
    Boolean readChatDosenByMahasiswa(String token);
}
