package com.pliesveld.flashnote.unit.service;

import com.pliesveld.flashnote.service.AccountRegistrationService;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.unit.spring.SpringMailServiceTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import org.subethamail.wiser.Wiser;

import java.util.UUID;

import static com.pliesveld.flashnote.unit.mail.WiserAssertions.assertReceivedMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = {SpringMailServiceTestConfig.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class RegistrationMailServiceTest {
    private static final Logger LOG = LogManager.getLogger();

    private Wiser wiser;
    
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private SimpleMailMessage templateMessage;
    
	@Autowired
	AccountRegistrationService accountService;
	
	@Before
	public void setupMailServer()
	{
		wiser = new Wiser();
		wiser.setPort(25252);
		wiser.start();
	}

	@After
	public void shutdownMailServer()
	{
		wiser.stop();
	}
	
	@Test
	public void registrationSendsEmail() throws Exception
	{		
		String recipient = UUID.randomUUID().toString() + "@example.com";
		String confirmURL = "REGISTRATION TOKEN";
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setTo(recipient);
		msg.setText(msg.getText() + confirmURL);
		
		mailSender.send(msg);
		
		wiser.getMessages().forEach(LOG::info);
		
		assertReceivedMessage(wiser)
			.from("flashnote@example.com")
			.to(recipient)
			.withContentContaining(confirmURL);		
	}
	
}
