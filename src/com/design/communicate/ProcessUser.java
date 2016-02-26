package com.design.communicate;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

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
		
		System.out.println("here: " + users.size());
		
		
		
		if (users == null || users.size() == 0) {
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
		}
	}

}
