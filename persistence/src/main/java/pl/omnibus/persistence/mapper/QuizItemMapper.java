package pl.omnibus.persistence.mapper;

import org.jooq.Record;
import org.jooq.RecordMapper;
import pl.omnibus.domain.Answer;
import pl.omnibus.domain.QuestionCategory;
import pl.omnibus.domain.QuizItem;
import pl.omnibus.domain.QuizItemType;

import static com.google.common.collect.Lists.newArrayList;
import static pl.omnibus.persistence.jooq.tables.Answers.ANSWERS;
import static pl.omnibus.persistence.jooq.tables.Questions.QUESTIONS;
import static pl.omnibus.persistence.jooq.tables.Users.USERS;

public class QuizItemMapper implements RecordMapper<Record, QuizItem> {
    @Override
    public QuizItem map(Record record) {
        QuizItem quizItem = new QuizItem();
        quizItem.setId(record.getValue(QUESTIONS.ID));
        quizItem.setQuestion(record.getValue(QUESTIONS.CONTENT));
        quizItem.setQuizItemType(QuizItemType.valueOf(record.getValue(QUESTIONS.QUESTION_TYPE)));
        quizItem.setQuestionCategory(QuestionCategory.valueOf(record.getValue(QUESTIONS.CATEGORY)));
        quizItem.setAuthorName(record.getValue(USERS.USERNAME));
        Answer answer = new Answer(
                record.getValue(ANSWERS.ID),
                record.getValue(ANSWERS.CONTENT),
                record.getValue(ANSWERS.IS_CORRECT));
        quizItem.setAnswers(newArrayList(answer));
        return quizItem;
    }

}
