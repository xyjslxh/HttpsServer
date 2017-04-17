package com.vtron.lc.service;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtron.lc.common.Keys;
import com.vtron.lc.common.Utils;

@Service
public class InternationalTradeService {
	@Autowired
	ClientService clientService;

	public JSONObject declare(String clientId, String key, String messageType, String messageText) {
		JSONObject result = new JSONObject();
		// 查询备案号与密钥
		if (clientService.isExist(clientId, key)) {
			// 解密
			try {
				// messageText = URLDecoder.decode(messageText, "UTF-8");
				Document document = DocumentHelper.parseText(messageText);
				Element root = document.getRootElement();
				Element dataElement = root.element("Data");
				String dataString = dataElement.getText().replaceAll(" ", "+");
				// System.out.println("Data=" + dataString);
				String decodeMessageText = Utils.decodeByBase64(dataString);
				// System.out.println("decodeData=" + decodeMessageText);
				// document = DocumentHelper.parseText(decodeMessageText);
				// 根据报文类型校验

				// 结果:成功
				result.put(Keys.RESULT, true);
				result.put(Keys.DESCRIPTION, decodeMessageText);
			} catch (DocumentException e) {
				e.printStackTrace();
				result.put(Keys.RESULT, false);
				result.put(Keys.ERROR_CODE, 0);
				result.put(Keys.DESCRIPTION, "申报报文格式有误");
			}

		} else {
			// 结果:失败
			result.put(Keys.RESULT, false);
			result.put(Keys.ERROR_CODE, 0);
			result.put(Keys.DESCRIPTION, "备案号或密钥不正确");
		}

		return result;
	}
}
