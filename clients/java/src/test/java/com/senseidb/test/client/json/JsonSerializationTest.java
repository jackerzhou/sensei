package com.senseidb.test.client.json;


import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.senseidb.search.client.json.JsonDeserializer;
import com.senseidb.search.client.json.JsonSerializer;
import com.senseidb.search.client.json.req.FacetInit;
import com.senseidb.search.client.json.req.Operator;
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
}
