package br.com.aniversarios;

import java.time.LocalDate;
import java.time.MonthDay;

final class BirthdayOrder {
 private BirthdayOrder() {}
 static int compare(String first, String second, boolean byBirthday) {
  LocalDate a = parse(first), b = parse(second);
  if (a == null) return b == null ? 0 : 1;
  if (b == null) return -1;
  return byBirthday ? MonthDay.from(a).compareTo(MonthDay.from(b)) : a.compareTo(b);
 }
 private static LocalDate parse(String value) {
  try { return LocalDate.parse(value); }
  catch (RuntimeException e) { return null; }
 }
}
