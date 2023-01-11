package com.ppl.siakngnewbe.tahunajaran;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ppl.siakngnewbe.rest.BaseResponse;
import lombok.AllArgsConstructor;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/tahunAjaran")
@AllArgsConstructor
public class TahunAjaranController {

    @Autowired
    private TahunAjaranService tahunAjaranService;

    private static final String MESSAGES_SUKSES = "sukses";
	private static final String LATEST = "latest";

    @PostMapping(path = "/", produces = { "application/json" })
    @ResponseBody
    public BaseResponse<Object> createTahunAjaran(@RequestHeader("Authorization") String token,
            @RequestBody TahunAjaran tahunAjaran) throws JsonProcessingException, SchedulerException {

        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES_SUKSES);
        response.setResult(tahunAjaranService.postTahunAjaran(token, tahunAjaran));
        return response;
    }

    @GetMapping(path = "/", produces = { "application/json" })
    @ResponseBody
    public BaseResponse<Object> getLatestTahunAjaran() throws JsonProcessingException {
        TahunAjaran latest = tahunAjaranService.getTahunAjaran(LATEST);
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES_SUKSES);
        response.setResult(latest);
        return response;
    }

    @GetMapping(path = "/getStatus", produces = { "application/json" })
    @ResponseBody
    public BaseResponse<Object> getStatus(@RequestHeader("Authorization") String token) throws JsonProcessingException {
        TahunAjaran latest = tahunAjaranService.getTahunAjaran(LATEST);
        String statusTA = tahunAjaranService.getTahunAjaranStatus(latest);
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES_SUKSES);
        response.setResult(Map.of("status", statusTA));
        return response;
    }

    @PostMapping(path = "/setStatus", produces = { "application/json" })
    @ResponseBody
    public BaseResponse<Object> setStatus(@RequestHeader("Authorization") String token,
            @RequestParam("status") String status) throws JsonProcessingException {
        TahunAjaran latest = tahunAjaranService.getTahunAjaran(LATEST);
        BaseResponse<Object> response = new BaseResponse<>();
        if (tahunAjaranService.setTahunAjaranStatus(token, latest, status)) {
            response.setStatus(200);
            response.setMessage(MESSAGES_SUKSES);
            response.setResult(null);
        } else {
            response.setStatus(500);
            response.setMessage("gagal");
            response.setResult(null);
        }
        return response;
    }
}