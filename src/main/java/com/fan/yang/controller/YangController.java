package com.fan.yang.controller;

import com.fan.yang.service.YangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhang_fan
 * @since 2023/1/5 下午 05:55
 */
@Controller
@Slf4j
public class YangController {
    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;
    @Resource
    private YangService yangService;
    @GetMapping("/")
    public String index() {
        logRequestInfo();
        return "index.html";
    }

    @GetMapping("/love")
    public String love() {
        logRequestInfo();
        return "redirect:love/index.html";
    }

    private void logRequestInfo() {
        yangService.logRequestInfo(request, response);
    }
}
