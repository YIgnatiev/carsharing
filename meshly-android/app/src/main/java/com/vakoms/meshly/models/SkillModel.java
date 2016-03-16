package com.vakoms.meshly.models;

/**
 * Created by taras.melko on 8/19/14.
 */
public class SkillModel implements Comparable {

    private String skill;
    private boolean isChecked;
    private int count;

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(Object another) {
        SkillModel anotherModel = (SkillModel) another;

        return anotherModel.getCount() - getCount();
    }
}
