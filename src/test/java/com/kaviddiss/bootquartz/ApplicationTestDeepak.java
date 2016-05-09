package com.kaviddiss.bootquartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringApplicationConfiguration(classes = Application.class)
@Rollback(value = false)
public class ApplicationTestDeepak extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private FactoryUtil factoryUtil;

    @Test
    public void test() throws Exception {
        JobDetailFactoryBean jobDetailFactoryBean = factoryUtil.getBeanByType(JobDetailFactoryBean.class);
        CronTriggerFactoryBean cronTriggerFactoryBean = factoryUtil.getBeanByType(CronTriggerFactoryBean.class, jobDetailFactoryBean);
        SchedulerFactoryBean schedulerFactoryBean = factoryUtil.getBeanByType(SchedulerFactoryBean.class);
        schedulerFactoryBean.setTriggers(cronTriggerFactoryBean.getObject());
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.scheduleJob(jobDetailFactoryBean.getObject(), cronTriggerFactoryBean.getObject());
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        System.out.println("Jai Mata Di");

        Thread.sleep(5000);
    }
}