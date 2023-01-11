package com.ppl.siakngnewbe.pembayaran;

import com.ppl.siakngnewbe.rest.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "pembayaran")
public class PembayaranController {

    @Autowired
    PembayaranService pembayaranService;

    @GetMapping(path = "/getPembayaran", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getPembayaran(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        List<Map<String, Object>> pembayaranData = pembayaranService.getPembayaran(token);

        if (pembayaranData == null) {
            response.setStatus(404);
            response.setMessage("gagal");
        } else {
            response.setStatus(200);
            response.setMessage("sukses");
            response.setResult(pembayaranData);
        }
        return response;
    }

}
