package com.company;

import com.company.exceptions.CsvFormatException;
import com.company.searchAlgorithms.informedSearch.GreedySearch;
import com.company.searchAlgorithms.informedSearch.MinimizationOfSolutionTotalCost;
import com.company.searchAlgorithms.uninformedSearch.*;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    var pathToFile = args[0];
    var citiesAsStrings = readCsv(pathToFile);

    Map<String, HashMap<String, Integer>> distances = distancesFromColumns(citiesAsStrings);

    Map<String, List<String>> cityConfiguration = toUninformedForm(distances);

    var start = "Казань";
    var finish = "Таллинн";

    var startTime = System.nanoTime();
    List<String> bfsResult = Bfs.search(start, finish, cityConfiguration);
    var finishTime = System.nanoTime();
    System.out.println("BFS -> \n\t" + bfsResult);
    System.out.println("\tsize -> " + (bfsResult.size() - 1));
    System.out.println("\ttime -> " + (finishTime - startTime));

    System.out.println("\n" + line + "\n");

    startTime = System.nanoTime();
    List<String> dfsResult = Dfs.search(start, finish, cityConfiguration);
    finishTime = System.nanoTime();
    System.out.println("DFS -> \n\t" + dfsResult);
    System.out.println("\tsize -> " + (dfsResult.size() - 1));
    System.out.println("\ttime -> " + (finishTime - startTime));

    System.out.println("\n" + line + "\n");

    startTime = System.nanoTime();
    List<String> dfsWithLimitResult = DfsWithLimit.search(start, finish, cityConfiguration, 6);
    finishTime = System.nanoTime();
    System.out.println("DFS with limit for depth -> \n\t" + dfsWithLimitResult);
    System.out.println("\tsize -> " + (dfsWithLimitResult.size() - 1));
    System.out.println("\ttime -> " + (finishTime - startTime));

    System.out.println("\n" + line + "\n");

    startTime = System.nanoTime();
    List<String> dfsWithDepthIncreasingResult = DfsWithDepthIncreasing.search(start, finish, cityConfiguration);
    finishTime = System.nanoTime();
    System.out.println("DFS with depth increasing -> \n\t" + dfsWithDepthIncreasingResult);
    System.out.println("\tsize -> " + (dfsWithDepthIncreasingResult.size() - 1));
    System.out.println("\ttime -> " + (finishTime - startTime));

    System.out.println("\n" + line + "\n");

    startTime = System.nanoTime();
    List<String> bidirectionalSearchResult = BidirectionalSearch.search(start, finish, cityConfiguration);
    finishTime = System.nanoTime();
    System.out.println("Bidirectional search -> \n\t" + bidirectionalSearchResult);
    System.out.println("\tsize -> " + (bidirectionalSearchResult.size() - 1));
    System.out.println("\ttime -> " + (finishTime - startTime));


    HashMap<String, HashMap<String, Integer>> distancesInStraight =
      distancesInStraightFromColumns(readCsv(args[1]));

    System.out.println("\n" + line + "\n");

    startTime = System.nanoTime();
    List<String> greedySearchResult = GreedySearch.search(start, finish, distances, distancesInStraight);
    finishTime = System.nanoTime();
    System.out.println("Greedy search -> \n\t" + greedySearchResult);
    System.out.println("\tsize -> " + (greedySearchResult.size() - 1));
    System.out.println("\ttime -> " + (finishTime - startTime));
    System.out.println("\tcost -> " + countCost(greedySearchResult, distances));

    System.out.println("\n" + line + "\n");

    startTime = System.nanoTime();
    List<String> minimizationOfSolutionTotalCostResult =
      MinimizationOfSolutionTotalCost.search(start, finish, distances, distancesInStraight);
    finishTime = System.nanoTime();
    System.out.println("MoTSC -> \n\t" + minimizationOfSolutionTotalCostResult);
    System.out.println("\tsize -> " + (minimizationOfSolutionTotalCostResult.size() - 1));
    System.out.println("\ttime -> " + (finishTime - startTime));
    System.out.println("\tcost -> " + countCost(minimizationOfSolutionTotalCostResult, distances));

  }

  private static int countCost(List<String> way, Map<String, HashMap<String, Integer>> distances) {
    var iterator = way.iterator();
    String previousCity = iterator.next();
    int result = 0;

    while (iterator.hasNext()) {
      String nextCity = iterator.next();
      result += distances.get(previousCity).get(nextCity);
      previousCity = nextCity;
    }

    return result;
  }

  private static HashMap<String, HashMap<String, Integer>> distancesFromColumns(List<List<String>> columns) {
    var result = new HashMap<String, HashMap<String, Integer>>();

    columns.forEach(row -> {
      if (row.size() != 3) {
        throw new CsvFormatException("Number of columns is not equal to 3");
      }

      String from = row.get(0), to = row.get(1);
      Integer dist = Integer.valueOf(row.get(2));

      HashMap<String, Integer> targets = result.getOrDefault(from, new HashMap<>());
      targets.put(to, dist);
      result.put(from, targets);

      targets = result.getOrDefault(to, new HashMap<>());
      targets.put(from, dist);
      result.put(to, targets);
    });

    return result;
  }

  private static List<List<String>> readCsv(String path) throws FileNotFoundException {
    return new BufferedReader(new FileReader(path))
      .lines()
      .map(line -> Arrays.asList(line.split(";")))
      .collect(Collectors.toList());
  }

  private static HashMap<String, HashMap<String, Integer>> distancesInStraightFromColumns(
    List<List<String>> columns
  ) {
    var result = new HashMap<String, HashMap<String, Integer>>();

    var cityNames = new ArrayList<>(columns.get(0).subList(1, columns.get(0).size()));

    if (!StreamEx.of(columns.subList(1, columns.size())).map(row -> row.get(0)).toList().equals(cityNames)) {
      throw new IllegalArgumentException("City names in columns is not equals to first column names");
    }

    List<List<String>> rows = columns.subList(1, columns.size());

    for (int cityIndex = 0; cityIndex < rows.size(); cityIndex++) {
      var row = rows.get(cityIndex).subList(1, rows.get(0).size());
      var from = cityNames.get(cityIndex);
      var newCurrentDistances = new HashMap<String, Integer>();
      newCurrentDistances.put(from, 0);

      for (int cityToIndex = cityIndex + 1; cityToIndex < rows.size(); cityToIndex++) {
        newCurrentDistances.put(cityNames.get(cityToIndex), Integer.valueOf(row.get(cityToIndex)));
      }

      var currentDistances = result.getOrDefault(from, new HashMap<>());
      currentDistances.putAll(newCurrentDistances);
      result.put(from, currentDistances);

      EntryStream.of(newCurrentDistances)
        .forKeyValue((to, dist) -> {
          if (result.containsKey(to)) {
            result.get(to).put(from, dist);
          } else {
            var distancesForNextCity = new HashMap<String, Integer>();
            distancesForNextCity.put(from, dist);
            result.put(to, distancesForNextCity);
          }
        });
    }

    return result;
  }

  private static Map<String, List<String>> toUninformedForm(Map<String, HashMap<String, Integer>> distances) {
    return EntryStream.of(distances)
      .mapValues(targets -> EntryStream.of(targets).keys().toList())
      .toMap();
  }

  private static final String line = "=".repeat(150);
}
