package com.delta.kylintask.controller;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.service.KylinService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kylin")
@Api(tags = "Kylin連接信息相关接口", description = "提供Kylin連接信息相关的 Rest API")
public class KylinController {
    @Autowired
    private KylinService kylinService;

    @PutMapping("/insert")
    public ServerResponse<Kylin> insert(Kylin kylin) {
        return kylinService.insert(kylin);
    }

    @PostMapping("/update")
    public ServerResponse<Kylin> update(Kylin kylin) {
        return kylinService.updateById(kylin);
    }

    @DeleteMapping("/delete/{id}")
    public ServerResponse<String> delete(@PathVariable String id) {
        return kylinService.deleteById(id);
    }

    @GetMapping("/select/{id}")
    public ServerResponse<Kylin> select(@PathVariable String id) {
        Kylin kylin = new Kylin();
        kylin.setId(id);
        return kylinService.selectById(id);
    }
}
