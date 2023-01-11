package com.ppl.siakngnewbe.chat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.dosen.DosenModelRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaModelRepository;
import java.util.Collections;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.TOKEN_PREFIX;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private MahasiswaModelRepository mahasiswaRepository;

    @Autowired
    private DosenModelRepository dosenRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public DecodedJWT decodeJwt(String token) {
        return JWT
                .require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));
    }
    
    @Override
    public Chat postChatMahasiswa(String token, Chat chat) {
        DecodedJWT verifier = decodeJwt(token);
        String npm = verifier.getClaim("npm").toString().replace("\"", "");
        Mahasiswa optionalMahasiswa = mahasiswaRepository.findByNpm(npm);
        Dosen optionalDosen = dosenRepository.findByNip(optionalMahasiswa.getPembimbingAkademik().getNip());

        var chatModel = new Chat(optionalMahasiswa, optionalDosen, chat.getIsiChat(), "MAHASISWA", null, false);
        Set<Chat> listChat =  optionalMahasiswa.getChatSet();
        listChat.add(chatModel);
        optionalMahasiswa.setChatSet(listChat);
            
        chatRepository.save(chatModel);
        return chatModel;
    }

    @Override
    public Chat postChatDosen(String token, Chat chat, String npm) {
        DecodedJWT verifier = decodeJwt(token);
        String nip = verifier.getClaim("nip").toString().replace("\"", "");
        Dosen optionalDosen = dosenRepository.findByNip(nip);
        Mahasiswa mahasiswa = mahasiswaRepository.findByNpm(npm);

        if (optionalDosen != null && mahasiswa != null) {
            var chatModel = new Chat(mahasiswa, optionalDosen, chat.getIsiChat(),"DOSEN", false, null);
            
            Set<Chat> listChat = mahasiswa.getChatSet();
            listChat.add(chatModel);
            mahasiswa.setChatSet(listChat);
            
            Set<Mahasiswa> listMahasiswa = optionalDosen.getDiChatOleh();
            listMahasiswa.add(mahasiswa);
            optionalDosen.setDiChatOleh(listMahasiswa);

            chatRepository.save(chatModel);
            return chatModel;
        }
        return null;
    }

    @Override
    public List<Chat> getChatForMahasiswa(String token) {
        DecodedJWT verifier = decodeJwt(token);
        String npm = verifier.getClaim("npm").toString().replace("\"", "");
        Mahasiswa optionalMahasiswa = mahasiswaRepository.findByNpm(npm);
        if (optionalMahasiswa != null) {
            Dosen dosen = optionalMahasiswa.getPembimbingAkademik();
            return chatRepository.findChatByMahasiswaAndDosen(dosen.getId(), optionalMahasiswa.getId());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Chat> getChatForDosen(String token, String npm) {
        DecodedJWT verifier = decodeJwt(token);
        String nip = verifier.getClaim("nip").toString().replace("\"", "");
        Dosen optionalDosen = dosenRepository.findByNip(nip);
        Mahasiswa optionalMahasiswa = mahasiswaRepository.findByNpm(npm);

        if (optionalDosen != null && optionalMahasiswa != null) {
            return chatRepository.findChatByMahasiswaAndDosen(optionalDosen.getId(), optionalMahasiswa.getId());
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean readChatMahasiswaByDosen(String token, String npm) {
        DecodedJWT verifier = decodeJwt(token);
        String nip = verifier.getClaim("nip").toString().replace("\"", "");
        Dosen dosen = dosenRepository.findByNip(nip);
        Mahasiswa mahasiswa = mahasiswaRepository.findByNpm(npm);

        if (dosen != null && mahasiswa != null) {
            List<Chat> listChat =  chatRepository.findChatByMahasiswaAndDosen(dosen.getId(), mahasiswa.getId());
            for (var i = 0 ; i < listChat.size() ; i++) {
                if (listChat.get(i).getSender().equals("MAHASISWA")) {
                    listChat.get(i).setReadByDosen(true);
                    chatRepository.save(listChat.get(i));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean readChatDosenByMahasiswa(String token) {
        DecodedJWT verifier = decodeJwt(token);
        String npm = verifier.getClaim("npm").toString().replace("\"", "");
        Mahasiswa mahasiswa = mahasiswaRepository.findByNpm(npm);

        if (mahasiswa != null) {
            Dosen dosen = dosenRepository.findByNip(mahasiswa.getPembimbingAkademik().getNip());
            List<Chat> listChat =  chatRepository.findChatByMahasiswaAndDosen(dosen.getId(), mahasiswa.getId());
            for (var i = 0 ; i < listChat.size() ; i++) {
                if (listChat.get(i).getSender().equals("DOSEN")) {
                    listChat.get(i).setReadByMahasiswa(true);
                    chatRepository.save(listChat.get(i));
                }
            }
            return true;
        }
        return false;
    }
}
