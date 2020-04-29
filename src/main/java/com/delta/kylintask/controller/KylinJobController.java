package com.delta.kylintask.controller;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.service.KylinJobService;
import com.delta.kylintask.service.KylinService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kylinjob")
@Api(tags = "Kylin Job信息相关接口", description = "提供Kylin Job信息相关的 Rest API")
public class KylinJobController {
    @Autowired
    private KylinJobService kylinJobService;

    @PutMapping("/insert")
    public ServerResponse<KylinJob> insert(KylinJob kylinJob) {
        return kylinJobService.insert(kylinJob);
    }

    @PostMapping("/update")
    public ServerResponse<KylinJob> update(KylinJob kylinJob) {
        return kylinJobService.updateById(kylinJob);
    }

    @DeleteMapping("/delete/{uuid}")
    public ServerResponse<String> delete(@PathVariable String uuid) {
        return kylinJobService.deleteById(uuid);
    }

    @GetMapping("/select/{uuid}")
    public ServerResponse<KylinJob> select(@PathVariable String uuid) {
        return kylinJobService.selectById(uuid);
    }
}
