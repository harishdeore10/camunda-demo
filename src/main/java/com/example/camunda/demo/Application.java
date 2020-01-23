package com.example.camunda.demo;

import org.camunda.bpm.application.ProcessApplicationInterface;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableProcessApplication
public class Application {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JobExecutor jobExecutor;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ProcessApplicationInterface application;



    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}