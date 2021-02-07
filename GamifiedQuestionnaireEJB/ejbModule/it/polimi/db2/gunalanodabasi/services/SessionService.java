package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateful;
import javax.ejb.Remove;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import it.polimi.db2.gunalanodabasi.entities.User;

import java.util.ArrayList;
import java.util.List;

@Stateful
public class SessionService  {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB", type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	List<Integer> list = new ArrayList<>();
	
	private User user;
	private List<String> answers = new ArrayList<String>();
	
	public SessionService() {
		// TODO Auto-generated constructor stub
	}
	
	public void saveUser(User u) {
		user = u;
	}
	
	public User getUser() {
		return user;
	}
	
	public void addAnswer(String a) {
		answers.add(a);
	}
	
	public List<String> getAnswers() {
		return answers;
	}
	
	public void changeAnswer(int i, String a) {
		answers.set(i, a);
	}
	
	@Remove
	public void remove() {}
}
