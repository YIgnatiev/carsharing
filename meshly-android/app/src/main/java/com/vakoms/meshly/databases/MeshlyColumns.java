package com.vakoms.meshly.databases;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/15/15.
 */
public class MeshlyColumns {


    public static String generateTableFromClass(Class clazz) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        String name = getTableName(clazz);

        builder.append(name);
        builder.append(" ( ");
        builder.append("_id  INTEGER PRIMARY KEY AUTOINCREMENT , \n");
        builder.append(generateColums("", clazz));
        if (!builder.toString().endsWith(", \n ")) builder.append(", \n ");

        builder.append(" UNIQUE (id)");
        builder.append(" );");
        return builder.toString();
    }


    private static String generateColums(String colPreffix, Class clazz) {

        StringBuilder builder = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String str = field.getType().toString();

            if (i != 0 && !builder.toString().endsWith(", \n ")) builder.append(", \n ");

            switch (str) {
                case "long":
                case "int":
                    builder.append(colPreffix).append(field.getName());
                    builder.append(" INTEGER ");
                    break;
                case "boolean":
                    builder.append(colPreffix).append(field.getName());
                    builder.append(" BOOLEAN ");
                    break;
                case "class java.lang.String":
                    builder.append(colPreffix).append(field.getName());
                    builder.append(" TEXT ");
                    break;
                default:

                    if (field.getAnnotation(ListRow.class) != null) {
                        builder.append(colPreffix).append(field.getName());
                        builder.append(" TEXT ");
                    } else if (field.getAnnotation(ObjectRow.class) != null) {

                        String innerTableName = colPreffix + field.getName() + "_";
                        builder.append(generateColums(innerTableName, field.getType()));
                    }
            }

        }

        return builder.toString();
    }

    public static String[] getColumns(Class clazz) {

        List<String> list = new ArrayList<>();
        list.add("_id");
        list.addAll(getColumnsList("", clazz));
        return list.toArray(new String[list.size()]);
    }

    private static List<String> getColumnsList(String parrentName, Class clazz) {
        List<String> list = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(ObjectRow.class) != null) {

                list.addAll(getColumnsList(parrentName + field.getName() + "_", field.getType()));
            } else {
                list.add(parrentName + field.getName());
            }

        }
        return list;
    }


    public static String getTableName(Class clazz) {
        return clazz.getCanonicalName().replace(".", "_");
    }

}




