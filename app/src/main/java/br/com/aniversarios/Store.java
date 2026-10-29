package br.com.aniversarios;
import android.content.*;
import org.json.*;
final class Store {
 static JSONArray read(Context c) {
  try { return new JSONArray(c.getSharedPreferences("birthdays",0).getString("people","[{\"name\":\"Isaque Burgheri\",\"date\":\"1994-05-20\"},{\"name\":\"Mãe\",\"date\":\"1957-11-16\"}]")); }
  catch(JSONException e) { return new JSONArray(); }
 }
 static void save(Context c, JSONArray a) { c.getSharedPreferences("birthdays",0).edit().putString("people",a.toString()).apply(); BirthdayWidget.refresh(c); }
}
