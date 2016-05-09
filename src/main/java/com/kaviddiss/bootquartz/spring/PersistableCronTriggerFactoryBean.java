package com.kaviddiss.bootquartz.spring;
import java.text.ParseException;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {

    public static final String JOB_DETAIL_KEY = "jobDetail";
    
    @Override
    public void afterPropertiesSet() throws ParseException {
        super.afterPropertiesSet();
        getJobDataMap().remove(JOB_DETAIL_KEY);
    }
}