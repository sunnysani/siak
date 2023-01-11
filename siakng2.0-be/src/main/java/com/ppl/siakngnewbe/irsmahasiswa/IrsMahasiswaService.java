package com.ppl.siakngnewbe.irsmahasiswa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.kelas.KelasRepository;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.kelasirs.KelasIrsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.TOKEN_PREFIX;

@Service
public class IrsMahasiswaService {

    @Autowired
    private IrsMahasiswaRepository irsMahasiswaRepository;

    @Autowired
    private KelasIrsRepository kelasIrsRepository;

    @Autowired
    private KelasRepository kelasRepository;

    private String decodeToken(String token) {
        DecodedJWT verifier = JWT
                .require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));

        return verifier.getClaim("npm").asString();
    }

    public IrsMahasiswa getIrs(String token) {
        String npm = decodeToken(token);
        IrsMahasiswa latestIrs = null;
        for (IrsMahasiswa irs: irsMahasiswaRepository.findAll()) {
            if (irs.getMahasiswa().getNpm().equals(npm) && (latestIrs == null || latestIrs.getSemester() < irs.getSemester())) {
                latestIrs = irs;
            }
        }
        return latestIrs;
    }

    private Set<KelasIrs> postIrsAdd(List<Kelas> kelass, Set<KelasIrs> kelasIrss, Map<String, Boolean> mapKelasIdFrom, IrsMahasiswa latestIrs) {

        Set<KelasIrs> kelasIrsAdded = new HashSet<>(kelasIrss);

        for (Kelas kelas: kelass) {
            if (!mapKelasIdFrom.containsKey(kelas.getId())) {
                Kelas kelasUpdate = kelasRepository.getById(kelas.getId());
                int kapasitasNow = kelasUpdate.getKapasitasSaatIni();
                kelasUpdate.setKapasitasSaatIni(kapasitasNow + 1);

                var kelasIrs = new KelasIrs();
                kelasIrs.setPosisi(kapasitasNow + 1);
                kelasIrs.setIrs(latestIrs);
                kelasIrs.setKelas(kelasUpdate);

                Set<KelasIrs> kelasIrsSet = kelasUpdate.getKelasIrsSet() != null ? kelasUpdate.getKelasIrsSet() : new HashSet<>();
                kelasIrsSet.add(kelasIrs);
                kelasUpdate.setKelasIrsSet(kelasIrsSet);

                kelasIrsSet = latestIrs.getKelasIrsSet() != null ? latestIrs.getKelasIrsSet() : new HashSet<>();
                kelasIrsSet.add(kelasIrs);
                latestIrs.setKelasIrsSet(kelasIrsSet);
                latestIrs.setSksa(latestIrs.getSksa() + kelasUpdate.getMataKuliah().getSks());

                kelasRepository.save(kelasUpdate);
                kelasIrsRepository.save(kelasIrs);
                irsMahasiswaRepository.save(latestIrs);

                kelasIrsAdded.add(kelasIrs);
            }
        }

        return kelasIrsAdded;
    }

    private Set<KelasIrs> postIrsDrop(Set<KelasIrs> kelasIrss, Map<String, Boolean> mapKelasIdTo, IrsMahasiswa latestIrs) {

        Set<KelasIrs> kelasIrsDropped = new HashSet<>(kelasIrss);

        for (KelasIrs kelasIrs: kelasIrss) {
            if (!mapKelasIdTo.containsKey(kelasIrs.getKelas().getId())) {
                Kelas kelasUpdate = kelasRepository.getById(kelasIrs.getKelas().getId());
                int kapasitasNow = kelasUpdate.getKapasitasSaatIni();
                kelasUpdate.setKapasitasSaatIni(kapasitasNow - 1);
                int posisi = kelasIrs.getPosisi();

                Set<KelasIrs> kelasIrsSet = kelasUpdate.getKelasIrsSet();
                kelasIrsSet.remove(kelasIrs);
                kelasUpdate.setKelasIrsSet(kelasIrsSet);

                kelasIrsSet = latestIrs.getKelasIrsSet();
                kelasIrsSet.remove(kelasIrs);
                latestIrs.setKelasIrsSet(kelasIrsSet);
                latestIrs.setSksa(latestIrs.getSksa() - kelasUpdate.getMataKuliah().getSks());

                kelasRepository.save(kelasUpdate);
                irsMahasiswaRepository.save(latestIrs);

                for (KelasIrs kelasIrsUpdate: kelasIrsRepository.findAll()) {
                    if (kelasIrsUpdate.getKelas().equals(kelasIrs.getKelas()) && kelasIrsUpdate.getPosisi() > posisi) {
                        kelasIrsUpdate.setPosisi(kelasIrsUpdate.getPosisi() - 1);
                        kelasIrsRepository.save(kelasIrsUpdate);
                    }
                }
                kelasIrsRepository.delete(kelasIrs);
                kelasIrsDropped.remove(kelasIrs);
            }
        }
        return kelasIrsDropped;
    }

    public Set<KelasIrs> postIrs(List<Kelas> kelass, String token) {
        IrsMahasiswa latestIrs = getIrs(token);
        Set<KelasIrs> kelasIrss = latestIrs.getKelasIrsSet();
        Map<String, Boolean> mapKelasIdFrom = new HashMap<>();
        Map<String, Boolean> mapKelasIdTo = new HashMap<>();

        kelasIrss = kelasIrss != null ? kelasIrss : new HashSet<>();

        for (KelasIrs kelasIrs: kelasIrss) {
            mapKelasIdFrom.put(kelasIrs.getKelas().getId(), true);
        }
        for (Kelas kelas: kelass) {
            mapKelasIdTo.put(kelas.getId(), true);
        }

        kelasIrss = postIrsAdd(kelass, kelasIrss, mapKelasIdFrom, latestIrs);
        kelasIrss = postIrsDrop(kelasIrss, mapKelasIdTo, latestIrs);

        return kelasIrss;
    }

}
