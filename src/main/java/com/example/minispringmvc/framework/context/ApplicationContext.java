package com.example.minispringmvc.framework.context;

import com.example.minispringmvc.framework.annotation.Autowired;
import com.example.minispringmvc.framework.annotation.Controller;
import com.example.minispringmvc.framework.annotation.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * @author kangdongyang
 * @version 1.0
 * @description:  应用上下文 -- 提供bean注册、依赖注入、配置解析功能
 * @date 2024/8/13 10:31
 */
public class ApplicationContext {
    /**
     * 存放示例对象的map
     */
    private Map<String, Object> instanceMapping = new HashMap<>();
    /**
     * 存放class信息的list
     */
    private List<String> classCache = new ArrayList<>();
    /**
     * 读取配置文件的properties
     */
    private Properties config = new Properties();

    /**
     * 构造方法：
     * 传入一个配置文件路径，对IOC进行初始化
     * @author JiaoFanTing
     */
    public ApplicationContext() {
        InputStream is;

        try {
            is = new FileInputStream("F:\\DemoWorkSpace\\mini-springmvc\\src\\main\\resources\\application.properties");
            config.load(is);
            // 2. 获取配置属性-- 扫描的包
            String packageName = config.getProperty("scanPackage");
            // 3. 注册
            doRegister(packageName);
            // 4. 初始化IOC
            doCreateBean();
            // 5. 依赖注入
            populate();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("IOC 容器已经初始化");

    }

    /**
     *   依赖注入
     */
    private void populate() {
        // 1. 判断IOC容器是否为空
        if (instanceMapping.isEmpty()){
            return;
        }
        // 2. 遍历 每个bean的字段
        for (Map.Entry<String,Object> entry: instanceMapping.entrySet()){
            // 获取每个bean的fields
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            // 遍历判断是否需要依赖注入
            for (Field field: fields){
                if (field.isAnnotationPresent(Autowired.class)){
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String id = autowired.value().trim();
                    if (id.equals("")){
                        // 如果用户没有自定义bean名， 则默认用类型来注入
                        id = field.getType().getName();
                    }
                    field.setAccessible(true);
                    try {
                        // entry.getValue() 代表的就是当前的类对象
                        field.set(entry.getValue(), instanceMapping.get(id));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     *  根据包名注册bean
     * @author JiaoFanTing
     * @param packageName 包名
     **/
    private void doRegister(String packageName) {
        // 1. 根据包名获取到 资源路径， 以便递归加载类信息
        URL resource = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        // 2. 递归加载类信息
        File files = new File(Objects.requireNonNull(resource).getFile());
        for (File file : Objects.requireNonNull(files.listFiles())) {
            // 判断是否是文件夹
            if (file.isDirectory()) {
                // 如果是文件夹，递归调用
                doRegister(packageName + "." + file.getName());
            } else {
                // 如果是文件， 将class去掉 ，把全限定文件名 放入classCache
                classCache.add(packageName + "." + file.getName().replaceAll(".class", "").trim());
            }
        }
    }

    /**
     * 创建bean
     *
     **/
    private void doCreateBean() {
        // 1. 检查是否有类信息注册缓存
        if (classCache == null) {
            return;
        }

        // 2. 遍历classCache 并创建实例 存放到 instanceMapping
        for (String className : classCache) {
            // 反射加载类
            try {
                Class<?> clazz = Class.forName(className);
                // 加了需要加入IOC容器的注解，才进行初始化
                if (clazz.isAnnotationPresent(Controller.class)){
                    // 类的首字母小写，并存入 instanceMapping
                    String id = firstCharToLower(clazz.getSimpleName());
                    instanceMapping.put(id, clazz.newInstance());
                }else if (clazz.isAnnotationPresent(Service.class)){
                    // service注解就有了 用户自定义名字的处理
                    Service service = clazz.getAnnotation(Service.class);
                    if(!service.value().equals("")){
                        instanceMapping.put(service.value(), clazz.newInstance());
                        continue;
                    }
                    // 再加载其接口类
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i: interfaces){
                        instanceMapping.put(i.getName(), clazz.newInstance());
                    }
                }

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

    }
    private String firstCharToLower(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return Arrays.toString(chars);
    }

    /**
     * 获取所有实例对象
     */
    public Map<String,Object> getAll(){
        return instanceMapping;
    }


    /**
     * 获取配置类对象
     */
    public Properties getConfig() {
        return config;
    }

}
