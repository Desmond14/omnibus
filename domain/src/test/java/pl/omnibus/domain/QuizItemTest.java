package pl.omnibus.domain;

import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class QuizItemTest {

    @Test
    public void shouldGetAllCorrectAnswers() {
        QuizItem quizItem = new QuizItem();
        Answer correct = new Answer(1L, "answer1", TRUE);
        Answer misleading = new Answer(2L, "answer2", FALSE);
        Answer secondCorrect = new Answer(3L, "answer3", TRUE);
        quizItem.setAnswers(newArrayList(correct, misleading, secondCorrect));

        assertThat(quizItem.getCorrectAnswers(), hasSize(2));
        assertThat(quizItem.getCorrectAnswers(), hasItem(correct));
        assertThat(quizItem.getCorrectAnswers(), hasItem(secondCorrect));
    }

    @Test
    public void shouldGetEmptyListForCorrectAnswers() {
        QuizItem quizItem = new QuizItem();
        Answer misleading = new Answer(1L, "answer1", FALSE);
        Answer secondMisleading = new Answer(2L, "answer2", FALSE);
        quizItem.setAnswers(newArrayList(misleading, secondMisleading));

        assertThat(quizItem.getCorrectAnswers(), is(notNullValue()));
        assertThat(quizItem.getCorrectAnswers().size(), is(equalTo(0)));
    }
}