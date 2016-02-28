package com.vakoms.meshly.models;

import com.vakoms.meshly.databases.ListRow;

import java.util.List;

public class Follow {
    @ListRow
    public List<String> events;
    @ListRow
    public List<String> users;

    public List<String> getEvents() {
        return events;
    }

    public List<String> getUsers() {
        return users;
    }

}