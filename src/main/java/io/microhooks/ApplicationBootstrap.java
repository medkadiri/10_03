package io.microhooks;

import java.io.IOException;

import javax.persistence.EntityListeners;

import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;

public class ApplicationBootstrap implements net.bytebuddy.build.Plugin {

    public static class Load extends ClassLoader{
        public Class findClass(String name, ClassFileLocator classFileLocator) {
            byte[] b;
            try {
                b = classFileLocator.locate(name).resolve();
                return defineClass(name, b, 0, b.length);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public
    boolean matches(TypeDescription target) {
        return true;
    }

    @Override
    public
    DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
        boolean b = typeDescription.getInheritedAnnotations().toString().contains("@io.microhooks.ddd.Source");
        Class trackable = null, sourceListener = null, customListener = null;

        try{
            Load l = new Load();
            trackable = l.findClass("io.microhooks.ddd.Trackable", classFileLocator);//classFileLocator.locate("io.microhooks.ddd.Trackable").getClass();
            sourceListener = l.findClass("io.microhooks.ddd.internal.SourceListener", classFileLocator);
            customListener = l.findClass("io.microhooks.ddd.internal.CustomListener", classFileLocator);
            //System.out.println("trackable: " + builder.getClass().getCanonicalName());//trackable.toString());
        }catch(Exception e){
            System.out.println("e2: " + e);
        }

        System.out.println("Transforming " + b);
        if(b){
            System.out.println("1");
            System.out.println(trackable);
            System.out.println("2");
            //builder = builder.implement(Trackable.class);
            builder = builder.implement(trackable).annotateType(AnnotationDescription.Builder.ofType(EntityListeners.class)
                            .defineTypeArray("value", sourceListener,
                                    customListener).build());//.make().load(trackable.getClassLoader());//defineField("trackedFields", Map<String, Object>, Modifier.PUBLIC | Modifier.TRANSIENT).value(new HashMap<>());
            System.out.println("Hello " + typeDescription.getDeclaredFields());
            return builder;//.implement(Trackable.class).defineField("trackedFields", Map<String, Object>, Modifier.PUBLIC);
        }
        else
            return builder;
    }

    @Override
    public
    void close() throws IOException {

    }
}