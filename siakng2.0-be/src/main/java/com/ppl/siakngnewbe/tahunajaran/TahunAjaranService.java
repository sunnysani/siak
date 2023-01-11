package com.ppl.siakngnewbe.tahunajaran;

import com.ppl.siakngnewbe.dosen.DosenModelRepository;
import com.ppl.siakngnewbe.dosen.DosenService;
import com.ppl.siakngnewbe.notifikasilonceng.NotifikasiLonceng;
import com.ppl.siakngnewbe.notifikasilonceng.NotifikasiLoncengRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TahunAjaranService {

    @Autowired
    private TahunAjaranRepository tahunAjaranRepository;

    @Autowired
    private DosenService dosenService;

    @Autowired
    DosenModelRepository dosenModelRepository;

    @Autowired
    private NotifikasiLoncengRepository notifikasiLoncengRepository;

    @Autowired
    private Scheduler scheduler;

    private JobDetail buildJobDetail(TahunAjaran tahunAjaran, String name, String status, String token) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("name", name);
        jobDataMap.put("periode", tahunAjaran.getNama() + "-" + Integer.toString(tahunAjaran.getTerm()));
        jobDataMap.put("status", status);
        jobDataMap.put("token", token);

        return JobBuilder.newJob(TahunAjaranJob.class) // sek sek
                .withIdentity(UUID.randomUUID().toString(), "jadwal")
                .withDescription("jobDescription")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail, Calendar startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "jadwal-trigger")
                .withDescription("Jadwal Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }

    private void createScehduler(Calendar triggerTime, TahunAjaran tahunAjaran, String name, String status,
            String token) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(tahunAjaran, name, status, token);
        Trigger trigger = buildTrigger(jobDetail, triggerTime);

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public TahunAjaran postTahunAjaran(String token, TahunAjaran tahunAjaran) throws SchedulerException {

        if (isDosen(token)) {

            if (tahunAjaran.getStatus() == null) {
                tahunAjaran.setStatus(TahunAjaranStatus.IRS);
            }

            tahunAjaranRepository.save(tahunAjaran);

            // Quartz Scheduler
            createScehduler(tahunAjaran.getIsiIRSStart(), tahunAjaran, "Isi IRS Start", "isi", token);
            createScehduler(tahunAjaran.getIsiIRSEnd(), tahunAjaran, "Isi IRS End", "irs", token);
            createScehduler(tahunAjaran.getAddDropIRSStart(), tahunAjaran, "Add/Drop IRS Start", "add-drop", token);
            createScehduler(tahunAjaran.getAddDropIRSEnd(), tahunAjaran, "Add/Drop IRS End", "irs", token);
            createScehduler(tahunAjaran.getIsiNilaiDosenStart(), tahunAjaran, "Dosen IRS Start", "dosen", token);
            createScehduler(tahunAjaran.getIsiNilaiDosenEnd(), tahunAjaran, "Dosen IRS End", "irs", token);
            createScehduler(tahunAjaran.getSelesaiDate(), tahunAjaran, "Close Tahun Ajaran", "closed", token);

            return tahunAjaran;
        }

        return null;
    }

    public TahunAjaran getTahunAjaran(String periode) {

        if (periode.equals("latest")) {
            return tahunAjaranRepository.findTopByOrderByNamaDescTermDesc();
        }
        return tahunAjaranRepository.findTopByNamaAndTerm(periode.substring(0, periode.length() - 2),
                Character.getNumericValue(periode.charAt(periode.length() - 1)));
    }

    public String getTahunAjaranStatus(TahunAjaran tahunAjaran) {
        return tahunAjaran.getStatus().toString();
    }

    private boolean isDosen(String token) {
        var found = false;
        if (dosenService.getDosenById(token) != null) {
            found = true;
        }
        return found;
    }

    public boolean setTahunAjaranStatus(String token, TahunAjaran tahunAjaran, String status) {
        if (isDosen(token)) {
            var statusChanged = false;
            if (status.equals("isi")) {
                tahunAjaran.setStatus(TahunAjaranStatus.IRS_ISI);
                statusChanged = true;
                NotifikasiLonceng notifikasiLoncengIsiIrs = new NotifikasiLonceng(
                        "Masa pengisian IRS sudah dapat dilakukan", null);
                notifikasiLoncengRepository.save(notifikasiLoncengIsiIrs);
            } else if (status.equals("add-drop")) {
                tahunAjaran.setStatus(TahunAjaranStatus.IRS_ADD_DROP);
                statusChanged = true;
                NotifikasiLonceng notifikasiLoncengAddDropIrs = new NotifikasiLonceng(
                        "Masa ADD/DROP IRS sudah dapat dilakukan", null);
                notifikasiLoncengRepository.save(notifikasiLoncengAddDropIrs);
            } else if (status.equals("irs")) {
                tahunAjaran.setStatus(TahunAjaranStatus.IRS);
                statusChanged = true;
            } else if (status.equals("dosen")) {
                tahunAjaran.setStatus(TahunAjaranStatus.IRS_DOSEN);
                statusChanged = true;
            } else if (status.equals("closed")) {
                tahunAjaran.setStatus(TahunAjaranStatus.CLOSED);
                statusChanged = true;
            }
            tahunAjaranRepository.save(tahunAjaran);
            return statusChanged;
        } else {
            return false;
        }
    }
}