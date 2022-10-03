package com.company.searchAlgorithms.uninformedSearch;

import one.util.streamex.StreamEx;

import java.util.*;
import java.util.function.Predicate;

public class Dfs {
  public static boolean outputSteps = false;

  public static List<String> search(String start, String finish, Map<String, List<String>> configuration) {
    outputStep();
    outputStep("Start DFS...");
    var arrived = new HashSet<String>();
    arrived.add(start);
    var way = new LinkedList<String>();
    way.add(start);

    while (!way.isEmpty() && !way.getLast().equals(finish)) {
      String from = way.getLast();
      outputStep("Now in '" + from + "'");
      List<String> neighbours = configuration.getOrDefault(from, Collections.emptyList());
      Optional<String> nextNeighbour = StreamEx.of(neighbours).findAny(Predicate.not(arrived::contains));

      if (nextNeighbour.isPresent()) {
        way.add(nextNeighbour.get());
        arrived.add(nextNeighbour.get());
      } else {
        way.removeLast();
        outputStep("\tGo back");
      }
      outputStep("\tNew way -> " + way);
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
