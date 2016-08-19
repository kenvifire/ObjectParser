package com.itluobo.objectparser;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;

public class ObjectParser {
    private Object object;

    public ObjectParser(Object obj) {
        this.object = obj;
    }

    public <T> T buildObject(Class<T> clazz) {
        try {
            T dest = clazz.newInstance();
            Field[] fields =  clazz.getDeclaredFields();
            for (Field field : fields)  {
                ClassFieldPath classFieldPath = field.getAnnotation(ClassFieldPath.class);
                FieldPath fieldPath = field.getAnnotation(FieldPath.class);
                //TODO add list support for object
                //ListFieldPath listFieldPath = field.getAnnotation(ListFieldPath.class);
                //list --> class --> field
                field.setAccessible(true);

                if(classFieldPath != null) {
                    Object value = getValue(classFieldPath.value());
                    ObjectParser currentParser = new ObjectParser(value);
                    Object result = currentParser.buildObject(field.getType());
                    ObjectUtils.setField(field,dest, result);
                }
                else if(fieldPath != null) {
                    ObjectUtils.setField(field, dest, getValue(fieldPath.value()));
                }
            }
            return dest;

        }catch (InstantiationException e) {
            throw new RuntimeException(e);
        }catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getValue(String fieldPath) {
        String[] paths = StringUtils.split(fieldPath, ".");
        Object current = this.object;
        String fieldName ;
        for (int i = 0; i < paths.length ; i++) {
            fieldName = paths[i];
            if(current == null) {
                return null;
            }
            Field field = ObjectUtils.findField(current.getClass(),fieldName);
            if(field == null) {
                return null;
            }
            field.setAccessible(true);
            current = ObjectUtils.getField(field, current);
        }
        return current;
    }
}
