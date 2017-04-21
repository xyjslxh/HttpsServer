package com.vtron.it.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vtron.it.dao.RecordDAO;
import com.vtron.it.model.Record;

@Service
public class RecordService {
	@Autowired
	private RecordDAO recordDAO;

	@Transactional
	public void save(Record record) {
		recordDAO.persist(record);
	}

}
