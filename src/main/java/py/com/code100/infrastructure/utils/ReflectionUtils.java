package py.com.code100.infrastructure.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

public class ReflectionUtils {

    public static Class<?> getParameterizedClass(Class<?> _class, int position) {
        return (Class<?>) ((ParameterizedType) _class.getGenericSuperclass())
                .getActualTypeArguments()[position];
    }

    /**
     * Obtiene los campos del objeto que tienen valor null
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> {
                    try {
                        return wrappedSource.getPropertyValue(propertyName) == null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toArray(String[]::new);
    }

    /**
     * Retorna true si la c1 o alguna de sus clases o interfaz padre, extiende de c2 o implementa la interfaz c2
     * @param c1
     * @param c2
     * @return
     */
    public static int subClassDepth(Class c1, Class c2) {
        int i = -1;
        while (c2.isAssignableFrom(c1)) {
            c1 = c1.getSuperclass();
            i++;
        }
        return i;
    }

    /**
     * Obtiene el valor de un atributo perteneciente al objeto obj
     * invocando al metodo getter "getAttrName()" según el nombre del atributo.
     * Soporta que el nombre del atributo sea una cadena de campos separada por
     * punto
     *
     * @param obj      Objeto
     * @param attrName Atributo
     * @return Retorna el valor del campo
     * @throws Exception Lanza una excepcion
     */
    @SneakyThrows
    public static Object getAttribute(Object obj, String attrName) {
        String[] fieldParts = attrName.split("\\.");
        String methodName = null;
        Method m;
        Object result = obj;
        for (String fieldPart : fieldParts) {
            Class<?> resultClass = result.getClass();
            Field f = getFieldByName(obj.getClass(), attrName);
            m = null;
            if (f != null) {
                if (f.getType() == Boolean.class || f.getType() == boolean.class) {
                    methodName = String.format("is%s", TextUtils.capitalize(fieldPart));
                    m = findMethod(resultClass, methodName);
                }
                if (m == null) {
                    methodName = String.format("get%s", TextUtils.capitalize(fieldPart));
                    m = resultClass.getMethod(methodName, new Class[0]);
                }
                result = m.invoke(result);
            } else {
                methodName = String.format("get%s", TextUtils.capitalize(fieldPart));
                try {
                    m = resultClass.getMethod(methodName, new Class[0]);
                }
                catch (NoSuchMethodException e) {
                    methodName = String.format("is%s", TextUtils.capitalize(fieldPart));
                    try {
                        m = resultClass.getMethod(methodName, new Class[0]);
                    }
                    catch (NoSuchMethodException ex) {
                        throw new RuntimeException("No se pudo obtener el atributo " + attrName + " para la clase " + obj.getClass().getName());
                    }
                }
                result = m.invoke(result);
            }
            if (result == null) return null;
        }
        return result;
    }

