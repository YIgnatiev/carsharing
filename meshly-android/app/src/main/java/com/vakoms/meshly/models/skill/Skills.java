package com.vakoms.meshly.models.skill;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by taras.teslyuk on 08.09.2014.
 */
public class Skills {

    private String _total;
    private List<SkillsItem> values;

    public String get_total() {
        return _total;
    }

    public void set_total(String _total) {
        this._total = _total;
    }


    public List<SkillsItem> getValues() {
        return values;
    }

    public void setValues(List<SkillsItem> values) {
        this.values = values;
    }

    /**
     * Convert user skills to JSON array. This array using to update user profile on Meshly server
     *
     * @return JSON array, filled with user skills
     */
    public JSONArray toJSONArray() {
        JSONArray jsonArray = new JSONArray();

        for (SkillsItem item : values) {
            if (item.getSkill() != null && !item.getSkill().getName().equals("")) {
                jsonArray.put(item.getSkill().getName());
            }
        }

        return jsonArray;
    }
}