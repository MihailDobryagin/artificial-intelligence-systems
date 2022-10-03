package com.company.searchAlgorithms.informedSearch;

import one.util.streamex.EntryStream;

import java.util.*;
import java.util.function.Supplier;

public class GreedySearch {
  public static boolean outputSteps = false;

  public static List<String> search(
    String start, String finish,
    Map<String, HashMap<String, Integer>> distances,
    Map<String, HashMap<String, Integer>> distancesInStraight
  ) {
    outputStep();
    outputStep("Starting greedy search ...");

    var arrived = new HashSet<String>();
    arrived.add(start);
    var way = new LinkedList<String>();
    way.add(start);

    while (!way.isEmpty() && !way.getLast().equals(finish)) {
      String from = way.getLast();
      outputStep();
      outputStep("=======================\nNow in '" + from + "'");

      Map<String, Integer> neighbours = distances.getOrDefault(from, new HashMap<>());
      Optional<Map.Entry<String, Integer>> nextNeighbour = EntryStream.of(neighbours)
        .filter(neighbour -> !arrived.contains(neighbour.getKey()))
        .peek(neighbour -> outputStep(() -> String.format("\t'%15s' | %5d",
          neighbour.getKey(),
          distancesInStraight.get(neighbour.getKey()).get(finish)
        )))
        .minBy(neighbour -> distancesInStraight.get(neighbour.getKey()).get(finish));

      if (nextNeighbour.isPresent()) {
        outputStep("===\n\tNext city - '" + nextNeighbour.get().getKey());
        way.add(nextNeighbour.get().getKey());
        arrived.add(nextNeighbour.get().getKey());
      } else {
        outputStep("Go back");
        way.removeLast();
      }
      outputStep("\tNew way -> " + way);
    }

    outputStep("Search finished\n=======================\n");
    return way;
  }

  private static void outputStep(String output) {
    if (outputSteps) {
      System.out.println(output);
    }
  }

  private static void outputStep(Supplier<String> outputSupplier) {
    if (outputSteps) {
      System.out.println(outputSupplier.get());
    }
  }

  private static void outputStep() {
    outputStep("");
  }
}
