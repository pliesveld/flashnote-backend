package com.pliesveld.tests.beans;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.util.generator.QuestionGenerator;
import com.pliesveld.flashnote.util.generator.StudentGenerator;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@Import(DomainBeanHelperService.class)
public class DomainEntities {

    @Autowired
    DomainBeanHelperService domainBeanHelperService;

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
        attachmentText.setAttachmentType(AttachmentType.TEXT);
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
    @Autowired
    public Deck deckBean(Student studentBean, Category categoryBean) {
        categoryBean = domainBeanHelperService.makeEntityIfNotFound(categoryBean);

        Deck deck = new Deck(categoryBean, UUID.randomUUID().toString());
        deck.setDescription("Deck title #" + incrementCounter());
        deck.setCategory(categoryBean);

        List<FlashCard> list = new ArrayList<FlashCard>();
        int max = RandomUtils.nextInt(1, 15);
        for (int i = 0; i < max; i++) {
            FlashCard fc = this.flashcardBean();
            fc = domainBeanHelperService.makeEntityIfNotFound(fc);
            list.add(fc);
        }
        deck.setFlashcards(list);
        return deck;
    }

    @Bean
    @Scope("prototype")
    @Autowired
    public QuestionBank questionBankBean(Category categoryBean) {
        categoryBean = domainBeanHelperService.makeEntityIfNotFound(categoryBean);

        QuestionBank questionBank = new QuestionBank(categoryBean, UUID.randomUUID().toString());

        int max = RandomUtils.nextInt(1, 8);
        for (int i = 0; i < max; i++) {
            Question question = this.questionBean();
            question = domainBeanHelperService.makeEntityIfNotFound(question);
            questionBank.add(question);
        }
        return questionBank;
    }

    @Bean
    @Scope("prototype")
    public FlashCard flashcardBean() {
        Question question = this.questionBean();
        Answer answer = this.answerBean();
        question = domainBeanHelperService.makeEntityIfNotFound(question);
        answer = domainBeanHelperService.makeEntityIfNotFound(answer);
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


@Service
@Transactional
class DomainBeanHelperService {
    @PersistenceContext
    EntityManager entityManager;

    private <T extends DomainBaseEntity<Integer>> T persist(Class<?> clazz, T domainObj) {
        T ret;
        if (domainObj.getId() == null || (ret = (T) entityManager.find(clazz, domainObj.getId())) == null) {
            entityManager.persist(domainObj);
        } else { // merge?
            return ret;
        }
        return domainObj;
    }

    Category makeEntityIfNotFound(Category category) {
        return persist(Category.class, category);
    }

    Student makeEntityIfNotFound(Student student) {
        return persist(Student.class, student);
    }

    Question makeEntityIfNotFound(Question question) {
        return persist(Question.class, question);
    }

    Answer makeEntityIfNotFound(Answer answer) {
        return persist(Answer.class, answer);
    }

    FlashCard makeEntityIfNotFound(FlashCard fc) {

        FlashCard ret = null;
        if (fc.getId() == null || (ret = entityManager.find(FlashCard.class, fc.getId())) == null) {
            entityManager.persist(fc);
        } else {
            return ret;
        }

        return fc;
    }
}

