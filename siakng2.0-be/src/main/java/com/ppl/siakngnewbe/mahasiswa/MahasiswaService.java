package com.ppl.siakngnewbe.mahasiswa;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface MahasiswaService {
    public DecodedJWT decodeJwt(String token);
    public Mahasiswa getMahasiswaByUsername(String token);
}
