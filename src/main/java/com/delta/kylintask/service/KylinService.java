package com.delta.kylintask.service;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.entity.Kylin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface KylinService {
    ServerResponse<Kylin> insert(Kylin kylin);
    ServerResponse<Kylin> updateById(Kylin kylin);
    ServerResponse<Kylin> selectById(Integer id);
    ServerResponse<Integer> deleteById(Integer id);
}
