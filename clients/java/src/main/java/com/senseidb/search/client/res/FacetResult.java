package com.senseidb.search.client.res;

public class FacetResult {
    private String value;
    private Boolean selected  = false;
    private Integer count;
    @Override
    public String toString() {
        return "FacetResult [value=" + value + ", selected=" + selected + ", count=" + count + "]";
    }
    public String getValue() {
      return value;
    }
    public Boolean getSelected() {
      return selected;
    }
    public Integer getCount() {
      return count;
    }
	public void setValue(String value) {
		this.value = value;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
