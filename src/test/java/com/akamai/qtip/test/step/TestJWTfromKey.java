package com.akamai.qtip.test.step;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.akamai.qtip.jwt.IECJWTBuilder;

public class TestJWTfromKey extends MyTestParameters {

	@Before
	public void before() throws Exception {

	}

	@Test
	public void test() {
		String jwt = null;

		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		try {
			jwt = jwtBuilder.setAuthGroups(this.getAgentAuthGroup()).setSigningKey(this.getKeyPrivate())
					.setClientId(this.getAgentID()).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jwt = jwtBuilder.build();
		System.out.println("jwt: " + jwt);

		assertNotNull("Verify that JWT not null", jwt);
	}

}
