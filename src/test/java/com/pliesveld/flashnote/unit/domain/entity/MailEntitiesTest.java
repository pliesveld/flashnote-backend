package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.AccountRegistrationToken;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.repository.RegistrationRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.schema.Constants;
import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class MailEntitiesTest {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	RegistrationRepository registrationRepository; 

	@Test
	public void contextLoads() {
		assertNotNull(studentRepository);
		assertNotNull(registrationRepository);
		assertNotNull(entityManager);
	}

	@Test
	public void studentCreate() {
		Student student = new Student();
		student.setEmail("newuser@example.com");
		student.setPassword("new password");
		studentRepository.save(student);
        entityManager.flush();
		
		assertNotNull(student);
		assertNotNull(student.getId());
		
		assertEquals(1,studentRepository.count());
		assertEquals(0,registrationRepository.count());
	}
	
	@Test
	public void registrationCreate() {
		Student student = new Student();
		student.setEmail("newuser@example.com");
		student.setPassword("new password");
		student = studentRepository.save(student);
        entityManager.flush();
		
		String test_token = UUID.randomUUID().toString(); 
		AccountRegistrationToken accountRegistrationToken = new AccountRegistrationToken(student,test_token);
		registrationRepository.saveAndFlush(accountRegistrationToken);
		
		assertEquals(1,studentRepository.count());
		assertEquals(1,registrationRepository.count());
	}
	
	@Test
	public void registrationCreateCascade() {
		Student student = new Student();
		student.setEmail("newuser@example.com");
		student.setPassword("new password");
		entityManager.persist(student);
		
		String test_token = UUID.randomUUID().toString(); 
		AccountRegistrationToken accountRegistrationToken = new AccountRegistrationToken(student,test_token);
		entityManager.persist(accountRegistrationToken);

		entityManager.flush();
		assertEquals(1,studentRepository.count());
		assertEquals(1,registrationRepository.count());
		
	}
	
	@Test
	public void registrationCreateCascadeSetters() {
		Student student = new Student();
		student.setEmail("newuser@example.com");
		student.setPassword("new password");
		entityManager.persist(student);
		
		String test_token = UUID.randomUUID().toString(); 
		AccountRegistrationToken accountRegistrationToken = new AccountRegistrationToken();
		accountRegistrationToken.setStudent(student);
		accountRegistrationToken.setToken(test_token);
		entityManager.persist(accountRegistrationToken);

		entityManager.flush();
		assertEquals(1,studentRepository.count());
		assertEquals(1,registrationRepository.count());
		
	}
	
	
	@Test
	public void registrationCreateFindByToken() {
		Student student = new Student();
		student.setEmail("newuser@example.com");
		student.setPassword("new password");
		student = studentRepository.save(student);
        entityManager.flush();
		
		String test_token = UUID.randomUUID().toString(); 
		AccountRegistrationToken accountRegistrationToken = new AccountRegistrationToken(student,test_token);
		registrationRepository.saveAndFlush(accountRegistrationToken);
		
		AccountRegistrationToken found = registrationRepository.findByToken(test_token);
		assertNotNull(found);
		assertEquals(found.getToken(),test_token);
		assertNotNull(found.getId());
		assertEquals(found.getId(),student.getId());
	}
	
	@Test
	public void registrationCreateFindExpired() throws Exception {
		Student student = new Student();
		student.setEmail("newuser@example.com");
		student.setPassword("new password");
		student = studentRepository.save(student);
        entityManager.flush();
		
		Integer student_id = student.getId();
		assertNotNull(student_id);
		
		String test_token = UUID.randomUUID().toString(); 
		AccountRegistrationToken accountRegistrationToken = new AccountRegistrationToken(student,test_token);
		accountRegistrationToken = registrationRepository.save(accountRegistrationToken);
		
		
		accountRegistrationToken.setExpiration(Instant.now().minus(1 + Constants.REGISTRATION_TOKEN_DURATION_DAYS,ChronoUnit.DAYS));
		entityManager.flush();
		
		assertNotNull(accountRegistrationToken);
		assertNotNull(accountRegistrationToken.getId());
		assertNotNull(accountRegistrationToken.getExpiration());
		
		Instant expiration = accountRegistrationToken.getExpiration();
		
		
		entityManager.clear();
		student = null;
		accountRegistrationToken = null;
		
		assertTrue(expiration.isBefore(Instant.now()));
		
		Stream<AccountRegistrationToken> expired = registrationRepository.findAllByExpirationLessThan(Instant.now());
		
		assertNotNull(expired);
		
		Iterator<AccountRegistrationToken> it = expired.iterator();
		assertNotNull(it);
		assertTrue(it.hasNext());
		
		AccountRegistrationToken accountExpired = it.next();
		registrationRepository.delete(accountExpired);
		
		entityManager.flush();
		
		/* deleting registration token should not cascade delete student */
		student = studentRepository.findOne(student_id);
		assertNotNull(student);
		assertNotNull(student.getId());
		


	}
		

	
}
