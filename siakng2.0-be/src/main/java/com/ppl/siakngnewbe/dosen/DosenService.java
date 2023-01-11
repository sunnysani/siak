package com.ppl.siakngnewbe.dosen;

import java.util.List;
import java.util.Set;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;

public interface DosenService {
    public DecodedJWT decodeJwt(String token);
    public Boolean addPembimbingAkademik(String token, String npm);
    public List<Dosen> getAllDosen();
    public Dosen getDosenById(String token);
    public Set<Mahasiswa> getMahasiswaBimbingan(String token);
    public Boolean setPersetujuanIrsMahasiswa(String token, String npm, IrsMahasiswa irsMahasiswa);
}
