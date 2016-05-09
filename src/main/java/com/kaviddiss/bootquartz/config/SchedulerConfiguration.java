package com.kaviddiss.bootquartz.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.kaviddiss.bootquartz.job.SampleJob;
import com.kaviddiss.bootquartz.spring.AutowiringSpringBeanJobFactory;
import com.kaviddiss.bootquartz.spring.PersistableCronTriggerFactoryBean;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@ComponentScan("com.appanalytixs.agent.scheduler.jobs")
public class SchedulerConfiguration {

    //// injecting SpringLiquibase to ensure liquibase is already initialized and created the quartz tables:
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext, SpringLiquibase springLiquibase) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    @Scope("prototype")
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(SampleJob.class);
        jobDetailFactoryBean.setDurability(true);
        Map<String, Object> map = new HashMap<String, Object>();
        jobDetailFactoryBean.setJobDataAsMap(map);
        jobDetailFactoryBean.setName(SampleJob.class.getName() + System.currentTimeMillis());
        return jobDetailFactoryBean;
    }

    @Bean
    @Scope("prototype")
    public CronTriggerFactoryBean cronTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new PersistableCronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        cronTriggerFactoryBean.setName(SampleJob.class.getName());
        cronTriggerFactoryBean.setGroup(SampleJob.class.getName());
        cronTriggerFactoryBean.setCronExpression("0/5 * * * * ? *");
        return cronTriggerFactoryBean;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws SchedulerException, IOException {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setDataSource(dataSource);
        scheduler.setQuartzProperties(quartzProperties());
        scheduler.setJobFactory(jobFactory);
        if (scheduler.getScheduler() != null) {
            scheduler.getScheduler().setJobFactory(jobFactory);
        }
        return scheduler;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}