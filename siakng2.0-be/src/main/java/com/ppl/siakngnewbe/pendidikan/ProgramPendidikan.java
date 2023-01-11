package com.ppl.siakngnewbe.pendidikan;

public enum ProgramPendidikan {
    VOKASI,
    S1_REGULER,
    S1_PARALEL,
    S1_INTERNASIONAL,
    S1_EKSTENSI,
    S2,
    S3,
    PROFESI,
    SPESIALIS1,
    SPESIALIS2;

    @Override
    public String toString() {
        String[] namelist = this.name().split("_");

        if (namelist.length == 1) {
            namelist[0] = namelist[0].substring(0,1) + namelist[0].substring(1).toLowerCase();
            return namelist[0];
        }

        namelist[1] = namelist[1].substring(0,1) + namelist[1].substring(1).toLowerCase();
        return namelist[0] + " " + namelist[1];
    }
}
