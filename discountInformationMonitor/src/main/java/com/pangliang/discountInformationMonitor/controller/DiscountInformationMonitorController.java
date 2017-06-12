package com.pangliang.discountInformationMonitor.controller;

import com.pangliang.discountInformationMonitor.vo.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
//@RequestMapping("/discountInformationMonitor/")
@Controller
public class DiscountInformationMonitorController {

    //1登陆首页，查询当前正在运行的线程信息，显示在页面上，至于爬取内容的输出由页面调用后台请求（轮询）


    //2用户设置发送邮箱账号密码和接收邮箱


    //3用户增删改关键词
    //通知调度器，调度器会对相应线程的状态做出处理


    @RequestMapping("/")
    public String index(Model model){
        Person single = new Person();
        single.setAge(11);
        single.setName("asdasd");
        List<Person> personList = new ArrayList<>();
        Person p1 = new Person();
        p1.setAge(11);
        p1.setName("AAAA");
        Person p2 = new Person();
        p2.setAge(222);
        p2.setName("BBBB");
        Person p3 = new Person();
        p3.setAge(333);
        p3.setName("CCCC");
        personList.add(p1);
        personList.add(p2);
        personList.add(p3);

        model.addAttribute("singlePerson",single);
        model.addAttribute("personList",personList);

        return "index";

    }

}
