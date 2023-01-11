package com.ppl.siakngnewbe.mahasiswa;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MahasiswaDTO {
    String namaLengkap;
    String username;
    String password;
    String npm;
    String statusAkademik;
    float ipk;
    String urlFoto;
}
