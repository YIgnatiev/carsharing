package com.vakoms.meshly.databases;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.util.Log;

import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.models.wall.WallModelMy;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/14/15.
 */
public class UserDAO {
    private static final UserDAO instance = new UserDAO();

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        return instance;
    }


    public void addUsers(ContentResolver contentResolver, List<NewUser> users) throws RemoteException, OperationApplicationException {

        ContentValues[] values = new ContentValues[users.size()];
        for (int i = 0; i < users.size(); i++) {
            values[i] = getContentValues(users.get(i));


        }
        contentResolver.bulkInsert(UserProvider.USERS_URI, values);
    }


    public NewUser getNewUser(Cursor cursor) {

        return setValuesByMethodNames(NewUser.class, cursor);
    }


    public Event getEvent(Cursor cursor){
        return setValuesByMethodNames(Event.class, cursor);
    }

    public WallModel getWallModel(Cursor cursor){
        return setValuesByMethodNames(WallModel.class, cursor);

    }

    public WallModelMy getWallModelMy(Cursor cursor){
        return setValuesByMethodNames(WallModelMy.class, cursor);

    }


    public void saveWall(ContentResolver contentResolver, List<WallModel> wallModels) throws RemoteException, OperationApplicationException {

        ContentValues[] values = new ContentValues[wallModels.size()];
        for (int i = 0; i < wallModels.size(); i++) {
            values[i] = getContentValues(wallModels.get(i));
        }
        contentResolver.bulkInsert(UserProvider.WALL_URI, values);
    }


    public void saveEvents (ContentResolver contentResolver, List<Event> events) throws RemoteException, OperationApplicationException {

        ContentValues[] values = new ContentValues[events.size()];
        for (int i = 0; i < events.size(); i++) {
            values[i] = getContentValues(events.get(i));
        }


        contentResolver.bulkInsert(UserProvider.EVENTS_URI, values);
    }





    public UserMe getUserMe(Cursor cursor) {
        return setValuesByMethodNames(UserMe.class, cursor);
    }

    public void saveUserMe(ContentResolver contentResolver, UserMe user) {
        ContentValues cv = getContentValues(user);
        contentResolver.insert(UserProvider.USER_ME_URI, cv);
    }


    private ContentValues getContentValues(Object object) {
        ContentValues cv = new ContentValues();
        try {
           cv.putAll(createContentValues("",object));
        } catch (IllegalAccessException e) {
            Log.e("sql", "parsing erorr" + e.getMessage());
        }

        return cv;
    }

    private ContentValues createContentValues(String parrentName,Object object) throws IllegalAccessException{
        ContentValues cv = new ContentValues();

        Class clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {


            if (field.getType() == List.class) {
                List list = (List) field.get(object);
                if (list != null)
                    cv.put(parrentName + field.getName(), list.toString());

            } else if (field.getType() == String.class) {
                cv.put(parrentName + field.getName(), (String) field.get(object));
            } else if (field.getType() == int.class) {
                cv.put(parrentName + field.getName(), (int) field.get(object));
            } else if (field.getType() == long.class) {
                cv.put(parrentName + field.getName(), (long) field.get(object));
            } else if (field.getAnnotation(ObjectRow.class) != null) {


                String childName = parrentName + field.getName() + "_";
                if(field.get(object)!= null)
                cv.putAll(createContentValues(childName,field.get(object)));

            }
        }
        return cv;
    }


