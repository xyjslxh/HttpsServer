package com.vtron.it.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vtron.it.dao.ClientDAO;
import com.vtron.it.model.Client;

@Service
public class ClientService {
	@Autowired
	private ClientDAO clientDAO;

	@Transactional
	public Client findByClientId(String clientId) {
		return clientDAO.findByClientId(clientId);
	}

}
