package com.company.structures;

import java.util.Objects;

public class Pair<T1, T2> {
  public T1 first;
  public T2 second;

  public T1 getFirst() {
    return first;
  }

  public void setFirst(T1 first) {
    this.first = first;
  }

  public T2 getSecond() {
    return second;
  }

  public void setSecond(T2 second) {
    this.second = second;
  }

  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  public static <T1, T2> Pair<T1, T2> of(T1 first, T2 second) {
    return new Pair<>(first, second);
  }

  @Override
  public String toString() {
    return "(" + first + ", " + second + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Pair<?, ?> pair = (Pair<?, ?>) o;

    if (!Objects.equals(first, pair.first))
      return false;
    return Objects.equals(second, pair.second);
  }

  @Override
  public int hashCode() {
    int result = first != null ? first.hashCode() : 0;
    result = 31 * result + (second != null ? second.hashCode() : 0);
    return result;
  }
}
