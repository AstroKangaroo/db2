package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gunalanodabasi.entities.Forbiddenwords;

import java.util.List;

@Stateless
public class ForbiddenwordsService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public ForbiddenwordsService() {
	}

	public Forbiddenwords findById(int id) {
		return em.find(Forbiddenwords.class, id);
	}

	public List<Forbiddenwords> findAllForbiddenwords() {
		return em.createNamedQuery("Forbiddenwords.findAll", Forbiddenwords.class).getResultList();
	}

	public int createForbiddenword(String text) {
		Forbiddenwords w = new Forbiddenwords();
	
		w.setText(text);
		em.persist(w);
		em.flush();
		return w.getId();
	}

	public void deleteForbiddenword(int id) {
		Forbiddenwords w = em.find(Forbiddenwords.class, id);
		if (w == null)
			return;
		em.remove(w);
	}

}
