package com.ypeksen.web.spring.service;

import com.ypeksen.web.spring.beans.Registration;

public interface IUserOperation {
	
	public int register(Registration info);
	
	public int login();

}
