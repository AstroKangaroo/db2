package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;

import it.polimi.db2.gunalanodabasi.entities.User;
import it.polimi.db2.gunalanodabasi.exceptions.*;

import java.sql.Timestamp;
import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public UserService() {
	}

	public List<User> findAllOrderedByScore() {
		return em.createNamedQuery("User.findAllOrderedByScore", User.class).getResultList();
	}

	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1) {
			/*if (uList.get(0).getIsbanned() == 1) {
				throw new CredentialsException("User is banned!!!");
			}*/
			return uList.get(0);
		}

		throw new NonUniqueResultException("More than one user registered with same credentials");
	}

	public User findById(int id) {
		return em.find(User.class, id);
	}

	public void banUser(int id) {
		User u = findById(id);
		u.setIsbanned(1);
		em.flush();
	}

	public void updateLastLogin(int id) {
		User u = findById(id);
		long millis = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(millis);
		timestamp.setNanos(0);
		u.setLastlogin(timestamp);
		em.flush();
	}

	public void updateScore(int id, int score) {
		User u = findById(id);
		u.setScore(score);
		em.flush();
	}

	public int createUser(String username, String password, String email) {
		User u = new User();
		u.setUsername(username);
		u.setPassword(password);
		u.setEmail(email);
		long millis = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(millis);
		timestamp.setNanos(0);
		u.setLastlogin(timestamp);
		u.setScore(0);
		em.persist(u);
		em.flush();
		return u.getId();
	}

	public void deleteUser(int id) {
		User u = em.find(User.class, id);
		if (u == null)
			return;
		em.remove(u);
	}

}
