package com.qx.config;

import com.qx.handler.MyAsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author qiux
 * @Date 2023/3/28
 * @since
 */
@EnableAsync
@Configuration
public class AsynConfig implements AsyncConfigurer {

    @Autowired
    private MyAsyncUncaughtExceptionHandler myAsyncUncaughtExceptionHandler;

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return myAsyncUncaughtExceptionHandler;
    }
}