//
//    private <T> T setValuesByMethodNames(Class<T> clazz, Cursor cursor) {
//        T instance = null;
//
//        try {
//            instance = clazz.newInstance();
//            for (Field field : clazz.getDeclaredFields()) {
//
//                if (field.getType() == int.class) {
//                    field.set(instance, getInt(cursor, field.getName()));
//                } else if (field.getType() == long.class) {
//                    field.set(instance, getLong(cursor, field.getName()));
//                } else if (field.getType() == String.class) {
//                    field.set(instance, string(cursor, field.getName()));
//                } else if (field.getType() == List.class) {
//
//                    ParameterizedType paramType = (ParameterizedType) field.getGenericType();
//                    Class genericType = (Class) paramType.getActualTypeArguments()[0];
//                    if (genericType == String.class) {
//                        String stringArray = string(cursor, field.getName());
//                        if (stringArray != null)
//                            field.set(instance, toListString(stringArray));
//
//                    } else if (genericType == Double.class) {
//                        String doubleArray = string(cursor, field.getName());
//                        if (doubleArray != null)
//                            field.set(instance, toListDouble(doubleArray));
//                    }
//                } else if (field.getAnnotation(ObjectRow.class) != null) {
//                    String parrentName = field.getName();
//                    Object innerObject  = field.getType().newInstance();
//
//
//                    for (Field innerField : field.getType().getDeclaredFields()) {
//
//                        if (innerField.getType() == int.class) {
//                            innerField.set(innerObject, getInt(cursor, parrentName + "_" + innerField.getName()));
//
//                        } else if (innerField.getType() == long.class) {
//
//                            innerField.set(instance, getLong(cursor, parrentName + "_" + innerField.getName()));
//                        } else if (innerField.getType() == String.class) {
//                            innerField.set(instance, string(cursor, innerField.getName()));
//                        } else if (innerField.getType() == List.class) {
//
//                            ParameterizedType paramType = (ParameterizedType) innerField.getGenericType();
//                            Class genericType = (Class) paramType.getActualTypeArguments()[0];
//                            if (genericType == String.class) {
//                                String stringArray = string(cursor, innerField.getName());
//                                if (stringArray != null)
//                                    innerField.set(instance, toListString(stringArray));
//
//                            } else if (genericType == Double.class) {
//                                String doubleArray = string(cursor, innerField.getName());
//                                if (doubleArray != null)
//                                    innerField.set(instance, toListDouble(doubleArray));
//                            }
//                        }
//                    }
//
//                    field.set(instance,innerObject);
//                }
//            }
//        } catch (InstantiationException e) {
//            Log.e("sql", "parsing erorr" + e.getMessage());
//
//        } catch (IllegalAccessException e) {
//            Log.e("sql", "parsing erorr" + e.getMessage());
//        } catch (IllegalArgumentException e) {
//            Log.e("sql", e.getMessage());
//        }
//
//        return instance;
//
//    }

 private <T> T setValuesByMethodNames(Class<T> clazz, Cursor cursor) {
        T instance = null;

        try {
            instance = getValueByClass("",clazz,cursor);
                }

         catch (InstantiationException e) {
            Log.e("sql", "parsing erorr" + e.getMessage());

        } catch (IllegalAccessException e) {
            Log.e("sql", "parsing erorr" + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("sql", e.getMessage());
        }

        return instance;

    }



    private <T> T getValueByClass(String parentName,Class<T> clazz, Cursor cursor)throws InstantiationException,IllegalAccessException,IllegalArgumentException{
        T instance = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {

            if (field.getType() == int.class) {
                field.set(instance, getInt(cursor, parentName+field.getName()));
            } else if (field.getType() == long.class) {

                field.set(instance, getLong(cursor, parentName+field.getName()));
            }else if(field.getType() == boolean.class){
                field.set(instance,getBoolean(cursor,parentName+field.getName()));
            } else if (field.getType() == String.class) {
                field.set(instance, string(cursor,parentName+ field.getName()));
            } else if (field.getType() == List.class) {

                ParameterizedType paramType = (ParameterizedType) field.getGenericType();
                Class genericType = (Class) paramType.getActualTypeArguments()[0];
                if (genericType == String.class) {
                    String stringArray = string(cursor,parentName+ field.getName());
                    if (stringArray != null)
                        field.set(instance, toListString(stringArray));

                } else if (genericType == Double.class) {
                    String doubleArray = string(cursor, parentName + field.getName());
                    if (doubleArray != null)
                        field.set(instance, toListDouble(doubleArray));
                }
            } else if (field.getAnnotation(ObjectRow.class) != null) {
                String childName = parentName + field.getName() + "_";
                field.set(instance, getValueByClass(childName,field.getType(),cursor));

            }

        }

        return instance;
    }





    private String string(Cursor cursor, String id) {
        int str = cursor.getColumnIndex(id);
        return  cursor.getString(str);
    }

    private boolean getBoolean(Cursor cursor, String id) {

        return cursor.getInt(cursor.getColumnIndex(id)) > 0;
    }

    private int getInt(Cursor cursor, String id) {

        return cursor.getInt(cursor.getColumnIndex(id));
    }

    private long getLong(Cursor cursor, String id) {

        return cursor.getLong(cursor.getColumnIndex(id));
    }

    public static List<String> toListString(String array) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, array.substring(1, array.length() - 1).split(", "));
        return list;
    }

    public static List<Double> toListDouble(String array) {
        List<Double> list = new ArrayList<Double>();
        String[] stringArray = array.substring(1, array.length() - 1).split(", ");
        for (String num : stringArray) {
            list.add(Double.parseDouble(num));
        }
        return list;
    }


}
