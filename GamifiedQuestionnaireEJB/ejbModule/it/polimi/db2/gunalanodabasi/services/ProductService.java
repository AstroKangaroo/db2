package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gunalanodabasi.entities.Product;

import java.util.List;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public ProductService() {
	}

	public Product findById(int id) {
		return em.find(Product.class, id);
	}

	public List<Product> findAllProducts() {
		return em.createNamedQuery("Product.findAll", Product.class).getResultList();
	}

	public int createProduct(String name, byte[] image) {
		Product p = new Product();
		p.setName(name);
		p.setImage(image);
		em.persist(p);
		em.flush();
		return p.getId();
	}

	public void deleteProduct(int id) {
		Product p = em.find(Product.class, id);
		if (p == null)
			return;
		em.remove(p);
	}

}
