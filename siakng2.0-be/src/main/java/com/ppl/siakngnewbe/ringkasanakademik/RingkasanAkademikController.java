package com.ppl.siakngnewbe.ringkasanakademik;

import com.ppl.siakngnewbe.rest.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/ringkasan-akademik")
public class RingkasanAkademikController {
    @Autowired
    private RingkasanAkademikServiceImpl ringkasanAkademikService;

    private static final String SUCCESS = "sukses";
    private static final String NOT_FOUND = "tidak ditemukan";

    @GetMapping(path = "/irs", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object>
    getMappingStatistikIrs(@RequestHeader("Authorization") String token) {
        Map<Integer, Map<String, Object>> mapStatistikIrs = ringkasanAkademikService.getMappingStatistikIrs(token);
        BaseResponse<Object> response = new BaseResponse<>();

        if (mapStatistikIrs == null) {
            response.setStatus(404);
            response.setMessage(NOT_FOUND);
        } else {
            response.setStatus(200);
            response.setMessage(SUCCESS);
            response.setResult(mapStatistikIrs);
        }

        return response;
    }

    @GetMapping(path = "/mahasiswa", produces = {"application/json"})
    @ResponseBody public BaseResponse<Object>
    getMappingDetailMahasiswa(@RequestHeader("Authorization") String token) {
        Map<String, Object> mapMahasiswa = ringkasanAkademikService.getMappingDetailMahasiswa(token);
        BaseResponse<Object> response = new BaseResponse<>();

        if (mapMahasiswa == null) {
            response.setStatus(404);
            response.setMessage(NOT_FOUND);
        } else {
            response.setStatus(200);
            response.setMessage(SUCCESS);
            response.setResult(mapMahasiswa);
        }

        return response;
    }

    @GetMapping(path = "/nilai", produces = {"application/json"})
    @ResponseBody public BaseResponse<Object>
    getMappingNilai(@RequestHeader("Authorization") String token) {
        Map<String, Integer> mapNilai = ringkasanAkademikService.getMappingNilai(token);
        BaseResponse<Object> response = new BaseResponse<>();

        if (mapNilai == null) {
            response.setStatus(404);
            response.setMessage(NOT_FOUND);
        } else {
            response.setStatus(200);
            response.setMessage(SUCCESS);
            response.setResult(mapNilai);
        }

        return response;
    }

    @GetMapping(path = "/ip", produces = {"application/json"})
    @ResponseBody public BaseResponse<Object>
    getMappingIp(@RequestHeader("Authorization") String token) {
        Map<String, List<Double>> mapIp = ringkasanAkademikService.getMappingIP(token);
        BaseResponse<Object> response = new BaseResponse<>();

        if (mapIp == null) {
            response.setStatus(404);
            response.setMessage(NOT_FOUND);
        } else {
            response.setStatus(200);
            response.setMessage(SUCCESS);
            response.setResult(mapIp);
        }

        return response;
    }
}