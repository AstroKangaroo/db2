package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gunalanodabasi.entities.Statisticalanswer;
import it.polimi.db2.gunalanodabasi.entities.Questionnaire;
import it.polimi.db2.gunalanodabasi.entities.User;

import java.util.List;

@Stateless
public class StatisticalanswerService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public StatisticalanswerService() {
	}

	public Statisticalanswer findById(int id) {
		return em.find(Statisticalanswer.class, id);
	}

	public List<Statisticalanswer> findAllStatisticalanswers() {
		return em.createNamedQuery("Statisticalanswer.findAll", Statisticalanswer.class).getResultList();
	}

	public int createStatisticalanswer(int userid, int questionnaireid, String age, String sex, String expertise) {
		User u = em.find(User.class, userid);
		if (u == null)
			return 0;
		Questionnaire qr = em.find(Questionnaire.class, questionnaireid);
		if (qr == null)
			return 0;
		Statisticalanswer s = new Statisticalanswer();
		s.setAge(age);
		s.setSex(sex);
		s.setExpertise(expertise);
		s.setUser(u);
		s.setQuestionnaire(qr);
		em.persist(s);
		em.flush();
		u.addStatisticalanswer(s);
		qr.addStatisticalanswer(s);
		return s.getId();
	}

	public void deleteStatisticalanswer(int id) {
		Statisticalanswer s = em.find(Statisticalanswer.class, id);
		if (s == null)
			return;
		em.remove(s);
	}

}
