package br.com.aniversarios;
import android.content.*;
import org.json.*;
import java.util.ArrayList;
import java.util.List;
final class Store {
 static JSONArray read(Context c) {
  try { return new JSONArray(c.getSharedPreferences("birthdays",0).getString("people","[{\"name\":\"Isaque Burgheri\",\"date\":\"1994-05-20\"},{\"name\":\"Mãe\",\"date\":\"1957-11-16\"}]")); }
  catch(JSONException e) { return new JSONArray(); }
 }
 static boolean byBirthday(Context c) {
  return c.getSharedPreferences("birthdays",0).getBoolean("sortByBirthday", false);
 }
 static void toggleOrder(Context c) {
  c.getSharedPreferences("birthdays",0).edit().putBoolean("sortByBirthday", !byBirthday(c)).apply();
  BirthdayWidget.refresh(c);
 }
 static JSONArray sorted(Context c) {
  JSONArray source = read(c);
  List<JSONObject> people = new ArrayList<>();
  for (int n = 0; n < source.length(); n++) {
   JSONObject person = source.optJSONObject(n);
   if (person != null) people.add(person);
  }
  boolean birthday = byBirthday(c);
  people.sort((a, b) -> BirthdayOrder.compare(a.optString("date"), b.optString("date"), birthday));
  JSONArray result = new JSONArray();
  for (JSONObject person : people) result.put(person);
  return result;
 }
 static void save(Context c, JSONArray a) { c.getSharedPreferences("birthdays",0).edit().putString("people",a.toString()).apply(); BirthdayWidget.refresh(c); }
}
