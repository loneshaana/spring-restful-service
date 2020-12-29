package com.springlearning.rest.webservices.restfulwebservices.sqsComponents;

public interface LifeCycle {
    public void onStart() throws Exception;

    public void onShutdown() throws Exception;
}