    /**
     * Obtiene el valor de un atributo perteneciente al objeto obj
     * invocando al metodo getter "getAttrName()".
     * Soporta que el nombre del atributo sea una cadena de campos separada por
     * punto
     * @param obj
     * @param field
     * @return
     */
    @SneakyThrows
    public static Object getAttribute(Object obj, Field field) {
        String methodName = null;
        Method m = null;
        Object result = obj;
        Class<?> resultClass = result.getClass();
        if (field != null) {
            if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                methodName = String.format("is%s", TextUtils.capitalize(field.getName()));
                m = findMethod(resultClass, methodName);
            }
            if (m == null) {
                methodName = String.format("get%s", TextUtils.capitalize(field.getName()));
                m = resultClass.getMethod(methodName, new Class[0]);
            }
            result = m.invoke(result);
        }
        return result;
    }

    public static Object replaceNullValueForPrimitiveType(Class objClass, String attrName, Object value) {
        Field field = getFieldByName(objClass, attrName);
        if (field.getType().isPrimitive() && value == null) {
            if (field.getType().equals(boolean.class))
                return false;
            if (field.getType().equals(int.class))
                return -1;
            if (field.getType().equals(float.class))
                return -1.0;
            if (field.getType().equals(long.class))
                return -1;
            if (field.getType().equals(double.class))
                return -1.0;
        }
        return value;
    }

    /**
     * Actualiza el valor de un atributo perteneciente al objeto obj
     * invocando al metodo setter "setAttrName(value)" segun el nombre del atributo
     *
     * @param obj      Objeto
     * @param attrName Nombre del atributo
     * @param value    Valor
     * @throws Exception Lanza una excepcion
     */
    @SneakyThrows
    public static void setAttribute(Object obj, String attrName, Object value) {
        String[] fieldParts = attrName.split("\\.");
        if (fieldParts.length == 1)
            setAttributeAux(obj, attrName, value);
        else {
            Object realObj = obj;
            for (int i = 0; i < fieldParts.length - 1; i++) {
                String fieldName = fieldParts[i];
                Field field = getFieldByName(realObj.getClass(), fieldName);
                Object fieldValue = getAttribute(realObj, field);
                if (fieldValue == null) {
                    fieldValue = field.getType().getDeclaredConstructor().newInstance();
                }
                setAttributeAux(realObj, fieldName, fieldValue);
                realObj = fieldValue;
            }
            setAttributeAux(realObj, fieldParts[fieldParts.length - 1], value);
        }
    }

    @SneakyThrows
    private static void setAttributeAux(Object obj, String attrName, Object value) {
        String methodName = getFieldAccessorMethod("set", attrName);
        value = replaceNullValueForPrimitiveType(obj.getClass(), attrName, value);
        Method m = findMethod(obj, methodName);
        m.invoke(obj, value);
    }

    public static Method findMethod(Object obj, String methodName) {
        if (obj == null)
            return null;
        return findMethod(obj.getClass(), methodName);
    }

    public static Method findMethod(Class cls, String methodName) {
        for (Method m : cls.getMethods()) {
            if (m.getName().equals(methodName))
                return m;
        }
        return null;
    }

    public static Object invokeMethod(Object obj, String methodName) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method m = findMethod(obj, methodName);
        return invokeMethod(obj, m);
    }

    @SneakyThrows
    public static Object invokeMethod(Object obj, String methodName, Object... args) {
        Method m = findMethod(obj, methodName);
        return invokeMethod(obj, m, args);
    }

    public static <T> T invokeMethod(Object obj, Method method) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (method != null)
            return (T) method.invoke(obj, new Object[]{});
        else
            throw new NoSuchMethodException();
    }

    public static Object invokeMethod(Object obj, Method method, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (method != null)
            return method.invoke(obj, args);
        else
            throw new NoSuchMethodException();
    }

    private static String getFieldAccessorMethod(String accesorPrefix, String fieldName) {
        String firstChar = String.valueOf(fieldName.charAt(0)).toUpperCase();
        fieldName = (fieldName.length() > 0) ? firstChar + fieldName.substring(1) : firstChar;
        return accesorPrefix + fieldName;
    }

    public static Optional<Method> findSetterMethod(Class cls, Field field) {
        return Optional.ofNullable(findMethod(cls, getFieldAccessorMethod("set", field.getName())));
    }

    public static Optional<Method> findGetterMethod(Class cls, Field field) {
        Method method = findMethod(cls, getFieldAccessorMethod("get", field.getName()));
        if (method == null && (field.getType() == Boolean.class || field.getType() == boolean.class)) {
            method = findMethod(cls, getFieldAccessorMethod("is", field.getName()));
        }
        return Optional.ofNullable(method);
    }


    public static Field[] getAllFields(Class cls) {
        // Devuelve los campos declarados en la clase y sus ancestros
        ArrayList<Field> fields = new ArrayList<Field>();
        while (cls != null) {
            fields.addAll(0, Arrays.asList(cls.getDeclaredFields()));
            cls = cls.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }

    public static Field[] getFieldsAnnotatedBy(Class cls, Class annotationClass) {
        Field[] allFields = getAllFields(cls);
        ArrayList<Field> fields = new ArrayList<Field>();
        for (Field field : allFields) {
            if (field.getAnnotation(annotationClass) != null)
                fields.add(field);
        }
        return fields.toArray(new Field[0]);
    }

    public static Field[] getFieldsNotAnnotatedBy(Class cls, Class annotationClass) {
        Field[] allFields = getAllFields(cls);
        ArrayList<Field> fields = new ArrayList<Field>();
        for (Field field : allFields) {
            if (field.getAnnotation(annotationClass) == null)
                fields.add(field);
        }
        return fields.toArray(new Field[0]);
    }

    public static Field getFieldByName(Class cls, String fieldName) {
        String[] fieldParts = fieldName.split("\\.");
        fieldName = fieldParts[0];

        // Devuelve el campo fieldName declarado en la clase o en sus ancestros
        Field[] fields = getAllFields(cls);
        for (Field f : fields)
            if (f.getName().equals(fieldName)) {
                if (fieldParts.length == 1)
                    return f;
                else {
                    fieldName = TextUtils.join(fieldParts, 1, ".");
                    return getFieldByName(f.getType(), fieldName);
                }
            }
        return null;
    }

    public static boolean hasField(Class cls, String fieldName) {
        return getFieldByName(cls, fieldName) != null;
    }

    public static Field getFieldByType(Class cls, Class fieldType) {
        // Devuelve el primer campo con el tipo especificado
        Field[] fields = getAllFields(cls);
        for (Field f : fields)
            if (f.getType().equals(fieldType))
                return f;
        return null;
    }

    public static Class getFieldOrMethodClass(Class cls, String fieldOrMethodName) throws NoSuchMethodException {
        Field field = getFieldByName(cls, fieldOrMethodName);
        Method m;
        if (field != null)
            return field.getType();
        else {
            m = findMethod(cls, "get" + fieldOrMethodName);
            return m.getReturnType();
        }
    }

    public static boolean hasAnnotation(Class klass, Class annotationClass) {
        if (klass == null) return false;
        return klass.isAnnotationPresent(annotationClass);
    }

    public static boolean hasAnnotationInDepth(Class klass, Class annotationClass) {
        if (klass == null) return false;
        else if (hasAnnotation(klass, annotationClass)) return true;
        else return hasAnnotationInDepth(klass.getSuperclass(), annotationClass);
    }

    public static Annotation getAnnotation(Class klass, Class annotationClass) {
        if (klass == null) return null;
        return klass.isAnnotationPresent(annotationClass)
                ? klass.getAnnotation(annotationClass)
                : null;
    }

    public static Annotation getAnnotationInDepth(Class klass, Class annotationClass) {
        if (klass == null) return null;
        else if (klass.isAnnotationPresent(annotationClass)) return klass.getAnnotation(annotationClass);
        else return getAnnotationInDepth(klass.getSuperclass(), annotationClass);
    }

    public static List getEnumValues(Class enumClass) {
        List enumValues = new ArrayList();
        for (Field field : enumClass.getDeclaredFields()) {
            if (!field.getName().contains("$")) {
                try {
                    enumValues.add(Enum.valueOf(enumClass, field.getName()));
                } catch (Exception e) {
                    //Do nothing
                    //Aquí se ignoran los métodos que puede tener un enumerador
                }
            }
        }
        return enumValues;
    }

    public static List<Class> getNumerics() {
        return new ArrayList<Class>(Arrays.asList(int.class,
                double.class, float.class, long.class, short.class, byte.class,
                Integer.class, Double.class, Float.class, Long.class, Short.class,
                Byte.class, Number.class, BigInteger.class, BigDecimal.class));
    }

    public static boolean isNumeric(Class _class) {
        return getNumerics().contains(_class);
    }

    public static boolean isBoolean(Class _class) {
        return new ArrayList<Class>(Arrays.asList(boolean.class,
                Boolean.class)).contains(_class);
    }

    public static boolean isDateOrTime(Class _class) {
        return new ArrayList<Class>(Arrays.asList(Date.class,
                LocalDate.class, LocalTime.class, LocalDateTime.class)).contains(_class);
    }

    public static boolean isCollection(Class _class) {
        return Collection.class.isAssignableFrom(_class);
    }

    public static boolean isDecimal(Class _class) {
        List<Class> classes = new ArrayList<Class>(Arrays.asList(double.class,
                float.class, long.class, Double.class, Float.class, BigDecimal.class));
        return classes.contains(_class);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getRealTypeFromString(String valueStr, Class<T> returnType) throws Exception {
        if (valueStr == null)
            return null;
        if (returnType.equals(String.class))
            return (T) valueStr;
        if (returnType.equals(int.class) || returnType.equals(Integer.class))
            return (T) Integer.valueOf(valueStr);
        else if (returnType.equals(double.class) || returnType.equals(Double.class))
            return (T) Double.valueOf(valueStr);
        else if (returnType.equals(short.class) || returnType.equals(Short.class))
            return (T) Short.valueOf(valueStr);
        else if (returnType.equals(float.class) || returnType.equals(Float.class))
            return (T) Float.valueOf(valueStr);
        else if (returnType.equals(long.class) || returnType.equals(Long.class))
            return (T) Long.valueOf(valueStr);
        else if (returnType.equals(boolean.class) || returnType.equals(Boolean.class)) {
            if (Arrays.asList("true", "false").contains(valueStr.toLowerCase()))
                return (T) Boolean.valueOf(valueStr);
        } else if (returnType.equals(BigInteger.class))
            return (T) new BigInteger(valueStr);
        else if (returnType.equals(BigDecimal.class))
            return (T) new BigDecimal(valueStr);
        else if (returnType.isEnum()) {
            return (T) Enum.valueOf((Class<? extends Enum>) returnType, valueStr);
        } else if (returnType.equals(char.class) || returnType.equals(Character.class))
            return (T) Character.valueOf(valueStr.charAt(0));
        throw new Exception("Tipo No Soportado");
    }

    public static Type[] getParameterizedTypesOfClass(Class<?> _class) {
        if (_class.getGenericSuperclass() instanceof ParameterizedType)
            return ((ParameterizedType) _class.getGenericSuperclass()).getActualTypeArguments();
        return new Type[]{};
    }

    /**
     * Crea objecto clonado
     *
     * @param obj Objecto a clonar
     * @param <T> Clase parametrizada
     * @return Retorna objecto clonado
     * @throws Exception Lanza una excepcion
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static <T> T clone(T obj) {
        if (obj instanceof List) {
            List copy = new ArrayList();
            for (Object item : (List) obj) {
                copy.add(cloneAux(item));
            }
            return (T) copy;
        }
        return cloneAux(obj);
    }

    @SneakyThrows
    private static <T> T cloneAux(T obj) {
        ObjectMapper mapper = JsonUtils.getObjectMapper();
        byte[] bytes = mapper.writeValueAsBytes(obj);
        return (T) mapper.readValue(bytes, obj.getClass());
    }
}
