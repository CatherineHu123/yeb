package com.xpz.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken 工具类
 *
 * @Author: Catherine
 */
@Component
public class JwtTokenUtil {
    //用户名、时间, 载荷，包括下面的过期时间等，该项目中只用了name time
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    // 从application.yml文件中拿到的
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户信息生成token
     * */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从 token 中获取用户名
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断 token 是否失效
     *   两个方面：1、当前用户名与userDetail里的；2、时间是否过期
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断 token 是否可以被刷新(是否需要生成新的token)
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * 过期重新生成 token
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
    /**
     * 判断时间是否失效
     */
    private boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expireDate = claims.getExpiration();
        return expireDate.before(new Date());
    }

    /**
     * 从 token 中获取荷载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据荷载生成 JWT TOKEN
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder() //创建jwt对象
                /**
                 * 载荷部分存在两个属性：payload和claims。两个属性均可作为载荷，
                 * jjwt中二者只能设置其一，如果同时设置，在终端方法compact()中将抛出异常
                 */
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                /**
                 *  安全密钥，生成签名的密钥和算法，相当于“盐”
                 */
                .signWith(SignatureAlgorithm.HS512, secret)
                /**
                 * 生成token: 1) 编码Header 和 payload； 2) 生成签名； 3)拼接字符串
                 */
                .compact();
    }

    /**
     * 生成 token 失效时间
     * 失效时间：当前系统的时间 + 配置的失效时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis()+expiration*1000);
    }
}
