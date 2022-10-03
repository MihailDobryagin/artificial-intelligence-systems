package com.company.searchAlgorithms.informedSearch;

import com.company.structures.Pair;
import one.util.streamex.EntryStream;

import java.util.*;
import java.util.function.Supplier;

public class MinimizationOfSolutionTotalCost {
  public static boolean outputSteps = false;

  public static List<String> search(
    String start, String finish,
    Map<String, HashMap<String, Integer>> distances,
    Map<String, HashMap<String, Integer>> distancesInStraight
  ) {

    outputStep();
    outputStep("Starting search with minimization of solution total cost ...");
    var arrivingCost = new HashMap<String, Integer>();
    arrivingCost.put(start, 0);

    var way = new HashMap<String, String>();

    TreeSet<Pair<Integer, Pair<String, String>>> sortedByTotalCost = new TreeSet<>((o1, o2) ->
      o1.first.equals(o2.first) ? (o1.second.second.compareTo(o2.second.second)) : o1.first.compareTo(o2.first)
    ); // (totalCost, (from, to))
    sortedByTotalCost.add(Pair.of(distancesInStraight.get(start).get(finish), Pair.of(null, start)));


    while (!sortedByTotalCost.isEmpty() && !way.containsKey(finish)) {
      outputStep();
      Pair<Integer, Pair<String, String>> entity = sortedByTotalCost.pollFirst();
      String from = entity.second.second;
      way.put(from, entity.second.first);

      outputStep("=======================\nNow in '" + from + "'");

      if (finish.equals(from)) {
        break;
      }

      int distToFromWithHeuristics = entity.first;

      EntryStream.of(distances.get(from))
        .forEach(nextCityEntity -> {
          String to = nextCityEntity.getKey();
          int distToNextCity = nextCityEntity.getValue();
          int newTargetFuncValue = distToFromWithHeuristics - distancesInStraight.get(from).get(finish)
            + distToNextCity + distancesInStraight.get(to).get(finish);

          outputStep(() -> String.format("\t%15s | g = %5d | h = %5d | f = %5d",
            nextCityEntity.getKey(),
            distToFromWithHeuristics - distancesInStraight.get(from).get(finish) + distToNextCity,
            distancesInStraight.get(nextCityEntity.getKey()).get(finish),
            newTargetFuncValue
          ));

          if (!arrivingCost.containsKey(to)) {
            arrivingCost.put(to, newTargetFuncValue);
            sortedByTotalCost.add(Pair.of(newTargetFuncValue, Pair.of(from, to)));
          } else {
            if (arrivingCost.get(to) > newTargetFuncValue) {
              sortedByTotalCost.remove(Pair.of(arrivingCost.get(to), Pair.of(null, to)));
              sortedByTotalCost.add(Pair.of(newTargetFuncValue, Pair.of(from, to)));
              arrivingCost.put(to, newTargetFuncValue);
            }
          }
        });
      outputStep("New queue configuration:\n\t" + sortedByTotalCost);
    }

    outputStep("Search finished\n=======================\n");
    return buildWay(start, finish, way);
  }

  private static List<String> buildWay(String start, String finish, HashMap<String, String> way) {
    var currentCity = finish;
    var result = new LinkedList<String>();
    result.add(currentCity);

    while (!currentCity.equals(start)) {
      currentCity = way.get(currentCity);
      result.addFirst(currentCity);
    }

    return result;
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
