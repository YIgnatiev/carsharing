package com.vakoms.meshly.models.geocoding;

import java.util.List;

/**
 * Created by Oleh Makhobey on 02.06.2015.
 * tajcig@ya.ru
 */
public class AddressComponents {

        private String long_name;
        private String short_name;
        private List<String> types;

        public String getLong_name() {
            return long_name;
        }

    public String getShort_name() {
        return short_name;
    }

        public List<String> getTypes() {
            return types;
        }

}
