package com.springlearning.rest.webservices.restfulwebservices.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.springlearning.rest.webservices.restfulwebservices.beans.SomeBean;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public MappingJacksonValue retrieveSomeBean() {
        SomeBean value = new SomeBean("value 1", "value 2", "value 3");
        // dynamically filtering the properties
        MappingJacksonValue mapping = new MappingJacksonValue(value);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1 , filed2");
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("SomeBeanFilter", filter);
        mapping.setFilters(filterProvider);
        return mapping;
    }
}

/*
    i will push the email into queue
        sns should send the notification of received item
    lambda should poll data from queue
 */