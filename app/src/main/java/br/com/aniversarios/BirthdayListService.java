package br.com.aniversarios;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

// Compatibilidade da lista rolável com Android 8 a 11.
public class BirthdayListService extends RemoteViewsService {
 @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
  return new BirthdayFactory(getApplicationContext());
 }
 static final class BirthdayFactory implements RemoteViewsFactory {
  private final Context context;
  private JSONArray people = new JSONArray();
  private LocalDate today = LocalDate.now();
  BirthdayFactory(Context context) { this.context = context; }
  @Override public void onCreate() { onDataSetChanged(); }
  @Override public void onDataSetChanged() {
   people = Store.sorted(context);
   today = LocalDate.now();
  }
  @Override public void onDestroy() { people = new JSONArray(); }
  @Override public int getCount() { return people.length(); }
  @Override public RemoteViews getViewAt(int position) {
   JSONObject person = people.optJSONObject(position);
   return BirthdayWidget.row(context, person == null ? new JSONObject() : person, today);
  }
  @Override public RemoteViews getLoadingView() { return null; }
  @Override public int getViewTypeCount() { return 1; }
  @Override public long getItemId(int position) { return position; }
  @Override public boolean hasStableIds() { return false; }
 }
}
