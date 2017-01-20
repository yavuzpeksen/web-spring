package com.ypeksen.web.spring.service;

import com.ypeksen.web.spring.beans.Registration;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

public class UserOperationImpl implements IUserOperation{
	
	private DataSource dataSource;
	
	public int register(Registration info){
		dataSource.hashCode();
		return 0;
	}
	
	public int login(){
		return 0;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
