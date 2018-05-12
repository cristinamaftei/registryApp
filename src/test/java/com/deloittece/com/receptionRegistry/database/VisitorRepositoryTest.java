package com.deloittece.com.receptionRegistry.database;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VisitorRepositoryTest {

	@MockBean
	private VisitorRepository testSubject;
	
	Visitor visitorTest;

	@Before
	public void setUp() {
		visitorTest = new Visitor(1L, "Visitor Test", "test@yahoo.com", "AA123456");

		Mockito.when(testSubject.findByIdentityCardInfo(visitorTest.getIdentityCardInfo()))
				.thenReturn(visitorTest);
	}

	@Test
	public void whenValidIdentityCardInfo_thenVisitorShouldBeFound() {
		String identityCardInfo = "AA123456";
		Visitor found = testSubject.findByIdentityCardInfo(identityCardInfo);

		assertThat(found.getIdentityCardInfo()).isEqualTo(identityCardInfo);
	}
}
