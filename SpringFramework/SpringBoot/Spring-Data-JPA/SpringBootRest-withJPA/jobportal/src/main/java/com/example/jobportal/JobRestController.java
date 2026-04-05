package com.example.jobportal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.jobportal.model.JobPost;
import com.example.jobportal.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class JobRestController {

	@Autowired
	private JobService jobService;

	@GetMapping(path = "jobPosts", produces = { "application/json" })
	@ResponseBody
	public List<JobPost> jobPosts() {
		return jobService.getAllJobs();
	}

	@GetMapping("jobPost/{id}")
	@ResponseBody
	public JobPost getJobPost(@PathVariable int id) {
		log.info("In Controller, recieved id, {}", id);
		JobPost value = jobService.getJob(id);
		log.info("In Controller, fetched jobPost, {}", value);
		return value;
	}

	@GetMapping("jobPost/keyword/{keyword}")
	@ResponseBody
	public List<JobPost> searchKeyword(@PathVariable String keyword) {
		return jobService.searchKeywod(keyword);
	}

	@PostMapping("jobPost")
	public ResponseEntity<JobPost> addJobPost(@RequestBody JobPost jobPost) {
		log.info("In Controller, recieved jobPost, {}", jobPost);
		JobPost value = jobService.addJobPost(jobPost);
		return ResponseEntity.ok(value);
	}

	@PutMapping("jobPost/{id}")
	public ResponseEntity<JobPost> updateJobPost(@PathVariable int id,
			@RequestBody JobPost jobPost) {
		log.info("In Controller, recieved id {} and jobPost, {}", id, jobPost);
		JobPost value = jobService.updateJobPost(id, jobPost);
		if (value != null) {
			return ResponseEntity.ok(value);
		}
		return ResponseEntity.status(404).body(null);
	}

	@DeleteMapping("jobPost/{id}")
	public ResponseEntity<String> removeJobPost(@PathVariable int id) {
		jobService.removeJobPost(id);
		return ResponseEntity.ok("JobPost removed successfully");
	}

	@DeleteMapping("jobPosts")
	@ResponseBody
	public String removeAllJobPost() {
		jobService.removeAllJobPost();
		return "All jobPosts removed";
	}

	@GetMapping("load")
	@ResponseBody
	public String loadJobs() {
		jobService.load();
		return "Success";
	}

}
