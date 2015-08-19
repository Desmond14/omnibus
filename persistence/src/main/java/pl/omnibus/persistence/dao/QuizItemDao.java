package pl.omnibus.persistence.dao;

import pl.omnibus.domain.QuizItem;

import java.util.List;

public interface QuizItemDao {
    List<QuizItem> fetchAll();
}
