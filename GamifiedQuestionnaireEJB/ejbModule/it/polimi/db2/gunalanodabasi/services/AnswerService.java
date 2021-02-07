package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gunalanodabasi.entities.Answer;
import it.polimi.db2.gunalanodabasi.entities.Question;
import it.polimi.db2.gunalanodabasi.entities.Questionnaire;
import it.polimi.db2.gunalanodabasi.entities.User;

import java.util.List;

@Stateless
public class AnswerService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public AnswerService() {
	}

	public Answer findById(int id) {
		return em.find(Answer.class, id);
	}

	public List<Answer> findAllAnswers() {
		return em.createNamedQuery("Answer.findAll", Answer.class).getResultList();
	}

	public int createAnswer(int userid, int questionid, int questionnaireid, String text) {
		User u = em.find(User.class, userid);
		if (u == null)
			return 0;
		Question q = em.find(Question.class, questionid);
		if (q == null)
			return 0;
		Questionnaire qr = em.find(Questionnaire.class, questionnaireid);
		if (qr == null)
			return 0;
		Answer a = new Answer();
		a.setQuestion(q);
		a.setQuestionnaire(qr);
		a.setUser(u);
		a.setText(text);
		em.persist(a);
		em.flush();
		u.addAnswer(a);
		q.addAnswer(a);
		qr.addAnswer(a);
		return a.getId();
	}

	public void deleteAnswer(int id) {
		Answer a = em.find(Answer.class, id);
		if (a == null)
			return;
		em.remove(a);
	}

}
