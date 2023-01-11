package com.ppl.siakngnewbe.pengecekanirs.checker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import com.ppl.siakngnewbe.jadwal.Jadwal;
import com.ppl.siakngnewbe.kelasirs.KelasIrs;
import com.ppl.siakngnewbe.pengecekanirs.result.JadwalResult;
import com.ppl.siakngnewbe.pengecekanirs.result.KelasProxy;

import org.springframework.data.util.Pair;

public class JadwalChecker {
    private final Map<String, Integer> hari = Map.ofEntries(
        Map.entry("Senin", 0),
        Map.entry("Selasa", 1),
        Map.entry("Rabu", 2),
        Map.entry("Kamis", 3),
        Map.entry("Jumat", 4),
        Map.entry("Sabtu", 5),
        Map.entry("Minggu", 6)
    );

    public int toMinuteOfWeek(String namaHari, Calendar waktu) {
        return hari.get(namaHari) * 24 * 60 + waktu.get(Calendar.HOUR_OF_DAY) * 60 + waktu.get(Calendar.MINUTE);
    }

    public Pair<Integer, Integer> toMinuteOfWeek(Jadwal jadwal) {
        return Pair.of(toMinuteOfWeek(jadwal.getHari(), jadwal.getWaktuMulai()), 
                       toMinuteOfWeek(jadwal.getHari(), jadwal.getWaktuSelesai()));
    }

    public boolean isConflicted(Jadwal first, Jadwal second) {
        Pair<Integer, Integer> firstRange = toMinuteOfWeek(first);
        Pair<Integer, Integer> secondRange = toMinuteOfWeek(second);
        return (secondRange.getFirst() <= firstRange.getFirst() && firstRange.getFirst() <= secondRange.getSecond()) ||
               (secondRange.getFirst() <= firstRange.getSecond() && firstRange.getSecond() <= secondRange.getSecond());
    }

    public boolean isConflicted(List<Jadwal> firstList, List<Jadwal> secondList) {
        for(Jadwal first : firstList) {
            for(Jadwal second : secondList) {
                if(isConflicted(first, second)) {
                    return true;
                }
            }
        }
        return false;
    }

    public JadwalResult check(KelasIrs toCheck, List<KelasIrs> allKelasIrs) {
        var result = new JadwalResult(toCheck.getKelas());
        result.setKonflik(new ArrayList<>());

        for(KelasIrs other : allKelasIrs) {
            if(other != toCheck) {
                List<Jadwal> toCheckJadwal = List.copyOf(toCheck.getKelas().getJadwalSet());
                List<Jadwal> otherJadwal = List.copyOf(other.getKelas().getJadwalSet());
                
                if(isConflicted(toCheckJadwal, otherJadwal)) {
                    result.getKonflik().add(new KelasProxy(other.getKelas()));
                }
            }
        }
        result.setOk(result.getKonflik().isEmpty());
        return result;
    }
}
