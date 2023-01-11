package com.ppl.siakngnewbe.pengumuman;

import java.util.List;

public interface PengumumanService {
    List<PengumumanModel> getPengumumanAll();
    PengumumanModel getPengumumanById(long id);
}
