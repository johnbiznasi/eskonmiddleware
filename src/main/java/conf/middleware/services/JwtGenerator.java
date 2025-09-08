package conf.middleware.services;

import io.smallrye.jwt.build.Jwt;

import java.time.Duration;
import java.util.Set;

public class JwtGenerator {

    public static String generate(String username, Set<String> roles,String account) {
        return Jwt.issuer("confidential-conversations")
                .claim("account", account)
                .expiresIn(Duration.ofDays(100))
                .upn(username)
                .groups(roles)
                .sign();
    }
}