package com.ypeksen.web.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
 
@Controller
public class Welcome {
 
	
	@RequestMapping(value="index.html", method = RequestMethod.GET )
	public String startHtml(){
	    return "/index";
	}
	
/*	@RequestMapping("index/" )
	public String startHtml(){
	    return "/index";
	}
	*/
	@RequestMapping("welcome/")
	public ModelAndView helloWorld() {
 
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hello World, Spring MVC Tutorial</h3>This message is coming from CrunchifyHelloWorld.java **********</div><br><br>";
		return new ModelAndView("welcome", "message", message);
	}
	@RequestMapping("showname/")
	public ModelAndView show(@RequestParam("name") String name, HttpServletRequest request) {
 
		System.out.println("Name is: " + name);
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hello World, Spring MVC Tutorial</h3>This message is coming from CrunchifyHelloWorld.java **********</div><br><br>";
		return new ModelAndView("welcome", "message", message);
	}
}
