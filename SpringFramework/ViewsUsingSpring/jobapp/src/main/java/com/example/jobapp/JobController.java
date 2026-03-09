
package com.example.jobapp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.jobapp.model.JobPost;
import com.example.jobapp.service.JobService;

@Controller
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);

	@Autowired
	private JobService jobService;

	@GetMapping({ "/", "home" })
	public String home() {
		logger.info("Home method called");
		return "home";
	}

	@GetMapping("addjob")
	public String addjob() {
		return "addjob";
	}

	@PostMapping("handleForm")
	public String handleForm(JobPost jobPost) {
		jobService.addJob(jobPost);
		return "success";
	}

	@GetMapping("viewalljobs")
	public String viewAllJobs(Model m) {
		List<JobPost> jobs = jobService.getAllJobs();
		m.addAttribute("jobPosts", jobs);
		return "viewalljobs";
	}

}
