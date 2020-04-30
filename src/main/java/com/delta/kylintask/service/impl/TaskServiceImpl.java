package com.delta.kylintask.service.impl;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.config.ScheduleManager;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private ScheduleManager scheduleManager;

    @Override
    public ServerResponse<List<TaskDto>> getTasks(TaskDto taskDto) {
        return ServerResponse.createBySuccess(scheduleManager.getJobs(taskDto.getKylinid()));
    }

    @Override
    public ServerResponse<TaskDto> addTask(TaskDto taskDto) {
        TaskDto resultTask = scheduleManager.addJob(taskDto);
        return ServerResponse.createBySuccess(resultTask);
    }

    @Override
    public ServerResponse<TaskDto> pauseTask(TaskDto taskDto) {
        TaskDto result = scheduleManager.pauseJob(taskDto);
        return result == null ? ServerResponse.createByErrorMessage("can't find jobDetail.") : ServerResponse.createBySuccess(result);
    }

    @Override
    public ServerResponse<TaskDto> resumeTask(TaskDto taskDto) {
        TaskDto result = scheduleManager.resumeJob(taskDto);
        return result == null ? ServerResponse.createByErrorMessage("can't find jobDetail.") : ServerResponse.createBySuccess(result);
    }

    @Override
    public ServerResponse<TaskDto> updateTask(TaskDto taskDto) {
        return null;
    }

    @Override
    public ServerResponse<TaskDto> deleteTask(TaskDto taskDto) {
        TaskDto result = scheduleManager.removeJob(taskDto);
        return result == null ? ServerResponse.createByErrorMessage("can't find jobDetail.") : ServerResponse.createBySuccess(result);
    }

    @Override
    public ServerResponse clearTasks(String clientKey) {
        scheduleManager.clear(clientKey);
        return ServerResponse.createBySuccess();
    }
}
