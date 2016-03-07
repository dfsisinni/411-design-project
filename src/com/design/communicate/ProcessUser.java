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

public class ProcessUser {
	
	public ProcessUser () {
		
	}
	
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
	
	public static void persistDirection (String [] data, Queries query, int distance, int time) {
		if (query.getType().equals("sms")) {
			persistDirection(data, query, distance, time, null);
		} else {
			
		}
		
	}
	
	public static void persistDirection (String [] data, Queries query, int distance, int time, String directions) {
		if (directions == null) {
			Communicate.sendText("Unable to parse your directions query.");
		} else {
			Communicate.sendText(directions);
		}
		
		Directions dirc = new Directions();
		dirc.setDestination(data[1]);
		dirc.setOrigin(data[0]);
		dirc.setQueries(query);
		dirc.setTime((double) time);
		dirc.setDistance((double) distance);
		
		int id = persistQuery(query);
		dirc.setId(id);
		dirc.getQueries().setId(id);
		
		em.getTransaction().begin();
		em.persist(dirc);
		em.getTransaction().commit();
	}
	
	
	
	public static void persistNews (Queries query, String publisher, String message) {
		if (query.getType().equals("sms")) {
			if (message == null) {
				Communicate.sendText("Unable to parse your news query.");
			} else {
				Communicate.sendText(message);
			}
		} else {
			
		}
		
		News news = new News();
		news.setPublisher(publisher);
		news.setQueries(query);
		
		int id = persistQuery(query);
		news.setId(id);
		news.getQueries().setId(id);
		
		
		
		em.getTransaction().begin();
		em.persist(news);
		em.getTransaction().commit();
	}
	
	public static void persistWolfram () {
		
	}
	
	public static int persistQuery (Queries query) {
		String str = "SELECT x FROM Queries AS x";
		List <Queries> qu = em.createQuery(str).getResultList();
		
		System.out.println("Queries: " + qu.size());
		
		int id = 1;
		
		if (qu != null && qu.size() > 0) {
			id = qu.size() + 1;
		}		
		
		query.setId(id);
		
		System.out.println("Phone #:" + query.getPhone().getPhone());
		
		em.getTransaction().begin();
		em.persist(query);
		em.getTransaction().commit();
		
		return id;
	}
	

}
