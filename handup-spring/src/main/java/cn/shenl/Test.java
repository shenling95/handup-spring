package cn.shenl;

import cn.shenl.service.UserService;
import cn.spring.ComponentScan;
import cn.spring.MyApplicationContext;

/**
 * @ClassName Test
 * @Description TODO
 * @Author dmm
 * @Date 2021/9/9 23:32
 * @Version 1.0
 */
public class Test {

    public static void main(String[] args) throws ClassNotFoundException {
        MyApplicationContext applicationContext = new MyApplicationContext(AppConfig.class);
        UserService userService = (UserService) applicationContext.getBean("userService");

        userService.test();
    }
}
