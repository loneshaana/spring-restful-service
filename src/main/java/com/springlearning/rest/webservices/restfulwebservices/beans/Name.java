package com.springlearning.rest.webservices.restfulwebservices.beans;

public class Name {
    private String firstName;
    private String lastname;

    public Name() {
    }

    public Name(String firstName, String lastname) {
        this.firstName = firstName;
        this.lastname = lastname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
