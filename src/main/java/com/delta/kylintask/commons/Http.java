package com.delta.kylintask.commons;

public enum Http {
    /**
     * resttype
     */
    GET("GET"),PUT("PUT"),POST("POST"),DELETE("DELETE");
    private String name;
    Http(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
