package com.delta.kylintask.service.impl;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.mapper.KylinMapper;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.service.KylinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KylinServiceImpl implements KylinService {
    @Autowired
    private KylinMapper kylinMapper;
    @Override
    public ServerResponse<Kylin> insert(Kylin kylin) {
        kylin.setId(kylin.hashCode());
        int count = kylinMapper.insert(kylin);
        return count == 1 ? ServerResponse.createBySuccess(kylin) : ServerResponse.createByErrorMessage("insert failed.");
    }

    @Override
    public ServerResponse<Kylin> updateById(Kylin kylin) {
        int count = kylinMapper.updateById(kylin);

        return count == 1 ? ServerResponse.createBySuccess(kylin) : ServerResponse.createByErrorMessage("update failed.");
    }

    @Override
    public ServerResponse<Kylin> selectById(Integer id) {
        return ServerResponse.createBySuccess(kylinMapper.selectById(id));
    }

    @Override
    public ServerResponse<Integer> deleteById(Integer id) {
        int count = kylinMapper.deleteById(id);
        return count == 1 ? ServerResponse.createBySuccess(id) : ServerResponse.createByErrorMessage("delete failed.");
    }
}
