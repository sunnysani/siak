package com.ppl.siakngnewbe.pembayaran;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.TOKEN_PREFIX;

@Service
public class PembayaranService {

    @Autowired
    private PembayaranRepository pembayaranRepository;

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

    public List<Map<String, Object>> getPembayaran(String token) {
        Mahasiswa mahasiswa = decodeTokenMahasiswa(token);
        List<Pembayaran> listPembayaran = pembayaranRepository.findByMahasiswaOrderBySemester(mahasiswa);
        List<Map<String, Object>> data = new ArrayList<>();

        for (Pembayaran pembayaran : listPembayaran) {
            Map<String, Object> eachData = new HashMap<>();
            eachData.put("Tahun Ajaran", pembayaran.getTahunAjaran().getNama());
            eachData.put("Term", pembayaran.getTahunAjaran().getTerm());
            eachData.put("Mata Uang", pembayaran.getMataUang());
            eachData.put("Tagihan", pembayaran.getTagihan());
            eachData.put("Tunggakan", pembayaran.getTunggakan());
            eachData.put("Denda", pembayaran.getDenda());

            int totalTagihan = pembayaran.getTagihan() + pembayaran.getTunggakan() + pembayaran.getDenda();

            eachData.put("Total Tagihan", totalTagihan);
            eachData.put("Total Pembayaran", pembayaran.getTotalDibayar());

            int sisaTagihan = totalTagihan - pembayaran.getTotalDibayar();

            eachData.put("Sisa Tagihan", sisaTagihan);
            eachData.put("Status", pembayaran.getStatus());
            eachData.put("Deadline", pembayaran.getDeadline());

            data.add(eachData);
        }

        return data;
    }
}
