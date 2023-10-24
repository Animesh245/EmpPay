package com.animesh245.emppay.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

@Service
public class AuthSignInKeyResolver implements SigningKeyResolver {

    @Value("${jwt.secret.key}")
    String secretKeyString;


    private SecretKey secretKey;

    public SecretKey getSecretKey()
    {


        if(secretKey == null)
        {
            this.secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(this.secretKeyString.getBytes()));
        }
        return this.secretKey;
    }


    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        return this.getSecretKey(); // this.getSecretKey();
    }

    public Key resolveSigningKey(JwsHeader jwsHeader, String s) {
        return this.getSecretKey(); //this.getSecretKey();
    }
}

