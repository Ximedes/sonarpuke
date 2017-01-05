package com.ximedes.sonarpuke;

/**
 * Created by rolf on 5-Jan-2017.
 */
public class Project {
	String id;
	String key;
	String name;
	String qualityGateStatus;
	public String homepage;

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public String getId() {
		return id;
	}

	public String getQualityGateStatus() {
		return qualityGateStatus;
	}

	public String getHomepage() {
		return homepage;
	}

	@Override
	public String toString() {
		return name;
	}
}
