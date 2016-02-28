package com.vakoms.meshly.databases;

/**
 * Created by Makhobey Oleh on 9/25/15.
 * emai: tajcig@ya.ru
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectRow {


}
