package com.ssq.util;

import com.ssq.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {

    //生成Token
    public static String generatorToken(String username)
    {
        Date expiredDate=new Date(System.currentTimeMillis()+JwtConstant.EXPIRATION);
        Map<String,Object> claims=new HashMap<>();
        claims.put(JwtConstant.USER_NAME_HEAD,username);
        //注意token需要前缀
        return JwtConstant.TOKEN_HEAD+Jwts.builder().setClaims(claims).setExpiration(expiredDate).signWith(SignatureAlgorithm.HS256,JwtConstant.SECRET).compact();
    }


    //检验Token是否过期 或者是否有效
    public static boolean isTokenExpired(String token)
    {
        Claims claims=getClaimsFromToken(token);
        Date expiredDate=claims.getExpiration();
        if(expiredDate!=null)
            return expiredDate.before(new Date());
        else
            return true;
    }


    //从中获取信息
    private static Claims getClaimsFromToken(String token)
    {
        Claims claims=null;
        try
        {
            claims= Jwts.parser().setSigningKey(JwtConstant.SECRET).parseClaimsJws(token).getBody();
            return claims;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String getUsernameFromToken(String token) {
        Claims claims=getClaimsFromToken(token);
        String username=claims.getSubject();
        if(username!=null)
            return username;
        return null;
    }
}
