package youdrive.today;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psuhoterin on 23.04.15.
 */
public class Region {

    @SerializedName("region_id")
    String id;
    String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
