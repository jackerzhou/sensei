package com.senseidb.test.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.senseidb.search.client.SenseiServiceProxy;
import com.senseidb.search.client.json.JsonDeserializer;
import com.senseidb.search.client.json.JsonSerializer;
import com.senseidb.search.client.req.Operator;
import com.senseidb.search.client.req.SenseiClientRequest;
import com.senseidb.search.client.req.filter.Filters;
import com.senseidb.search.client.req.query.Queries;
import com.senseidb.search.client.req.relevance.Relevance;
import com.senseidb.search.client.res.SenseiResult;

public class JsonSerializationTest extends Assert {


    @Test
    public void testHitsDeserialization() throws Exception {
        String response = new String(IOUtils.getBytes(getClass().getClassLoader().getResourceAsStream("json/senseiresult.json")), "UTF-8");
        System.out.println(new JSONObject(response).toString(2));
        SenseiResult senseiResult = JsonDeserializer.deserialize(SenseiResult.class, new JSONObject(response));
        assertEquals(senseiResult.getHits().get(0).getStoredFields().size(), 14);
        assertEquals(senseiResult.getHits().get(0).getStoredFields().get(0).getFieldName(),"contents");
        System.out.println(senseiResult);
    }

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
       SenseiClientRequest senseiRequest2 = JsonDeserializer.deserialize(SenseiClientRequest.class, new JSONObject(strRepresentation));
       assertEquals(senseiRequest2.getFacets().size(), 1);
       String strRepresentation2 = JsonSerializer.serialize(senseiRequest2).toString();
       assertEquals(strRepresentation2, strRepresentation);

    }
    //@Test
    public void test3DeserializeFacetInit() throws Exception {
       SenseiClientRequest senseiRequest =  SenseiClientRequest.builder().build();
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
    
    // @Test
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
    
}
