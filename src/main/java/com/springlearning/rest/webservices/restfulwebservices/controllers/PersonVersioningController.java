package com.springlearning.rest.webservices.restfulwebservices.controllers;

import com.springlearning.rest.webservices.restfulwebservices.beans.Name;
import com.springlearning.rest.webservices.restfulwebservices.beans.PersonV1;
import com.springlearning.rest.webservices.restfulwebservices.beans.PersonV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonVersioningController {
    @GetMapping(value = "/person/param", params = "version=1")
    public PersonV1 personV1() {
        return new PersonV1("Bob Charlie");
    }

    @GetMapping(value = "/person/param", params = "version=2")
    public PersonV2 personV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }

    // versioning based on Header
    @GetMapping(value = "/person/header", headers = "X-API-VERSION=1")
    public PersonV1 headerV1() {
        return new PersonV1("Bob Charlie");
    }

    @GetMapping(value = "/person/header", headers = "X-API-VERSION=2")
    public PersonV2 headerV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }
}
