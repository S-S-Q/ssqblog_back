package com.ssq.constant;

public class JwtConstant {
    //响应头以及请求头中 发送token的头部
    public static final String TOKEN_HEADER ="Authorization";

    //json web token的前缀
    public static final String TOKEN_HEAD ="Bearer";

    public static final String USER_NAME_HEAD="sub";

    //密钥
    public static final String SECRET ="ssq_blog_ssq";

    //token有效时间
    public static final Long EXPIRATION =604800L;

    //token在服务器存在时间
    public static final Long SAVE_TIME=86400L;
}
