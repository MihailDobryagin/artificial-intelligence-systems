package com.company.searchAlgorithms.uninformedSearch;

import java.util.*;

public class Bfs {
  public static boolean outputSteps = false;
  public static List<String> search(String start, String finish, Map<String, List<String>> configuration) {
    var processed = new HashMap<String, String>(); // to, from
    var waitingCities = new HashMap<String, String>(); // to, from

    outputStep("Start BFS...");

    Queue<String> waitingCitiesQueue = new LinkedList<>();
    waitingCitiesQueue.add(start);

    while (!processed.containsKey(finish) && !waitingCitiesQueue.isEmpty() && !waitingCities.containsKey(finish)) {
      String from = waitingCitiesQueue.poll();
      outputStep();
      outputStep("Now in '" + from + "'");

      processed.put(from, waitingCities.get(from));
      waitingCities.remove(from);

      List<String> neighbours = configuration.getOrDefault(from, Collections.emptyList());

      neighbours.forEach(to -> {
        outputStep("\t'" + to + "'");
        if (!waitingCities.containsKey(to) && !processed.containsKey(to)) {
          outputStep("\t\tAdding '" + to + "' into queue");
          waitingCitiesQueue.add(to);
          waitingCities.put(to, from);
        }
      });

      outputStep("\tNew queue value -> " + waitingCitiesQueue);
    }

    if(waitingCities.containsKey(finish)) {
      processed.put(finish, waitingCities.get(finish));
    }

    var result = new LinkedList<String>();

    var curCity = finish;

    while (!start.equals(curCity)) {
      result.addFirst(curCity);
      curCity = processed.get(curCity);
    }

    result.addFirst(start);

    outputStep("\n\nSearch finished\n=======================\n");
    return result;
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
