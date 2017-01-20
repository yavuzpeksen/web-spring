package com.ypeksen.web.spring.utils;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;

import com.google.gson.Gson;

public class ObjectUtils {

private static final Gson gson = new Gson();

public static String object2String(Object obj) {
	return gson.toJson(obj);
}

public static String object2StringXML(Object obj) {

	if(obj == null)
		return null;
	
	try{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		
		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(baos));
		encoder.writeObject(obj);
		encoder.flush();
		encoder.close();
		
		byte[] byteArray = baos.toByteArray();
		
		return new String(byteArray);
	}catch (Throwable th) {
		return obj.toString();
	}
}
}

