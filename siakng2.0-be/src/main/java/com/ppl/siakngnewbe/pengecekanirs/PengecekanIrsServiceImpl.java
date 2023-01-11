package com.ppl.siakngnewbe.pengecekanirs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.kelasirs.KelasIrsRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.pengecekanirs.checker.IpsOutOfBoundException;
import com.ppl.siakngnewbe.pengecekanirs.checker.JadwalChecker;
import com.ppl.siakngnewbe.pengecekanirs.checker.KapasitasChecker;
import com.ppl.siakngnewbe.pengecekanirs.checker.PrasyaratChecker;
import com.ppl.siakngnewbe.pengecekanirs.checker.SksChecker;
import com.ppl.siakngnewbe.pengecekanirs.result.JadwalResult;
import com.ppl.siakngnewbe.pengecekanirs.result.KapasitasResult;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratResult;
import com.ppl.siakngnewbe.pengecekanirs.result.SksResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PengecekanIrsServiceImpl implements PengecekanIrsService {
    @Autowired
    IrsMahasiswaRepository irsMahasiswaRepository;

    @Autowired
    KelasIrsRepository kelasIrsRepository;

    public List<KapasitasResult> checkKapasitasOfLatestIrs(Mahasiswa mahasiswa) {
        var irs = irsMahasiswaRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa);
        var listKelasIrs = Optional.ofNullable(kelasIrsRepository.findByIrs(irs)).orElse(Collections.emptyList());

        var results = new ArrayList<KapasitasResult>();
        var checker = new KapasitasChecker();
        for(KelasIrs kelasIrs : listKelasIrs) {
            results.add(checker.check(kelasIrs));
        }

        return results;
    }

    public List<JadwalResult> checkJadwalOfLatestIrs(Mahasiswa mahasiswa) {
        var irs = irsMahasiswaRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa);
        var listKelasIrs = Optional.ofNullable(kelasIrsRepository.findByIrs(irs)).orElse(Collections.emptyList());

        var results = new ArrayList<JadwalResult>();
        var checker = new JadwalChecker();
        for(KelasIrs kelasIrs : listKelasIrs) {
            results.add(checker.check(kelasIrs, listKelasIrs));
        }
        return results;
    }

    public SksResult checkSksOfLatestIrs(Mahasiswa mahasiswa) throws IpsOutOfBoundException {
        var listIrs = irsMahasiswaRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa);
        if(listIrs == null) return null;

        var checker = new SksChecker();
        if(listIrs.size() == 1) {
            return checker.check(listIrs.get(0));
        }
        else if(listIrs.size() > 1) {
            return checker.check(listIrs.get(0), listIrs.get(1));
        }

        return null;
    }

    public List<PrasyaratResult> checkPrasyaratOfLatestIrs(Mahasiswa mahasiswa) {
        var listIrs = irsMahasiswaRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa);
        if(listIrs == null || listIrs.isEmpty()) return Collections.emptyList();

        var irsNow = listIrs.get(0);
        var listKelasIrsNow = kelasIrsRepository.findByIrs(irsNow);
        if(listKelasIrsNow == null || listKelasIrsNow.isEmpty()) return Collections.emptyList();

        var listKelasIrsBefore = kelasIrsRepository.findByIrsIn(listIrs.subList(1, listIrs.size()));
        var mataKuliahLulus = new TreeMap<String, MataKuliah>();
        for(KelasIrs kelasIrs : listKelasIrsBefore) {
            if(55.00 <= kelasIrs.getNilaiAkhir()){
                var mataKuliah = kelasIrs.getKelas().getMataKuliah();
                mataKuliahLulus.put(mataKuliah.getId(), mataKuliah);
            }
        }
        
        var checker = new PrasyaratChecker();
        var results = new ArrayList<PrasyaratResult>();
        for(KelasIrs kelasIrs : listKelasIrsNow) {
            var mataKuliahDiambil = kelasIrs.getKelas().getMataKuliah();
            results.add(checker.check(mataKuliahDiambil, mataKuliahLulus));
        }
        return results;
    }
}
