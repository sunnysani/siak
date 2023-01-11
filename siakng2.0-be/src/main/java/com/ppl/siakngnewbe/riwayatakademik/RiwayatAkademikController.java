package com.ppl.siakngnewbe.riwayatakademik;

import com.ppl.siakngnewbe.rest.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "riwayat-akademik")
public class RiwayatAkademikController {
    @Autowired
    RiwayatAkademikServiceImpl riwayatAkademikService;

    private static final String SUCCESS = "sukses";
    private static final String NOT_FOUND = "tidak ditemukan";

    @GetMapping(path = "/irs", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getMappingRiwayatIrs(@RequestHeader("Authorization") String token) {
        Map<Integer, Map<String, Object>> mapRiwayatIrs = riwayatAkademikService.getMappingRiwayatIrs(token);
        BaseResponse<Object> response = new BaseResponse<>();

        if (mapRiwayatIrs == null) {
            response.setStatus(404);
            response.setMessage(NOT_FOUND);
        } else {
            response.setStatus(200);
            response.setMessage(SUCCESS);
            response.setResult(mapRiwayatIrs);
        }

        return response;
    }

    @GetMapping(path = "/nilai/{idKelasIrs}", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getMappingDetailNilai(@PathVariable(value = "idKelasIrs") long id) {
        Map<String, Map<String, Double>> mapDetailNilai = riwayatAkademikService.getMappingDetailNilai(id);
        BaseResponse<Object> response = new BaseResponse<>();

        if (mapDetailNilai == null) {
            response.setStatus(404);
            response.setMessage(NOT_FOUND);
        } else {
            response.setStatus(200);
            response.setMessage(SUCCESS);
            response.setResult(mapDetailNilai);
        }

        return response;
    }
}
