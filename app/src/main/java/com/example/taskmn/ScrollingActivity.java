package com.example.taskmn;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

    private ArrayList<Tarea> tareas = new ArrayList<Tarea>();
    private ListView listView;
    private TareaAdapter adapter;
    private final static String NUEVA_TAREA = "NUEVA_TAREA";
    private final static String VER_TAREA = "VER_TAREA";
    private final static int AGREGAR_TAREA = 0;
    private final static int MODIFICAR_TAREA = 1;
    private final static String TAREA_MODIFICADA = "TAREA_MODIFICADA";
    private final static String TAREA_ANTERIOR = "TAREA_ANTERIOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgregarTareaActivity.class);
                startActivityForResult(intent,AGREGAR_TAREA);
            }
        });

        init_tareas(tareas);
        adapter = new TareaAdapter(this, R.layout.list_item, tareas);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setNestedScrollingEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tarea selected = (Tarea) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), VerTareaActivity.class);
                intent.putExtra(VER_TAREA,selected);
                startActivityForResult(intent, MODIFICAR_TAREA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AGREGAR_TAREA) {
            if (resultCode == RESULT_OK) {
                Tarea nueva = data.getParcelableExtra(NUEVA_TAREA);
                tareas.add(nueva);
                adapter.notifyDataSetChanged();
                return;
            }
        }
        if (requestCode == MODIFICAR_TAREA) {
            if (resultCode == RESULT_OK){
                Tarea modificada = data.getParcelableExtra(TAREA_MODIFICADA);
                Tarea anterior = data.getParcelableExtra(TAREA_ANTERIOR);
                if (modificada.equals(anterior)){
                    return;
                }
                tareas.remove(anterior);
                tareas.add(modificada);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private void init_tareas(ArrayList<Tarea> tareas) {
        for (int i = 0; i <= 10 ; i++) {
            String s = Integer.toString(i);
            Tarea t = new Tarea(s + "/03/2020", "Tarea " + s,
                    "Asignatura " + s, "Descripcion " + s);
            tareas.add(t);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
