package com.springlearning.rest.webservices.restfulwebservices.beans;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("SomeBeanFilter") // dynamically filtering
public class SomeBean {
    private String field1;
    private String field2;

    // @JsonIgnore // static filtering :  if we want to ignore some field , this field will not be sent to consumer
    // or we can use the @JsonIgnoreProperties at class level
    private String field3;

    public SomeBean(String field1, String field2, String field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }
}
