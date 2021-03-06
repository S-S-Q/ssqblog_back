package com.ssq.pojo;

import com.ssq.config.aspect.Identity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean implements Serializable {

    private int code;
    private String message;
    private Object data;
    private Enum identity= Identity.NOT_LOG;


    public  RespBean(int code,String message,Object data){
        this.code=code;
        this.message=message;
        this.data=data;
    }
    //成功返回
    public static RespBean success(String message)
    {
        return new RespBean(200,message,null);
    }

    //成功返回
    public static RespBean success(String message,Object obj)
    {
        return new RespBean(200,message,obj);
    }

    //失败返回
    public static RespBean error(String message)
    {
        return new RespBean(500,message,null);
    }

    //失败返回
    public static RespBean error(String message,Object obj)
    {
        return new RespBean(500,message,obj);
    }
}
