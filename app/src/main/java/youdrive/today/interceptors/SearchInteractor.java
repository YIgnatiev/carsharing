package youdrive.today.interceptors;


import youdrive.today.listeners.SearchActionListener;

public interface SearchInteractor {
    void postSearchCars(double lat, double lon, int radius, SearchActionListener listener);
    void getSearchCar(SearchActionListener listener);
    void deleteSearchCars(SearchActionListener listener);
}
