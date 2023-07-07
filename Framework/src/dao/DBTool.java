/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
/**
 *
 * @author hoby
 */
public class DBTool {
    public static String upperFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
    
    public static Field[] getField(Object object) {
        Vector<Field> list = new Vector<>(object.getClass().getDeclaredFields().length);
        for (int i = 0; i < list.size(); i++) {
            list.addAll(Collections.singleton(object.getClass().getDeclaredFields()[i]));
        }
        return list.toArray(new Field[0]);
    }
    
    public static Field[] getFieldWithSuperclass(Object object) {
        List<Field> fieldList = new ArrayList<>(object.getClass().getSuperclass().getDeclaredFields().length);
        for(int i = 0;i<object.getClass().getDeclaredFields().length;i++)
            fieldList.add(object.getClass().getDeclaredFields()[i]);
        return fieldList.toArray(new Field[0]);
    }
    
}
