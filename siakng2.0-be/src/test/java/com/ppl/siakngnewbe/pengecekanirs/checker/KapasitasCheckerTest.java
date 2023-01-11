package com.ppl.siakngnewbe.pengecekanirs.checker;

import static org.assertj.core.api.Assertions.assertThat;

import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.pengecekanirs.result.KapasitasResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KapasitasCheckerTest {
    private KapasitasChecker checker;

    private MataKuliah mataKuliah;
    private Kelas kelas;
    private KelasIrs kelasIrs;
    private KapasitasResult expected;

    @BeforeEach
    void setUpEach() {
        checker = new KapasitasChecker();

        mataKuliah = new MataKuliah();
        mataKuliah.setNama("Test A");
        mataKuliah.setId("CSGE000000");

        kelas = new Kelas();
        kelas.setKapasitasTotal(50);
        kelas.setNama("Kelas Test A");
        kelas.setMataKuliah(mataKuliah);
        
        kelasIrs = new KelasIrs();
        kelasIrs.setKelas(kelas);
        
        expected = new KapasitasResult(kelas);
        expected.setKapasitasTotal(50);
    }
    
    @Test
    void testCheckOk() throws Exception {
        kelasIrs.setPosisi(25);
 
        expected.setPosisi(25);
        expected.setOk(true);
 
        assertThat(checker.check(kelasIrs)).usingRecursiveComparison().isEqualTo(expected);
    }
    
    @Test
    void testCheckNotOk() throws Exception {
        kelasIrs.setPosisi(100);
        
        expected.setPosisi(100);
        expected.setOk(false);
        
        assertThat(checker.check(kelasIrs)).usingRecursiveComparison().isEqualTo(expected);
    }
}