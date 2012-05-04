package com.senseidb.test.client.json;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.senseidb.search.client.json.JsonDeserializer;
import com.senseidb.search.client.json.JsonSerializer;
import com.senseidb.search.client.json.SenseiServiceProxy;
import com.senseidb.search.client.json.req.FacetInit;
import com.senseidb.search.client.json.req.Operator;
import com.senseidb.search.client.json.req.Relevance;
import com.senseidb.search.client.json.req.SenseiClientRequest;
import com.senseidb.search.client.json.req.filter.Filters;
import com.senseidb.search.client.json.req.query.Queries;
import com.senseidb.search.client.json.req.query.Query;
import com.senseidb.search.client.json.res.SenseiResult;

public class JsonSerializationTest extends Assert {

    @Test
    public void test1Deserialization() throws Exception {
        String response = new String(IOUtils.getBytes(getClass().getClassLoader().getResourceAsStream("json/senseiresult.json")), "UTF-8");
        System.out.println(new JSONObject(response).toString(2));
        SenseiResult senseiResult = JsonDeserializer.deserialize(SenseiResult.class, new JSONObject(response));
        assertEquals(senseiResult.getFacets().size(), 2);
        System.out.println(senseiResult);
    }
    @Test
    public void test2Serialization() throws Exception {
        SenseiClientRequest senseiRequest = Examples.basicWithSelections(SenseiClientRequest.builder()).build();
       String strRepresentation = JsonSerializer.serialize(senseiRequest).toString();
       System.out.println(strRepresentation);
       SenseiClientRequest senseiRequest2 = JsonDeserializer.deserialize(SenseiClientRequest.class, new JSONObject(strRepresentation));
       assertEquals(senseiRequest2.getFacets().size(), 1);
       System.out.println(senseiRequest2.toString());
       String strRepresentation2 = JsonSerializer.serialize(senseiRequest2).toString();
       System.out.println(strRepresentation2);
       assertEquals(strRepresentation2, strRepresentation);

    }
    //@Test
    public void test3DeserializeFacetInit() throws Exception {
        SenseiClientRequest senseiRequest =  SenseiClientRequest.builder()
                .addFacetInit("name", "parameter", FacetInit.build("string", "val1", "val2")).build();
       String strRepresentation = JsonSerializer.serialize(senseiRequest).toString();
       SenseiClientRequest senseiRequest2 = JsonDeserializer.deserialize(SenseiClientRequest.class, new JSONObject(strRepresentation));
       String strRepresentation2 = JsonSerializer.serialize(senseiRequest2).toString();
       System.out.println(strRepresentation2);
       assertEquals(strRepresentation2, strRepresentation);

    }
    //@Test
    public void test4FiltersSerialization() throws Exception {
        SenseiClientRequest senseiRequest =  Examples.filters(SenseiClientRequest.builder()).build();
       JSONObject json = (JSONObject) JsonSerializer.serialize(senseiRequest);
       System.out.println(json.toString(3));
    }
    //@Test
    public void test5QueriesSerialization() throws Exception {
        SenseiClientRequest senseiRequest =  Examples.queries(SenseiClientRequest.builder()).build();
       JSONObject json = (JSONObject) JsonSerializer.serialize(senseiRequest);
       System.out.println(json.toString(3));
    }
    
    @Test
    public void testTextQueriesWithAnalyzerSerialization() throws Exception {
    	SenseiClientRequest.Builder builder = SenseiClientRequest.builder();
    	List innerQueries = Arrays.asList(
    			Queries.textQuery("name", "anything", Operator.or, 1.0, "org.apache.lucene.analysis.WhitespaceAnalyzer")
    	);
    	builder.query(Queries.bool(innerQueries, null, null, 2, 2.0, true));
    	SenseiClientRequest senseiRequest =  builder.build();
        JSONObject json = (JSONObject) JsonSerializer.serialize(senseiRequest);
        assertTrue(json.toString().indexOf("analyzer") != 0);
        System.out.println(json.toString());
    }
    
    @Test
    public void testStringQueriesWithAnalyzerSerialization() throws Exception {
    	SenseiClientRequest.Builder builder = SenseiClientRequest.builder();
    	List innerQueries = Arrays.asList(
    			Queries.stringQueryBuilder().query("anything").defaultField("anyfield").analyzer("org.apache.lucene.analysis.WhitespaceAnalyzer").build()
    	);
    	builder.query(Queries.bool(innerQueries, null, null, 2, 2.0, true));
    	SenseiClientRequest senseiRequest =  builder.build();
        JSONObject json = (JSONObject) JsonSerializer.serialize(senseiRequest);
        assertTrue(json.toString().indexOf("analyzer") != 0);
        System.out.println(json.toString());
    }
    
