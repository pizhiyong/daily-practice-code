package com.pizhiyong.dailypractice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextAwareService implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextAwareService.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        init();
    }

    public void init() {
        logger.info(ApplicationContextAwareService.class.getSimpleName() + "执行初始化逻辑");
    }
}
