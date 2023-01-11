package com.ppl.siakngnewbe.mahasiswa;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.TOKEN_PREFIX;

@Service
public class MahasiswaServiceImpl implements MahasiswaService {

    @Autowired
    private MahasiswaModelRepository mahasiswaModelRepository;


    @Override
    public DecodedJWT decodeJwt(String token) {
        return JWT
                .require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));
    }

    @Override
    public Mahasiswa getMahasiswaByUsername(String token) {
        DecodedJWT verifier = decodeJwt(token);
        String username = verifier.getSubject();
        var mahasiswa = mahasiswaModelRepository.findByUsername(username);
        return mahasiswa.orElse(null);
    }

}
