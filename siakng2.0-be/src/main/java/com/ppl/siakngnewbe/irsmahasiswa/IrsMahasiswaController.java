package com.ppl.siakngnewbe.irsmahasiswa;

import com.ppl.siakngnewbe.rest.BaseResponse;
import lombok.AllArgsConstructor;

import java.util.List;

import com.ppl.siakngnewbe.kelas.Kelas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/irsMahasiswa")
@AllArgsConstructor
public class IrsMahasiswaController {

    @Autowired
    private IrsMahasiswaService irsService;

    @GetMapping(path = "/get", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getIrs(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("sukses");
        response.setResult(irsService.getIrs(token));
        return response;
    }

    @PostMapping(path = "/post", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> postIrs(@RequestHeader("Authorization") String token, @RequestBody(required = false) List<Kelas> kelass) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("sukses");
        response.setResult(irsService.postIrs(kelass, token));
        return response;
    }

}