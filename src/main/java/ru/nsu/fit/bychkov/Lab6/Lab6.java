package ru.nsu.fit.bychkov.Lab6;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Lab6 {
    static class CustomClassLoader extends ClassLoader {
        public Class findClass(String path) {
            byte buffer[];
            try {
                buffer = Files.readAllBytes(Paths.get(path));
                return defineClass(null, buffer, 0, buffer.length);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

    }
    static List<Class>getClasses(String path) {
        CustomClassLoader classLoader = new CustomClassLoader();
        return Arrays.stream(new File(path).listFiles()).map(
            x -> classLoader.findClass(x.getAbsolutePath())
        ).collect(Collectors.toList());
    }

    static void scanClasses(List<Class> list) {
        Class[] classes = new Class[list.size()];
        list.toArray(classes);
        Arrays.stream(classes).forEach(
                clazz -> {
                    if (clazz != null) {
                        Arrays.stream(clazz.getDeclaredMethods()).forEach(
                                method -> {
                                    if (method.getName().equals("getSecurityMessage")) {
                                        try {
                                            System.out.println("\t" + clazz.getName());
                                            System.out.println(method.invoke(clazz.newInstance()));
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                        );
                    }
                }
        );
    }

    public static void main(String[] args) {
        scanClasses(getClasses("task5/A"));
    }
}
