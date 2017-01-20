package com.ypeksen.web.spring.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

//import javax.validation.Valid;

import com.ypeksen.web.spring.jdbc.Database;

import com.ypeksen.web.spring.service.IUser;
import com.ypeksen.web.spring.service.IUserOperation;

import com.ypeksen.web.spring.beans.Registration;
@Controller
@SessionAttributes
public class UserRegistration {
		
		//@Autowired
		private IUser userService;
		
		@Autowired
		private IUserOperation registerService;
		
		// GET Method of Register - shows the page.
/*		@RequestMapping(value="/register", method = RequestMethod.GET)
		public String createNewUser(ModelMap model) {
			// redirect to the registerUser.jsp
			return "/ExternalUsers/registerUser";
		}*/
		

		public IUserOperation getRegisterService() {
			return registerService;
		}

		public void setRegisterService(IUserOperation registerService) {
			this.registerService = registerService;
		}

		// POST Method of Register - comes back after the submit of User Details Form.
		@RequestMapping(value="/register", method = RequestMethod.POST)
		public ModelAndView createNewUser(@ModelAttribute("form") Registration form, ModelMap model, HttpServletRequest request) {
			 
			int serverPort = request.getServerPort();
			if(serverPort != 80 && serverPort != 443) {
				//url.append(":").append(serverPort);
				//return error
			}
			
			
			// Trying arraylist option for displaying errors.
			System.out.println(form.getEmailAddress());
			//System.out.println(userService);
			ArrayList<String> errorCode = new ArrayList<String>();

			Database database = new Database("localhost:3306/saglik", "root", "123456yp");
			int responseCode = registerService.register(form);
			/*if(responseCode == 1){
				//Basarili sekilde kayit oldu. Kod uret ve kodu ilgili email adresine gonder.
			}else if(responseCode == 2){
				//Bu email adresi veritabaninda kayitli, bu bilgiyi kullaniciya gonder..
			}else if(responseCode == 3){
				//Veritabani baglantisi hatali, baska bir zaman tekrar deneyin.
			}*/
			
			try {
				database.sendQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			model.addAttribute("sd",database);
			// use the Form Elements values from Registration form and check for the validations
	
		/*	if (result.hasErrors()){
				System.out.println("comes in form errors of register");
				model.addAttribute("userRegistrationStatus", "Please fill the necessary fields and try again");
				//model.addAttribute("userRegistrationFormBean",userRegistrationFormBean);
				return new ModelAndView( "/ExternalUsers/registerUser",model);
			}*/
			
			
			// Case 2 : Server-side validation : Start the validation before calling the Add User service.
	
			Boolean serverValidationError = false;
			

			// Captcha Check.
			// Commenting the captcha check as in server it is giving some issues. 
			// Check for all the UserRegistrationFormBean values.
/*			if(userRegistrationFormBean.getUserName()==null || !userRegistrationFormBean.getUserName().matches("^[a-zA-Z0-9 ,.]+$"))
			{
				errorCode.add("UserName is not in expected format");
				model.addAttribute("userRegistrationStatus",errorCode);
				serverValidationError = true;
			}			
			if(userRegistrationFormBean.getEmailId()==null || !userRegistrationFormBean.getEmailId().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
			{
				errorCode.add("Email is not in expected format");
				model.addAttribute("userRegistrationStatus",errorCode);
				serverValidationError = true;
			}
			// Go back to register page with these validation errors.
			if(serverValidationError){
				return new ModelAndView("/ExternalUsers/registerUser", model); // return back to register
			}
			

			// Special Error Message : User Already Exists.	
			if (userService.getUserDTOByUsername(userRegistrationFormBean.getUserName()) !=null){
				System.out.println("User exists ");
				model.addAttribute("userRegistrationStatus", "This user already exists. Please try again with another username/email");
				model.addAttribute("userRegistrationFormBean",userRegistrationFormBean);
				return new ModelAndView("/ExternalUsers/registerUser", model);	
			}*/
			
			//TODO for ssn and others.
			
			//|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9])|(?:1[0-2]))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$
		
				return new ModelAndView("/securedLogin/login", model);
	
			
		}
		
	
}
