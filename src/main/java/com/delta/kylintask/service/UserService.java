package com.delta.kylintask.service;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.UserDto;
import com.delta.kylintask.entity.UserInfo;

public interface UserService {
    ServerResponse<UserInfo> login(UserDto userDto);
    ServerResponse<UserInfo> logout(UserDto userDto);
}
