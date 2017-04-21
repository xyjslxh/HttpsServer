package com.vtron.it.aspect;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import net.sf.json.JSONObject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.Blob;
import com.vtron.it.common.Utils;
import com.vtron.it.model.Record;
import com.vtron.it.service.RecordService;

@Service
@Aspect
public class WebServiceLoger {
	// public ThreadLocal<String> localInfo = new ThreadLocal<String>();
	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	RecordService recordService;

	@SuppressWarnings("finally")
	@Around("execution(public * com.vtron.it.service.InternationalTradeService.declare(..))")
	@Transactional
	private Object logger(final ProceedingJoinPoint pjp) throws Throwable {
		Object returnValue = null;// 返回值
		FutureTask<Object> result = null;
		Record record = new Record();
		try {
			result = new FutureTask<Object>(new Callable<Object>() {

				@Override
				public Object call() throws Exception {
					try {
						return pjp.proceed();
					} catch (Throwable e) {
						e.printStackTrace();
					}
					return null;
				}
			});
			threadPoolTaskExecutor.execute(result);
			Object[] args = pjp.getArgs();
			record.setClientId((String) args[0]);
			record.setKey((String) args[1]);
			record.setMessageType((String) args[2]);
			record.setMessageText(Hibernate.createBlob(((String) args[3]).getBytes()));
			Document document = DocumentHelper.parseText((String) args[3]);
			Element rootElement = document.getRootElement();
			Element dataElement = rootElement.element("Data");
			String dataString = dataElement.getText().replaceAll(" ", "+");
			record.setDecodeText(Hibernate.createBlob(Utils.decodeByBase64(dataString).getBytes()));
			returnValue = result.get();
			record.setReturnText(JSONObject.fromObject(returnValue).toString());
			recordService.save(record);
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return result.get();
		}
	}

	private static String[] types = { "java.lang.String", "java.lang.Integer", "java.lang.Double", "java.lang.Float",
			"java.lang.Long", "java.lang.Short", "java.lang.Byte", "java.lang.Boolean", "java.lang.Char", "int",
			"double", "long", "short", "byte", "boolean", "char", "float" };

	private static String writeLogInfo(String[] paramNames, ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		StringBuilder sb = new StringBuilder();
		boolean clazzFlag = true;
		for (int k = 0; k < args.length; k++) {
			clazzFlag = true;
			Object arg = args[k];
			if (arg != null) {
				sb.append(paramNames[k]);
				// 获取对象类型
				String typeName = arg.getClass().getName();

				for (String t : types) {
					if (t.equals(typeName)) {
						sb.append("=").append(arg).append("; ");
						clazzFlag = false;
						break;
					}
				}
				if (clazzFlag) {
					sb.append(getFieldsValue(arg));
				}
			} else {
				sb.append(paramNames[k]).append("=null");
			}
		}
		return sb.toString();
	}

	/**
	 * 得到方法参数的名称
	 * 
	 * @param cls
	 * @param clazzName
	 * @param methodName
	 * @return
	 * @throws NotFoundException
	 */
	private static String[] getFieldsName(Class cls, String clazzName, String methodName) throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		// ClassClassPath classPath = new ClassClassPath(this.getClass());
		ClassClassPath classPath = new ClassClassPath(cls);
		pool.insertClassPath(classPath);

		CtClass cc = pool.get(clazzName);
		CtMethod cm = cc.getDeclaredMethod(methodName);
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
		if (attr == null) {
			// exception
		}
		String[] paramNames = new String[cm.getParameterTypes().length];
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + pos); // paramNames即参数名
		}
		return paramNames;
	}

	/**
	 * 得到参数的值
	 * 
	 * @param obj
	 */
	public static String getFieldsValue(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		String typeName = obj.getClass().getName();
		for (String t : types) {
			if (t.equals(typeName))
				return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("【");
		for (Field f : fields) {
			f.setAccessible(true);
			try {
				for (String str : types) {
					if (f.getType().getName().equals(str)) {
						sb.append(f.getName()).append(" = ").append(f.get(obj)).append("; ");
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sb.append("】");
		return sb.toString();
	}
}