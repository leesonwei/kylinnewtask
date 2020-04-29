package com.delta.kylintask.service.impl;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.config.ScheduleManager;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private ScheduleManager scheduleManager;

    @Override
    public ServerResponse<TaskDto> addTask(TaskDto taskDto) {
        TaskDto resultTask = scheduleManager.addJob(taskDto);
        return ServerResponse.createBySuccess(resultTask);
    }

    @Override
    public ServerResponse clearTasks(String clientKey) {
        scheduleManager.clear(clientKey);
        return ServerResponse.createBySuccess();
    }
}
