package com.vakoms.meshly.models;

import com.vakoms.meshly.models.job.Positions;
import com.vakoms.meshly.models.skill.Skills;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taras.melko on 8/19/14.
 */
@SuppressWarnings("unused")
public class LinkedinData {

    private String firstName;
    private String id;
    private String summary;
    private String lastName;
    private PictureUrls pictureUrls;
    private Skills skills;
    private Positions positions;
    private String emailAddress;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PictureUrls getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(PictureUrls pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    public Positions getPositions() {
        return positions;
    }

    public void setPositions(Positions positions) {
        this.positions = positions;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "LinkedInData{" +
                "firstName='" + firstName + '\'' +
                ", id='" + id + '\'' +
                ", summary='" + summary + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pictureUrls=" + pictureUrls +
                ", skills=" + skills +
                ", positions=" + positions +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }



    public class PictureUrls {
        private String _total;
        private List<String> values;

        /**
         * Get values
         * @return values or empty array list if not available
         */
        public List<String> getValues() {
            if (values == null) {
                return new ArrayList<>();
            }
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

        public String get_total() {
            return _total;
        }

        public void set_total(String _total) {
            this._total = _total;
        }
    }
}
