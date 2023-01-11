package com.ppl.siakngnewbe.auth;

import com.ppl.siakngnewbe.dosen.DosenDTO;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaDTO;
import com.ppl.siakngnewbe.rest.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/register/mahasiswa")
    public BaseResponse<Object> registerMahasiswa(@RequestBody MahasiswaDTO mahasiswaDto) throws IllegalAccessException {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("sukses");
        response.setResult(authService.registerMahasiswa(mahasiswaDto));
        return response;
    }

    @PostMapping(path = "/register/dosen")
    public BaseResponse<Object> registerDosen(@RequestBody DosenDTO dosenDTO) throws IllegalAccessException {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("sukses");
        response.setResult(authService.registerDosen(dosenDTO));
        return response;
    }
}
