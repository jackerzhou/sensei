package com.senseidb.search.query.filters;

import java.lang.reflect.Constructor;

import org.apache.commons.codec.binary.Base64;
import org.apache.lucene.search.Filter;
import org.json.JSONArray;
import org.json.JSONObject;

public class CustomFilterConstructor extends FilterConstructor
{

  public static final String FILTER_TYPE = "custom";

  public static final String PARAM_TYPE_INT = "int";
  public static final String PARAM_TYPE_STRING = "string";
  public static final String PARAM_TYPE_BOOLEAN = "boolean";
  public static final String PARAM_TYPE_LONG = "long";
  public static final String PARAM_TYPE_BYTES = "bytes";
  public static final String PARAM_TYPE_DOUBLE = "double";
  
//  custom
//  params type, valid values are: "int","string","boolean","long","bytes","double"
//  "custom" : {
//    "class" : "com.sensidb.query.TestFilter"
// 	  "params": [long, string]
//	  "values": [1, "test"] 
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
      JSONArray valueArray = jsonObject.getJSONArray(VALUES_PARAM);
      Object[] values = new Object[valueArray.length()];
      for(int i = 0; i < paramArray.length(); i++) {
    	  String type = paramArray.getString(i);
    	  if(type.equals(CustomFilterConstructor.PARAM_TYPE_INT)) {
        	  params[i] = Integer.class;
    		  values[i] = valueArray.getInt(i);
    	  } else if(type.equals(CustomFilterConstructor.PARAM_TYPE_STRING)) {
        	  params[i] = String.class;
    		  values[i] = valueArray.getString(i);
    	  } else if(type.equals(CustomFilterConstructor.PARAM_TYPE_BOOLEAN)) {
        	  params[i] = Boolean.class;
    		  values[i] = valueArray.getBoolean(i);
    	  } else if(type.equals(CustomFilterConstructor.PARAM_TYPE_LONG)) {
        	  params[i] = Long.class;
    		  values[i] = valueArray.getLong(i);
    	  } else if(type.equals(CustomFilterConstructor.PARAM_TYPE_BYTES)) {
        	  params[i] = byte[].class;
    	      String base64 = valueArray.getString(i);
    	      byte[] bytes = Base64.decodeBase64(base64);
    	      values[i] = bytes;
    	  } else if(type.equals(CustomFilterConstructor.PARAM_TYPE_DOUBLE)) {
        	  params[i] = Double.class;
    		  values[i] = valueArray.getDouble(i);
    	  }
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
