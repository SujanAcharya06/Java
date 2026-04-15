package com.example.jobportal.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // for getter setters
@AllArgsConstructor // all fields constructor
@NoArgsConstructor // empty constructor
@Component // to be able to view in other classes
public class JobPost {
	private int postId;
	private String postProfile;
	private String postDesc;
	private int reqExperience;
	private List<String> postTechStack;
}
