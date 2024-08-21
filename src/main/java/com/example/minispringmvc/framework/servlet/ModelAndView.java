package com.example.minispringmvc.framework.servlet;

import lombok.Data;

import java.util.Map;

/**
 * @author kangdongyang
 * @version 1.0
 * @description:
 * @date 2024/8/13 10:27
 */
@Data
public class ModelAndView {
    /**
     * 页面模板
     */
    private String view;
    /**
     * 传输的数据
     */
    private Map<String, Object> model;

    public ModelAndView(String view, Map<String, Object> model) {
        this.view = view;
        this.model = model;
    }

    public ModelAndView() {
    }
}
