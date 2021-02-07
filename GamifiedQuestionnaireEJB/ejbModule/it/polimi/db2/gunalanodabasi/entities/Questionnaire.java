package it.polimi.db2.gunalanodabasi.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * The persistent class for the questionnaire database table.
 * 
 */
@Entity
@Table(name = "questionnaire", schema = "gamifiedquestionnaire")
@NamedQueries({ @NamedQuery(name = "Questionnaire.findAll", query = "SELECT q FROM Questionnaire q"),
				@NamedQuery(name = "Questionnaire.findAllBeforeDate", query = "Select s from Questionnaire s where s.date <= :date ORDER BY s.date DESC"),
				@NamedQuery(name = "Questionnaire.findAllAfterDate", query = "Select s from Questionnaire s where s.date > :date ORDER BY s.date ASC"),
				@NamedQuery(name = "Questionnaire.findByDate", query = "Select s from Questionnaire s where s.date = :date")})





public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private Date date;

	// bi-directional many-to-one association to Question
	@OneToMany(mappedBy = "questionnaire", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Question> questions;

	// bi-directional many-to-one association to Answer
	@OneToMany(mappedBy = "questionnaire", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Answer> answers;

	// bi-directional many-to-one association to Statisticalanswer
	@OneToMany(mappedBy = "questionnaire", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Statisticalanswer> statisticalanswers;

	// bi-directional many-to-one association to Submitstatus
	@OneToMany(mappedBy = "questionnaire", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Submitstatus> submitstatuses;

	// bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "productid")
	private Product product;

	public Questionnaire() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Question addQuestion(Question question) {
		getQuestions().add(question);
		question.setQuestionnaire(this);

		return question;
	}

	public Question removeQuestion(Question question) {
		getQuestions().remove(question);
		question.setQuestionnaire(null);

		return question;
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answer addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setQuestionnaire(this);

		return answer;
	}

	public Answer removeAnswer(Answer answer) {
		getAnswers().remove(answer);
		answer.setQuestionnaire(null);

		return answer;
	}
	
	public List<Statisticalanswer> getStatisticalanswers() {
		return this.statisticalanswers;
	}

	public void setStatisticalanswers(List<Statisticalanswer> statisticalanswers) {
		this.statisticalanswers = statisticalanswers;
	}

	public Statisticalanswer addStatisticalanswer(Statisticalanswer statisticalanswer) {
		getStatisticalanswers().add(statisticalanswer);
		statisticalanswer.setQuestionnaire(this);

		return statisticalanswer;
	}

	public Statisticalanswer removeStatisticalanswer(Statisticalanswer statisticalanswer) {
		getStatisticalanswers().remove(statisticalanswer);
		statisticalanswer.setQuestionnaire(null);

		return statisticalanswer;
	}
	
	public List<Submitstatus> getSubmitstatuses() {
		return this.submitstatuses;
	}

	public void setSubmitstatuses(List<Submitstatus> submitstatuses) {
		this.submitstatuses = submitstatuses;
	}

	public Submitstatus addSubmitstatus(Submitstatus submitstatus) {
		getSubmitstatuses().add(submitstatus);
		submitstatus.setQuestionnaire(this);

		return submitstatus;
	}

	public Submitstatus removeSubmitstatus(Submitstatus submitstatus) {
		getSubmitstatuses().remove(submitstatus);
		submitstatus.setQuestionnaire(null);

		return submitstatus;
	}

}