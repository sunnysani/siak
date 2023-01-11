package com.ppl.siakngnewbe.pengecekanirs.checker;

import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.pengecekanirs.result.SksResult;

public class SksChecker {
    public int getSksMaksimumFromIps(double ips) throws IpsOutOfBoundException {
        if(3.50 <= ips && ips <= 4.00) return 24;
        else if(3.00 <= ips && ips < 3.50) return 21;
        else if(2.50 <= ips && ips < 3.00) return 18;
        else if(2.00 <= ips && ips < 2.50) return 15;
        else if(ips <= 2.00) return 12;
        else throw new IpsOutOfBoundException();
    }

    public SksResult check(IrsMahasiswa now) {
        var result = new SksResult();

        result.setPeriode(now.getSemester());
        result.setIpAcuan(4.00);
        result.setSksDiambil(now.getSksa());
        result.setSksMaksimum(24);
        result.setOk(result.getSksDiambil() <= result.getSksMaksimum());
        return result;
    }

    public SksResult check(IrsMahasiswa now, IrsMahasiswa before) throws IpsOutOfBoundException {
        var result = new SksResult();
        
        result.setPeriode(now.getSemester());
        result.setIpAcuan(before.getIps());
        result.setSksDiambil(now.getSksa());
        result.setSksMaksimum(getSksMaksimumFromIps(before.getIps()));
        result.setOk(result.getSksDiambil() <= result.getSksMaksimum());
        return result;
    }
}
