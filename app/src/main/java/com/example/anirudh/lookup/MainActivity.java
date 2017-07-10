package com.example.anirudh.lookup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.example.anirudh.lookup.Adapters.HistoryAdapter;
import com.example.anirudh.lookup.Api.SingeltonApi;
import com.example.anirudh.lookup.DataBase.DatabaseHelper;
import com.example.anirudh.lookup.DataBase.HistoryTable;
import com.example.anirudh.lookup.models.HistoryModel;
import com.example.anirudh.lookup.models.LexicalEntry;
import com.example.anirudh.lookup.models.Word;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity" ;
    public static final String Dictionary_BaseUrl = "https://od-api.oxforddictionaries.com/api/v1/" ;
    public static final String Dictionary_App_Id="d9905059" ;
    public static final String Dictionary_App_Key = "e5357b09aea78eef1e1a2c23d30e5547" ;
    public static  String sourceLanguage = "en" ;
    DatabaseHelper databaseHelper ;
    SQLiteDatabase db ;
    ArrayList<HistoryModel> historylist  ;
    RecyclerView rvRecentLookups ;
    HistoryAdapter adapter ;
    boolean isStar = false ;

    TextView tvTodaysWord ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvRecentLookups = (RecyclerView)findViewById(R.id.rvRecentLookups) ;
        tvTodaysWord = (TextView) findViewById(R.id.tvTodaysWord);
        databaseHelper = new DatabaseHelper(this) ;
        db = databaseHelper.getWritableDatabase() ;
        historylist = new ArrayList<>() ;

        rvRecentLookups.setLayoutManager(new LinearLayoutManager(this));
        historylist = HistoryTable.getHistory(db);

        Log.d(TAG, "onCreate: " + historylist.size());
        adapter = new HistoryAdapter(historylist , this) ;

        rvRecentLookups.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnStarClickListener(new HistoryAdapter.onStarClickedListener() {
            @Override
            public void onStarClicked(boolean isStar , int pos) {
                Log.d(TAG, "onStarClicked: " + isStar + pos);
                HistoryModel h = historylist.get(pos) ;
                h.setIsstar(isStar ? 1 : 0);
                historylist.set(pos , h) ;
                HistoryTable.updateStar(db , h.getWord() ,isStar);
                adapter.notifyDataSetChanged();
            }
        });

        Typeface myfont = Typeface.createFromAsset(getAssets() ,"my_cursive_font.ttf" ) ;
        tvTodaysWord.setTypeface(myfont);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem item = menu.findItem(R.id.menuSearch) ;
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String TextGiven) {
                final ProgressDialog pd  ;
                pd = ProgressDialog.show(
                        MainActivity.this ,
                        "Loading" ,"Please wait..",
                        true ,
                        false) ;

                SingeltonApi.build()
                        .getDictionaryApi()
                        .getDefination(TextGiven.toLowerCase(),sourceLanguage, Dictionary_App_Id ,Dictionary_App_Key)
                        .enqueue(new Callback<Word>() {
                    @Override
                    public void onResponse(Call<Word> call, Response<Word> response) {
                        if(pd != null)
                            pd.dismiss();

                        String example = "" ;
                        if(response.body() != null){

                            Intent i = new Intent(MainActivity.this , WordDefinitionAct.class) ;
                            LexicalEntry[] lexes = response.body()
                                    .getResults()[0]
                                    .getLexicalEntries() ;
                            int count = 0 ;
                            for(LexicalEntry entry : lexes){
                                if(count>4){
                                    break ;
                                }
                                i.putExtra("LexicalCategory"+count , entry.getLexicalCategory()) ;
                                i.putExtra("definition"+count , entry.getEntries()[0].getSenses()[0].getDefinitions()[0]) ;
                                if(entry.getEntries()[0].getSenses()[0].getExamples() != null) {
                                    example = entry.getEntries()[0].getSenses()[0].getExamples()[0].getText() ;
                                    i.putExtra("examples" + count, entry.getEntries()[0].getSenses()[0].getExamples()[0].getText());

                                }else
                                {
                                    example = "No example" ;
                                    i.putExtra("examples" + count, "No example");
                                }
                                count++;
                            }
                            if(sourceLanguage.equals("en")) {
                                i.putExtra("audioUrl", response.body()
                                        .getResults()[0]
                                        .getLexicalEntries()[0]
                                        .getPronunciations()[0]
                                        .getAudioFile());
                            }
                            i.putExtra("count" ,count) ;
                            i.putExtra("word" , TextGiven) ;
                            historylist.add(0 ,new HistoryModel(
                                    TextGiven ,
                                    response.body().getResults()[0].getLexicalEntries()[0].getLexicalCategory() ,
                                    response.body().getResults()[0].getLexicalEntries()[0].getEntries()[0].
                                            getSenses()[0].getDefinitions()[0] ,
                                   example , 0
                            ));
                            Log.d(TAG, "onResponse: history list size"+historylist.size());
                            HistoryTable.addHistory(
                                    db ,
                                    TextGiven ,
                                    response.body().getResults()[0].getLexicalEntries()[0].getLexicalCategory() ,
                                    response.body().getResults()[0].getLexicalEntries()[0].getEntries()[0].
                                            getSenses()[0].getDefinitions()[0] ,
                                    example ,
                                    1

                            );
                            adapter.notifyDataSetChanged();

                            startActivity(i);
                        }
                        else{
                            Toast.makeText(
                                    MainActivity.this,
                                    "Word Not Found",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Word> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Failed to Search", Toast.LENGTH_SHORT).show();
                    }
                });



                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Lang_English) {
            item.setChecked(true) ;
            sourceLanguage = "en" ;
            return true;
        }else if(id ==R.id.action_Lang_Spanish){
            item.setChecked(true) ;
            sourceLanguage = "es" ;
            return true ;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            startActivity(new Intent(MainActivity.this , HistoryActivity.class));
        } else if (id == R.id.nav_starred) {

        } else if (id == R.id.nav_gitLink) {
            String url = "https://github.com/Swoosh-ver22/LookUp" ;
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
