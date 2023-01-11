package com.ppl.siakngnewbe.pengecekanirs;

import java.util.List;
import java.util.Map;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaModelRepository;
import com.ppl.siakngnewbe.pengecekanirs.checker.IpsOutOfBoundException;
import com.ppl.siakngnewbe.pengecekanirs.result.JadwalResult;
import com.ppl.siakngnewbe.pengecekanirs.result.KapasitasResult;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratResult;
import com.ppl.siakngnewbe.pengecekanirs.result.SksResult;
import com.ppl.siakngnewbe.rest.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.TOKEN_PREFIX;

@RestController
@RequestMapping(path = "/pengecekan-irs")
public class PengecekanIrsController {
    @Autowired
    private MahasiswaModelRepository mahasiswaRepository;

    @Autowired
    private PengecekanIrsService pengecekanIrsService;

    private static final String MESSAGES_SUCCESS = "sukses";
    private static final String MESSAGES_KOSONG = "kosong";

    public Mahasiswa getCurrentMahasiswa(String token) {
        DecodedJWT verifier = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(token.replace(TOKEN_PREFIX, ""));
        var npm = verifier.getClaim("npm").asString();
        return mahasiswaRepository.findByNpm(npm);
    }

    @GetMapping(value = "/cek-kapasitas")
    public BaseResponse<Object> cekKapasitas(@RequestHeader("Authorization") String token) {
        var mahasiswa = getCurrentMahasiswa(token);

        List<KapasitasResult> kapasitasResults = pengecekanIrsService.checkKapasitasOfLatestIrs(mahasiswa);
        BaseResponse<Object> response = new BaseResponse<>();
        if(kapasitasResults.isEmpty()) {
            response.setStatus(204);
            response.setMessage(MESSAGES_KOSONG);
        }
        else {
            response.setStatus(200);
            response.setMessage(MESSAGES_SUCCESS);
            response.setResult(Map.of("data", kapasitasResults));
        }
        return response;

    }

    @GetMapping(value = "/cek-jadwal")
    public BaseResponse<Object> cekJadwal(@RequestHeader("Authorization") String token) {
        var mahasiswa = getCurrentMahasiswa(token);

        List<JadwalResult> jadwalResults = pengecekanIrsService.checkJadwalOfLatestIrs(mahasiswa);
        BaseResponse<Object> response = new BaseResponse<>();
        if(jadwalResults.isEmpty()) {
            response.setStatus(204);
            response.setMessage(MESSAGES_KOSONG);
        }
        else {
            response.setStatus(200);
            response.setMessage(MESSAGES_SUCCESS);
            response.setResult(Map.of("data", jadwalResults));
        }
        return response;
    }

    @GetMapping(value = "/cek-sks")
    public BaseResponse<Object> cekSks(@RequestHeader("Authorization") String token) throws IpsOutOfBoundException {
        var mahasiswa = getCurrentMahasiswa(token);
        SksResult result;
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            result = pengecekanIrsService.checkSksOfLatestIrs(mahasiswa);
        }
        catch(IpsOutOfBoundException ex) {
            response.setStatus(500);
            response.setMessage("ips out of bound");
            return response;
        }
        if(result == null) {
            response.setStatus(204);
            response.setMessage(MESSAGES_KOSONG);
        }
        else {
            response.setStatus(200);
            response.setMessage(MESSAGES_SUCCESS);
            response.setResult(Map.of("data", result));
        }
        return response;
    }

    @GetMapping(value = "/cek-prasyarat")
    public BaseResponse<Object> cekPrasyarat(@RequestHeader("Authorization") String token) {
        var mahasiswa = getCurrentMahasiswa(token);

        List<PrasyaratResult> results = pengecekanIrsService.checkPrasyaratOfLatestIrs(mahasiswa);
        BaseResponse<Object> response = new BaseResponse<>();
        if(results.isEmpty()) {
            response.setStatus(204);
            response.setMessage(MESSAGES_KOSONG);
        }
        else {
            response.setStatus(200);
            response.setMessage(MESSAGES_SUCCESS);
            response.setResult(Map.of("data", results));
        }
        return response;
    }
}
