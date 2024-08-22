package com.pizhiyong.dailypractice.masksdk.conf;

import com.pizhiyong.dailypractice.masksdk.condition.EnableSensitiveMaskCondition;
import com.pizhiyong.dailypractice.masksdk.dao.SensitiveMaskDao;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Service
@Lazy(value = false)
@MapperScan(basePackages = "com.pizhiyong.dailypractice.masksdk.dao", sqlSessionFactoryRef = SensitiveMaskHandlerConfig.SQL_SESSION_FACTORY_REF)
@Conditional(EnableSensitiveMaskCondition.class)
public class SensitiveMaskHandlerConfig implements ApplicationContextAware {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SensitiveMaskHandlerConfig.class);

    protected static final String SQL_SESSION_FACTORY_REF = "sqlSessionFactory";

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static String getConf(String item) {
        return Objects.requireNonNull(getApplicationContext().getEnvironment().getProperty(item)).trim();
    }

    @Bean(name = SQL_SESSION_FACTORY_REF)
    @Conditional(EnableSensitiveMaskCondition.class)
    public SqlSessionFactory getSqlSessionFactory(Map<String, DataSource> dataSourceMap) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置数据源
        String dsName = getConf("sensitive.mask.sql.session.datasource");
        logger.info("信息掩码绑定的数据源:{}，现有的数据源:{}", dsName, dataSourceMap.keySet());
        sqlSessionFactoryBean.setDataSource(context.getBean(dsName, DataSource.class));
        // 设置mapper扫描路径
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        sqlSessionFactoryBean.setSqlSessionFactoryBuilder(new SqlSessionFactoryBuilder());
        return sqlSessionFactoryBean.getObject();
    }

    @EventListener
    public void onContextStartedEvent(ContextStartedEvent event) {
        try {
            // 打印日志，判断是否开启信息掩码
            SensitiveMaskDao sensitiveMaskDao = context.getBean(SensitiveMaskDao.class);
            logger.info("掩码绑定的dao:{}", sensitiveMaskDao);
        } catch (Exception e) {
            logger.info("信息掩码绑定的dao为空");
        }
    }
}
