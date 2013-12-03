package com.senseidb.search.query.filters;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.json.JSONObject;

import com.senseidb.search.query.QueryConstructor;

public class QueryFilterConstructor extends FilterConstructor {
  public static final String FILTER_TYPE = "query";

  private final QueryParser _qparser;

  public QueryFilterConstructor(QueryParser qparser) {
    _qparser = qparser;
  }

  @Override
  protected Filter doConstructFilter(Object json) throws Exception {
    Query q = QueryConstructor.constructQuery((JSONObject) json, _qparser);
    if (q == null) return null;
    return new QueryWrapperFilter(q);
  }

}
