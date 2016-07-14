package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.util.generator.QuestionGenerator;
import com.pliesveld.flashnote.util.generator.StudentGenerator;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Configuration
public class SimpleDomainEntityBeans {

    @Bean
    @Scope("prototype")
    public Student studentBean() {
        return StudentGenerator.randomizedStudent();
    }

    @Bean
    @Scope("prototype")
    public Question questionBean() {
        return QuestionGenerator.randomQuestion();
    }

    @Bean
    @Scope("prototype")
    public Answer answerBean() {
        Answer answer = new Answer();
        answer.setContent("The answer is " + UUID.randomUUID().toString());
        return answer;
    }

    @Bean
    @Scope("prototype")
    public AttachmentText attachmentTextBean() {
        AttachmentText attachmentText = new AttachmentText();
        String contents = StringUtils.arrayToCommaDelimitedString(new Object[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()});
        attachmentText.setContents(contents);
        attachmentText.setFileName(UUID.randomUUID().toString().concat(".txt"));
        attachmentText.setAttachmentType(AttachmentType.DOCUMENT_TEXT);
        return attachmentText;
    }

    @Bean
    @Scope("prototype")
    public AttachmentBinary attachmentBinaryBean() {
        AttachmentBinary attachmentBinary = new AttachmentBinary();
        byte[] contents = RandomUtils.nextBytes(1024);
        attachmentBinary.setContents(contents);
        attachmentBinary.setFileName(UUID.randomUUID().toString().concat(".txt"));
        return attachmentBinary;
    }

    @Bean
    @Scope("prototype")
    public Category categoryBean() {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString().substring(0, 5));
        category.setDescription(UUID.randomUUID().toString());
        return category;
    }

    private static int count = 0;

    public static synchronized int incrementCounter() {
        count++;
        return count;
    }

    @Bean
    @Scope("prototype")
    public Deck deckBean() {
        Deck deck = new Deck();
        deck.setDescription("Deck title #" + incrementCounter());
        return deck;
    }

    @Bean
    @Scope("prototype")
    public QuestionBank questionBankBean() {
        QuestionBank questionBank = new QuestionBank();
        questionBank.setDescription(UUID.randomUUID().toString());
        return questionBank;
    }

    @Bean
    @Scope("prototype")
    public FlashCard flashcardBean() {
        Question question = this.questionBean();
        Answer answer = this.answerBean();

        FlashCard flashCard = new FlashCard(question, answer);
        return flashCard;
    }

    @Bean
    @Scope("prototype")
    public AccountPasswordResetToken accountPasswordResetTokenBean(Student student) {
        String token = UUID.randomUUID().toString().toUpperCase();
        AccountPasswordResetToken pw_token = new AccountPasswordResetToken(student, token);
        return pw_token;
    }
}
