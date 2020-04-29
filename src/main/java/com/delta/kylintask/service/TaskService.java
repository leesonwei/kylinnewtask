package com.delta.kylintask.service;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.TaskDto;


public interface TaskService {
    ServerResponse<TaskDto> addTask(TaskDto taskDto);
    ServerResponse clearTasks(String clientKey);
}
