package pl.omnibus.persistence.dao.impl;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.omnibus.domain.QuizItem;
import pl.omnibus.persistence.dao.QuizItemDao;
import pl.omnibus.persistence.mapper.QuizItemMapper;

import javax.sql.DataSource;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.omnibus.persistence.jooq.tables.Answers.ANSWERS;
import static pl.omnibus.persistence.jooq.tables.Questions.QUESTIONS;
import static pl.omnibus.persistence.jooq.tables.Users.USERS;


@Repository
public class QuizItemDaoImpl implements QuizItemDao{
    private final Configuration configuration;

    @Autowired
    public QuizItemDaoImpl(DataSource dataSource) {
        this.configuration = new DefaultConfiguration().set(SQLDialect.POSTGRES_9_4).set(dataSource);
    }

    public List<QuizItem> fetchAll() {
        return DSL.using(configuration)
                .select()
                .from(ANSWERS
                        .join(QUESTIONS
                                .join(USERS)
                                .on(QUESTIONS.AUTHOR_ID.equal(USERS.ID)))
                        .on(QUESTIONS.ID.equal(ANSWERS.QUESTION_ID)))
                .fetch()
                .intoGroups(QUESTIONS.ID, new QuizItemMapper())
                .values()
                .stream()
                .map(items -> items.stream()
                        .reduce(
                                (quizItem, quizItem2) -> {
                                    quizItem.getAnswers().addAll(quizItem2.getAnswers());
                                    return quizItem;
                                })
                        .get())
                .collect(toList());
    }

}
