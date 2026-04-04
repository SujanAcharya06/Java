package com.example.jobportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.jobportal.model.JobPost;
import com.example.jobportal.repo.JobRepo;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobService {

	@Autowired
	private JobRepo jobRepo;

	public void addJob(JobPost job) {
		jobRepo.addJob(job);

	}

	public List<JobPost> getAllJobs() {
		return jobRepo.getAllJobs();
	}

	public JobPost getJob(int id) {
		log.info("In Service, recieved id, {}", id);
		JobPost value = jobRepo.getJob(id);
		log.info("In Service, got value from repo, {}", value);
		return value;
	}

	public JobPost addJobPost(JobPost jobPost) {
		log.info("In Service, recieved jobPost, {}", jobPost);
		JobPost value = jobRepo.addJobPost(jobPost);
		log.info("In Service, Fetched jobPost, {}", value);
		return value;
	}

	public JobPost updateJobPost(int id, JobPost jobPost) {
		log.info("In Service, recieved JobPost, {}", jobPost);
		JobPost value = jobRepo.updateJobPost(id, jobPost);
		log.info("In Service, got jobPost after updating, {}", jobPost);
		return value;
	}

	public void removeJobPost(int id) {
		jobRepo.removeJobPost(id);
	}

}
