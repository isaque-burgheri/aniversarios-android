import java.time.*;
import br.com.aniversarios.Age;
class AgeCheck { public static void main(String[] args) {
 String a=Age.between(LocalDate.of(1994,5,20),LocalDate.of(2026,10,28));
 String b=Age.between(LocalDate.of(1957,11,16),LocalDate.of(2026,10,28));
 if(!a.equals("32 anos, 5 meses e 8 dias")||!b.equals("68 anos, 11 meses e 12 dias"))throw new AssertionError();
 if(!Age.between(LocalDate.of(2026,10,28),LocalDate.of(2026,10,28)).equals("0 anos, 0 meses e 0 dias"))throw new AssertionError();
 try {Age.between(LocalDate.of(2027,1,1),LocalDate.of(2026,10,28));throw new AssertionError();}catch(IllegalArgumentException expected){}
 System.out.println(a+"\n"+b);
 }}