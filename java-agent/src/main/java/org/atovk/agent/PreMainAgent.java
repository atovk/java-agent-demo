package org.atovk.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.springframework.web.bind.annotation.*;

import java.lang.instrument.Instrumentation;


public class PreMainAgent {

    /**
     * 在这个 premain 函数中，开发者可以进行对类的各种操作。
     * 1、agentArgs 是 premain 函数得到的程序参数，随同 “– javaagent”一起传入。与 main
     * 函数不同的是，
     * 这个参数是一个字符串而不是一个字符串数组，如果程序参数有多个，程序将自行解析这个字符串。
     * 2、Inst 是一个 java.lang.instrument.Instrumentation 的实例，由 JVM 自动传入。*
     * java.lang.instrument.Instrumentation 是 instrument 包中定义的一个接口，也是这
     * 个包的核心部分，
     * 集中了其中几乎所有的功能方法，例如类定义的转换和操作等等。
     * 类中提供两个静态方法，方法名均为premain，不能拼错。
     * 在pom文件中添加打包插件：
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        //创建一个转换器，转换器可以修改类的实现
        //ByteBuddy对java agent提供了转换器的实现，直接使用即可
        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                    TypeDescription typeDescription,
                                                    ClassLoader classLoader, JavaModule javaModule) {

                return builder
                        // 拦截任意方法
                        .method(ElementMatchers.any()
                                // 需要从配置文件加载
                                .and(ElementMatchers.isAnnotatedWith(RequestMapping.class)
                                        .or(ElementMatchers.isAnnotatedWith(GetMapping.class))
                                        .or(ElementMatchers.isAnnotatedWith(PostMapping.class))
                                        .or(ElementMatchers.isAnnotatedWith(PutMapping.class))
                                        .or(ElementMatchers.isAnnotatedWith(PatchMapping.class))
                                        .or(ElementMatchers.isAnnotatedWith(DeleteMapping.class))
                                )
                        )
                        // 拦截到的方法委托给TimeInterceptor
                        .intercept(MethodDelegation.to(MyInterceptor.class));
            }
        };


        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String typeName, ClassLoader classLoader,
                                    JavaModule module, boolean loaded) {
                //System.out.println("onDiscovery");
            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader,
                                         JavaModule module, boolean loaded, DynamicType dynamicType) {
                //System.out.println("onDiscovery");
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader,
                                  JavaModule module, boolean loaded) {
                //System.out.println("onIgnored");
            }

            @Override
            public void onError(String typeName, ClassLoader classLoader,
                                JavaModule module, boolean loaded, Throwable throwable) {
                //System.out.println("onError");
            }

            @Override
            public void onComplete(String typeName, ClassLoader classLoader,
                                   JavaModule module, boolean loaded) {
                //System.out.println("onComplete");
            }
        };


        new AgentBuilder // Byte Buddy专门有个AgentBuilder来处理Java Agent的场景
                .Default()

                // 根据包名前缀拦截类
                .type(ElementMatchers.nameStartsWith("org.atovk.web.controller")
                        .or(ElementMatchers.nameStartsWith("org.atovk.web.controller"))
                )
                // 拦截到的类由transformer处理
                .transform(transformer)
                .with(listener)
                .installOn(inst);

    }

    /**
     * 如果不存在 premain(String agentArgs, Instrumentation inst)
     * 则会执行 premain(String agentArgs)
     *
     * @param agentArgs
     */
    public static void premain(String agentArgs) {
        System.out.println("=========premain方法执行2========");
        System.out.println(agentArgs);
    }


    /**
     * 动态 attach 方式启动，运行此方法
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("attach agentmain");
    }


}