package com.ppl.siakngnewbe.matakuliah;

import com.ppl.siakngnewbe.rest.BaseResponse;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/mataKuliah")
@AllArgsConstructor
public class MataKuliahController {

    @Autowired
    private MataKuliahService mataKuliahService;

    @GetMapping(path = "/getAll", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object>  getMataKuliah(@RequestHeader("Authorization") String token, @RequestParam("periode") String periode, @RequestParam("type") String type) throws JsonProcessingException {
        BaseResponse<Object> response = new BaseResponse<>();
        String listMataKuliah = mataKuliahService.getMataKuliah(token, periode, type);
        if(listMataKuliah.equals("gagal")) {
            response.setStatus(500);
            response.setMessage("bukan masa pengisian");
        }
        else {
            response.setStatus(200);
            response.setMessage("sukses");
            response.setResult(listMataKuliah);
        }
        return response;
    }

    @GetMapping(path = "/getTaken", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getMataKuliahTaken(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("sukses");
        response.setResult(mataKuliahService.getMataKuliahTaken(token));
        return response;
    }
}