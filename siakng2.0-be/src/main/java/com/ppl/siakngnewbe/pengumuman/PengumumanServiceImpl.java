package com.ppl.siakngnewbe.pengumuman;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Setter
public class PengumumanServiceImpl implements PengumumanService {

    @Autowired
    private PengumumanRepository pengumumanRepository;

    @Override
    public List<PengumumanModel> getPengumumanAll() {
        return pengumumanRepository.findAll();
    }

    @Override
    public PengumumanModel getPengumumanById(long id) {
        var result = pengumumanRepository.findById(id);
        return result.orElse(null);
    }
}
