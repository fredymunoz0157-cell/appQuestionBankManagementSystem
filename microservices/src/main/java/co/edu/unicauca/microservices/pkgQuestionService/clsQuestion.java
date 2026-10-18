package co.edu.unicauca.microservices.pkgQuestionService;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_question")

@Getter
@Setter

public class clsQuestion {
    @Id
    @Column(name = "question_ouid")
    private String attOUID;

    @Column(name = "question_context")
    private String attContext;

    @Column(name = "question_directquestion")
    private String attDirectQuestion;
    
    //Distractors are stored in a separate table with a foreign key relationship to the question table
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_question_distractor", joinColumns = @JoinColumn(name = "question_ouid"))
    @Column(name = "distractor_text", nullable = false, length = 1000)
    private Set<String> attDistractors = new HashSet<>();

    @Column(name = "question_right_answer")
    private String attRightAnswer;

    @Column(name = "question_justification")
    private String attJustification;

    @Column(name = "question_bibliography")
    private String attBibliography;

    @Column(name = "question_competence")
    private String attCompetence;

    @Column(name = "question_topic")
    private String attTopic;

    @Column(name = "question_subtopic")
    private String attSubTopic;

    @Column(name = "question_difficultylevel")
    private Integer attDifficultyLevel;

    public clsQuestion() {
    }

    public clsQuestion(String prmOUID, String prmContext, String prmDirectQuestion, Set<String> prmDistractors,
            String prmRightAnswer, String prmJustification, String prmBibliography, String prmCompetence,
            String prmTopic, String prmSubTopic, Integer prmDifficultyLevel) {
        attOUID = prmOUID;
        attContext = prmContext;
        attDirectQuestion = prmDirectQuestion;
        attDistractors = prmDistractors;
        attRightAnswer = prmRightAnswer;
        attJustification = prmJustification;
        attBibliography = prmBibliography;
        attCompetence = prmCompetence;
        attTopic = prmTopic;
        attSubTopic = prmSubTopic;
        attDifficultyLevel = prmDifficultyLevel;
    }

}
