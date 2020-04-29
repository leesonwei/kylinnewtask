package com.delta.kylintask.controller;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.service.TaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@Api(tags = "task 相关接口", description = "提供 task 相关的 Rest API")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PutMapping("/insert")
    public ServerResponse<TaskDto> insert(TaskDto taskDto) {
        return taskService.addTask(taskDto);
    }

    @GetMapping("/clear")
    public ServerResponse clear(String clientKey) {
        return taskService.clearTasks(clientKey);
    }

}
