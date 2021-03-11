package com.wujiuye.hotkit.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 反射为对象设置值
 *
 * @author wujiuye 2020/03/30
 */
public class ReflectUtils {

    private final static Class<?>[] PRIMITIVE_CLASS = new Class[]{
            boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class,
            Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
            String.class, BigDecimal.class, Date.class
    };

    public static class ValueOfTypeException extends RuntimeException {
        public ValueOfTypeException(Class<?> tclass, Class<?> vclass) {
            super("value type is " + vclass.getName() + ", cannot be converted to " + tclass.getName());
        }
    }

    public static boolean isPrimitiveType(Class<?> tclass) {
        for (Class<?> c : PRIMITIVE_CLASS) {
            if (c == tclass) {
                return true;
            }
        }
        return false;
    }

    public static void applyValueBy(Object obj, Field field, Object value) throws IllegalAccessException, ValueOfTypeException {
        Class<?> fieldType = field.getType();
        field.setAccessible(true);
        if (Integer.class.equals(fieldType)) {
            field.set(obj, getInt(value));
        } else if (Long.class.equals(fieldType)) {
            field.set(obj, getLong(value));
        } else if (Double.class.equals(fieldType)) {
            field.set(obj, getDouble(value));
        } else if (Float.class.equals(fieldType)) {
            field.set(obj, getFloat(value));
        } else if (BigDecimal.class.equals(fieldType)) {
            field.set(obj, getBigDecimal(value));
        } else if (String.class.equals(fieldType)) {
            field.set(obj, value.toString());
        } else if (Date.class.equals(fieldType)) {
            field.set(obj, getDate(value));
        } else {
            throw new ValueOfTypeException(fieldType, value.getClass());
        }
    }

    public static <T> T getValueOfType(Object value, Class<T> tClass) throws ValueOfTypeException {
        if (Integer.class.equals(tClass)) {
            return (T) getInt(value);
        } else if (Long.class.equals(tClass)) {
            return (T) getLong(value);
        } else if (Double.class.equals(tClass)) {
            return (T) getDouble(value);
        } else if (Float.class.equals(tClass)) {
            return (T) getFloat(value);
        } else if (BigDecimal.class.equals(tClass)) {
            return (T) getBigDecimal(value);
        } else if (String.class.equals(tClass)) {
            return (T) value.toString();
        } else if (Date.class.equals(tClass)) {
            return (T) getDate(value);
        } else {
            throw new ValueOfTypeException(tClass, value.getClass());
        }
    }

    private static Integer getInt(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else if (value instanceof Double || value instanceof Float) {
            return new BigDecimal(value.toString()).intValue();
        } else if (value instanceof Long) {
            return ((Long) value).intValue();
        } else {
            return null;
        }
    }

    private static Long getLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof String) {
            return Long.parseLong((String) value);
        } else if (value instanceof Double || value instanceof Float) {
            return new BigDecimal(value.toString()).longValue();
        } else if (value instanceof Integer) {
            return Long.valueOf(value.toString());
        } else {
            return null;
        }
    }

    private static Float getFloat(Object value) {
        if (value instanceof Integer) {
            return Float.parseFloat(value.toString());
        } else if (value instanceof String) {
            return Float.parseFloat((String) value);
        } else if (value instanceof Double || value instanceof Float) {
            return new BigDecimal(value.toString()).floatValue();
        } else if (value instanceof Long) {
            return ((Long) value).floatValue();
        } else {
            return null;
        }
    }

    private static Double getDouble(Object value) {
        if (value instanceof Integer) {
            return Double.parseDouble(value.toString());
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else if (value instanceof Double || value instanceof Float) {
            return new BigDecimal(value.toString()).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else {
            return null;
        }
    }

    private static BigDecimal getBigDecimal(Object value) {
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        } else if (value instanceof String) {
            return new BigDecimal((String) value);
        } else {
            return null;
        }
    }

    private static Date getDate(Object value) {
        if (value instanceof Long) {
            return new Date((Long) value);
        } else if (value instanceof String) {
            String v = (String) value;
            SimpleDateFormat sdf;
            if (v.contains("HH")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            try {
                return sdf.parse(v);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static String getMethodDescriptor(final Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        Class<?>[] parameters = method.getParameterTypes();
        for (Class<?> parameter : parameters) {
            appendDescriptor(parameter, stringBuilder);
        }
        stringBuilder.append(')');
        appendDescriptor(method.getReturnType(), stringBuilder);
        return stringBuilder.toString();
    }

    private static void appendDescriptor(final Class<?> clazz, final StringBuilder stringBuilder) {
        Class<?> currentClass = clazz;
        while (currentClass.isArray()) {
            stringBuilder.append('[');
            currentClass = currentClass.getComponentType();
        }
        if (currentClass.isPrimitive()) {
            char descriptor;
            if (currentClass == Integer.TYPE) {
                descriptor = 'I';
            } else if (currentClass == Void.TYPE) {
                descriptor = 'V';
            } else if (currentClass == Boolean.TYPE) {
                descriptor = 'Z';
            } else if (currentClass == Byte.TYPE) {
                descriptor = 'B';
            } else if (currentClass == Character.TYPE) {
                descriptor = 'C';
            } else if (currentClass == Short.TYPE) {
                descriptor = 'S';
            } else if (currentClass == Double.TYPE) {
                descriptor = 'D';
            } else if (currentClass == Float.TYPE) {
                descriptor = 'F';
            } else if (currentClass == Long.TYPE) {
                descriptor = 'J';
            } else {
                throw new AssertionError();
            }
            stringBuilder.append(descriptor);
        } else {
            stringBuilder.append('L').append(getInternalName(currentClass)).append(';');
        }
    }

    public static String getInternalName(final Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }

    public static Class<?>[] getArgumentTypes(String methodDescriptor) throws ClassNotFoundException {
        List<String> types = new ArrayList<>();
        int startIndex, endIndex;
        startIndex = methodDescriptor.indexOf("(");
        endIndex = methodDescriptor.indexOf(")");
        String argumentTypes = methodDescriptor.substring(startIndex + 1, endIndex);
        for (int i = 0; i < argumentTypes.length(); ) {
            if (argumentTypes.charAt(i) == 'L') {
                int j = i + 1;
                for (; j < argumentTypes.length(); j++) {
                    if (argumentTypes.charAt(j) == ';') {
                        break;
                    }
                }
                String clName = argumentTypes.substring(i + 1, j);
                types.add(clName.replace("/", "."));
                i = j + 1;
            } else {
                types.add("" + argumentTypes.charAt(i));
                i++;
            }
        }
        Class<?>[] typeClasss = new Class<?>[types.size()];
        for (int i = 0; i < typeClasss.length; i++) {
            String currentClass = types.get(i);
            if ("I".equals(currentClass)) {
                typeClasss[i] = Integer.class;
            } else if ("V".equals(currentClass)) {
                typeClasss[i] = Void.class;
            } else if ("Z".equals(currentClass)) {
                typeClasss[i] = Boolean.class;
            } else if ("B".equals(currentClass)) {
                typeClasss[i] = Byte.class;
            } else if ("C".equals(currentClass)) {
                typeClasss[i] = Character.class;
            } else if ("S".equals(currentClass)) {
                typeClasss[i] = Short.class;
            } else if ("D".equals(currentClass)) {
                typeClasss[i] = Double.class;
            } else if ("F".equals(currentClass)) {
                typeClasss[i] = Float.class;
            } else if ("J".equals(currentClass)) {
                typeClasss[i] = Long.class;
            } else {
                typeClasss[i] = Class.forName(currentClass);
            }
        }
        return typeClasss;
    }

}
