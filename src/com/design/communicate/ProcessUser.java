/**
 * Processes and handles a user for the servlet
 */
package com.design.communicate;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.design.data.Weather;
import com.design.persistence.Directions;
import com.design.persistence.News;
import com.design.persistence.Queries;
import com.design.persistence.Users;

public interface ProcessUser {
	
	static EntityManager em = Persistence.createEntityManagerFactory("DesignProject").createEntityManager();

	public static Users userExists (String from) {
		Users user = new Users();
		user.setPhone(from);
		user.setFirstUse(new Date());
		
		String str = "SELECT x FROM Users AS x WHERE x.phone='" + from + "'";
		List <Users> users = em.createQuery(str).getResultList();
		
		if (users == null || users.size() == 0) {
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
			return user;
		} else {
			return users.get(0);
		}
	}
	
	public static void persistWeather (Weather weather, Queries query) {
		
	}
	
	public static void persistDirection (Directions dirc, Queries query) {
		dirc.setId(persistQuery(query));
		
		em.getTransaction().begin();
		em.persist(dirc);
		em.getTransaction().commit();
	}
	
	public static void persistNews (News news, Queries query) {
		news.setId(persistQuery(query));
		
		em.getTransaction().begin();
		em.persist(news);
		em.getTransaction().commit();
	}
	
	public static void persistWolfram () {
		
	}
	
	public static int persistQuery (Queries query) {
		String str = "SELECT x FROM Queries AS x";
		List <Queries> qu = em.createQuery(str).getResultList();
		
		int id = 1;
		
		if (qu != null && qu.size() > 1) {
			id = qu.size() + 1;
		}		
		
		query.setId(id);
		
		em.getTransaction().begin();
		em.persist(query);
		em.getTransaction().commit();
		
		return id;
	}
	

}
