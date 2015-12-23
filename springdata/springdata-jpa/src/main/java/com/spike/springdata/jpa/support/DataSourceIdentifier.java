package com.spike.springdata.jpa.support;

import org.springframework.util.StringUtils;

/**
 * identifier of a data source
 * 
 * @author zhoujiagen
 *
 */
public final class DataSourceIdentifier {
	private String identifier = "<NOT CARE>";

	private String driverClassName;
	private String url;
	private String username;
	private String password;

	public DataSourceIdentifier() {
	}

	public DataSourceIdentifier(String identifier, String driverClassName, String url, String username, String password) {
		if (!StringUtils.isEmpty(identifier)) {
			this.identifier = identifier;
		}
		this.driverClassName = driverClassName;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "DataSourceIdentifier [identifier=" + identifier + ", driverClassName=" + driverClassName + ", url="
				+ url + ", username=" + username + ", password=<***>]";
	}

}