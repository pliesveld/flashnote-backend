package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AccountRegistrationToken;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.repository.RegistrationRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.schema.Constants;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.flashnote.spring.SpringMailServiceTestConfig;
import com.pliesveld.flashnote.spring.SpringServiceTestConfig;
import com.pliesveld.tests.AbstractRepositoryUnitTest;
import com.pliesveld.tests.AbstractTransactionalRepositoryUnitTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { SpringServiceTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { SpringMailServiceTestConfig.class }, loader = AnnotationConfigContextLoader.class)
})
@Transactional
public class MailServiceTest extends AbstractRepositoryUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    protected EntityManager entityManager;

    @After
    public void flushAfter()
    {
        if(entityManager != null && entityManager.isJoinedToTransaction())
        {
            LOG_SQL.debug("Flushing Persistence Context");
            entityManager.flush();
        }
    }

	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	RegistrationRepository registrationRepository;
	
	@Autowired
	AccountRegistrationService accountService;  

	@Test
	public void contextLoads() {
		assertNotNull(studentRepository);
		assertNotNull(registrationRepository);
		assertNotNull(accountService);
		assertNotNull(entityManager);
	}

	@Bean
	@Scope("SCOPE_PROTOTYPE")
	Student studentBean()
	{
		Student student = new Student();
		student.setEmail(UUID.randomUUID().toString() + "@example.com");
		student.setPassword(UUID.randomUUID().toString());
		student.setRole(StudentRole.ROLE_ACCOUNT);
		entityManager.persist(student);
		
		return student;
	}
	
	@Bean
	@Scope("SCOPE_PROTOTYPE")
    AccountRegistrationToken registrationExpiredBean()
	{
		Student student = this.studentBean();
		AccountRegistrationToken registration = new AccountRegistrationToken();
		registration.setStudent(student);
		registration.setToken(UUID.randomUUID().toString());
		entityManager.persist(registration);
		registration.setExpiration(Instant.now().minus(1 + Constants.REGISTRATION_TOKEN_DURATION_DAYS,ChronoUnit.DAYS));
		entityManager.flush();
		return registration;
	}
	
	@Bean
	@Scope("SCOPE_PROTOTYPE")
    AccountRegistrationToken registrationBean()
	{
		Student student = this.studentBean();
		AccountRegistrationToken registration = new AccountRegistrationToken();
		registration.setStudent(student);
		registration.setToken(UUID.randomUUID().toString());
		entityManager.persist(registration);
		return registration;
	}
	
	@Test
	public void registrationServiceCreateFindExpired() throws Exception {
		Student student = new Student();
		student.setEmail("newuser@example.com");
		student.setPassword("new password");
		student = studentRepository.save(student);
        entityManager.flush();
		
		Integer student_id = student.getId();
		assertNotNull(student_id);
		
		String test_token = UUID.randomUUID().toString(); 
		AccountRegistrationToken accountRegistrationToken = new AccountRegistrationToken(student,test_token);
		accountRegistrationToken = registrationRepository.saveAndFlush(accountRegistrationToken);
		
		assertNotNull(accountRegistrationToken);
		assertNotNull(accountRegistrationToken.getId());
		assertNotNull(accountRegistrationToken.getExpiration());
		
		Instant expiration = accountRegistrationToken.getExpiration();
		assertTrue(expiration.isAfter(Instant.now()));
		

		accountRegistrationToken.setExpiration(Instant.now().minus(1 + Constants.REGISTRATION_TOKEN_DURATION_DAYS,ChronoUnit.DAYS));
		entityManager.flush();
		entityManager.clear();
		student = null;
		accountRegistrationToken = null;
		
		{
			Stream<AccountRegistrationToken> expired = registrationRepository.findAllByExpirationLessThan(Instant.now());
			assertNotNull(expired);
			Iterator<AccountRegistrationToken> it = expired.iterator();
			assertNotNull(it);
			assertTrue(it.hasNext());
			expired.close();
		}
		
		accountService.taskPurgeExpiredAccounts();
		entityManager.flush();
		
		/* deleting registration token should cascade delete student */
		student = studentRepository.findOne(student_id);
		assertNull(student);
	}
		
	@Test
	public void registrationServiceCreateMultiple() {
		AccountRegistrationToken registration1 = this.registrationExpiredBean(); // new account to be purged
		AccountRegistrationToken registration2 = this.registrationExpiredBean(); // user account with expired registration, should not be purged
		//AccountRegistrationToken registration3 = this.registrationBean(); // user account without matching registration
		AccountRegistrationToken registration4 = this.registrationBean(); // new account registration has not expired
		
		Student student1 = registration1.getStudent();
		Student student2 = registration2.getStudent();
		Student student3 = this.studentBean();
		Student student4 = registration4.getStudent();
		
		assertNotEquals(student1.getEmail(),student2.getEmail());
		assertNotEquals(student2.getEmail(),student3.getEmail());
		assertNotEquals(student1.getEmail(),student3.getEmail());
		assertNotEquals(student1.getEmail(),student4.getEmail());
		
		Serializable student1_id = student1.getId();
		Serializable student2_id = student2.getId();
		Serializable student3_id = student3.getId();
		Serializable student4_id = student4.getId();

		student2.setRole(StudentRole.ROLE_USER);
		student3.setRole(StudentRole.ROLE_USER);
	
		entityManager.flush();
		entityManager.clear();
		registration1 = registration2 = registration4 = null;
		student1 = student2 = student3 = student4 = null;
		
		assertEquals(3,registrationRepository.count());
		assertEquals(4,studentRepository.count());

		/*
		 * At this point we have:
		 * 	(2) ROLE_USER accounts;  one with an expired registration, the other with no registration
		 *  (2) ROLE_ACCOUNT accounts; only one with an expired registration
		 * 
		 * after invoking accountService.taskPurgeExpiredAccounts(), the following is tested:
		 * 
		 * 		1) the expired registration and the expired account of type ROLE_ACCOUNT should be deleted
		 * 
		 * 		2) the expired registration of the ROLE_USER account should be deleted, but the account itself persists.
		 * 
		 * 		3) the registration that has not expired should persist for accounts of type ROLE_ACCOUNT 
		 */
		
		assertEquals(1L, registrationRepository.findAllAsStream()
				.map((r) -> r.getStudent())
				.map((s) -> s.getRole())
				.filter((role) -> role == StudentRole.ROLE_USER)
				.count());
		
		assertEquals(2L, studentRepository.findAllAsStream()
				.map((s) -> s.getRole())
				.filter((role) -> role == StudentRole.ROLE_USER)
				.count());

		
		assertEquals(2L, registrationRepository.findAllAsStream()
				.map((r) -> r.getStudent())
				.map((s) -> s.getRole())
				.filter((role) -> role == StudentRole.ROLE_ACCOUNT)
				.count());
		

		assertEquals(1L, registrationRepository.findAllByExpirationLessThan(Instant.now())
				.map(AccountRegistrationToken::getStudent)
				.map(Student::getRole)
				.filter((role) -> role == StudentRole.ROLE_ACCOUNT)
				.count());
				
			
		assertEquals(1L, registrationRepository.findAllByExpirationLessThan(Instant.now())
				.map((r) -> r.getStudent())
				.map((s) -> s.getRole())
				.filter((role) -> role == StudentRole.ROLE_USER)
				.count());


		

		accountService.taskPurgeExpiredAccounts();
		entityManager.flush();

	
		assertEquals(0L, registrationRepository.findAllByExpirationLessThan(Instant.now())
				.filter((account) -> account.getStudent().getRole() == StudentRole.ROLE_ACCOUNT).count());
		
		assertEquals(0L, registrationRepository.findAllByExpirationLessThan(Instant.now())
				.map(AccountRegistrationToken::getStudent)
				.map(Student::getRole)
				.filter((role) -> role == StudentRole.ROLE_USER).count());


		assertEquals(2L, studentRepository.findAllAsStream()
				.map(Student::getRole)
				.filter((role) -> role == StudentRole.ROLE_USER).count());
		
		assertEquals(1L, studentRepository.findAllAsStream()
				.map(Student::getRole)
				.filter((role) -> role == StudentRole.ROLE_ACCOUNT).count());

		

		assertEquals(1,registrationRepository.count());
		assertEquals(3,studentRepository.count());
		
	}

	@Test
	public void registrationServiceVerify() {
		AccountRegistrationToken registration = this.registrationBean();
		String token = registration.getToken();
		
		assertEquals(1,studentRepository.count());
		assertEquals(1,registrationRepository.count());
		
		assertEquals(1L, studentRepository.findAllAsStream()
				.map(Student::getRole)
				.filter((role) -> role == StudentRole.ROLE_ACCOUNT).count());
		
		Student student = accountService.processRegistrationConfirmation(token);
		assertNotNull(student);
		//entityManager.flush();
		
		assertEquals(1,studentRepository.count());
		assertEquals(0,registrationRepository.count());
		
		assertEquals(1L, studentRepository.findAllAsStream()
				.map(Student::getRole)
				.filter((role) -> role == StudentRole.ROLE_USER).count());

	}
}
