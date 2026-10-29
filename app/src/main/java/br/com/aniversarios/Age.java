package br.com.aniversarios;
import java.time.*;
public final class Age {
 public static String between(LocalDate birth, LocalDate today) {
  if (birth.isAfter(today)) throw new IllegalArgumentException("A data não pode estar no futuro.");
  Period p=Period.between(birth,today);
  return p.getYears()+ (p.getYears()==1?" ano, ":" anos, ")+p.getMonths()+(p.getMonths()==1?" mês e ":" meses e ")+p.getDays()+(p.getDays()==1?" dia":" dias");
 }
}
