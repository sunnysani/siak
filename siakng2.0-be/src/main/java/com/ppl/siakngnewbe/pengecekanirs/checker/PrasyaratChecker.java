package com.ppl.siakngnewbe.pengecekanirs.checker;

import java.util.ArrayList;
import java.util.Map;

import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratProxy;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratResult;

public class PrasyaratChecker {
    public PrasyaratResult check(MataKuliah mataKuliahDiambil, Map<String, MataKuliah> mataKuliahLulus) {
        var result = new PrasyaratResult(mataKuliahDiambil);
        result.setPrasyarat(new ArrayList<>());
        result.setOk(true);
        
        for(MataKuliah mataKuliahPrasyarat : mataKuliahDiambil.getPrasyaratMataKuliahSet()) {
            var id = mataKuliahPrasyarat.getId();

            var prasyaratProxy = new PrasyaratProxy(mataKuliahPrasyarat);
            if(!mataKuliahLulus.containsKey(id)) {
                result.setOk(false);
                prasyaratProxy.setLulus(false);
            }
            else prasyaratProxy.setLulus(true);
            result.getPrasyarat().add(prasyaratProxy);
        }
        return result;
    }
}
