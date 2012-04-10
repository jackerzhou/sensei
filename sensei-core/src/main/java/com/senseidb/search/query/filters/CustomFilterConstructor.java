package com.senseidb.search.query.filters;

import java.lang.reflect.Constructor;

import org.apache.lucene.search.Filter;
import org.json.JSONArray;
import org.json.JSONObject;

public class CustomFilterConstructor extends FilterConstructor
{

  public static final String FILTER_TYPE = "custom";
  
  
//  // custom
//  "custom" : {
//    "class" : "com.sensidb.query.TestFilter"
// 	  "params": [Long]
//	  "values": [9220789803] 
// 
//	  
//  }
  
  @Override
  protected Filter doConstructFilter(Object json) throws Exception
  {
    try
    {
      JSONObject jsonObject = (JSONObject) json; 
      String className = jsonObject.getString(CLASS_PARAM);
      
      JSONArray paramArray = jsonObject.getJSONArray(PARAMS_PARAM);
      Class[] params = new Class[paramArray.length()];
      for(int i = 0; i < paramArray.length(); i++) {
    	  params[i] = Class.forName((String)paramArray.get(i));
      }
      
      JSONArray valueArray = jsonObject.getJSONArray(VALUES_PARAM);
      Object[] values = new Object[valueArray.length()];
      for(int i = 0; i < valueArray.length(); i++) {
    	  values[i] = params[i].cast(valueArray.get(i));
      }
      Class filterClass = Class.forName(className);
      Constructor constructor = filterClass.getConstructor(params);
      Filter f = (Filter)constructor.newInstance(values);
      
      //Filter f = (Filter)filterClass.newInstance();
      return f;
    }
    catch(Throwable t)
    {
      throw new IllegalArgumentException(t.getMessage());
    }
  }

}
