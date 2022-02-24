package org.atovk.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.atovk.from.Param;
import org.atovk.utils.HttpUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/")
public class DemoController {

    @GetMapping("demo")
    public String demo() {
        return "hello world api resp!!!";
    }


    @PostMapping("full")
    public String full(@RequestParam String type, @RequestBody Param param) throws Exception {

        HttpServletRequest req = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String s = headerNames.nextElement();
            log.info("-header.{}: value: {}.", s, req.getHeader(s));
        }

        String reqMethod = req.getMethod();
        log.info("ReqMethod: {}", reqMethod);

        String requestURI = req.getRequestURI();
        log.info("URI: {}", requestURI);


        String ipAddress = HttpUtil.getIpAddress(req);
        log.info("IP: {}", ipAddress);
        log.info("type: {}, param: {}", type, new ObjectMapper().writeValueAsString(param));

        return "hello full api resp!!!";
    }

}
