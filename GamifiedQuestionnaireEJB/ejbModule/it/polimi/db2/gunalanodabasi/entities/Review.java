package it.polimi.db2.gunalanodabasi.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * The persistent class for the review database table.
 * 
 */
@Entity
@Table(name = "review", schema = "gamifiedquestionnaire")
@NamedQuery(name = "Review.findAll", query = "SELECT r FROM Review r")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String text;

	private Timestamp date;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

	// bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "productid")
	private Product product;

	public Review() {
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

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}