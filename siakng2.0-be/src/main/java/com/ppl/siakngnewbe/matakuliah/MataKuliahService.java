package com.ppl.siakngnewbe.matakuliah;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaService;
import com.ppl.siakngnewbe.kelas.KelasRepository;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaran;
import com.ppl.siakngnewbe.tahunajaran.TahunAjaranService;

import com.ppl.siakngnewbe.tahunajaran.TahunAjaranStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MataKuliahService {

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private IrsMahasiswaRepository irsMahasiswaRepository;

    @Autowired
    private KelasRepository kelasRepository;

    @Autowired
    private TahunAjaranService tahunAjaranService;

    @Autowired
    private IrsMahasiswaService irsMahasiswaService;

    private String matkulToJson(MataKuliah matkul, boolean eligible) throws JsonProcessingException {

        String jsonStr = new ObjectMapper().writeValueAsString(matkul);
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1) + String.format(",\"eligible\":%s},", eligible ? "1" : "0");

        return jsonStr;
    }

    private boolean isOnStatus(TahunAjaran tahunAjaran, String type) {
        var onStatus = false;
        if ((tahunAjaran != null) && ((type.equals("isi") && tahunAjaran.getStatus().equals(TahunAjaranStatus.IRS_ISI)) || (type.equals("add-drop") && tahunAjaran.getStatus().equals(TahunAjaranStatus.IRS_ADD_DROP)))) {
            onStatus = true;
        }
        return onStatus;
    }

    private boolean checkIfEligible(Set<MataKuliah> matkulTaken, Set<MataKuliah> prasyarats) {
        for (MataKuliah prasyarat: prasyarats) {
            if (!matkulTaken.contains(prasyarat)) {
                return false;
            }
        }
        return true;
    }

    public String getMataKuliah(String token, String periode, String type) throws JsonProcessingException {
        TahunAjaran tahunAjaran = tahunAjaranService.getTahunAjaran(periode);
        if (isOnStatus(tahunAjaran, type)) {
            Set<MataKuliah> matkulTaken = new HashSet<>(getMataKuliahTaken(token));

            var matkuls = new StringBuilder("[");
            for (MataKuliah matkul: mataKuliahRepository.findAllByOrderByNamaAsc()) {
                if (matkul.getTahunAjaran().equals(tahunAjaran)) {
                    Set<MataKuliah> prasyarats = matkul.getPrasyaratMataKuliahSet() != null ? matkul.getPrasyaratMataKuliahSet() : new HashSet<>();
                    boolean eligible = checkIfEligible(matkulTaken, prasyarats);
                    matkul.setKelasSet(kelasRepository.findByMataKuliahOrderByNamaAsc(matkul));
                    matkuls.append(matkulToJson(matkul, eligible));

                }
            }
            if (matkuls.length() > 1) {
                matkuls.deleteCharAt(matkuls.length() - 1);
            }
            matkuls.append("]");

            return matkuls.toString();
        }
        return "gagal";
    }

    public List<MataKuliah> getMataKuliahTaken(String token) {

        List<IrsMahasiswa> irss = irsMahasiswaRepository.findAll();
        List<IrsMahasiswa> irsTaken = new ArrayList<>();
        Set<MataKuliah> matkulTaken = new HashSet<>();
        var latestIrs = irsMahasiswaService.getIrs(token);

        for (IrsMahasiswa irs: irss) {
            if (irs.getMahasiswa().equals(latestIrs.getMahasiswa()) && irs.getSemester() < latestIrs.getSemester()) {
                irsTaken.add(irs);
            }
        }

        for (IrsMahasiswa irs: irsTaken) {
            for (KelasIrs kelasIrs: irs.getKelasIrsSet()) {
                var mataKuliah = kelasIrs.getKelas().getMataKuliah();
                matkulTaken.add(mataKuliah);
            }
        }

        return new ArrayList<>(matkulTaken);
    }

}