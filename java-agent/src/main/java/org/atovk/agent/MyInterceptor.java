package org.atovk.agent;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class MyInterceptor {


    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Enter Method method) {


        System.out.println("onMethodEnter");
    }


    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();
        Object resObj = null;
        try {
            //执行原方法
            resObj = callable.call();
            return resObj;
        } finally {
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

    @RuntimeType
    @Advice.OnMethodExit
    public static void onMethodExit() {
        System.out.println("onMethodExit");
    }

}