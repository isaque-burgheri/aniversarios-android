package br.com.aniversarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BirthdayOrderCheck {
 public static void main(String[] args) {
  List<String> dates = Arrays.asList("1994-05-20", "1957-11-16", "1955-06-19", "1991-02-17", "2025-06-09", "1967-10-14");
  check(dates, false, "1955-06-19", "1957-11-16", "1967-10-14", "1991-02-17", "1994-05-20", "2025-06-09");
  check(dates, true, "1991-02-17", "1994-05-20", "2025-06-09", "1955-06-19", "1967-10-14", "1957-11-16");
  check(Arrays.asList("2000-03-01", "2000-02-29", "2020-01-01", "1980-12-31", "invalid"), true,
      "2020-01-01", "2000-02-29", "2000-03-01", "1980-12-31", "invalid");
  if (BirthdayOrder.compare("1950-05-20", "2000-05-20", true) != 0) throw new AssertionError("Year must be ignored");
  if (BirthdayOrder.compare("invalid", "invalid", false) != 0) throw new AssertionError("Invalid dates");
  System.out.println("Ordenação validada: idade, mês/dia, ano ignorado, bissexto e datas inválidas.");
 }
 static void check(List<String> source, boolean birthday, String... expected) {
  List<String> actual = new ArrayList<>(source);
  actual.sort((a, b) -> BirthdayOrder.compare(a, b, birthday));
  if (!actual.equals(Arrays.asList(expected))) throw new AssertionError(actual);
 }
}
