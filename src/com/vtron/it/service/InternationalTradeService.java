package com.vtron.it.service;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtron.it.common.HttpRequest;
import com.vtron.it.common.Keys;
import com.vtron.it.common.ObjectUtils;
import com.vtron.it.model.Client;

@Service
public class InternationalTradeService {
	@Autowired
	ClientService clientService;
	private static final String URL = "http://58.63.50.170:18080/cbt/client/declare/sendMessage.action";

	public JSONObject declare(String clientId, String key, String messageType, String messageText) {
		JSONObject result = new JSONObject();
		// 查询备案号
		Client client = clientService.findByClientId(clientId);
		// 校验备案号
		if (ObjectUtils.isNotEmpty(client)) {
			// 校验密钥
			if (!client.getKey().equals(key)) {
				// 结果:失败
				result.put(Keys.DESCRIPTION, "电子口岸备案号【" + clientId + "】接口调用密钥不正确！");
				result.put(Keys.ERROR_CODE, "0.3.2");
				result.put(Keys.RESULT, false);
				return result;
			}

			// 解密
			try {
				// messageText = URLDecoder.decode(messageText, "UTF-8");
				Document document = DocumentHelper.parseText(messageText);
				Element root = document.getRootElement();
				Element dataElement = root.element("Data");
				String dataString = dataElement.getText().replaceAll(" ", "+");
				dataElement.setText(dataString);
				messageText = document.asXML();
				// System.out.println("Data=" + dataString);
				// String decodeMessageText = Utils.decodeByBase64(dataString);
				// System.out.println("decodeData=" + decodeMessageText);
				// document = DocumentHelper.parseText(decodeMessageText);
				// 根据报文类型校验

				// 结果:成功
				// result.put(Keys.RESULT, true);
				// result.put(Keys.DESCRIPTION, decodeMessageText);
				String string = HttpRequest.sendPost(URL, "clientid=" + clientId + "&key=" + key + "&messageType="
						+ messageType + "&messageText=" + messageText);
				return JSONObject.fromObject(string);
			} catch (DocumentException e) {
				e.printStackTrace();
				result.put(Keys.RESULT, false);
				result.put(Keys.ERROR_CODE, 0);
				result.put(Keys.DESCRIPTION, "申报报文格式有误");
				return result;
			}

		}
		// 结果:失败
		result.put(Keys.DESCRIPTION, "电子口岸备案号【" + clientId + "】不存在，请与系统管理员联系！");
		result.put(Keys.ERROR_CODE, "0.3.1");
		result.put(Keys.RESULT, false);
		return result;

	}
}
