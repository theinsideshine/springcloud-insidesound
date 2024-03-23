package com.theinsideshine.insidesound.backend.users.auth;

import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class TokenJwtConfig {

    public final static String llaveJwt = "algun_token_con_alguna_frase_secreta";

    public final static Key SECRET_KEY = Keys.hmacShaKeyFor(Base64.getEncoder().encode(llaveJwt.getBytes()));
    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String HEADER_AUTHORIZATION = "Authorization";

}