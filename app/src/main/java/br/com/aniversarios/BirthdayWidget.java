package br.com.aniversarios;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

public class BirthdayWidget extends AppWidgetProvider {
 static final String TOGGLE_ORDER = "br.com.aniversarios.TOGGLE_ORDER";
 static final String REFRESH = "br.com.aniversarios.REFRESH";
 static void refresh(Context c) {
  AppWidgetManager m = AppWidgetManager.getInstance(c);
  for (int id : m.getAppWidgetIds(new ComponentName(c, BirthdayWidget.class))) render(c, m, id);
 }
 @Override public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
  for (int id : ids) render(c, m, id);
 }
 @Override public void onAppWidgetOptionsChanged(Context c, AppWidgetManager m, int id, Bundle b) {
  render(c, m, id);
 }
 @Override public void onReceive(Context c, Intent i) {
  super.onReceive(c, i);
  String action = i.getAction();
  if (TOGGLE_ORDER.equals(action)) {
   Store.toggleOrder(c);
   return;
  }
  if (REFRESH.equals(action) || Intent.ACTION_DATE_CHANGED.equals(action)
      || Intent.ACTION_TIME_CHANGED.equals(action) || Intent.ACTION_TIMEZONE_CHANGED.equals(action)
      || Intent.ACTION_MY_PACKAGE_REPLACED.equals(action)) refresh(c);
 }
 static RemoteViews row(Context c, JSONObject person, LocalDate today) {
  RemoteViews r = new RemoteViews(c.getPackageName(), R.layout.row);
  r.setTextViewText(R.id.name, person.optString("name"));
  try {
   LocalDate birth = LocalDate.parse(person.getString("date"));
   r.setTextViewText(R.id.age, Age.between(birth, today));
   r.setTextViewText(R.id.date, birth.format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
  } catch (Exception e) {
   r.setTextViewText(R.id.age, "Confira a data no aplicativo");
   r.setTextViewText(R.id.date, "");
  }
  r.setOnClickFillInIntent(R.id.person, new Intent());
  return r;
 }
 static void render(Context c, AppWidgetManager m, int id) {
  RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget);
  LocalDate today = LocalDate.now();
  v.setTextViewText(R.id.header, "Aniversários · " + today.format(DateTimeFormatter.ofPattern("dd/MM")));
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
   JSONArray people = Store.sorted(c);
   RemoteViews.RemoteCollectionItems.Builder items = new RemoteViews.RemoteCollectionItems.Builder()
       .setHasStableIds(false).setViewTypeCount(1);
   for (int n = 0; n < people.length(); n++) {
    JSONObject person = people.optJSONObject(n);
    if (person != null) items.addItem(n, row(c, person, today));
   }
   v.setRemoteAdapter(R.id.rows, items.build());
  } else {
   Intent adapter = new Intent(c, BirthdayListService.class);
   adapter.setData(Uri.parse("birthdays://widget/" + id));
   v.setRemoteAdapter(R.id.rows, adapter);
  }
  v.setEmptyView(R.id.rows, R.id.empty);
  Intent activity = new Intent(c, MainActivity.class);
  PendingIntent open = PendingIntent.getActivity(c, 0, activity,
      PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
  v.setOnClickPendingIntent(R.id.manage, open);
  v.setOnClickPendingIntent(R.id.header, open);
  int mutable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0;
  v.setPendingIntentTemplate(R.id.rows, PendingIntent.getActivity(c, 1, activity,
      mutable | PendingIntent.FLAG_UPDATE_CURRENT));
  Intent refresh = new Intent(c, BirthdayWidget.class).setAction(REFRESH);
  v.setOnClickPendingIntent(R.id.refresh, PendingIntent.getBroadcast(c, 0, refresh,
      PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
  v.setTextViewText(R.id.order, Store.byBirthday(c) ? "↕ Data" : "↕ Idade");
  v.setContentDescription(R.id.order, Store.byBirthday(c)
      ? "Ordem por mês e dia. Toque para mostrar mais velhos primeiro."
      : "Mais velhos primeiro. Toque para ordenar por mês e dia.");
  Intent order = new Intent(c, BirthdayWidget.class).setAction(TOGGLE_ORDER);
  v.setOnClickPendingIntent(R.id.order, PendingIntent.getBroadcast(c, 2, order,
      PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
  m.updateAppWidget(id, v);
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) m.notifyAppWidgetViewDataChanged(id, R.id.rows);
 }
}
