package com.delta.kylintask.service.impl;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.service.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public ServerResponse<TaskDto> addTask(TaskDto taskDto) {
        return null;
    }
}
