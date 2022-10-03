package com.mkadiri;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;

import java.util.Map;
import java.io.IOException;
import java.lang.reflect.Modifier;

import java.util.HashMap;

import javax.persistence.*;

class MicrohooksPlugin implements net.bytebuddy.build.Plugin {

    public static class Load extends ClassLoader{
        public Class findClass(String name, ClassFileLocator classFileLocator) {
            byte[] b;
            try {
                b = classFileLocator.locate(name).resolve();
                return defineClass(name, b, 0, b.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean matches(TypeDescription target) {
        return true;
    }

    @Override
    public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
        boolean b = typeDescription.getInheritedAnnotations().toString().contains("@io.microhooks.ddd.Source");
        Class trackable = null;

        try{
            Load l = new Load();
            trackable = l.findClass("io.microhooks.ddd.Trackable", classFileLocator);
        }catch(Exception e){
            System.out.println("e2: " + e);
        }

        System.out.println("Transforming " + b);
        if(b){
            System.out.println("1");
            System.out.println(trackable);
            System.out.println("2");
            builder = builder.implement(trackable);
            System.out.println("Hello " + typeDescription.getDeclaredFields());
            return builder;
        }
        else
            return builder;
    }

    @Override
    public void close() throws IOException {
    }
}