package cn.spring;

import cn.shenl.AppConfig;

/**
 * @ClassName MyApplicationContext
 * @Description TODO
 * @Author dmm
 * @Date 2021/9/9 23:31
 * @Version 1.0
 */
public class MyApplicationContext {
    private Class config;

    public MyApplicationContext(Class configClazz) {
        this.config = configClazz;
    }

    public Object getBean(String beanName) {
        return null;
    }
}
