package com.ximedes.sonarpuke;


import org.junit.Test;

/**
 * Created by rolf on 5-Jan-2017.
 */
public class AppTest {
	public static final String SONAR_URL = "http://sonar.chess.int";
	public static final String TEMPLATE = "target/classes/dashboard.vm";
	public static final String OUTPUT = "target/index.html";

	@Test
	public void main() throws Exception {
		String[] args = {SONAR_URL, TEMPLATE, OUTPUT};

		App.main(args);
	}
}