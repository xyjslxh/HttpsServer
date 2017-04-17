package com.vtron.lc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vtron.lc.common.ObjectUtils;
import com.vtron.lc.dao.ClientDAO;
import com.vtron.lc.model.Client;

@Service
public class ClientService {
	@Autowired
	private ClientDAO clientDAO;

	@Transactional
	public Client find(String clientId, String key) {
		return clientDAO.find(clientId, key);
	}

	/**
	 * 是否已存在该公司
	 * 
	 * @param clientId
	 * @param key
	 * @return
	 */
	@Transactional
	public boolean isExist(String clientId, String key) {
		if (ObjectUtils.isNotEmpty(find(clientId, key))) {
			return true;
		}
		return false;
	}
}
