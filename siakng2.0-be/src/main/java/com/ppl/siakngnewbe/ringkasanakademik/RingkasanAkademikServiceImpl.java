package com.ppl.siakngnewbe.ringkasanakademik;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.TOKEN_PREFIX;

@Service
public class RingkasanAkademikServiceImpl {
    @Autowired
    private IrsMahasiswaRepository irsMahasiswaRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    public Mahasiswa decodeTokenMahasiswa(String token) {
        DecodedJWT decodedJWT = JWT
                .require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));

        String npm = decodedJWT.getClaim("npm").toString().replace("\"", "");
        return mahasiswaRepository.findByNpm(npm);
    }

    public int getSksL(Mahasiswa mahasiswa) {
        List<IrsMahasiswa>  listIrs = irsMahasiswaRepository.findByMahasiswaOrderBySemester(mahasiswa);
        int sksL = 0;
        for (IrsMahasiswa irs : listIrs) {
            sksL += irs.getSksl();
        }
        return sksL;
    }

    public double getTotalMutu(Mahasiswa mahasiswa) {
        List<IrsMahasiswa>  listIrs = irsMahasiswaRepository.findByMahasiswaOrderBySemester(mahasiswa);
        double totalMutu = 0;
        for (IrsMahasiswa irs : listIrs) {
            totalMutu += irs.getTotalMutu();
        }
        return totalMutu;
    }

    public double getIpk(Mahasiswa mahasiswa) {
        int sksL = getSksL(mahasiswa);
        return sksL == 0 ? 0 : getTotalMutu(mahasiswa) / sksL;
    }

    public Map<Integer, Map<String, Object>> getMappingStatistikIrs(String token) {
        Mahasiswa mahasiswa = decodeTokenMahasiswa(token);
        List<IrsMahasiswa>  listIrs = irsMahasiswaRepository.findByMahasiswaOrderBySemester(mahasiswa);

        Map<Integer, Map<String, Object>> mappingStatistikIrs = new HashMap<>();
        Map<String, Object> detailIrs;
        double sksAKumulatif = 0;
        double sksLKumulatif = 0;
        double totalMutuKumulatif = 0;

        for (IrsMahasiswa irs : listIrs) {
            detailIrs = new HashMap<>();
            int semester = irs.getSemester();

            detailIrs.put("Periode Tahun", irs.getTahunAjaran().getNama());
            detailIrs.put("Term", irs.getTahunAjaran().getTerm());
            detailIrs.put("Nilai Defined", irs.isNilaiDefined());
            detailIrs.put("MK", irs.getKelasIrsSet().size());
            detailIrs.put("SKSA Semester", irs.getSksa());
            detailIrs.put("SKSL Semester", irs.getSksl());
            detailIrs.put("Total Mutu", irs.getTotalMutu());
            detailIrs.put("IP", irs.getIps());

            sksAKumulatif += irs.getSksa();
            detailIrs.put("SKSA Kumulatif", sksAKumulatif);

            sksLKumulatif += irs.getSksl();
            detailIrs.put("SKSL Kumulatif", sksLKumulatif);

            totalMutuKumulatif += irs.getTotalMutu();
            detailIrs.put("Total Mutu Kumulatif", totalMutuKumulatif);

            double ipt = totalMutuKumulatif / sksAKumulatif;
            detailIrs.put("IPT", ipt);

            double ipk = sksLKumulatif == 0 ? 0 : totalMutuKumulatif / sksLKumulatif;
            detailIrs.put("IPK", ipk);

            mappingStatistikIrs.put(semester, detailIrs);
        }

        return mappingStatistikIrs;
    }

    public Map<String, Object> getMappingDetailMahasiswa(String token){
        Mahasiswa mahasiswa = decodeTokenMahasiswa(token);

        IrsMahasiswa irsTerbaru = irsMahasiswaRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa);
        Map<String, Object> mappingDetailMahasiswa = new HashMap<>();

        mappingDetailMahasiswa.put("Nama", mahasiswa.getNamaLengkap());
        mappingDetailMahasiswa.put("Program Studi", mahasiswa.getProgramStudi().toString());
        mappingDetailMahasiswa.put("NPM", mahasiswa.getNpm());
        mappingDetailMahasiswa.put("IPS", irsTerbaru.getIps());
        mappingDetailMahasiswa.put("IPK", getIpk(mahasiswa));
        mappingDetailMahasiswa.put("SKS Lulus", getSksL(mahasiswa));
        mappingDetailMahasiswa.put("Total Mutu", getTotalMutu(mahasiswa));
        mappingDetailMahasiswa.put("Tahun Ajaran", irsTerbaru.getTahunAjaran().getNama());
        mappingDetailMahasiswa.put("Semester", irsTerbaru.getSemester());
        mappingDetailMahasiswa.put("Status Akademik", mahasiswa.getStatus());
        mappingDetailMahasiswa.put("Pembimbing Akademik", mahasiswa.getPembimbingAkademik().getNamaLengkap());
        mappingDetailMahasiswa.put("NIP Pembimbing Akademik", mahasiswa.getPembimbingAkademik().getNip());

        return mappingDetailMahasiswa;
    }

    public Map<String, Integer> getMappingNilai(String token) {
        Mahasiswa mahasiswa = decodeTokenMahasiswa(token);
        List<IrsMahasiswa> listIrs = irsMahasiswaRepository.findByMahasiswaOrderBySemester(mahasiswa);
        Map<String, Integer> mappingNilai = new HashMap<>();

        for (IrsMahasiswa irs : listIrs) {
            Set<KelasIrs> setKelas = irs.getKelasIrsSet();

            for (KelasIrs kelasIrs : setKelas) {
                var nilaiHuruf = kelasIrs.getNilaiHuruf();
                nilaiHuruf = nilaiHuruf == null ? "U" : nilaiHuruf;

                if (!mappingNilai.containsKey(nilaiHuruf)) {
                    mappingNilai.put(nilaiHuruf, 1);
                } else {
                    int newcount = mappingNilai.get(nilaiHuruf) + 1;
                    mappingNilai.replace(nilaiHuruf, newcount);
                }
            }
        }
        return mappingNilai;
    }

    public Map<String, List<Double>> getMappingIP(String token) {
        Mahasiswa mahasiswa = decodeTokenMahasiswa(token);
        List<IrsMahasiswa> listIrs = irsMahasiswaRepository.findByMahasiswaOrderBySemester(mahasiswa);
        Map<String, List<Double>> mappingIP = new HashMap<>();
        List<Double> ipsList = new ArrayList<>();
        List<Double> ipkList = new ArrayList<>();
        double totalMutuKumulatif = 0;
        double sksLKumulatif = 0;
        for (IrsMahasiswa irs : listIrs) {
            ipsList.add(irs.getIps());

            totalMutuKumulatif += irs.getTotalMutu();
            sksLKumulatif += irs.getSksl();
            double ipk = sksLKumulatif == 0 ? 0 : totalMutuKumulatif / sksLKumulatif;
            ipkList.add(ipk);
        }
        mappingIP.put("IP", ipsList);
        mappingIP.put("IPK", ipkList);

        return mappingIP;
    }
}
