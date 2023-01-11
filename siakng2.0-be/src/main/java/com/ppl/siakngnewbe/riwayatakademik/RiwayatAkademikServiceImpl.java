
package com.ppl.siakngnewbe.riwayatakademik;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.kelasirs.KelasIrsRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaRepository;
import com.ppl.siakngnewbe.penilaian.KomponenPenilaian;
import com.ppl.siakngnewbe.penilaian.Nilai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.TOKEN_PREFIX;

@Service
public class RiwayatAkademikServiceImpl {
    @Autowired
    private KelasIrsRepository kelasIrsRepository;

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

    public Map<Integer, Map<String, Object>> getMappingRiwayatIrs(String token) {
        Mahasiswa mahasiswa = decodeTokenMahasiswa(token);
        List<IrsMahasiswa> listIrs = irsMahasiswaRepository.findByMahasiswaOrderBySemester(mahasiswa);

        Map<Integer, Map<String, Object>> mappingRiwayatIrs = new HashMap<>();
        Map<String, Object> detailIrs;

        for (IrsMahasiswa irs : listIrs) {
            detailIrs = new HashMap<>();
            int semester = irs.getSemester();

            detailIrs.put("Tahun Ajaran", irs.getTahunAjaran().getNama());
            detailIrs.put("Term", irs.getTahunAjaran().getTerm());
            detailIrs.put("SKSA", irs.getSksa());
            detailIrs.put("IPS", irs.getIps());

            Map<String, Object> mappingMataKuliah = new TreeMap<>();
            Map<String, Object> detailMatkul;

            for (KelasIrs kelasIrs : irs.getKelasIrsSet()) {
                detailMatkul = new HashMap<>();

                detailMatkul.put("ID Matkul", kelasIrs.getKelas().getMataKuliah().getId());
                detailMatkul.put("SKS", kelasIrs.getKelas().getSks());
                detailMatkul.put("Nilai Akhir", kelasIrs.getNilaiAkhir());
                detailMatkul.put("Nilai Huruf", kelasIrs.getNilaiHuruf());
                detailMatkul.put("ID Kelas IRS", kelasIrs.getId());

                mappingMataKuliah.put(kelasIrs.getKelas().getMataKuliah().getNama(), detailMatkul);
            }

            detailIrs.put("Mata Kuliah", mappingMataKuliah);

            mappingRiwayatIrs.put(semester, detailIrs);
        }

        return mappingRiwayatIrs;
    }

    public Map<String, Map<String, Double>> getMappingDetailNilai(long id) {
        KelasIrs kelasIrs = kelasIrsRepository.findById(id);
        List<Nilai> listNilai = kelasIrs.getNilaiList();
        Map<String, Map<String, Double>> mappingDetailNilai = new HashMap<>();
        Map<String, Double> mappingKomponenNilai;

        for (Nilai nilai : listNilai) {
            mappingKomponenNilai = new HashMap<>();
            KomponenPenilaian komponenPenilaian = nilai.getKomponenPenilaian();

            mappingKomponenNilai.put("Bobot", komponenPenilaian.getBobot());
            mappingKomponenNilai.put("Nilai", nilai.getAngka());

            mappingDetailNilai.put(komponenPenilaian.getNama(), mappingKomponenNilai);
        }

        return mappingDetailNilai;
    }
}
