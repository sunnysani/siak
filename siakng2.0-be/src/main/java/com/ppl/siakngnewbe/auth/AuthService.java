package com.ppl.siakngnewbe.auth;

import com.ppl.siakngnewbe.dosen.DosenDTO;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaDTO;

public interface AuthService {
    Mahasiswa registerMahasiswa(MahasiswaDTO mahasiswaDto) throws IllegalAccessException;
    Dosen registerDosen(DosenDTO dosenDTO) throws IllegalAccessException;
}
