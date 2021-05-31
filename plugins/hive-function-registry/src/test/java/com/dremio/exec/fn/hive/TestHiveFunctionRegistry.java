package com.dremio.exec.fn.hive;

import com.dremio.exec.expr.fn.HiveFunctionRegistry;
import com.dremio.test.DremioTest;
import org.junit.Test;

public class TestHiveFunctionRegistry extends DremioTest {
  @Test
  public void printHiveRegistryFunctions() {
    HiveFunctionRegistry registry = new HiveFunctionRegistry(DEFAULT_SABOT_CONFIG);
    for (String fn_name : registry.getMethodsUDF().keySet()){
      System.out.println(fn_name);
    }
    for (String fn_name : registry.getMethodsGenericUDF().keySet()) {
      System.out.println(fn_name);
    }
  }
}
