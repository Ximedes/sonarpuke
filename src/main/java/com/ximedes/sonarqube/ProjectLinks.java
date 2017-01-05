package com.ximedes.sonarqube;

import com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectLinks {
	private final SonarApi sonarapi;
	private Map<String, String> urls = new HashMap<>();

	public ProjectLinks(SonarApi api, String projectKey) {
		this.sonarapi = api;

		LinkedTreeMap response = sonarapi.getLinkedTreeMap("/api/project_links/search?projectKey=" + projectKey);
		List<LinkedTreeMap> links = (List) response.get("links");

		for (LinkedTreeMap link : links) {
			String name = (String) link.get("name");
			String purl = (String) link.get("url");
			urls.put(name, purl);
		}
	}

	public String getHome() {
		return urls.get("Home");
	}

}
