package com.ppl.siakngnewbe.tahunajaran;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class TahunAjaranJob extends QuartzJobBean {

    @Autowired
    TahunAjaranService tahunAjaranService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        String periode = jobDataMap.getString("periode");
        String status = jobDataMap.getString("status");
        String token = jobDataMap.getString("token");

        tahunAjaranService.setTahunAjaranStatus(token, tahunAjaranService.getTahunAjaran(periode), status);
    }

}
