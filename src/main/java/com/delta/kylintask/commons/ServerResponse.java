package com.delta.kylintask.commons;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg = ResponseCode.SUCCESS.getDesc();
    private T data;
    private Timestamp timestamp;

    private ServerResponse(int status){
        this.status = status;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    private ServerResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }
    public Timestamp getTimestamp(){
        return timestamp;
    }


    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        ServerResponse<T> serverResponse = new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
        serverResponse.msg = ResponseCode.SUCCESS.getDesc();
        return serverResponse;
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }


    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}