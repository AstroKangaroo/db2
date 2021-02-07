package it.polimi.db2.gunalanodabasi.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Base64;
import java.util.List;

/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@Table(name = "product", schema = "gamifiedquestionnaire")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Lob
	private byte[] image;

	private String name;

	// bi-directional many-to-one association to Review
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Review> reviews;

	// bi-directional many-to-one association to Questionnaire
	@OneToMany(mappedBy = "product")
	private List<Questionnaire> questionnaires;

	public Product() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public String getImageData() {
		return Base64.getMimeEncoder().encodeToString(image);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Questionnaire> getQuestionnaires() {
		return this.questionnaires;
	}

	public void setQuestionnaires(List<Questionnaire> questionnaires) {
		this.questionnaires = questionnaires;
	}

	public Questionnaire addQuestionnaire(Questionnaire questionnaire) {
		getQuestionnaires().add(questionnaire);
		questionnaire.setProduct(this);

		return questionnaire;
	}

	public Questionnaire removeQuestionnaire(Questionnaire questionnaire) {
		getQuestionnaires().remove(questionnaire);
		questionnaire.setProduct(null);

		return questionnaire;
	}
	
	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setProduct(this);

		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setProduct(null);

		return review;
	}

}