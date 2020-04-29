package com.delta.kylintask.service.impl;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.mapper.KylinJobMapper;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.service.KylinJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KylinJobServiceImpl implements KylinJobService {
    @Autowired
    private KylinJobMapper kylinJobMapper;

    @Override
    public ServerResponse<KylinJob> insert(KylinJob kylinJob) {
        int count = kylinJobMapper.insert(kylinJob);
        return count == 1 ? ServerResponse.createBySuccess(kylinJob) : ServerResponse.createByErrorMessage("insert failed.");
    }

    @Override
    public ServerResponse<KylinJob> updateById(KylinJob kylinJob) {
        int count = kylinJobMapper.updateById(kylinJob);
        return count == 1 ? ServerResponse.createBySuccess(kylinJob) : ServerResponse.createByErrorMessage("update failed.");
    }

    @Override
    public ServerResponse<KylinJob> selectById(String uuid) {
        return ServerResponse.createBySuccess(kylinJobMapper.selectById(uuid));
    }

    @Override
    public ServerResponse<String> deleteById(String uuid) {
        int count = kylinJobMapper.deleteById(uuid);
        return count == 1 ? ServerResponse.createBySuccess(uuid) : ServerResponse.createByErrorMessage("delete failed.");
    }
}
