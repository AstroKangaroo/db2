package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gunalanodabasi.entities.Submitstatus;
import it.polimi.db2.gunalanodabasi.entities.Questionnaire;
import it.polimi.db2.gunalanodabasi.entities.User;

import java.util.List;

@Stateless
public class SubmitstatusService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public SubmitstatusService() {
	}

	public Submitstatus findById(int id) {
		return em.find(Submitstatus.class, id);
	}

	public List<Submitstatus> findAllSubmitstatuses() {
		return em.createNamedQuery("Submitstatus.findAll", Submitstatus.class).getResultList();
	}
	
	public Submitstatus findSubmitstatus(int userid, int questionnaireid) {
		List<Submitstatus> submitstatuses = em.createNamedQuery("Submitstatus.find", Submitstatus.class)
				.setParameter("userid", userid).setParameter("questionnaireid", questionnaireid).getResultList();
		if(submitstatuses.isEmpty())
			return  null;
		else
			return submitstatuses.get(0);
	}

	public int createSubmitstatus(int userid, int questionnaireid, String status) {
		User u = em.find(User.class, userid);
		if (u == null)
			return 0;
		Questionnaire qr = em.find(Questionnaire.class, questionnaireid);
		if (qr == null)
			return 0;
		Submitstatus s = new Submitstatus();
		s.setStatus(status);
		long millis=System.currentTimeMillis();
		java.sql.Timestamp timestamp=new java.sql.Timestamp(millis);
		timestamp.setNanos(0);
		s.setDate(timestamp);
		s.setUser(u);
		s.setQuestionnaire(qr);
		em.persist(s);
		em.flush();
		u.addSubmitstatus(s);
		qr.addSubmitstatus(s);
		return s.getId();
	}

	public void deleteSubmitstatus(int id) {
		Submitstatus s = em.find(Submitstatus.class, id);
		if (s == null)
			return;
		em.remove(s);
	}

}
