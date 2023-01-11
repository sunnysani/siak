package com.ppl.siakngnewbe.pengecekanirs.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SksResult {
    int periode;
    double ipAcuan;
    int sksDiambil;
    int sksMaksimum;
    boolean ok;
}
