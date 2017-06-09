package com.addictyou.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataBinder {

	private StringProperty temp = new SimpleStringProperty();
	
	public StringProperty getValue(String value){
		temp.set(value);
		return temp;
	}
}
