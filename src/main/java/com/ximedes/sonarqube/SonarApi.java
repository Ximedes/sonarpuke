package com.ximedes.sonarqube;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

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

		try {
			List<LinkedTreeMap> projectList = getProjectList();
			for (int i = 0; i < projectList.size(); i++) {
				Project project = new Project(
						this,
						(String) projectList.get(i).get("id"),
						(String) projectList.get(i).get("k"),
						(String) projectList.get(i).get("nm"));
				projectArrayList.add(project);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return projectArrayList;
	}

	private List<LinkedTreeMap> getProjectList() throws IOException {
		return getLinkedTreeMaps("/api/projects/index");
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


	private Map getProjectLinks(String id) throws IOException {

		URL url = new URL(sonarurl + "/api/project_links/search?projectKey=" + id);

		HashMap<String, String> urls = new HashMap<>();

		try (InputStream content = (InputStream) url.getContent();
			 // Dirty trick using Scanner to read stream into string.
			 Scanner scanner = new Scanner(content).useDelimiter("\\a")
		) {
			while (scanner.hasNext()) {
				String rawJson = scanner.next();
				Gson gson = new Gson();


				LinkedTreeMap linkedTreeMap = gson.fromJson(rawJson, LinkedTreeMap.class);
				List<LinkedTreeMap> links = (List) linkedTreeMap.get("links");

				for (LinkedTreeMap link : links) {
					String name = (String) link.get("name");
					String prul = (String) link.get("url");
					urls.put(name, prul);
				}
			}
		}
		return urls;
	}


}
