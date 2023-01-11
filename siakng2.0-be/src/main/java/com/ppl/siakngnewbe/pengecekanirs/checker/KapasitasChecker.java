package com.ppl.siakngnewbe.pengecekanirs.checker;

import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.pengecekanirs.result.KapasitasResult;

public class KapasitasChecker {
    public KapasitasResult check(KelasIrs kelasIrs) {
        var kelas = kelasIrs.getKelas();

        var result = new KapasitasResult(kelas);
        result.setPosisi(kelasIrs.getPosisi());
        result.setKapasitasTotal(kelas.getKapasitasTotal());
        result.setOk(kelasIrs.getPosisi() <= kelas.getKapasitasTotal());
        return result;
    }
}
