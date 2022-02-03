package org.artisoft.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.*;
import org.artisoft.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
@Component
public class JwtTokenProvider {
    static final long EXPIRATIONTIME = 2_147_483_646; // 10 days
    static String SECRET = "sT76RhA$#gl@(>T3099-";
    static final String SALT = "statSaLT$#@12388-";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    HttpServletRequest request;

    @PostConstruct
    protected void init() {
        SECRET = Base64.getEncoder().encodeToString(SECRET.getBytes());
    }

    public Authentication GetAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(GetUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /*public String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }*/

    public String GetUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("uname").asString();
        } catch (JWTDecodeException exception) {
            //Invalid token
            return null;
        }
    }

    public String FetchToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HEADER_STRING);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATIONTIME);
        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
              //  .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, SECRET)//
                .compact();
    }

    public String CreateSessionToken(User userInfo) {
        try {
            //            String userIp = request.getRemoteAddr();
//            String userAgent = request.getHeader("User-Agent");
            String userIp ="333";
            String userAgent ="555";

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] authHash = digest.digest(("begin:" + userInfo.getUsername() + userIp + userAgent + SALT).getBytes());

            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    .withSubject("session")
                    .withIssuer("auth0")
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                    .withArrayClaim("roles", userInfo.getRoles().toArray(new String[0]))
                    .withClaim("uid", userInfo.getUserId())
                    .withClaim("uname", userInfo.getUsername())
                    .withClaim("auth", Base64.getUrlEncoder().encodeToString(authHash))
                    .sign(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean ValidateToken(String token, HttpServletRequest request) {
        if (token == null) return false;

        try {
//            String userIp = request.getRemoteAddr();
//            String userAgent = request.getHeader("User-Agent");
            String userIp ="333";
            String userAgent ="555";


            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .withSubject("session")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

            Long userId = jwt.getClaim("uid").asLong();
            String authCode = jwt.getClaim("auth").asString();
            String username = jwt.getClaim("uname").asString();

            String[] roles = jwt.getClaim("roles").asArray(String.class);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] authHash = digest.digest(("begin:" + username + userIp + userAgent + SALT).getBytes());
            String newAuthCode = Base64.getUrlEncoder().encodeToString(authHash);
            if(!newAuthCode.equals(authCode)) {
                // todo log et: Bu da təhlükəsizlik məsələsidir
                return false;
            }

        } catch (JWTVerificationException | NoSuchAlgorithmException exception) {
            // Expired or invalid JWT token
            return false;
        }

        return true;
    }
}
