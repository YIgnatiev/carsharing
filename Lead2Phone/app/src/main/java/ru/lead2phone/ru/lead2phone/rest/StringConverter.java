package ru.lead2phone.ru.lead2phone.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/3/15.
 */
public class StringConverter implements Converter {


        @Override
        public Object fromBody(TypedInput body, Type type) throws ConversionException {
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            if (body != null) {
                try {

                    reader = new BufferedReader(new InputStreamReader(body.in()));

                    String line;

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                            sb.append(" ");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return sb.toString();
            } else return null;
        }

        @Override
        public TypedOutput toBody(Object object) {
           return null;
        }


}
