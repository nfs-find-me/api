package com.findme.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

@Component
public class JwtService {
	
	@Value("${jwt.secret}")
	public String secret;
	
	// 1 day
	private long expiration = System.currentTimeMillis()+ 86400000;
	
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	
	public String generateToken(String userName){
		Map<String,Object> claims=new HashMap<>();
		return createToken(claims,userName);
	}
	
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(expiration))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}
	
	public String createRefreshToken() {
		return generateRandomWords(3).toString();
	}
	
	public static StringBuilder generateRandomWords(int numberOfWords)
	{
		StringBuilder randomStrings = new StringBuilder();
		Random random = new Random();
		for(int i = 0; i < numberOfWords; i++)
		{
			char[] word = new char[random.nextInt(8)+3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
			for(int j = 0; j < word.length; j++)
			{
				word[j] = (char)('a' + random.nextInt(26));
			}
			randomStrings.append(word);
		}
		return randomStrings;
	}
	
	private Key getSignKey() {
		byte[] keyBytes= Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public long getExpiration() {
		return expiration;
	}
}
