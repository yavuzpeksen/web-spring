package com.ypeksen.web.spring.service;

import java.util.List;

public interface IUser {
	// Login check
	public boolean validateUser(String userName, String password);
	//public UserDTO getUserDTOByUsername(String userName);
	
	// Register User / Add User
	//public boolean addNewUser(UserRegistrationFormBean userRegistrationFormBean);

	//Update/Edit User details
	//public Boolean updateUserDetails(UserDetailsFormBean userDetailsFormBean);
	//public UserDTO getUserDTOByUserId(Integer userId);

	//Get User(s) information
	//public List<UserDTO> listUsers();
	//public UserDTO getUserinfo(Integer userId); // single user DTO

    //Delete User
	public void deleteUser(Integer userId);	
	

}
