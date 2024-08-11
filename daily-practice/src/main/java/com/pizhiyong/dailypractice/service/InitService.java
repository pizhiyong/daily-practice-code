package com.pizhiyong.dailypractice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InitService {

    private static final Logger logger = LoggerFactory.getLogger(InitService.class);

    @PostConstruct
    private void init() {
        logger.info("开始执行初始化脚本");
        logger.info("初始化脚本执行完毕");
    }

}
