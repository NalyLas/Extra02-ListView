package cursoandroid.com.extra02;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ExpandableListView elv;
    ExpandableListAdapter ela;
    ArrayList<String> libros;
    HashMap<String, List<String>> contenido;
    private String url_consulta = "http://iesayala.ddns.net/natalia/php.php";
    private JSONArray jSONArray;
    private DevuelveJSON devuelveJSON;
    private Libro libro;
    private ArrayList<Libro> arrayLibros;
    static public SharedPreferences pref;
    ArrayList<HashMap<String, String>> librosList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devuelveJSON = new DevuelveJSON();
        new ListaLibros().execute();
        pref = getSharedPreferences("cursoandroid.com.extra02_preferences", MODE_PRIVATE);
        url_consulta = MainActivity.pref.getString("ip","0");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.preferencias:
                Intent i = new Intent(this,Preferences.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ListaLibros extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {

            try {
                HashMap<String, String> parametrosPost = new HashMap<>();
                parametrosPost.put("ins_sql", "Select * from Libros");
                jSONArray = devuelveJSON.sendRequest(url_consulta,
                        parametrosPost);
                if (jSONArray != null) {
                    return jSONArray;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray json) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                arrayLibros =new ArrayList<Libro>();
                for (int i = 0; i < json.length(); i++) {
                    try {
                        JSONObject jsonObject = json.getJSONObject(i);
                        libro = new Libro();
                        libro.setTitulo(jsonObject.getString("titulo"));
                        libro.setAutor(jsonObject.getString("autor"));
                        libro.setFecha(jsonObject.getString("fecha"));
                        libro.setPortada(jsonObject.getString("portada"));
                        libro.setSinopsis(jsonObject.getString("sinopsis"));
                        arrayLibros.add(libro);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                libros = new ArrayList<String>();
                contenido = new HashMap<String,List<String>>();
                for(int i=0;i< arrayLibros.size(); i++){
                    if(contenido.containsKey(arrayLibros.get(i).getAutor())){
                        contenido.get(arrayLibros.get(i).getAutor()).add(arrayLibros.get(i).getTitulo());
                    }else{
                        libros.add(arrayLibros.get(i).getAutor());
                        contenido.put(arrayLibros.get(i).getTitulo(),new ArrayList<String>());
                        contenido.get(arrayLibros.get(i).getTitulo()).add("Autor: " + arrayLibros.get(i).getAutor());
                        contenido.get(arrayLibros.get(i).getTitulo()).add("Fecha: " + arrayLibros.get(i).getFecha());
                        contenido.get(arrayLibros.get(i).getTitulo()).add("Sinopsis: " + arrayLibros.get(i).getSinopsis());

                    }
                }

                cargarList();

            } else {
                Toast.makeText(MainActivity.this, "JSON Array nulo",
                        Toast.LENGTH_LONG).show();
            }


        }


        }

        public void cargarList() {
            elv = (ExpandableListView) findViewById(R.id.expandableListView);
            libros = new ArrayList<String>(contenido.keySet());
            ela = new Adaptador(MainActivity.this, libros, contenido);
            elv.setAdapter(ela);
        }

    @Override
    protected void onRestart() {
        super.onRestart();
        libros.clear();
        contenido.clear();
        devuelveJSON = new DevuelveJSON();
        new ListaLibros().execute();
        pref = getSharedPreferences("cursoandroid.com.extra02_preferences", MODE_PRIVATE);
        url_consulta = MainActivity.pref.getString("ip","0");

        cargarList();

    }
}


