package com.ct5106.swishStats.service;

import java.security.Key;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtService
{
	// 1 day in ms - should be shorter in production
	static final long EXPIRATIONTIME = 86400000;

	// PREFIX defines the prefix of the token, and the "Bearer" schema is typically
	// used
	// A JWT is sent in the Authorization header and the content of
	// the header looks like the following when using the Bearer schema:
	// Authorization: Bearer <token>
	static final String PREFIX = "Bearer";

	// Generate secret key. Only for demonstration purposes
	// In production, you should read it from the application configuration
	static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	// Generate signed JWT token
	public String getToken(String username)
	{
		String token = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME)).signWith(key).compact();
		return token;
	}

	// Get a token from request Authorization header,
	// verify the token, and get username
	public String getAuthUser(HttpServletRequest request)
	{
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (token != null)
		{
			String user = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token.replace(PREFIX, ""))
					.getBody().getSubject();
			if (user != null)
				return user;
		}
		return null;
	}
}