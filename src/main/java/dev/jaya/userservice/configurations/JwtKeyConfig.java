package dev.jaya.userservice.configurations;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class JwtKeyConfig {

    private final JwtConfig jwtConfig;

    @Bean
    public SecretKey jwtSecretKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(jwtConfig.getSecret());

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
