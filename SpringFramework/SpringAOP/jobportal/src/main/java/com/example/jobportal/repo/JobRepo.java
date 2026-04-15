package com.example.jobportal.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.jobportal.model.JobPost;

@Repository
public class JobRepo {

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

	public List<JobPost> getAllJobs() {
		return jobs;
	}

	public void addJob(JobPost job) {
		jobs.add(job);
	}

	public JobPost getJob(int id) {
		JobPost returnValue = null;
		for (JobPost jobPost : jobs) {
			if (jobPost.getPostId() == id) {
				returnValue = jobPost;
				break;
			}
		}
		return returnValue;
	}

	public JobPost addJobPost(JobPost jobPost) {
		jobs.add(jobPost);
		return jobPost;
	}

	public JobPost updateJobPost(int id, JobPost jobPost) {
		for (JobPost jobpost : jobs) {
			if (jobPost.getPostId() == id) {
				jobPost.setPostProfile(jobPost.getPostProfile());
				jobPost.setPostDesc(jobPost.getPostDesc());
				jobPost.setReqExperience(jobPost.getReqExperience());
				jobPost.setPostTechStack(jobPost.getPostTechStack());
				break;
			}
		}

		return jobPost;
	}

	public void removeJobPost(int id) {

		for (JobPost jobPost : jobs) {
			if (jobPost.getPostId() == id) {
				jobs.remove(jobPost.getPostId());
				break;
			}
		}

	}
}
