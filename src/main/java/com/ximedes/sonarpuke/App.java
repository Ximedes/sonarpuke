package com.ximedes.sonarpuke;

import com.ximedes.sonarqube.Project;
import com.ximedes.sonarqube.ProjectStatus;
import com.ximedes.sonarqube.SonarApi;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Dirty code to read Sonar Project Status and generate an overview in a table.
 */
public class App {

	public static final String SONAR_URL = "http://sonar.chess.int";

	public static void main(String[] args) throws Exception {

		SonarApi sonarApi = new SonarApi(SONAR_URL);
		List<Project> projects = sonarApi.getProjects();

		// Example for project sorting
		Collections.sort(projects, new Comparator<Project>() {
			@Override
			public int compare(Project o1, Project o2) {
				String status1 = o1.getProjectStatus().getStatus();
				ProjectStatus status2 = o2.getProjectStatus();

				if (status1 != null) {
					return status1.compareTo(status2.getStatus());
				} else {
					return -1;
				}

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
}
