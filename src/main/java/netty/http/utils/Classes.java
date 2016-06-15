package netty.http.utils;

import org.springframework.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by zxw on 2016/1/13.
 */
public final class Classes {

    private Classes() {
    }

    /**
     *
     * <p>比较参数类型是否一致</p>
     *
     * @param types asm的类型({@link Type})
     * @param clazzes java 类型({@link Class})
     * @return
     */
    private static boolean sameType(Type[] types, Class<?>[] clazzes) {
        // 个数不同
        if (types.length != clazzes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if(!Type.getType(clazzes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * <p>获取方法的参数名</p>
     *
     * @param m
     * @return
     */
    public static String[] getMethodParamNames(final Method m) {
        final String[] paramNames = new String[m.getParameterTypes().length];
        final String n = m.getDeclaringClass().getName();
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassReader cr = null;
        try {
            cr = new ClassReader(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cr.accept(new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public MethodVisitor visitMethod(final int access,
                                             final String name, final String desc,
                                             final String signature, final String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(desc);
                // 方法名相同并且参数个数相同
                if (!name.equals(m.getName())
                        || !sameType(args, m.getParameterTypes())) {
                    return super.visitMethod(access, name, desc, signature,
                            exceptions);
                }
                MethodVisitor v = cv.visitMethod(access, name, desc, signature,
                        exceptions);
                return new MethodVisitor(Opcodes.ASM4, v) {
                    @Override
                    public void visitLocalVariable(String name, String desc,
                                                   String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if(Modifier.isStatic(m.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < paramNames.length) {
                            paramNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start,
                                end, index);
                    }

                };
            }
        }, 0);
        return paramNames;
    }

    public static <T>  T getValue(String value, Class<T> type){
        T result = null;
        if (type == Boolean.TYPE || type == Boolean.class) {        // boolean
            if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
                result = (T)Boolean.valueOf(value);
            }
            if(result == null && type == Boolean.TYPE){
                result = (T)Boolean.FALSE;
            }
        } else if (type == Character.TYPE || type == Character.class) { // char
            if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
                result = (T)(Character.valueOf(value.charAt(0)));
            }
            if(result == null && type == Character.TYPE){
                result = (T)Character.valueOf('\0');
            }
        } else if (type == Byte.TYPE || type == Byte.class) {    // byte
            if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
                result = (T)Byte.valueOf(value);
            }
            if(result == null && type == Byte.TYPE){
                result = (T)Byte.valueOf((byte)0);
            }
        } else if (type == Short.TYPE || type == Short.class) {   // short
            if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
                result = (T)Short.valueOf(value);
            }
            if(result == null && type == Short.TYPE){
                result = (T)Short.valueOf((short)0);
            }
        } else if (type == Integer.TYPE || type == Integer.class) { // int
            if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
                result = (T)Integer.valueOf(value);
            }
            if(result == null && type == Integer.TYPE){
                result = (T)Integer.valueOf(0);
            }
        } else if (type == Long.TYPE || type == Long.class) {    // long
            if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
                result = (T)Long.valueOf(value);
            }
            if(result == null && type == Long.TYPE){
                result = (T)Long.valueOf(0);
            }
        } else if (type == Float.TYPE || type == Float.class) {   // float
            if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
                result = (T)Float.valueOf(value);
            }
            if(result == null && type == Float.TYPE){
                result = (T)Float.valueOf(0);
            }
        } else if (type == Double.TYPE || type == Double.class) {  // double
            if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
                result = (T)Double.valueOf(value);
            }
            if(result == null && type == Double.TYPE){
                result = (T)Double.valueOf(0);
            }
        }else if(type == String.class){
            if(value != null){
                result = (T)value;
            }
        }else{
            throw new IllegalArgumentException("不能使用非基本类型，" + type.getName());
        }
        return result;
    }

}
