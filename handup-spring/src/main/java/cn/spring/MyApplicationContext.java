package cn.spring;

import cn.shenl.AppConfig;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MyApplicationContext
 * @Description TODO
 * @Author dmm
 * @Date 2021/9/9 23:31
 * @Version 1.0
 */
public class MyApplicationContext {
    private Class config;
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String, BeanDefinition>();
    private Map<String, Object> singletonObjects = new HashMap<>();

    public MyApplicationContext(Class configClazz) {
        this.config = configClazz;

        scan(configClazz);

        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (beanDefinition.getScope().equals("singleton")) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        });
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        Object instance = null;
        try {
            instance = clazz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private void scan(Class configClazz) {
        if (configClazz.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClazz.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();

            // 根据拿到的path去找文件，路径指向的的target中编译的classes文件
            String filePath = path.replace(".", "/");

            ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(filePath);
            File file = new File(resource.getFile());

            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    String absolutePath = f.getAbsolutePath();
                    String classPath = absolutePath.substring(absolutePath.indexOf("cn"), absolutePath.indexOf(".class"));
                    classPath = classPath.replace("\\", ".");
//                    System.out.println(classPath);
                    Class<?> aClass = null;
                    BeanDefinition beanDefinition = new BeanDefinition();
                    try {
                        aClass = classLoader.loadClass(classPath);
                        if (aClass.isAnnotationPresent(Component.class)) {
                            Component componentAnnotation = aClass.getAnnotation(Component.class);
                            String name = componentAnnotation.value();
                            if ("".equals(name)){
                                name = Introspector.decapitalize(aClass.getSimpleName());
                            }

                            beanDefinition.setType(aClass);
                            if (aClass.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = aClass.getAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(name, beanDefinition);
                        }
                    } catch(ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
//            System.out.println(path);
        }
    }

    public Object getBean(String beanName) {

        if ( !beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException();
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if ("singleton".equals(beanDefinition.getScope()) ){
            Object instance = singletonObjects.get(beanName);
            if (null == instance) {
                instance = createBean(beanName, beanDefinitionMap.get(beanName));
                singletonObjects.put(beanName, instance);
            }
            return instance;
        } else {
            return createBean(beanName, beanDefinition);
        }
    }
}
