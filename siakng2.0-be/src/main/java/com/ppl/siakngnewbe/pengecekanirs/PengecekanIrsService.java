package com.ppl.siakngnewbe.pengecekanirs;

import java.util.List;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.pengecekanirs.checker.IpsOutOfBoundException;
import com.ppl.siakngnewbe.pengecekanirs.result.JadwalResult;
import com.ppl.siakngnewbe.pengecekanirs.result.KapasitasResult;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratResult;
import com.ppl.siakngnewbe.pengecekanirs.result.SksResult;

public interface PengecekanIrsService {
    public List<KapasitasResult> checkKapasitasOfLatestIrs(Mahasiswa mahasiswa);
    public List<JadwalResult> checkJadwalOfLatestIrs(Mahasiswa mahasiswa);
    public SksResult checkSksOfLatestIrs(Mahasiswa mahasiswa) throws IpsOutOfBoundException;
    public List<PrasyaratResult> checkPrasyaratOfLatestIrs(Mahasiswa mahasiswa);
}
