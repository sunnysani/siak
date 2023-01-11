package com.ppl.siakngnewbe.pengumuman;

import com.ppl.siakngnewbe.rest.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/pengumuman")
public class PengumumanController {

    @Autowired
    private PengumumanService pengumumanService;

    @GetMapping(path = "", produces = {"application/json"})
    @ResponseBody public BaseResponse<Object> getPengumumanAll() {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("sukses");
        response.setResult(pengumumanService.getPengumumanAll());
        return response;
    }

    @GetMapping(path = "/{id}", produces = {"application/json"})
    @ResponseBody public BaseResponse<Object> getPengumumanById(@PathVariable(value = "id") long id) {
        PengumumanModel result = pengumumanService.getPengumumanById(id);
        BaseResponse<Object> response = new BaseResponse<>();

        if (result == null) {
            response.setMessage("tidak ditemukan");
            response.setStatus(404);
        }
        else {
            response.setStatus(200);
            response.setMessage("sukses");
            response.setResult(result);
        }
        return response;
    }
}
