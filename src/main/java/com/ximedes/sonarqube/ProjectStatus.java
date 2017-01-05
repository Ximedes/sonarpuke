package com.ximedes.sonarqube;

import com.google.gson.internal.LinkedTreeMap;

public class ProjectStatus {

	private final SonarApi sonarapi;
	private final String status;

	ProjectStatus(SonarApi api, String projectKey) {
		this.sonarapi = api;
		LinkedTreeMap response = sonarapi.getLinkedTreeMap("/api/qualitygates/project_status?projectKey=" + projectKey);
		LinkedTreeMap projectStatus = (LinkedTreeMap) response.get("projectStatus");
		this. status = (String) projectStatus.get("status");
	}

	public String getStatus() {
		return status;
	}
}
