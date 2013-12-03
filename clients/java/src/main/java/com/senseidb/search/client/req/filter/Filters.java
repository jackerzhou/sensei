package com.senseidb.search.client.req.filter;

import java.util.Arrays;
import java.util.List;

import com.senseidb.search.client.req.Operator;
import com.senseidb.search.client.req.Path;
import com.senseidb.search.client.req.Range;
import com.senseidb.search.client.req.Selection;
import com.senseidb.search.client.req.Term;
import com.senseidb.search.client.req.Terms;
import com.senseidb.search.client.req.filter.Filter.AndOr;
import com.senseidb.search.client.req.query.Query;

public class Filters {
	public static CustomFilter customFilter(String cls, List<String> params, List<Object> values) {
		return new CustomFilter(cls, params, values);
	}
	
    public static Ids ids(List<String> values, List<String> excludes) {
        return new Ids(values, excludes);
    }

    public static AndOr and(Filter... filters) {
        return new AndOr(Arrays.asList(filters), Operator.and);
    }

    public static AndOr or(Filter... filters) {
        return new AndOr(Arrays.asList(filters), Operator.or);
    }

    public static QueryFilter query(Query query) {
        return new QueryFilter(query);

    }

    public static BoolFilter bool(List<Filter> must, List<Filter> must_not, List<Filter> should) {
        return new BoolFilter(must, must_not, should);
    }

    public static BoolFilter boolMust(Filter... must) {
        return new BoolFilter(Arrays.asList(must), null, null);
    }

    public static BoolFilter boolMustNot(Filter... mustNot) {
        return new BoolFilter( null, Arrays.asList(mustNot), null);
    }

    public static BoolFilter boolShould(Filter... should) {
        return new BoolFilter( null, null, Arrays.asList(should));
    }

    public static IsNull isNull(String fieldName) {
      return new IsNull(fieldName);
  }

    public static Term term(String field, String value) {
        return (Term)new Term(value).setField(field);
    }

  public static Selection terms(String field, List<String> values, List<String> excludes,
      Operator op) {
        return new Terms(values,excludes, op).setField(field);
    }

  public static Selection range(String field, String lower, String upper, boolean includeUpper,
      boolean includeLower) {
         return new Range(lower, upper, includeUpper, includeLower).setField(field);
    }

    public static Selection range(String field, String lower, String upper) {
        return new Range(lower, upper, true, true).setField(field);
   }

  public static Range range(String field, String from, String to, boolean includeLower,
      boolean includeUpper, boolean noOptimize, String type) {
    return (Range) new Range(from, to, includeLower, includeUpper, (Double) null, noOptimize)
        .setField(field);
  }

    public static Selection path(String field, String value, boolean strict, int depth) {
        return new Path(value, strict, depth).setField(field);
    }
}
