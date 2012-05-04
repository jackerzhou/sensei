package com.senseidb.search.client.json.req;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relevance {
	private Model model = new Model();
	private Map<String, Object> values = new HashMap<String, Object>();

	public static class Model {
		private Map<String, List<String>> variables = new HashMap<String, List<String>>();
		private Map<String, List<String>> facets = new HashMap<String, List<String>>();
		private List<String> function_params;
		private String function;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Relevance relevance = new Relevance();

		public Builder modelVariables(String type, List<String> variables) {
			relevance.model.variables.put(type, variables);
			return this;
		}

		public Builder modelFacets(String type, List<String> facets) {
			relevance.model.facets.put(type, facets);
			return this;
		}

		public Builder modelFunctionParams(List<String> params) {
			relevance.model.function_params = params;
			return this;
		}

		public Builder modelFunction(String function) {
			relevance.model.function = function;
			return this;
		}

		public Builder values(String variable, Object values) {
			relevance.values.put(variable, values);
			return this;
		}

		public Builder values(String variable, List<?> values) {
			relevance.values.put(variable, values);
			return this;
		}

		public Builder values(String variable, Map<?, ?> map) {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("key", map.keySet());
			values.put("value", map.values());
			relevance.values.put(variable, values);
			return this;
		}

		public Relevance build() {
			return relevance;
		}
	}
}
