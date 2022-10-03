package com.company.searchAlgorithms.uninformedSearch;

import one.util.streamex.StreamEx;

import java.util.*;

public class DfsWithLimit {
  public static boolean outputSteps = false;
  public static List<String> search(
    String start, String finish, Map<String, List<String>> configuration, int maxDepth
  ) {
    outputStep();
    outputStep("Start DFS with depth limit (" + maxDepth + ") ...");

    var arrivedAtCurrentWay = new HashSet<String>();
    arrivedAtCurrentWay.add(start);
    var arriveBestPosition = new HashMap<String, Integer>();
    arriveBestPosition.put(start, 0);

    var way = new LinkedList<String>();
    way.add(start);

    while (!way.isEmpty() && !way.getLast().equals(finish)) {
      outputStep();
      outputStep("Now in '" + way.getLast() + "'");

      if (way.size() - 1 == maxDepth) {
        outputStep("\tTo deep - go back");
        arrivedAtCurrentWay.remove(way.getLast());
        way.removeLast();
        outputStep("\tNew way -> " + way);
        outputStep("\t\tsize -> " + way.size());
        continue;
      }

      String from = way.getLast();
      List<String> neighbours = configuration.getOrDefault(from, Collections.emptyList());
      Optional<String> nextNeighbour = StreamEx.of(neighbours).findAny(neighbour ->
        !(
          arrivedAtCurrentWay.contains(neighbour)
            || arriveBestPosition.containsKey(neighbour) && arriveBestPosition.get(neighbour) <= way.size()
        )
      );

      if (nextNeighbour.isPresent()) {
        String to = nextNeighbour.get();
        arrivedAtCurrentWay.add(to);
        arriveBestPosition.put(to, way.size());
        way.add(to);
      } else {
        arrivedAtCurrentWay.remove(way.getLast());
        way.removeLast();
        outputStep("\tCannot find next city - Go back");
      }

      outputStep("\tNew way -> " + way);
      outputStep("\t\tsize -> " + way.size());
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
