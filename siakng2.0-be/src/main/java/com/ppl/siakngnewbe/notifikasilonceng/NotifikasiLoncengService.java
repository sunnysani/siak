package com.ppl.siakngnewbe.notifikasilonceng;

import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaRepository;
import java.util.Collections;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.TOKEN_PREFIX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifikasiLoncengService {
    @Autowired
    private NotifikasiLoncengRepository notifikasiLoncengRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    public DecodedJWT decodeJwt(String token) {
        return JWT
                .require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));
    }

    public List<NotifikasiLonceng> getNotifikasiForMahasiswa(String tokenMahasiswa) {
        DecodedJWT verifier = decodeJwt(tokenMahasiswa);
        String npm = verifier.getClaim("npm").toString().replace("\"", "");
        Mahasiswa mahasiswa = mahasiswaRepository.findByNpm(npm);

        if (mahasiswa != null) {
            return notifikasiLoncengRepository.getAllNotifikasiMahasiswa(mahasiswa.getId());
        }
        
        return Collections.emptyList();
    }

    public Boolean readAllNotifikasiByMahasiswa(String tokenMahasiswa) {
        DecodedJWT verifier = decodeJwt(tokenMahasiswa);
        String npm = verifier.getClaim("npm").toString().replace("\"", "");
        Mahasiswa mahasiswa = mahasiswaRepository.findByNpm(npm);

        if (mahasiswa != null) {
            List<NotifikasiLonceng> listNotif = notifikasiLoncengRepository.getAllNotifikasiMahasiswa(mahasiswa.getId());
            for (var i = 0 ; i < listNotif.size() ; i++) {
                listNotif.get(i).setRead(true);
                notifikasiLoncengRepository.save(listNotif.get(i));
            }
            return true;
        }
        return false;
    }
}
