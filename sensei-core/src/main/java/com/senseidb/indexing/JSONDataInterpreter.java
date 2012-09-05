package com.senseidb.indexing;

import org.apache.lucene.document.Document;
import org.json.JSONException;
import org.json.JSONObject;

import proj.zoie.api.indexing.AbstractZoieIndexable;
import proj.zoie.api.indexing.AbstractZoieIndexableInterpreter;
import proj.zoie.api.indexing.ZoieIndexable;
import proj.zoie.api.indexing.ZoieIndexable.IndexingReq;


public abstract class JSONDataInterpreter extends AbstractZoieIndexableInterpreter<JSONObject> {

	@Override
	public ZoieIndexable convertAndInterpret(final JSONObject src) {
		return new AbstractZoieIndexable(){

			@Override
			public IndexingReq[] buildIndexingReqs() {
			  try{
				return new IndexingReq[]{new IndexingReq(buildDoc(src))};
			  }
			  catch(JSONException e){
				throw new RuntimeException(e.getMessage(),e);
			  }
			}

      //@Override
      //public byte[] getStoreValue()
      //{
        //throw new NotImplementedException();
      //}

      @Override
			public long getUID() {
			  try{
				return extractUID(src);
			  }
			  catch(JSONException e){
				throw new RuntimeException(e.getMessage(),e);
			  }
			}

			@Override
			public boolean isDeleted() {
				return extractDeleteFlag(src);
			}

			@Override
			public boolean isSkip() {
				return extractSkipFlag(src);
			}
			
		};
	}

	public abstract long extractUID(JSONObject obj) throws JSONException;
	public abstract Document buildDoc(JSONObject obj) throws JSONException;
	
	public boolean extractSkipFlag(JSONObject obj){
		return false;
	}
	
	public boolean extractDeleteFlag(JSONObject obj){
		return false;
	}
}
