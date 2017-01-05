package com.ximedes.sonarqube;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Abstraction layer for accessing the Sonar Web API.
 */
public class SonarApi {

	private String sonarurl;

	public SonarApi(String url) {
		this.sonarurl = url;
	}

	public List<Project> getProjects() {
		List<Project> projectArrayList = new ArrayList<>();

		List<LinkedTreeMap> projectList = getLinkedTreeMaps("/api/projects/index");
		for (int i = 0; i < projectList.size(); i++) {
			Project project = new Project(
					this,
					(String) projectList.get(i).get("id"),
					(String) projectList.get(i).get("k"),
					(String) projectList.get(i).get("nm"));
			projectArrayList.add(project);
		}

		return projectArrayList;
	}

	LinkedTreeMap getLinkedTreeMap(String s) {
		try (InputStream content = (InputStream) new URL(sonarurl + s).getContent();
			 // Dirty trick using Scanner to read stream into string.
			 Scanner scanner = new Scanner(content).useDelimiter("\\a")
		) {
			while (scanner.hasNext()) {
				String rawJson = scanner.next();
				Gson gson = new Gson();
				return gson.fromJson(rawJson, LinkedTreeMap.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new LinkedTreeMap();
	}

	List<LinkedTreeMap> getLinkedTreeMaps(String s) {
		try (InputStream content = (InputStream) new URL(sonarurl + s).getContent();
			 // Dirty trick using Scanner to read stream into string.
			 Scanner scanner = new Scanner(content).useDelimiter("\\a")
		) {
			while (scanner.hasNext()) {
				String rawJson = scanner.next();
				Gson gson = new Gson();
				return gson.fromJson(rawJson, List.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
