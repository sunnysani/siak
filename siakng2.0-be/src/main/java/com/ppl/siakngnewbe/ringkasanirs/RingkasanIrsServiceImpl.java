package com.ppl.siakngnewbe.ringkasanirs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.kelasirs.KelasIrsRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RingkasanIrsServiceImpl implements RingkasanIrsService {
    @Autowired
    private IrsMahasiswaRepository irsRepository;

    @Autowired
    private KelasIrsRepository kelasIrsRepository;

    public IrsMahasiswa getLatestDetailIrs(Mahasiswa mahasiswa) {
        List<IrsMahasiswa> listIrs = irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa);
        if(listIrs == null) return null;

        return listIrs.get(0);
    }

    public List<Kelas> getLatestMatkulDipilih(Mahasiswa mahasiswa) {
        List<IrsMahasiswa> listIrs = irsRepository.findByMahasiswaOrderBySemesterDesc(mahasiswa);
        if(listIrs == null) return Collections.emptyList();

        IrsMahasiswa irs = listIrs.get(0);
        List<KelasIrs> listKelasIrs = kelasIrsRepository.findByIrs(irs);
        if(listKelasIrs == null) return Collections.emptyList();
        
        List<Kelas> result = new ArrayList<>();
        for(KelasIrs kelasIrs : listKelasIrs){
            result.add(kelasIrs.getKelas());
        }

        return result;
    }
}
