package it.polimi.db2.gunalanodabasi.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "gamifiedquestionnaire")
@NamedQueries({ @NamedQuery(name = "User.findAllOrderedByScore", query = "Select r from User r  where r.score > 0 ORDER BY r.score DESC"),
@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2")})

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String username;

	private String password;

	private String email;

	private Timestamp lastlogin;

	private int score;
	
	private int isbanned;

	// bi-directional many-to-one association to Review
	@OneToMany(mappedBy = "user")
	private List<Review> reviews;

	// bi-directional many-to-one association to Answer
	@OneToMany(mappedBy = "user")
	private List<Answer> answers;

	// bi-directional many-to-one association to Statisticalanswer
	@OneToMany(mappedBy = "user")
	private List<Statisticalanswer> statisticalanswers;

	// bi-directional many-to-one association to Submitstatus
	@OneToMany(mappedBy = "user")
	private List<Submitstatus> submitstatuses;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getLastlogin() {
		return this.lastlogin;
	}

	public void setLastlogin(Timestamp lastlogin) {
		this.lastlogin = lastlogin;
	}

	public int getIsbanned() {
		return this.isbanned;
	}

	public void setIsbanned(int isbanned) {
		this.isbanned = isbanned;
	}
	
	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setUser(this);

		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setUser(null);

		return review;
	}
	
	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answer addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setUser(this);

		return answer;
	}

	public Answer removeAnswer(Answer answer) {
		getAnswers().remove(answer);
		answer.setUser(null);

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
		statisticalanswer.setUser(this);

		return statisticalanswer;
	}

	public Statisticalanswer removeStatisticalanswer(Statisticalanswer statisticalanswer) {
		getStatisticalanswers().remove(statisticalanswer);
		statisticalanswer.setUser(null);

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
		submitstatus.setUser(this);

		return submitstatus;
	}

	public Submitstatus removeSubmitstatus(Submitstatus submitstatus) {
		getSubmitstatuses().remove(submitstatus);
		submitstatus.setUser(null);

		return submitstatus;
	}

}