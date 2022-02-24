package org.atovk.agent;


import com.alibaba.fastjson.JSONObject;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.concurrent.Callable;

public class MyInterceptor {


    // @Advice.OnMethodEnter
    public static void onMethodEnter(@Origin Method method, @SuperCall Callable<?> callable) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        try {
            HttpServletRequest httpServletRequest = requestAttributes.getRequest();

            Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
            String remoteAddr = httpServletRequest.getRemoteAddr();
            String requestMethod = httpServletRequest.getMethod();
            String uri = httpServletRequest.getRequestURI();

            System.out.println(JSONObject.toJSONString(headerNames));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("onMethodEnter");
    }


    @RuntimeType
    public static Object intercept(
            @Origin Method method,            //注入目标类方法
            @AllArguments Object[] args,    //注入目标方法参数列表
            @SuperCall Callable<?> callable    //注入可执行的目标方法
    ) throws Exception {

        onMethodEnter(method, callable);
        long start = System.currentTimeMillis();
        Object resObj = null;
        try {
            //执行原方法
            resObj = callable.call();
            return resObj;
        } finally {
            onMethodExit(method, callable);
            System.out.println("方法名称：" + method.getName());
            System.out.println("入参个数：" + method.getParameterCount());
            String s = method.getParameterTypes().length == 0 ?
                    "empty" : method.getParameterTypes()[0].getTypeName() + "、" + method.getParameterTypes()[1].getTypeName();
            System.out.println("入参类型：" + s);
            System.out.println("出参类型：" + method.getReturnType().getName());
            System.out.println("出参结果：" + resObj);
            System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
        }
    }

    // @RuntimeType
    // @Advice.OnMethodExit
    public static void onMethodExit(@Origin Method method, @SuperCall Callable<?> callable) {
        System.out.println("onMethodExit");
    }

}