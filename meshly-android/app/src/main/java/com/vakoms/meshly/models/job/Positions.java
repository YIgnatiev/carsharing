package com.vakoms.meshly.models.job;

import java.util.List;

/**
 * Created by taras.melko on 10/6/14.
 */
@SuppressWarnings("unused")
public class Positions {
    private String _total;
    private List<PositionsItems> values;

    public String get_total() {
        return _total;
    }

    public void set_total(String _total) {
        this._total = _total;
    }

    public List<PositionsItems> getValues() {
        return values;
    }

    public void setValues(List<PositionsItems> values) {
        this.values = values;
    }
}