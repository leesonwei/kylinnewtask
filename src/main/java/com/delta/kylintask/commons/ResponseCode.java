package com.delta.kylintask.commons;

public enum ResponseCode {

    /**
     * response success info
     */
    SUCCESS(0,"SUCCESS"),

    /**
     * response error info
     */
    ERROR(1,"ERROR"),

    /**
     * unlogin info
     */
    NEED_LOGIN(10,"NEED_LOGIN"),

    /**
     * bad request para info
     */
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT"),

    /**
     * op after refreshed
     */
    NEED_REFRESH(3,"刷新數據后再進行操作");

    /**
     * 编码
     */
    private final int code;

    /**
     * 描述信息
     */
    private final String desc;


    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }

}