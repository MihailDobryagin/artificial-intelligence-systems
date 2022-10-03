package com.company.searchAlgorithms.uninformedSearch;

import one.util.streamex.StreamEx;

import java.util.*;
import java.util.function.Predicate;

public class BidirectionalSearch {
  public static boolean outputSteps = false;

  public static List<String> search(String start, String finish, Map<String, List<String>> configuration) {
    outputStep();
    outputStep("Starting bidirectional search (dfs)...");

    var arrivedBySide = new ArrayList<Set<String>>(2);
    arrivedBySide.add(0, new HashSet<>());
    arrivedBySide.add(1, new HashSet<>());
    arrivedBySide.get(0).add(start);
    arrivedBySide.get(1).add(finish);

    var waysBySide = new ArrayList<LinkedList<String>>(2);
    waysBySide.add(new LinkedList<>());
    waysBySide.add(new LinkedList<>());
    waysBySide.get(0).add(start);
    waysBySide.get(1).add(finish);

    int currentSide = 0;

    while (
      StreamEx.of(waysBySide).allMatch(Predicate.not(List::isEmpty))
        && StreamEx.of(arrivedBySide.get(0)).noneMatch(arrivedBySide.get(1)::contains)
    ) {
      LinkedList<String> way = waysBySide.get(currentSide);
      String from = way.getLast();
      outputStep();
      outputStep("On " + (currentSide == 0 ? "left" : "right") + " side in '" + from + "'");
      List<String> neighbours = configuration.getOrDefault(from, Collections.emptyList());

      Set<String> arrived = arrivedBySide.get(currentSide);
      Optional<String> nextNeighbour = StreamEx.of(neighbours).findAny(Predicate.not(arrived::contains));

      if (nextNeighbour.isPresent()) {
        way.add(nextNeighbour.get());
        arrived.add(nextNeighbour.get());
      } else {
        way.removeLast();
        outputStep("\tCannot find next city - Go back");
      }

      outputStep("\tNew ways:");
      outputStep("\t\t" + waysBySide.get(0));
      outputStep("\t\t\tsize -> " + waysBySide.get(0).size());
      outputStep("\t\t" + waysBySide.get(1));
      outputStep("\t\t\tsize -> " + waysBySide.get(1).size());

      currentSide ^= 1;
    }

    if(StreamEx.of(waysBySide).anyMatch(List::isEmpty)) {
      return Collections.emptyList();
    }

    String intersectionCity =  StreamEx.of(arrivedBySide.get(0)).findAny(arrivedBySide.get(1)::contains).get();

    LinkedList<String> wayFromStart = waysBySide.get(0);
    LinkedList<String> wayFromFinish = waysBySide.get(1);

    var way = new LinkedList<String>();
    for(String city : wayFromStart) {
      if(city.equals(intersectionCity)) {
        break;
      }
      way.add(city);
    }

    var additionalWay = new LinkedList<>(wayFromFinish);

    Collections.reverse(additionalWay);

    while(!additionalWay.getFirst().equals(intersectionCity)) {
      additionalWay.removeFirst();
    }

    way.addAll(additionalWay);

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
