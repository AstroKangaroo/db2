package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gunalanodabasi.entities.Product;
import it.polimi.db2.gunalanodabasi.entities.Review;
import it.polimi.db2.gunalanodabasi.entities.User;

import java.sql.Timestamp;
import java.util.List;

@Stateless
public class ReviewService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public ReviewService() {
	}

	public Review findById(int id) {
		return em.find(Review.class, id);
	}

	public List<Review> findAllReviews() {
		return em.createNamedQuery("Review.findAll", Review.class).getResultList();
	}

	public int createReview(String text, Timestamp date, int userid, int productid) {
		User u = em.find(User.class, userid);
		if (u == null)
			return 0;
		Product p = em.find(Product.class, productid);
		if (p == null)
			return 0;
		Review r = new Review();
		r.setText(text);
		r.setDate(date);
		r.setUser(u);
		r.setProduct(p);
		em.persist(r);
		em.flush();
		u.addReview(r);
		p.addReview(r);
		return r.getId();
	}

	public void deleteReview(int id) {
		Review r = em.find(Review.class, id);
		if (r == null)
			return;
		em.remove(r);
	}

}
