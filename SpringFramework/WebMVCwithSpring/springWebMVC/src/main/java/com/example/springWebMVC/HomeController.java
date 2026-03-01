
package com.example.springWebMVC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@ModelAttribute("course")
	public String course() {
		return "Java";
	}

	@RequestMapping("/")
	public String home() {
		return "index";
	}

	@RequestMapping("addStudent")
	public String add(Student student) {
		logger.info("Received sId: {}, sName: {}", student.getsId(),
				student.getsName());
		return "result";
	}

}
