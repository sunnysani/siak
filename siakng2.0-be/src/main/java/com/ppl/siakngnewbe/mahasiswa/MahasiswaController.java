package com.ppl.siakngnewbe.mahasiswa;

import com.ppl.siakngnewbe.rest.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mahasiswa")
@AllArgsConstructor
public class MahasiswaController {

    @Autowired
    private MahasiswaServiceImpl mahasiswaService;

    @GetMapping(path = "/detail", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getMahasiswa(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("sukses");
        response.setResult(mahasiswaService.getMahasiswaByUsername(token));
        return response;
    }
}
