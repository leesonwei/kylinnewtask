package com.delta.kylintask.service;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.TaskDto;

import java.util.List;


public interface TaskService {
    ServerResponse<List<TaskDto>> getTasks(TaskDto taskDto);
    ServerResponse<TaskDto> addTask(TaskDto taskDto);
    ServerResponse<TaskDto> pauseTask(TaskDto taskDto);
    ServerResponse<TaskDto> resumeTask(TaskDto taskDto);
    ServerResponse<TaskDto> updateTask(TaskDto taskDto);
    ServerResponse<TaskDto> deleteTask(TaskDto taskDto);
    ServerResponse clearTasks(String clientKey);
}
