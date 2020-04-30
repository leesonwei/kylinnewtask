package com.delta.kylintask.controller;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.service.TaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Api(tags = "task 相关接口", description = "提供 task 相关的 Rest API")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("")
    public ServerResponse<List<TaskDto>> getTasks(TaskDto taskDto) {
        return taskService.getTasks(taskDto);
    }

    @PutMapping("/insert")
    public ServerResponse<TaskDto> insert(TaskDto taskDto) {
        return taskService.addTask(taskDto);
    }

    @PostMapping("/pause")
    public ServerResponse<TaskDto> pause(TaskDto taskDto) {
        return taskService.pauseTask(taskDto);
    }

    @PostMapping("/resume")
    public ServerResponse<TaskDto> resume(TaskDto taskDto) {
        return taskService.resumeTask(taskDto);
    }

    @DeleteMapping("/delete")
    public ServerResponse<TaskDto> delete(TaskDto taskDto) {
        return taskService.deleteTask(taskDto);
    }

    @DeleteMapping("/clear")
    public ServerResponse clear(String clientKey) {
        return taskService.clearTasks(clientKey);
    }

}
