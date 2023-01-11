package com.ppl.siakngnewbe.dosen;

import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.rest.BaseResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/dosen")
@AllArgsConstructor
public class DosenController {
    
    @Autowired
    private DosenService dosenService;

    private static final String MESSAGES = "sukses";

    @PostMapping(path = "/pa/{npm}",produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> addPA(@RequestHeader("Authorization") String token, @PathVariable(value = "npm") String npm) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(dosenService.addPembimbingAkademik(token, npm));
        return response;
    }

    @GetMapping(path = "/all", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getAllDosen() {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(dosenService.getAllDosen());
        return response;
    }

    @GetMapping(path = "/profile", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getDosenByToken(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(dosenService.getDosenById(token));
        return response;
    }

    @GetMapping(path = "/bimbingan", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getMahasiswaBimbingan(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(dosenService.getMahasiswaBimbingan(token));
        return response;
    }

    @PostMapping(path = "/setuju-irs/{npm}", produces = {"application/json"}) 
    @ResponseBody
    public BaseResponse<Object> setPersetujuanIRSMahasiswa(@RequestHeader("Authorization") String token, @PathVariable(value = "npm") String npm, @RequestBody IrsMahasiswa irsMahasiswa ) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(dosenService.setPersetujuanIrsMahasiswa(token, npm, irsMahasiswa));
        return response;
    }
}
