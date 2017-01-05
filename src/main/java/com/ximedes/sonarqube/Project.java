package com.ximedes.sonarqube;

/**
 * Created by rolf on 5-Jan-2017.
 */
public class Project {
	private SonarApi sonarapi;
	private String id;
	private String key;
	private String name;
	private ProjectStatus projectStatus;
	private ProjectLinks projectLinks;

	public Project(SonarApi api, String id, String key, String name) {
		this.sonarapi = api;
		this.id = id;
		this.key = key;
		this.name = name;

		projectStatus = new ProjectStatus(sonarapi, key);
		projectLinks = new ProjectLinks(sonarapi, key, id);
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public String getId() {
		return id;
	}

	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public ProjectLinks getLinks() {
		return projectLinks;
	}

	@Override
	public String toString() {
		return name;
	}
}
