package cn.shenl.service;

import cn.spring.Autowired;
import cn.spring.Component;
import cn.spring.Scope;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author dmm
 * @Date 2021/9/9 23:32
 * @Version 1.0
 */
@Component//("userService")
@Scope//("prototype")
public class UserService {

    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println(orderService);
    }
}
