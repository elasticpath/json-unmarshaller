/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.rest.json.unmarshalling.data.multi_level;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.elasticpath.rest.client.unmarshalling.annotations.JsonPath;

//TODO use lombok
public final class SecondLevel {

	//absolute path
	@JsonPath("$.second_level.field1")
	private String field1;

	//relative path
	@JsonPath("@.field2")//must resolve as relative path to @JsonPath/Property annotation on ThirdLevel class
	private String field2;

	@JsonProperty("field3")//must resolve as relative path to @JsonPath/Property annotation on ThirdLevel class
	private String field3;

	private String field4;//matches JSON field in "third-level" JSON node; must be set

	private String field5 = "default 2nd field5";;//doesn't match any Json node; will not be set nor affected

		/*
		Jway has a problem with setter methods - confilict
		private ThirdLevel third_level;

		 */

	@JsonProperty("third_level")
	private ThirdLevel thirdLevelJsonProperty;

	@JsonPath("$.second_level.third_level")
	private ThirdLevel thirdLevelAbsoluteJPath;

	@JsonPath("@.third_level")
	private ThirdLevel thirdLevelRelativeJPath;

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

		/*public ThirdLevel getThird_level() {
			return third_level;
		}

		public void setThird_level(ThirdLevel third_level) {
			this.third_level = third_level;
		}*/

	public ThirdLevel getThirdLevelJsonProperty() {
		return thirdLevelJsonProperty;
	}

	public void setThirdLevelJsonProperty(ThirdLevel thirdLevelJsonProperty) {
		this.thirdLevelJsonProperty = thirdLevelJsonProperty;
	}

	public ThirdLevel getThirdLevelAbsoluteJPath() {
		return thirdLevelAbsoluteJPath;
	}

	public void setThirdLevelAbsoluteJPath(ThirdLevel thirdLevelAbsoluteJPath) {
		this.thirdLevelAbsoluteJPath = thirdLevelAbsoluteJPath;
	}

	public ThirdLevel getThirdLevelRelativeJPath() {
		return thirdLevelRelativeJPath;
	}

	public void setThirdLevelRelativeJPath(ThirdLevel thirdLevelRelativeJPath) {
		this.thirdLevelRelativeJPath = thirdLevelRelativeJPath;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SecondLevel that = (SecondLevel) o;

		if (!field1.equals(that.field1)) {
			return false;
		}
		if (!field2.equals(that.field2)) {
			return false;
		}
		if (!field3.equals(that.field3)) {
			return false;
		}
		if (!field4.equals(that.field4)) {
			return false;
		}
		if (!field5.equals(that.field5)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = field1.hashCode();
		result = 31 * result + field2.hashCode();
		result = 31 * result + field3.hashCode();
		result = 31 * result + field4.hashCode();
		result = 31 * result + field5.hashCode();
		return result;
	}
}
