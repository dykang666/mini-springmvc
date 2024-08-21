package com.example.minispringmvc.framework.sample.controller;

import com.example.minispringmvc.framework.annotation.Autowired;
import com.example.minispringmvc.framework.annotation.Controller;
import com.example.minispringmvc.framework.annotation.RequestMapping;
import com.example.minispringmvc.framework.annotation.RequestParam;
import com.example.minispringmvc.framework.sample.pojo.User;
import com.example.minispringmvc.framework.sample.service.UserService;
import com.example.minispringmvc.framework.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kangdongyang
 * @version 1.0
 * @description:
 * @date 2024/8/13 11:43
 */
@Controller
@RequestMapping("/web")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/hello.json")
    public ModelAndView hello(){
        User user = userService.getUser();
        ModelAndView mv = new ModelAndView();
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("addr", user.getAddr());
        mv.setView("template.fantj");
        mv.setModel(map);
        return mv;
    }

    @RequestMapping("/doTest")
    public void test1(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("param") String param){
        System.out.println(param);
        try {
            response.getWriter().write( "doTest method success! param:"+param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
