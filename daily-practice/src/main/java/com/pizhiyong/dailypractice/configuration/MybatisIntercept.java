package com.pizhiyong.dailypractice.configuration;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts(
        {
                @Signature(type = Executor.class,
                        method = "query",
                        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        }
)
public class MybatisIntercept implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(MybatisIntercept.class.getSimpleName());

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        logger.info("开始执行自定义拦截器逻辑, {}", JSON.toJSONString(invocation.getMethod().getName()));
        return invocation.proceed();
    }
}
