package com.itluobo.objectparser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

public class JsonObjectParser {

    private static Map<Class, Function> numberParser = new HashMap<Class, Function>();

    static {
        Function<Double,Integer> intParser = new Function<Double, Integer>() {
            @Override
            public Integer apply(Double aDouble) {
                return aDouble.intValue();
            }
        };
        numberParser.put(Integer.class, intParser);
        numberParser.put(int.class, intParser);

        Function<Double,Long> longParser = new Function<Double, Long>() {
            @Override
            public Long apply(Double aDouble) {
                return aDouble.longValue();
            }
        };
        numberParser.put(Long.class, longParser);
        numberParser.put(long.class, longParser);


        Function<Double,Double> doubleParser = new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return aDouble;
            }
        };
        numberParser.put(Double.class, doubleParser);
        numberParser.put(double.class, doubleParser);

        Function<Double,Date> dateParser = new Function<Double, Date>() {
            @Override
            public Date apply(Double aDouble) {
                Date date = new Date(aDouble.longValue());
                date.setTime(aDouble.longValue());
                return date;
            }
        };
        numberParser.put(Date.class, dateParser);


    }

    private Map<String, Object> objMap;

    public JsonObjectParser(String json) {
        objMap = new Gson().fromJson(json, Map.class);
    }

    public JsonObjectParser(Map<String,Object> jsonObject) {
        objMap = jsonObject;
    }



    public Object getObject(String fieldPath) {
        String[] paths = StringUtils.split(fieldPath,".");
        Map<String,Object> currentMap = objMap;
        for (int i = 0; i < paths.length - 1; i++) {
            String path = getActualPath(paths[i]);
            int index = getIndexForPath(paths[i]) ;
            Object val = currentMap.get(path);
            if(val == null) {
                return null;
            }
            val = getIndexedValue(val,index);
            if(val instanceof  Map) {
                currentMap =  (Map<String, Object>)val;
            }else {
                throw new RuntimeException("no such field, fieldPath:" + fieldPath);
            }

        }

        if(currentMap == null) {
            throw new RuntimeException("no such field, fieldPath:" + fieldPath);
        }
        String path = getActualPath(paths[paths.length-1]);
        int index = getIndexForPath(paths[paths.length - 1]);
        return getIndexedValue(currentMap.get(path), index);

    }

    private Object getIndexedValue(Object object, int index) {
        if(index < 0) return object;
        if(object instanceof  List)  {
            return ((List)object).get(index);
        }else {
            throw new RuntimeException("indexed value is not an instance of list");
        }

    }
    private String getActualPath(String path) {
        if(path.endsWith("]")) {
            int end = path.indexOf("[");
            return path.substring(0, end);
        }
        return path;
    }

    private int getIndexForPath(String path) {
        if(path.endsWith("]")) {
            int start = path.indexOf('[');
            int end = path.indexOf(']');
            if(start < 0 || end < 0) return -1;
            return Integer.valueOf(path.substring(start+1,end).trim());
        }

        return -1;

    }

    public Integer getInt(String fieldPath) {
        Object result = getObject(fieldPath);
        if(result == null) return null;
        if (result instanceof Double) {
            //TODO 判断是否是Integer
           return ((Double) result).intValue();
        }else {
            throw new RuntimeException("field type mismatch for " + fieldPath +", actual" + result.getClass());
        }
    }

    public Boolean getBoolean(String fieldPath) {
        Object result = getObject(fieldPath);
        if(result == null) return null;
        if (result instanceof Boolean) {
            return (Boolean)result;
        }else {
            throw new RuntimeException("field type mismatch for " + fieldPath +", actual" + result.getClass());
        }
    }

    public Long getLong(String fieldPath) {
        Object result = getObject(fieldPath);
        if(result == null) return null;
        if (result instanceof Double) {
            return ((Double) result).longValue();
        }else {
            throw new RuntimeException("field type mismatch for " + fieldPath +", actual" + result.getClass());
        }
    }

    public Double getDouble(String fieldPath) {
        Object result = getObject(fieldPath);
        if(result == null) return null;
        if (result instanceof Double) {
            return (Double)result;
        }else {
            throw new RuntimeException("field type mismatch for " + fieldPath +", actual" + result.getClass());
        }
    }

    public String getString(String fieldPath) {
        Object result = getObject(fieldPath);
        if(result == null) return null;
        if (result instanceof String) {
            return (String)result;
        }else {
            throw new RuntimeException("field type mismatch for " + fieldPath +", actual" + result.getClass());
        }
    }

    public Date getDate(String fieldPath) {
        Long time = getLong(fieldPath);
        if(time == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.getTime();
    }

    public Map<String,Object> getMap(String fieldPath) {
        Object result = getObject(fieldPath);
        if(result == null) {
            return null;
        }else if(result instanceof Map) {
            return (Map<String, Object>) result;
        }else {
            throw new RuntimeException("file type mismatch for :" + fieldPath + ",actual:" + result.getClass());
        }
    }

    public List getList(String fieldPath) {
        Object result = getObject(fieldPath);
        if(result == null) {
            throw new RuntimeException("get null for " + fieldPath);
        }else if(result instanceof List) {
            return (List) result;
        }else {
            throw new RuntimeException("file type mismatch for :" + fieldPath + ",actual:" + result.getClass());
        }
    }


    public JsonArray getJsonArray(String fieldPath) {
        List obj = getList(fieldPath);
        if(obj == null) return null;

        return new Gson().toJsonTree(obj).getAsJsonArray();
    }


    public <T> T buildFromJson(Class<T> clazz) {
        try {
            T dest = clazz.newInstance();
            Field[] fields =  clazz.getDeclaredFields();
            for (Field field : fields)  {
                ListFieldPath listFieldPath = field.getAnnotation(ListFieldPath.class);
                ClassFieldPath classFieldPath = field.getAnnotation(ClassFieldPath.class);
                FieldPath fieldPath = field.getAnnotation(FieldPath.class);

                field.setAccessible(true);
                //list-> class -> field
                if(listFieldPath != null) {
                    List list = getList(listFieldPath.value());
                    Class eleClass = listFieldPath.clazz();

                    if(!eleClass.isPrimitive()
                            && eleClass!= String.class) {
                        List value = new ArrayList();
                        for (int i = 0; i < list.size(); i++) {
                            if(eleClass == Date.class) {
                                Date date = new Date((Long) list.get(i));
                                value.add(date);
                            }else {
                                JsonObjectParser entryParser = new JsonObjectParser((Map<String, Object>) list.get(i));
                                Object entryObject = entryParser.buildFromJson(eleClass);
                                value.add(entryObject);
                            }
                        }
                        field.set(dest, value);

                    }else if(eleClass.isPrimitive() || eleClass == Date.class){
                        //primitive list
                        Function parser = numberParser.get(eleClass);
                        if(parser == null) {
                            throw new RuntimeException("parser not found for type :" + eleClass);
                        }
                        List value = new ArrayList();
                        for (Object obj : list ) {
                            value.add(parser.apply(obj));
                        }
                        field.set(dest, value);
                    }else {
                        field.set(dest, list);
                    }

                } else if(classFieldPath != null) {
                    Map<String,Object> nodeMap = getMap(classFieldPath.value());
                    JsonObjectParser nodeParser = new JsonObjectParser(nodeMap);
                    field.set(dest, nodeParser.buildFromJson(field.getType()));
                } else if(fieldPath != null) {
                    Class type = field.getType();
                    if (type == Integer.class || type == Integer.TYPE) {
                        field.set(dest, getInt(fieldPath.value()));
                    }else if(type == Boolean.class || type == Boolean.TYPE) {
                        field.set(dest, getBoolean(fieldPath.value()));
                    }else if(type == Long.class || type == Long.TYPE) {
                        field.set(dest, getLong(fieldPath.value()));
                    }else if(type == Double.class || type == Double.TYPE) {
                        field.set(dest, getDouble(fieldPath.value()));
                    }else if(type == String.class ) {
                        field.set(dest, getString(fieldPath.value()));
                    }else if (type == Date.class) {
                        field.set(dest, getDate(fieldPath.value()));
                    }else {
                        field.set(dest, getObject(fieldPath.value()));
                    }
                }
            }
            return dest;

        }catch (InstantiationException e) {
            throw new RuntimeException(e);
        }catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


}