    @Test
    public void testCustomFilterSerialization() throws Exception {
    	SenseiClientRequest.Builder builder = SenseiClientRequest.builder();
    	builder.filter(
    			Filters.customFilter("org.apache.lucene.analysis.Filter", 
    							     Arrays.asList("long"), 
    							     Arrays.asList((Object)new Integer(100000)))
    	);

    	SenseiClientRequest senseiRequest =  builder.build();
        JSONObject json = (JSONObject) JsonSerializer.serialize(senseiRequest);
        assertTrue(json.toString().indexOf("custom") != 0);
        System.out.println(json.toString());    	
    }
    
    @Test
    public void testQuerySomething() throws Exception {
    	SenseiServiceProxy ssp = new SenseiServiceProxy("localhost", 8100);
    	SenseiClientRequest.Builder builder = SenseiClientRequest.builder();
    	
//    	List innerQueries = Arrays.asList(
//    			Queries.textQuery("name", "anybody", Operator.or, 1.0, "org.apache.lucene.analysis.WhitespaceAnalyzer")
//    	);
    	List innerQueries = Arrays.asList(
    			Queries.stringQueryBuilder().query("anybody").defaultField("name").analyzer("org.apache.lucene.analysis.WhitespaceAnalyzer").build()
    	);    	
    	builder.query(Queries.bool(innerQueries, null, null, 2, 2.0, true));

    	builder.filter(
    			Filters.customFilter("org.apache.lucene.analysis.Filter", 
    							     Arrays.asList("long"), 
    							     Arrays.asList((Object)new Long(100000)))
    	);
    	builder.fetchStored(true);
    	SenseiClientRequest senseiRequest =  builder.build();

    	SenseiResult result = ssp.sendSearchRequest(senseiRequest);
    	System.out.println(result);
    }
    
    @Test
	public void testTextQueryWithRelevanceSerialization() throws Exception {
		SenseiClientRequest.Builder builder = SenseiClientRequest.builder();
		
		Map<Integer, Float> mileageWeight = new HashMap<Integer,Float>();
		mileageWeight.put(11400, 777.9f);
		mileageWeight.put(11100, 10.2f);
		
		Map<Integer, String> yearcolor = new HashMap<Integer,String>();
		yearcolor.put(1998, "red");
		
		Map<String, Float> colorweight = new HashMap<String,Float>();
		colorweight.put("red", 335.5f);
		
		Map<String, String> categorycolor = new HashMap<String,String>();
		categorycolor.put("compact", "red");
		
		Relevance relevance = Relevance
				.builder()
				.modelVariables("set_int", Arrays.asList("goodYear"))
				.modelVariables("int", Arrays.asList("thisYear"))
				.modelVariables("string", Arrays.asList("coolTag"))
				.modelVariables("map_int_float", Arrays.asList("yearcolor"))
				.modelVariables("map_string_float", Arrays.asList("colorweight"))
				.modelVariables("map_string_string", Arrays.asList("categorycolor"))
				.modelFacets("int", Arrays.asList("year", "mileage"))
				.modelFacets("long", Arrays.asList("groupid"))
				.modelFacets("string", Arrays.asList("color","category"))
				.modelFacets("mstring", Arrays.asList("tags"))
				.modelFunctionParams(Arrays.asList("_INNER_SCORE", "thisYear", "year","goodYear","mileageWeight","mileage","color", "yearcolor", "colorweight", "category", "categorycolor"))
				.modelFunction("  if(tags.contains(coolTag)) return 999999f; if(categorycolor.containsKey(category) && categorycolor.get(category).equals(color))  return 10000f; if(colorweight.containsKey(color) ) return 200f + colorweight.getFloat(color); if(yearcolor.containsKey(year) && yearcolor.get(year).equals(color)) return 200f; if(mileageWeight.containsKey(mileage)) return 10000+mileageWeight.get(mileage); if(goodYear.contains(year)) return (float)Math.exp(2d);   if(year==thisYear) return 87f   ; return  _INNER_SCORE;")
				.values("goodYear", Arrays.asList(1996, 1997))
				.values("thisYear", 2001)
				.values("mileageWeight", mileageWeight)
				.values("yearcolor", yearcolor)
				.values("categorycolor", categorycolor)
				.values("coolTag", "cool")
				.build();
		
		SenseiClientRequest senseiRequest = builder.query(Queries.stringQuery("", relevance)).paging(0, 6).build();
		JSONObject json = (JSONObject) JsonSerializer.serialize(senseiRequest);
        assertTrue(json.toString().indexOf("relevance") != 0);
        System.out.println(json.toString(3));
	}
}
