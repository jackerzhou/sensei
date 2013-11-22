package com.senseidb.test.plugin;

import com.browseengine.bobo.facets.AbstractRuntimeFacetHandlerFactory;
import com.browseengine.bobo.facets.FacetHandlerInitializerParam;
import com.browseengine.bobo.facets.RuntimeFacetHandler;

public class MockRuntimeFacetHandlerFactory extends
    AbstractRuntimeFacetHandlerFactory<FacetHandlerInitializerParam, RuntimeFacetHandler<?>> {

  @Override
  public String getName() {
    return "mockHandlerFactory";
  }

  @Override
  public RuntimeFacetHandler<?> get(FacetHandlerInitializerParam params) {

    return null;
  }
}
