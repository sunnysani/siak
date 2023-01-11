package com.ppl.siakngnewbe.kelasIrs;

import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.kelas.Kelas;

import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KelasIrsModelTest {

    private KelasIrs kelasIrs = new KelasIrs();
    private IrsMahasiswa irs = new IrsMahasiswa();
    private Kelas kelas = new Kelas();

    @BeforeEach
    public void setUp() {

        kelasIrs.setId(1);
        kelasIrs.setIrs(irs);
        kelasIrs.setKelas(kelas);
        kelasIrs.setPosisi(1);

    }

    @Test
    void modelGetId() {
        assertEquals(1, kelasIrs.getId());
    }

    @Test
    void modelGeIrs() {
        assertEquals(irs, kelasIrs.getIrs());
    }

    @Test
    void modelGetKelas() {
        assertEquals(kelas, kelasIrs.getKelas());
    }

    @Test
    void modelGetPosisi() {
        assertEquals(1, kelasIrs.getPosisi());
    }

}
