package com.vtron.lc.controller;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vtron.lc.service.InternationalTradeService;

@Controller
@RequestMapping("InternationalTrade")
public class InternationalTradeController {
	@Autowired
	InternationalTradeService internationalTradeService;

	@RequestMapping(value = "/declare", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject declare(String clientId, String key, String messageType, String messageText) {
//		System.out.println("clientId=" + clientId);
//		System.out.println("key=" + key);
//		System.out.println("messageType=" + messageType);
//		System.out.println("messageText=" + messageText);
		return internationalTradeService.declare(clientId, key, messageType, messageText);
	}
}
