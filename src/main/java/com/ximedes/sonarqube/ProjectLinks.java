package com.ximedes.sonarqube;

import com.google.gson.internal.LinkedTreeMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectLinks {
	private final SonarApi sonarapi;
	private String encodedProjectId = "";
	private Map<String, String> urls = new HashMap<>();

	public ProjectLinks(SonarApi api, String projectKey, String projectId) {
		this.sonarapi = api;

		try {
			this.encodedProjectId = URLEncoder.encode(projectId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}

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

	public String getDashboard(){
		return sonarapi.getSonarurl() + "/dashboard?id=" + encodedProjectId;
	}

}
