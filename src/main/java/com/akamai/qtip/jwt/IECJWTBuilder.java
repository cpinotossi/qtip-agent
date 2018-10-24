package com.akamai.qtip.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class IECJWTBuilder {
	static private SignatureAlgorithm ALG = SignatureAlgorithm.RS256;

	private JwtBuilder jws;

	public IECJWTBuilder() {
		this.jws = Jwts.builder();
		this.jws.setHeaderParam("typ", "JWT");
	}

	public IECJWTBuilder setAlg(String alg) {
		this.jws.setHeaderParam("alg", alg);
		return this;
	}

	/**
	 * RSA private key for signing.
	 *
	 * @param key
	 * @return IECJWTBuilder
	 * @throws IOException 
	 */
	public IECJWTBuilder setSigningKey(String key) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(key));
		Security.addProvider(new BouncyCastleProvider());
		PEMParser pp = new PEMParser(br);
		PEMKeyPair pemKeyPair = (PEMKeyPair) pp.readObject();
		KeyPair kp = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
		pp.close();

		this.jws.signWith(kp.getPrivate(), ALG);
		return this;
	}

	public IECJWTBuilder setSigningKey(Key key) {
		this.jws.signWith(key, ALG);
		return this;
	}

	public IECJWTBuilder setClientId(String clientId) {
		this.jws.claim("client-id", clientId);
		return this;
	}

	public IECJWTBuilder setAuthGroups(String[] authGroups) {
		this.jws.claim("auth-groups", String.join(";", authGroups));
		return this;
	}

	public String build() {
		return this.jws.compact();
	}
}