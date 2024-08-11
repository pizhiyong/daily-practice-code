package com.pizhiyong.dailypractice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class InitializingBeanService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(InitializingBeanService.class);

    @Override
    public void afterPropertiesSet() throws Exception {

        logger.info("InitializingBean 执行脚本");
    }
}
