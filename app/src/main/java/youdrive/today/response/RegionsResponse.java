package youdrive.today.response;

import java.util.List;

import youdrive.today.Region;

/**
 * Created by psuhoterin on 24.05.15.
 */
public class RegionsResponse extends BaseResponse {
    private List<Region> regions;

    public List<Region> getRegions() {
        return regions;
    }
}
