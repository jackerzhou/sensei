package com.senseidb.search.client.req.filter;

import java.util.ArrayList;
import java.util.List;

import com.senseidb.search.client.json.CustomJsonHandler;
import com.senseidb.search.client.json.JsonField;

/**
 * User may supply his own implementation of the query
 *
 */
@CustomJsonHandler(FilterJsonHandler.class)
public class CustomFilter implements Filter {
  @JsonField("class")
  private String cls;
  @JsonField("params")
  private List<String> params = new ArrayList<String>();
  @JsonField("values")
  private List<Object> values = new ArrayList<Object>();

  public CustomFilter(String cls, List<String> params, List<Object> values) {
    super();
    this.cls = cls;
    this.params = params;
    this.values = values;
  }
  
  public String getCls() {
	  return cls;
  }
  
  public List<String> getParams() {
	  return params;
  }

  public List<Object> getValues() {
	  return values;
  }

}
