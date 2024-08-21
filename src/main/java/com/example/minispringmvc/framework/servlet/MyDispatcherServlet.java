//package com.example.minispringmvc.framework.servlet;
//
//import com.example.minispringmvc.framework.annotation.Controller;
//import com.example.minispringmvc.framework.annotation.RequestMapping;
//
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Method;
//import java.net.URL;
//import java.util.*;
//
///**
// * @author kangdongyang
// * @version 1.0
// * @description:
// * @date 2024/8/19 23:07
// */
//public class MyDispatcherServlet   extends HttpServlet {
//    private Properties properties = new Properties();
//    private List<String> classNames = new ArrayList<>();
//    private Map<String, Object> ioc = new HashMap<>();
//    private Map<String, Method> handlerMapping = new HashMap<>();
//    private Map<String, Object> controllerMap = new HashMap<>();
//
//    /**
//     33      * 初始化阶段
//     34      */
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//
//            // 1.加载配置文件
//            doLoadConfig(config.getInitParameter("contextConfigLocation"));
//
//            // 2.初始化所有相关联的类,扫描用户设定的包下面所有的类
//            doScanner(properties.getProperty("scanPackage"));
//
//            // 3.拿到扫描到的类,通过反射机制,实例化,并且放到ioc容器中(k-v beanName-bean) beanName默认是首字母小写
//            doInstance();
//
//            // 4.初始化HandlerMapping(将url和method对应上)
//            initHandlerMapping();
//
//        }
//
//  // 1.加载配置文件
//  private void doLoadConfig(String location) {
//      // 把web.xml中的contextConfigLocation对应value值的文件加载到流里面
//      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(location);
//      try {
//          // 用Properties文件加载文件里的内容
//          properties.load(resourceAsStream);
//      } catch (IOException e) {
//          e.printStackTrace();
//      } finally {
//          // 关流
//          if (null != resourceAsStream) {
//              try {
//                  resourceAsStream.close();
//              } catch (IOException e) {
//                  e.printStackTrace();
//              }
//          }
//      }
//
//  }
//
//
// // 2.初始化所有相关联的类,扫描用户设定的包下面所有的类
// private void doScanner(String packageName) {
//     // 把所有的.替换成/
//     URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
//     File dir = new File(url.getFile());
//     for (File file : dir.listFiles()) {
//         if (file.isDirectory()) {
//             // 递归读取包
//             doScanner(packageName + "." + file.getName());
//         } else {
//             String className = packageName + "." + file.getName().replace(".class", "");
//             classNames.add(className);
//         }
//     }
// }
//
//    // 3.拿到扫描到的类,通过反射机制,实例化,并且放到ioc容器中(k-v beanName-bean) beanName默认是首字母小写
//    private void doInstance() {
//        if (classNames.isEmpty()) {
//            return;
//        }
//        for (String className : classNames) {
//            try {
//                // 把类搞出来,反射来实例化(只有加@MyController需要实例化)
//                Class<?> clazz = Class.forName(className);
//                if (clazz.isAnnotationPresent(Controller.class)) {
//                    ioc.put(clazz.getSimpleName(), clazz.newInstance());
//                } else {
//                    continue;
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
//    }
//
//
//    // 4.初始化HandlerMapping(将url和method对应上)
//    private void initHandlerMapping() {
//        if (ioc.isEmpty()) {
//            return;
//        }
//        try {
//            for (Map.Entry<String, Object> entry : ioc.entrySet()) {
//                Class<? extends Object> clazz = entry.getValue().getClass();
//                if (!clazz.isAnnotationPresent(Controller.class)) {
//                    continue;
//                }
//
//                // 拼url时,是controller头的url拼上方法上的url
//                String baseUrl = "";
//                if (clazz.isAnnotationPresent(RequestMapping.class)) {
//                    RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
//                    baseUrl = annotation.value();
//                }
//                Method[] methods = clazz.getMethods();
//                for (Method method : methods) {
//                    if (!method.isAnnotationPresent(RequestMapping.class)) {
//                        continue;
//                    }
//                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
//                    String url = annotation.value();
//
//                    url = (baseUrl + "/" + url).replaceAll("/+", "/");
//                    handlerMapping.put(url, method);
//                    controllerMap.put(url, clazz.newInstance());
//                    System.out.println(url + "," + method);
//                }
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    /**
//     53      * 运行阶段
//     54      * 每一次请求将会调用doGet或doPost方法，所以统一运行阶段都放在doDispatch方法里处理
//     55      */
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        this.doPost(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try {
//            // 处理请求
//            doDispatch(req, resp);
//        } catch (Exception e) {
//            resp.getWriter().write("500!! Server Exception");
//        }
//
//    }
//
//    //每次请求都来这里
//    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//        //handlerMapping这个hashmap中存放的是   url 和  method 的键值对（即哪个url对应哪个方法），如果为空表示没有方法来处理请求
//        if (handlerMapping.isEmpty()) {
//            return;
//        }
//
//        //获得请求的url和上下文，把url中的上下文去掉
//        String url = req.getRequestURI();
//        System.out.println("请求中的url参数： " + url);
//        String contextPath = req.getContextPath();
//        System.out.println("请求中的contextPath上下文参数： " + contextPath);
//        url = url.replace(contextPath, "").replaceAll("/+", "/");
//        System.out.println("把url中的上下文去掉得到新的url即为我们程序中标识方法的路径： " + url);
//
//        if (!this.handlerMapping.containsKey(url)) {
//            resp.getWriter().write("404 NOT FOUND!");
//            return;
//        }
//
//        //根据url请求去HandlerMapping中匹配到对应的Method
//        Method method = this.handlerMapping.get(url);
//
//        // 获取方法的参数列表
//        Class<?>[] parameterTypes = method.getParameterTypes();
//
//        // 获取请求的参数
//        Map<String, String[]> parameterMap = req.getParameterMap();
//
//        // 保存参数值
//        Object[] paramValues = new Object[parameterTypes.length];
//
//        // 方法的参数列表
//        for (int i = 0; i < parameterTypes.length; i++) {
//            // 根据参数名称，做某些处理
//            String requestParam = parameterTypes[i].getSimpleName();
//
//            if (requestParam.equals("HttpServletRequest")) {
//                // 参数类型已明确，这边强转类型
//                paramValues[i] = req;
//                continue;
//            }
//            if (requestParam.equals("HttpServletResponse")) {
//                paramValues[i] = resp;
//                continue;
//            }
//            if (requestParam.equals("String")) {
//                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
//                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
//                    paramValues[i] = value;
//                }
//            }
//        }
//        // 利用反射机制来调用
//        try {
//            method.invoke(this.controllerMap.get(url), paramValues);// 第一个参数是method所对应的实例
//            // 在ioc容器中
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
