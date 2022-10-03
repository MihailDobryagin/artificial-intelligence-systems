package com.company.searchAlgorithms.uninformedSearch;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DfsWithDepthIncreasing {
  public static boolean outputSteps = false;

  public static List<String> search(
    String start, String finish, Map<String, List<String>> configuration
  ) {
    outputStep();
    outputStep("Starting dfs with depth increasing ...");
    outputStep();
    int currentDepth = 1;

    List<String> way = Collections.emptyList();

    while (way.isEmpty() && currentDepth <= configuration.size()) {
      boolean dsfWithLimitOutputMode = DfsWithLimit.outputSteps;
      DfsWithLimit.outputSteps = outputSteps;
      way = DfsWithLimit.search(start, finish, configuration, currentDepth++);
      DfsWithLimit.outputSteps = dsfWithLimitOutputMode;
    }

    return way;
  }

  private static void outputStep(String output) {
    if(outputSteps) {
      System.out.println(output);
    }
  }

  private static void outputStep() {
    outputStep("");
  }
}
