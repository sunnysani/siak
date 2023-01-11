package com.ppl.siakngnewbe.ringkasanirs;

import java.util.List;
import java.util.Map;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.kelas.Kelas;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;

import com.ppl.siakngnewbe.rest.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/ringkasan-irs")
public class RingkasanIrsController {
    @Autowired
    private RingkasanIrsService ringkasanIrsService;

    @GetMapping(value = "/detail-irs")
    public BaseResponse<Object> getDetailIrs() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Mahasiswa mahasiswa = (Mahasiswa) auth.getPrincipal();

        IrsMahasiswa irsMahasiswa = ringkasanIrsService.getLatestDetailIrs(mahasiswa);
        BaseResponse<Object> response = new BaseResponse<>();
        if(irsMahasiswa == null) {
            response.setStatus(204);
            response.setMessage("kosong");
        }
        else {
            response.setStatus(200);
            response.setMessage("sukses");
            response.setResult(Map.of("data", irsMahasiswa));
        }
        return response;

    }

    @GetMapping(path = "/matkul-dipilih")
    public BaseResponse<Object> getMatkulDipilih() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Mahasiswa mahasiswa = (Mahasiswa) auth.getPrincipal();

        List<Kelas> listKelas = ringkasanIrsService.getLatestMatkulDipilih(mahasiswa);
        BaseResponse<Object> response = new BaseResponse<>();
        if(listKelas.isEmpty()) {
            response.setStatus(204);
            response.setMessage("kosong");
        }
        else {
            response.setStatus(200);
            response.setMessage("sukses");
            response.setResult(Map.of("data", listKelas));
        }
        return response;
    }
}
