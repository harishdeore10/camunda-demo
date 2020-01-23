package com.example.camunda.demo;


import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
public class Controller {

    private final Logger logger = getLogger(this.getClass());


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    private Task task;

    private String processInstanceId;

    @RequestMapping(value = "/process/{assignee}")
    @ResponseBody
    public String startProcess(@PathVariable("assignee") String assignee) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("assignee", assignee);

        processInstanceId = runtimeService.startProcessInstanceByKey("InrementalRoleFlow", variables).getProcessInstanceId();
        logger.info("started instance: {}", processInstanceId);
        return processInstanceId;
    }

    @RequestMapping("/task/{processInstanceId}")
    @ResponseBody
    public String startTask(@PathVariable("processInstanceId") String processInstanceId) {
        task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        logger.info("Task: owner " + task.getId());
        return task.getId();
    }

    @RequestMapping("/completeTask/{taskId}")
    @ResponseBody
    public void completeTask(@PathVariable("taskId") String taskId) {
        //Trigger email
        //Put data into DB - call to other micro-service
        taskService.complete(taskId);
        logger.info("completed task: {}", taskId);
    }

    @RequestMapping("/completeTask/{taskId}/{approved}")
    @ResponseBody
    public void completeTaskWithApproval(@PathVariable("taskId") String taskId, @PathVariable("approved") Boolean approved) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved);
        taskService.complete(taskId, variables);
        logger.info("completed task: {}", taskId);
    }

    @RequestMapping("/list/task/{user}")
    @ResponseBody
    public Map<String, String> listOfTask(@PathVariable("user") String user) {

        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(user)
                .list();
        logger.info(" tasks:", tasks);

        Random rnd = new Random();
        int randomId = 100000 + rnd.nextInt(999999);

        // construct a new map from the stream
        Map<String, String> taskDetails =
                tasks.stream().collect(Collectors.toMap(t -> t.getName() + " :" + randomId, t -> t.getId()));

        return taskDetails;
    }
}
