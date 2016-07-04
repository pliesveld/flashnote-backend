package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.repository.PopulatedCategoriesRepositoryTest;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.util.generator.QuestionGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = {PopulatedCategoriesRepositoryTest.class}, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class BankServiceTest extends AbstractTransactionalServiceUnitTest {

    @Autowired
    private BankService bankService;

    private Integer category_id;

    @Before
    public void givenExistingCategory() {
        Category category = findFirstCategory();
        category_id = category.getId();
    }

    @Transactional
    private Category findFirstCategory() {
        Category category = categoryRepository.findOneByNameEquals("TEST CATEGORY");
        assertNotNull(category);
        return category;
    }

    @Test
    @DirtiesContext
    public void whenQuestionBankCreate_thenCorrect() {
        Category category = new Category();
        category.setId(category_id);

        QuestionBank bank = new QuestionBank(category, UUID.randomUUID().toString());
        bank = bankService.createQuestionBank(bank);
        assertNotNull(bank);
        assertQuestionBankRepositoryCount(1);
    }


    @Test
    @DirtiesContext
    public void givenBank_whenUpdateBankAddNewQuestion_thenCorrect() {
        Category category = new Category();
        category.setId(category_id);

        QuestionBank bank = new QuestionBank(category, UUID.randomUUID().toString());
        bank = bankService.createQuestionBank(bank);
        assertNotNull(bank);

        Question question = QuestionGenerator.randomQuestion();
        bank.add(question);

        bankService.updateQuestionBankAddQuestion(bank.getId(), question);
        assertQuestionRepositoryCount(1);
    }

    @Test
    @DirtiesContext
    public void givenBank_whenUpdateBankAddExistingQuestion_thenCorrect() {
        Category category = new Category();
        category.setId(category_id);

        QuestionBank bank = new QuestionBank(category, UUID.randomUUID().toString());
        bank = bankService.createQuestionBank(bank);
        assertNotNull(bank);

        Question question = QuestionGenerator.randomQuestion();
        question = questionRepository.save(question);

        bank.add(question);

        bankService.updateQuestionBankAddQuestion(bank.getId(), question);
        assertQuestionRepositoryCount(1);
    }


}
