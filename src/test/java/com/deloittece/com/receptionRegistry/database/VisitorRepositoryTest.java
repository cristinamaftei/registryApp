package com.deloittece.com.receptionRegistry.database;

import static org.junit.Assert.assertEquals;

import org.junit.Before;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VisitorRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
 
	@MockBean
    private VisitorRepository visitorRepository;
	Visitor visitorTest;
    @Before
    public void setUp() {
    visitorTest = new Visitor( 1L,"Visitor Test","test@yahoo.com" ,"AA123456");
     
        Mockito.when(visitorRepository.findByIdentityCardInfo(visitorTest.getIdentityCardInfo()))
          .thenReturn(visitorTest);
    }
    
    
    @Test
    public void whenValidIdentityCardInfo_thenVisitorShouldBeFound() {
        String identityCardInfo = "AA123456";
        Visitor found = visitorRepository.findByIdentityCardInfo(identityCardInfo);
      
         assertThat(found.getIdentityCardInfo())
          .isEqualTo(identityCardInfo);
     }
    

}
