package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gunalanodabasi.entities.Question;
import it.polimi.db2.gunalanodabasi.entities.Questionnaire;

import java.util.List;

@Stateless
public class QuestionService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public QuestionService() {
	}

	public Question findById(int id) {
		return em.find(Question.class, id);
	}

	public List<Question> findAllQuestions() {
		return em.createNamedQuery("Question.findAll", Question.class).getResultList();
	}

	public int createQuestion(int number, String text, int questionnaireid) {
		Questionnaire qr = em.find(Questionnaire.class, questionnaireid);
		if (qr == null)
			return 0;
		Question q = new Question();
		q.setNumber(number);
		q.setText(text);
		q.setQuestionnaire(qr);
		em.persist(q);
		em.flush();
		qr.addQuestion(q);
		return q.getId();
	}

	public void deleteQuestion(int id) {
		Question q = em.find(Question.class, id);
		if (q == null)
			return;
		em.remove(q);
	}

}
