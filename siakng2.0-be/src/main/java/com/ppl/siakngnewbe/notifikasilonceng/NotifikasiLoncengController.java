package com.ppl.siakngnewbe.notifikasilonceng;

import com.ppl.siakngnewbe.rest.BaseResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/notifikasi-lonceng")
@AllArgsConstructor
public class NotifikasiLoncengController {
    
    @Autowired
    private NotifikasiLoncengService notifikasiLoncengService;

    private static final String MESSAGES = "sukses";

    @GetMapping(path = "",produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getAllNotifikasiForMahasiswa(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(notifikasiLoncengService.getNotifikasiForMahasiswa(token));
        return response;
    }

    @PostMapping(path = "/read", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> setReadNotifByMahasiswa(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(notifikasiLoncengService.readAllNotifikasiByMahasiswa(token));
        return response;
    }
}
