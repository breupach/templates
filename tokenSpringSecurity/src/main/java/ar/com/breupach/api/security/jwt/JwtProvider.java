package ar.com.breupach.api.security.jwt;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import ar.com.breupach.api.security.dto.JwtDto;
import ar.com.breupach.api.security.entity.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expire}")
    private Long expire;

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return this.jwtBuilder(userDetails.getUsername(), roles);
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error(">>> token mal formado. " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error(">>> token no soportado. " + e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error(">>> token expirado. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error(">>> token vacio. " + e.getMessage());
        } catch (SignatureException e) {
            logger.error(">>> fail en la firma. " + e.getMessage());
        }

        return false;
    }

    public String refreshToken(JwtDto token) throws ParseException {
        JWT jwt = JWTParser.parse(token.getToken());
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        String username = claims.getSubject();
        List<String> roles = (List<String>) claims.getClaims().get("roles");
        return this.jwtBuilder(username, roles);
    }
    
    private String jwtBuilder(String username, List<String> roles) {
    	return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }
}
