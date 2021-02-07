package it.polimi.db2.gunalanodabasi.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
@Table(name = "answer", schema = "gamifiedquestionnaire")
@NamedQuery(name = "Answer.findAll", query = "SELECT a FROM Answer a")
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String text;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

	// bi-directional many-to-one association to Questionnaire
	@ManyToOne
	@JoinColumn(name = "questionnaireid")
	private Questionnaire questionnaire;

	// bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name = "questionid")
	private Question question;

	public Answer() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Questionnaire getQuestionnaire() {
		return this.questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
	
	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}