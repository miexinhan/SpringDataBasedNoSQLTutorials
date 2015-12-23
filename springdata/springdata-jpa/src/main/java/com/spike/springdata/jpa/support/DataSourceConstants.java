package com.spike.springdata.jpa.support;

public final class DataSourceConstants {

	/**
	 * hold keys of spring data source properties
	 * 
	 * spring.datasource.driverClassName=com.mysql.jdbc.Driver<br>
	 * spring.datasource.url=jdbc:mysql://localhost:3306/jpa?useUnicode=yes&
	 * characterEncoding=UTF-8<br>
	 * spring.datasource.username=root<br>
	 * spring.datasource.password=root<br>
	 * 
	 * @author zhoujiagen
	 *
	 */
	public static enum PropertyKey {
		MULTIPLE_TARGET_PREFIX("datasource."), //
		SEPARATOR("."),
		KEY_VALUE_SEP("="),
		
		DRIVER_CLASS_NAME("spring.datasource.driverClassName"), //
		URL("spring.datasource.url"), //
		USERNAME("spring.datasource.username"), //
		PASSWORD("spring.datasource.password");//

		private String key;

		PropertyKey(String key) {
			this.key = key;
		}

		public String getKey() {
			return this.key;
		}
	}

}
