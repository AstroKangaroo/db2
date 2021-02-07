package it.polimi.db2.gunalanodabasi.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the statisticalanswer database table.
 * 
 */
@Entity
@Table(name = "statisticalanswer", schema = "gamifiedquestionnaire")
@NamedQuery(name = "Statisticalanswer.findAll", query = "SELECT s FROM Statisticalanswer s")
public class Statisticalanswer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String age;

	private String sex;

	private String expertise;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

	// bi-directional many-to-one association to Questionnaire
	@ManyToOne
	@JoinColumn(name = "questionnaireid")
	private Questionnaire questionnaire;

	public Statisticalanswer() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAge() {
		return this.age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getExpertise() {
		return this.expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
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