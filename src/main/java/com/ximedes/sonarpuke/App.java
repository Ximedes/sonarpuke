package com.ximedes.sonarpuke;

import com.ximedes.sonarqube.Project;
import com.ximedes.sonarqube.ProjectStatus;
import com.ximedes.sonarqube.SonarApi;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Dirty code to read Sonar Project Status and generate an overview in a table.
 */
public class App {

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println("Usage:");
			System.out.println("  sonarpuke <sonar url> <velocity template> <output>");

			System.out.println("");
			System.out.println("Example:");
			System.out.println("  sonarpuke http://sonar.mycompany.net dashboard.vm index.html");

			return;
		}


		String sonarurl = args[0];
		String velocitytemplate = args[1];
		String outputfile = args[2];

		SonarApi sonarApi = new SonarApi(sonarurl);
		List<Project> projects = sonarApi.getProjects();

		// Example for project sorting
		Collections.sort(projects, new Comparator<Project>() {
			@Override
			public int compare(Project o1, Project o2) {
				List<String> statusOrder = Arrays.asList(new String[]{"ERROR", "WARN", "NONE", "OK"});

				int i1 = statusOrder.indexOf(o1.getProjectStatus().getStatus());
				int i2 = statusOrder.indexOf(o2.getProjectStatus().getStatus());

				return i1-i2;
			}
		});

		// Example for using Velocity to generate an html page
		Velocity.init();
		VelocityContext ctx = new VelocityContext();
		ctx.put("projects", projects);

		try (FileWriter fileWriter = new FileWriter(new File(outputfile))) {
			Velocity.mergeTemplate(velocitytemplate, "UTF-8", ctx, fileWriter);
		}

		System.out.println("Written dashboard for "+sonarurl);
		System.out.println("             based on "+velocitytemplate);
		System.out.println("                   to "+outputfile);
	}
}
