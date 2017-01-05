package com.ximedes.sonarpuke;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Dirty code to read Sonar Project Status and generate an overview in a table.
 */
public class App {

	public static final String SONAR_URL = "http://sonar.chess.int";

	public static void main(String[] args) throws Exception {
		List<Project> projects = new ArrayList<>();
		List<LinkedTreeMap> projectList = getProjectList();

		// Getting all projects and statusses.
		for (int i = 0; i < projectList.size(); i++) {
			Project project = new Project();
			project.id = (String) projectList.get(i).get("id");
			project.key = (String) projectList.get(i).get("k");
			project.name = (String) projectList.get(i).get("nm");

			LinkedTreeMap response = getProjectStatus(project.key);
			LinkedTreeMap projectStatus = (LinkedTreeMap) response.get("projectStatus");
			project.qualityGateStatus = (String) projectStatus.get("status");


			Map<String,String> projectLinks = getProjectLinks(project.key);
			project.homepage = projectLinks.get("Home");

			projects.add(project);
		}


		// Example for project sorting
		Collections.sort(projects, new Comparator<Project>() {
			@Override
			public int compare(Project o1, Project o2) {
				return o1.qualityGateStatus.compareTo(o2.qualityGateStatus);
			}
		});

		// Example for using Velocity to generate an html page
		Velocity.init();
		VelocityContext ctx = new VelocityContext();
		ctx.put("projects", projects);

		try (FileWriter fileWriter = new FileWriter(new File("target/index.html"))) {
			Velocity.mergeTemplate("target/classes/dashboard.vm", "UTF-8", ctx, fileWriter);
		}
	}

	private static LinkedTreeMap getProjectStatus(String id) throws IOException {
		URL url = new URL(SONAR_URL + "/api/qualitygates/project_status?projectKey=" + id);

		try (InputStream content = (InputStream) url.getContent();
			 // Dirty trick using Scanner to read stream into string.
			 Scanner scanner = new Scanner(content).useDelimiter("\\a")
		) {
			while (scanner.hasNext()) {
				String rawJson = scanner.next();
				Gson gson = new Gson();
				return gson.fromJson(rawJson, LinkedTreeMap.class);
			}
		}
		return null;

	}

	private static List<LinkedTreeMap> getProjectList() throws IOException {
		URL url = new URL(SONAR_URL + "/api/projects/index");
		try (InputStream content = (InputStream) url.getContent();
			 // Dirty trick using Scanner to read stream into string.
			 Scanner scanner = new Scanner(content).useDelimiter("\\a")
		) {
			while (scanner.hasNext()) {
				String rawJson = scanner.next();
				Gson gson = new Gson();
				return gson.fromJson(rawJson, List.class);
			}
		}
		return null;
	}

	private static Map getProjectLinks(String id) throws IOException {

		URL url = new URL(SONAR_URL + "/api/project_links/search?projectKey=" + id);

		HashMap<String, String> urls = new HashMap<>();

		try (InputStream content = (InputStream) url.getContent();
			 // Dirty trick using Scanner to read stream into string.
			 Scanner scanner = new Scanner(content).useDelimiter("\\a")
		) {
			while (scanner.hasNext()) {
				String rawJson = scanner.next();
				Gson gson = new Gson();


				LinkedTreeMap linkedTreeMap = gson.fromJson(rawJson, LinkedTreeMap.class);
				List<LinkedTreeMap> links = (List)linkedTreeMap.get("links");

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
