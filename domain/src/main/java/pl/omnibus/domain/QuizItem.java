package pl.omnibus.domain;

import javax.persistence.Entity;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
public class QuizItem {
    private Long id;
    private String question;
    private String authorName;
    private List<Answer> answers;
    private QuizItemType quizItemType;
    private QuestionCategory questionCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getCorrectAnswers() {
        return answers
                .stream()
                .filter(Answer::getIsCorrect)
                .collect(toList());
    }

    public QuizItemType getQuizItemType() {
        return quizItemType;
    }

    public void setQuizItemType(QuizItemType quizItemType) {
        this.quizItemType = quizItemType;
    }

    public QuestionCategory getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(QuestionCategory questionCategory) {
        this.questionCategory = questionCategory;
    }
}
