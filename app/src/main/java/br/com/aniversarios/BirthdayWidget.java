package br.com.aniversarios;
import android.app.*; import android.appwidget.*; import android.content.*; import android.os.Bundle; import android.widget.*; import java.time.*; import java.time.format.*; import org.json.*;
public class BirthdayWidget extends AppWidgetProvider {
 static final String NEXT="br.com.aniversarios.NEXT";
 static void refresh(Context c) { AppWidgetManager m=AppWidgetManager.getInstance(c); for(int id:m.getAppWidgetIds(new ComponentName(c,BirthdayWidget.class))) render(c,m,id); }
 public void onUpdate(Context c,AppWidgetManager m,int[] ids) { for(int id:ids) render(c,m,id); }
 public void onAppWidgetOptionsChanged(Context c,AppWidgetManager m,int id,Bundle b) { render(c,m,id); }
 public void onReceive(Context c,Intent i) { super.onReceive(c,i); if(NEXT.equals(i.getAction())) {int id=i.getIntExtra("id",-1); android.content.SharedPreferences p=c.getSharedPreferences("pages",0); p.edit().putInt("p"+id,p.getInt("p"+id,0)+1).apply();} refresh(c); }
 public void onDeleted(Context c,int[] ids) { for(int id:ids)c.getSharedPreferences("pages",0).edit().remove("p"+id).apply(); }
 static void render(Context c,AppWidgetManager m,int id) {
  JSONArray a=Store.read(c); int pages=Math.max(1,(a.length()+1)/2); int page=c.getSharedPreferences("pages",0).getInt("p"+id,0)%pages;
  RemoteViews v=new RemoteViews(c.getPackageName(),R.layout.widget); v.removeAllViews(R.id.rows);
  v.setTextViewText(R.id.header,"Aniversários · "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")));
  for(int n=page*2;n<Math.min(a.length(),page*2+2);n++) {JSONObject o=a.optJSONObject(n); if(o==null)continue; RemoteViews r=new RemoteViews(c.getPackageName(),R.layout.row); r.setTextViewText(R.id.name,o.optString("name")); try {LocalDate d=LocalDate.parse(o.getString("date"));r.setTextViewText(R.id.age,Age.between(d,LocalDate.now()));r.setTextViewText(R.id.date,d.format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));}catch(Exception e){r.setTextViewText(R.id.age,"Confira a data no aplicativo");} v.addView(R.id.rows,r); }
  if(a.length()==0)v.setTextViewText(R.id.header,"Cadastre seu primeiro aniversário");
  PendingIntent open=PendingIntent.getActivity(c,0,new Intent(c,MainActivity.class),PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
  v.setOnClickPendingIntent(R.id.manage,open);v.setOnClickPendingIntent(R.id.rows,open);
  Intent next=new Intent(c,BirthdayWidget.class).setAction(NEXT).putExtra("id",id);
  v.setOnClickPendingIntent(R.id.next,PendingIntent.getBroadcast(c,id,next,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT));
  v.setTextViewText(R.id.next,pages>1?(page+1)+"/"+pages+"  ›":"↻ Atualizar"); m.updateAppWidget(id,v);
 }
}
