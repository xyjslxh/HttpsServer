package com.vtron.lc.dao;

// Generated 2015-4-17 9:12:04 by Hibernate Tools 3.4.0.CR1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vtron.lc.common.ObjectUtils;
import com.vtron.lc.model.Client;

@Repository
public class ClientDAO {

	private static final Log log = LogFactory.getLog(ClientDAO.class);
	private static final String DELETE_SQL = "delete from Client where id = ? ";
	@Autowired
	private SessionFactory sessionFactory;

	public int remove(int id) {
		log.debug("deleting Client instance");
		try {
			Query deleteQuery = sessionFactory.getCurrentSession().createQuery(DELETE_SQL);
			deleteQuery.setInteger(0, id);
			log.debug("delete successful");
			int result = deleteQuery.executeUpdate();
			return result;
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void persist(Client transientInstance) {
		log.debug("persisting Client instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Client instance) {
		log.debug("attaching dirty Client instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Client instance) {
		log.debug("attaching clean Client instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Client persistentInstance) {
		log.debug("deleting Client instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Client merge(Client detachedInstance) {
		log.debug("merging Client instance");
		try {
			Client result = (Client) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Client findById(int id) {
		log.debug("getting Client instance with id: " + id);
		try {
			Client instance = (Client) sessionFactory.getCurrentSession().get(Client.class, id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Client> findByExample(Client instance) {
		log.debug("finding Client instance by example");
		try {
			List<Client> results = (List<Client>) sessionFactory.getCurrentSession().createCriteria(Client.class)
					.add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public Client find(String clientId, String key) {
		try {
			String hql = " FROM Client WHERE clientId = ? AND key = ?";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, clientId);
			query.setString(1, key);
			List<Client> list = query.list();
			if (ObjectUtils.isNotEmpty(list)) {
				return list.get(0);
			}
		} catch (RuntimeException re) {
			throw re;
		}
		return null;
	}
}
