package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gunalanodabasi.entities.Product;
import it.polimi.db2.gunalanodabasi.entities.Questionnaire;

import java.sql.Date;
import java.util.List;

@Stateless
public class QuestionnaireService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public QuestionnaireService() {
	}

	public Questionnaire findById(int id) {
		return em.find(Questionnaire.class, id);
	}
	
	public Questionnaire findDefault() {
		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);
		List<Questionnaire> questionnaires = findAllBeforeDate(date);
		return questionnaires.get(0);
	}

	public List<Questionnaire> findAllQuestionnaires() {
		return em.createNamedQuery("Questionnaire.findAll", Questionnaire.class).getResultList();
	}

	public List<Questionnaire> findAllBeforeDate(Date date) {
		List<Questionnaire> questionnaires = em.createNamedQuery("Questionnaire.findAllBeforeDate", Questionnaire.class)
				.setParameter("date", date).getResultList();
		return questionnaires;
	}
	
	public List<Questionnaire> findAllAfterDate(Date date) {
		List<Questionnaire> questionnaires = em.createNamedQuery("Questionnaire.findAllAfterDate", Questionnaire.class)
				.setParameter("date", date).getResultList();
		return questionnaires;
	}

	public Questionnaire findByDate(Date date) {
		Questionnaire found = null;

		List<Questionnaire> questionnaires = em.createNamedQuery("Questionnaire.findByDate", Questionnaire.class)
				.setParameter("date", date).getResultList();
		if (questionnaires.size() > 0)
			found = questionnaires.get(0);
		return found;
	}

	public int createQuestionnaire(Date date, int productid) {
		Product p = em.find(Product.class, productid);
		if (p == null)
			return 0;
		Questionnaire q = new Questionnaire();
		q.setDate(date);
		q.setProduct(p);
		em.persist(q);
		em.flush();
		p.addQuestionnaire(q);
		return q.getId();
	}

	public void deleteQuestionnaire(int id) {
		Questionnaire q = em.find(Questionnaire.class, id);
		if (q == null)
			return;
		em.remove(q);
	}

}
