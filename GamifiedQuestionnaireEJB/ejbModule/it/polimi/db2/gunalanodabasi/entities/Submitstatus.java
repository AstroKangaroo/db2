package it.polimi.db2.gunalanodabasi.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * The persistent class for the submitstatus database table.
 * 
 */
@Entity
@Table(name = "submitstatus", schema = "gamifiedquestionnaire")
@NamedQueries({ @NamedQuery(name = "Submitstatus.findAll", query = "SELECT s FROM Submitstatus s"),
	@NamedQuery(name = "Submitstatus.find", query = "SELECT s FROM Submitstatus s where s.user.id = :userid and s.questionnaire.id = :questionnaireid")})

public class Submitstatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String status;

	private Timestamp date;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

	// bi-directional many-to-one association to Questionnaire
	@ManyToOne
	@JoinColumn(name = "questionnaireid")
	private Questionnaire questionnaire;

	public Submitstatus() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
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

}