package it.polimi.db2.gunalanodabasi.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;

import it.polimi.db2.gunalanodabasi.entities.Admin;
import it.polimi.db2.gunalanodabasi.exceptions.*;
import java.util.List;

@Stateless
public class AdminService {
	@PersistenceContext(unitName = "GamifiedQuestionnaireEJB")
	private EntityManager em;

	public AdminService() {
	}

	public Admin checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<Admin> uList = null;
		try {
			uList = em.createNamedQuery("Admin.checkCredentials", Admin.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");

	}

}
