package com.delta.kylintask.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.config.KylinProperties;
import com.delta.kylintask.mapper.KylinMapper;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.service.KylinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;

@Service
@Slf4j
public class KylinServiceImpl implements KylinService {
    @Autowired
    private KylinMapper kylinMapper;

    @Override
    public ServerResponse<Kylin> insert(Kylin kylin) {
        kylin.setId(String.valueOf(kylin.hashCode()));
        int count = kylinMapper.insert(kylin);
        return count == 1 ? ServerResponse.createBySuccess(kylin) : ServerResponse.createByErrorMessage("insert failed.");
    }

    @Override
    public ServerResponse<Kylin> updateById(Kylin kylin) {
        int count = kylinMapper.updateById(kylin);

        return count == 1 ? ServerResponse.createBySuccess(kylin) : ServerResponse.createByErrorMessage("update failed.");
    }

    @Override
    public ServerResponse<Kylin> selectById(String id) {
        return ServerResponse.createBySuccess(kylinMapper.selectById(id));
    }

    @Override
    public ServerResponse<List<Kylin>> select() {
        return ServerResponse.createBySuccess(kylinMapper.selectList(new EntityWrapper<Kylin>().isNotNull("kylinid")));
    }

    @Override
    public ServerResponse<String> deleteById(String id) {
        int count = kylinMapper.deleteById(id);
        return count == 1 ? ServerResponse.createBySuccess(id) : ServerResponse.createByErrorMessage("delete failed.");
    }
}
