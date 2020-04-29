package com.delta.kylintask.service;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.entity.KylinJob;

public interface KylinJobService {
    ServerResponse<KylinJob> insert(KylinJob kylinJob);
    ServerResponse<KylinJob> updateById(KylinJob kylinJob);
    ServerResponse<KylinJob> selectById(String uuid);
    ServerResponse<String> deleteById(String uuid);
}
