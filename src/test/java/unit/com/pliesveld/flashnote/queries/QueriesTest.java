package com.pliesveld.flashnote.queries;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ActiveProfiles(Profiles.INTEGRATION_TEST)
//@ContextHierarchy({
//        @ContextConfiguration(classes = SpringEntityTestConfig.class, loader = AnnotationConfigContextLoader.class)
//})
//@TestPropertySource(locations = "classpath:test-datasource.properties" )
//@Transactional
public class QueriesTest {
//
//    @PersistenceContext
//    EntityManager entityManager;
//
////    @Before
//    public void setUp() throws Exception {
//        Question question = new Question(UUID.randomUUID().toString());
//        entityManager.persist(question);
//
//        Answer answer = new Answer(UUID.randomUUID().toString());
//        entityManager.persist(answer);
//
//        Category category = new Category("TEST CATEGORY",UUID.randomUUID().toString());
//        entityManager.persist(category);
//
//        QuestionBank bank = new QuestionBank(category, UUID.randomUUID().toString());
//        bank.add(question);
//        entityManager.persist(bank);
//
//        Deck deck = new Deck(category, UUID.randomUUID().toString());
//        deck.getFlashcards().add(new FlashCard(question,answer));
//        entityManager.persist(deck);
//
//        entityManager.flush();
//        entityManager.clear();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        entityManager.flush();
//    }
//
//    @Test
//    @Ignore
//    public void whenContextLoad_thenCorrect() {
//
//    }
//
//    @Test
//    @Ignore
//    public void whenFindQuestionByJPQL() {
//        TypedQuery<Question> query = entityManager.createQuery("select q from Question q where q.id = :id", Question.class);
//        query.setParameter("id", 9000);
//        Question question = query.getSingleResult();
//        assertNotNull(question);
//    }
//
//    @Test
//    @Ignore
//    public void whenFindAnswerByJPQL() {
//        TypedQuery<Answer> query = entityManager.createQuery("select a from Answer a where a.id = :id", Answer.class);
//        query.setParameter("id", 9001);
//        Answer answer = query.getSingleResult();
//        assertNotNull(answer);
//    }
//
//
//    public List<FlashCardPrimaryKey> findFlashCardPrimaryKeyByAnswerIdByJPQL() {
//        TypedQuery<FlashCardPrimaryKey> query = entityManager.createQuery("select f.id from FlashCard f where f.id.answerId = :id", FlashCardPrimaryKey.class);
//        query.setParameter("id", 9001);
//        List<FlashCardPrimaryKey> flashCardPrimaryKeyList = query.getResultList();
//        return flashCardPrimaryKeyList;
//    }
//
//    @Test
//    @Ignore
//    public void whenBankByQuestionId() {
//        TypedQuery<Long> query = entityManager.createQuery("select count(*) from QuestionBank qb inner join qb.questions qc where qc.id = :questionId", Long.class);
//        query.setParameter("questionId", 9000);
//        long result = query.getSingleResult();
//        assertEquals(1, result);
//    }
//
//    @Test
//    @Ignore
//    public void whenDeckByQuestionId() {
//
//        TypedQuery<Long> query = entityManager.createQuery("select count(*) from Deck d inner join d.flashcards fc where fc.id.questionId = :questionId", Long.class);
//        query.setParameter("questionId", 9000);
//        long result = query.getSingleResult();
//        assertEquals(1, result);
//    }
//
//    @Test
//    @Ignore
//    public void whenDeckByAnswerId() {
//
//        TypedQuery<Long> query = entityManager.createQuery("select count(*) from Deck d inner join d.flashcards fc where fc.id.answerId = :answerId", Long.class);
//        query.setParameter("answerId", 9001);
//        long result = query.getSingleResult();
//        assertEquals(1, result);
//    }

}
