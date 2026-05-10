package com.example.jobportal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jobportal.model.JobPost;
import com.example.jobportal.repo.JobRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobService {

	@Autowired
	private JobRepo jobRepo;

	public void addJob(JobPost job) {
		jobRepo.save(job);
	}

	public List<JobPost> getAllJobs() {
		return jobRepo.findAll();
	}

	public JobPost getJob(int id) {
		log.info("In Service, recieved id, {}", id);
		JobPost value = jobRepo.findById(id).orElse(new JobPost());
		log.info("In Service, got value from repo, {}", value);
		return value;
	}

	public JobPost addJobPost(JobPost jobPost) {
		log.info("In Service, recieved jobPost, {}", jobPost);
		JobPost value = jobRepo.save(jobPost);
		log.info("In Service, Fetched jobPost, {}", value);
		return value;
	}

	public JobPost updateJobPost(int id, JobPost jobPost) {
		log.info("In Service, recieved JobPost, {}", jobPost);
		JobPost value = jobRepo.save(jobPost);
		log.info("In Service, got jobPost after updating, {}", jobPost);
		return value;
	}

	public void removeJobPost(int id) {
		jobRepo.deleteById(id);
	}

	public void removeAllJobPost() {
		jobRepo.deleteAll();

	}

	public void load() {
		List<JobPost> jobs = new ArrayList<>(
				Arrays.asList(
						new JobPost(
								1,
								"Java Developer",
								"Must have good experience in core java",
								4,
								new ArrayList<>(Arrays.asList("Java"))),

						new JobPost(
								2,
								"Frontend Developer",
								"Must have good experience in React and JavaScript",
								4,
								new ArrayList<>(Arrays.asList("React", "JavaScript", "TypeScript"))),

						new JobPost(
								3,
								"Data Scientist",
								"Must have good experience in Python",
								4,
								new ArrayList<>(Arrays.asList("Python"))),

						new JobPost(
								4,
								"Network Engineer",
								"Must have good experience in Network layers, Protocols",
								4,
								new ArrayList<>(Arrays.asList("TCP/IP layers", "Networking"))),

						new JobPost(
								5,
								"Spring Boot Developer",
								"Must have good experience in core Java and Spring Boot",
								4,
								new ArrayList<>(Arrays.asList("Java", "Spring Boot", "Database")))));

		jobRepo.saveAll(jobs);
	}

	public List<JobPost> searchKeywod(String keyword) {
		return jobRepo.findByPostProfileContainingOrPostDescContaining(keyword, keyword);
	}

}
