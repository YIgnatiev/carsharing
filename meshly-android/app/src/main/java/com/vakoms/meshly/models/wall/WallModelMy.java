package com.vakoms.meshly.models.wall;

import com.vakoms.meshly.databases.ListRow;
import com.vakoms.meshly.databases.ObjectRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/29/15.
 */
public class WallModelMy  {

        private String title;
        private String description;
        @ListRow
        private List<String> industries;
        @ListRow
        private List<String> skills;
        @ListRow
        private List<String> posts;
        private int time;
        private int interval;
        private long expirationDate;
        @ListRow
        private List<Double> geo;
        private boolean isOpened;
        private String id;
        private String createdAt;
        private String updateAt;
        @ObjectRow
        private WallUser user;
        private String lng;
        private String lat;

        public void setPosts(ArrayList<String> posts) {
            this.posts = posts;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public boolean isOpened() {
            return isOpened;
        }

        public void setInterval(int _interval){
            this.interval = _interval;
        }

        public void setIsOpened(boolean isOpened) {
            this.isOpened = isOpened;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getIndustries() {
            return industries;
        }

        public void setIndustries(List<String> industries) {
            this.industries = industries;
        }

        public List<String> getSkills() {
            return skills;
        }

        public void setSkills(ArrayList<String> skills) {
            this.skills = skills;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public long getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(long expirationDate) {
            this.expirationDate = expirationDate;
        }

        public List<Double> getGeo() {

            return geo;
        }

        public void setGeo(ArrayList<Double> geo) {

            this.geo = geo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedAt() {

            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(String updateAt) {
            this.updateAt = updateAt;
        }

        public WallUser getUser() {
            return user;
        }

        public void setUser(WallUser user) {
            this.user = user;
        }


}
