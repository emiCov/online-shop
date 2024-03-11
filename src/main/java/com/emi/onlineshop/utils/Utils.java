package com.emi.onlineshop.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Utils {

    private Utils() {
    }

    public static List<Filter> getFilters(String filters) {
        List<Filter> params = new ArrayList<>();
        if (filters != null) {
            List<String> filtersSplit = Arrays.stream(filters.split(";")).toList();
            filtersSplit.forEach(x -> params.add(getFilter(x)));
        }
        return params;
    }

    private static Filter getFilter(String filter) {
        List<String> filterParams = Arrays.stream(filter.split(",")).toList();
        return new Filter(filterParams.get(0), filterParams.get(1), filterParams.get(2));
    }
}
