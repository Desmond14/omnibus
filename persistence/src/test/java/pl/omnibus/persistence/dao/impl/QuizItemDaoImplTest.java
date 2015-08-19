package pl.omnibus.persistence.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.omnibus.domain.Answer;
import pl.omnibus.domain.QuizItem;
import pl.omnibus.persistence.dao.DaoTestBase;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DataSourceAutoConfiguration.class)
public class QuizItemDaoImplTest extends DaoTestBase{
    @Autowired
    private DataSource dataSource;
    private QuizItemDaoImpl quizItemDao;

    @Before
    public void setUp() throws Exception {
        quizItemDao = new QuizItemDaoImpl(dataSource);
        prepareDatabase();
    }

    @Test
    public void shouldFetchAll() throws SQLException {
        List<QuizItem> quizItems = quizItemDao.fetchAll();
        assertThat(quizItems, is(notNullValue()));
        assertThat(quizItems.size(), is(equalTo(3)));
    }

    @Test
    public void shouldFetchTwoCorrectAnswersForQuestion() {
        List<QuizItem> quizItems = quizItemDao.fetchAll();
        assertThat(quizItems, is(notNullValue()));
        List<Answer> answers = getAnswersForQuestionId(quizItems, 2L);
        assertThat(answers.size(), is(equalTo(2)));
        assertThat(answers, everyItem(hasProperty("isCorrect", equalTo(true))));
    }

    @Test
    public void shouldFetchBothCorrectAndWrongAnswer() {
        List<QuizItem> quizItems = quizItemDao.fetchAll();
        assertThat(quizItems, is(notNullValue()));
        List<Answer> answers = getAnswersForQuestionId(quizItems, 1L);
        assertThat(answers.size(), is(equalTo(2)));
        assertThat(answers, hasItem(hasProperty("isCorrect", equalTo(true))));
        assertThat(answers, hasItem(hasProperty("isCorrect", equalTo(false))));
    }

    @Test
    public void shouldFetchAuthorName() {
        List<QuizItem> quizItems = quizItemDao.fetchAll();
        assertThat(quizItems, is(notNullValue()));
        assertThat(getQuizItemById(quizItems, 1L).getAuthorName(), is(equalTo("user1")));
    }

    private List<Answer> getAnswersForQuestionId(List<QuizItem> quizItems, Long questionId) {
        return getQuizItemById(quizItems, questionId).getAnswers();
    }

    private QuizItem getQuizItemById(List<QuizItem> quizItems, Long questionId) {
        return quizItems.stream().filter(quizItem -> quizItem.getId().equals(questionId)).findFirst().get();
    }
}