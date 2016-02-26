/**
 * Processes and handles a user for the servlet
 */
package com.design.communicate;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.design.persistence.Queries;
import com.design.persistence.Users;

public interface ProcessUser {
	
	static EntityManager em = Persistence.createEntityManagerFactory("DesignProject").createEntityManager();

	public static void userExists (String from) {
		Users user = new Users();
		user.setPhone(from);
		user.setHomeLocation(null);
		user.setFirstUse(new Date());
		
		String str = "SELECT x FROM Users AS x WHERE x.phone='" + from + "'";
		List <Users> users = em.createQuery(str).getResultList();
		
		if (users == null || users.size() == 0) {
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
		}
	}
	
	public static void persistQuery(String from, String quer, String cla, boolean suc, String type) {
		String str = "SELECT x FROM Users AS x WHERE x.phone='" + from + "'";
		Users users = (Users) em.createQuery(str).getResultList().get(0);
		
		str = "SELECT x FROM Queries AS x";
		List <Queries> qu = em.createQuery(str).getResultList();
		
		Queries query = new Queries();
		query.setClass1(cla);
		query.setPhone(users);
		query.setSuccessful(suc);
		query.setQuery(quer);
		query.setTime(new Date());
		query.setType(type);
		
		if (qu == null) {
			query.setId(qu.size() + 1);
		} else {
			query.setId(1);
		}		
		
		em.getTransaction().begin();
		em.persist(query);
		em.getTransaction().commit();
	}

}
