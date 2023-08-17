package io.github.lancelothuxi.mock.agent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InterceptorClassLoader extends ClassLoader {

    public InterceptorClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bt = loadClassData(name);
        return defineClass(name, bt, 0, bt.length);
    }


    private byte[] loadClassData(String className) {
        //read class
        InputStream is = getClass().getClassLoader().getResourceAsStream(className.replace(".", "/") + ".class");
        ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
        //write into byte
        int len = 0;
        try {
            while ((len = is.read()) != -1) {
                byteSt.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //convert into byte array
        return byteSt.toByteArray();
    }

}
