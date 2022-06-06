package com.example.myweather.data;

import java.util.List;

public interface LocationRepository {

    void getAll(List<Location> locations);
    void add(String result);
    void update(String result);
    void findByCity(Location loc);
    void findBySelected(Location loc);
    void delete(String result);
}
