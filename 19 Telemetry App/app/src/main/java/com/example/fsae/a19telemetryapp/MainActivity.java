package com.example.fsae.a19telemetryapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final double MAX_BRAKE_PRESSURE = 40.0;

//    private static final String REGISTER_USER_SUCCESSFUL = "Registered";
//    private static final String AUDIO_TRANSMIT_ACCEPTED = "Granted";
//    private static final String AUDIO_TRANSMIT_REJECTED = "Channel in use";
//    private static final String AUDIO_RECEIVE_ACTIVE= "Transmit";
//    private static final String AUDIO_TERMINATE = "Terminate";

    private UdpListener udpListener;
    private DataStorage dataStorage;
    private TcpClient tcpClient;
    private TcpClient.OnMessageReceived tcpDelegate;
//    private GraphUpdate mGraph;

    private Handler dataUpdateHandler;

    protected String ARG_SECTION_NUMBER;

    private TextView Speed;
    private TextView ThrottlePos;
    private TextView ThrottlePed;
    private TextView BPresF;
    private TextView BPresR;
    private TextView BPresPercent;
    private TextView BTempF;
    private TextView BTempR;
    private TextView IAT;
    private TextView Gear;
    private TextView OilTemp;
    private TextView EngTemp;
    private TextView FuelPres;
    private TextView OilPres;
    private TextView Bias;
    private TextView RPM;
    private TextView Lambda;
    private TextView FuelAim;
    private TextView BattVolt;
    private TextView Auto;
    private TextView Clutch;
    private TextView Launch;
    private TextView Radio;
    private ProgressBar TpBar;
    private ProgressBar BPresBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Speed = (TextView) findViewById(R.id.speed);
        Gear = (TextView) findViewById(R.id.gear);
        RPM = (TextView) findViewById(R.id.rpm);
        ThrottlePos = (TextView) findViewById(R.id.tp);
        TpBar = (ProgressBar) findViewById(R.id.tpProgressBar);
        BPresF = (TextView) findViewById(R.id.brake);
        BPresBar = (ProgressBar) findViewById(R.id.brakeProgressBar);


//        initializeUdpListener();
        dataUpdateHandler = new Handler();
        dataUpdateHandler.postDelayed(updateUI, 250);
//        connectToServer();
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
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_general) {
            // Handle the camera action
            setContentView(R.layout.activity_general);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else if (id == R.id.nav_engine) {
            setContentView(R.layout.activity_engine);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else if (id == R.id.nav_suspension) {
            setContentView(R.layout.activity_suspension);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else if (id == R.id.nav_misc) {
            setContentView(R.layout.activity_misc);        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else if (id == R.id.nav_graph) {
            setContentView(R.layout.activity_graph);        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else if (id == R.id.nav_comms) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void initializeUdpListener() {
//        dataStorage = new DataStorage();
//        udpListener = new UdpListener(dataStorage);
//        udpListener.setPriority(Thread.NORM_PRIORITY);
//        udpListener.start();
//    }

//    private void connectToServer () {
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        if(audioThread != null && audioThread.isAlive()) {
////            audioThread.interrupt();
////        }
////        if(audioReceiveThread != null) {
////            audioReceiveThread.interrupt();
////        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(tcpClient != null) {
//            tcpClient.stopClient();
//        }
//    }

    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
//            int currentTp = dataStorage.getThrottlePos();
//            int currentBrake = dataStorage.getBPresF();
//
//            Speed.setText(Integer.toString(dataStorage.getSpeed()));
//            Gear.setText(Integer.toString(dataStorage.getGear()));
//            RPM.setText(Double.toString(dataStorage.getRPM()));
//            ThrottlePos.setText(Integer.toString(currentTp));
//            BPresPercent.setText(Integer.toString((int) (currentBrake / MAX_BRAKE_PRESSURE * 100)));
//            IAT.setText(Integer.toString(dataStorage.getIAT()));
//            OilTemp.setText(Integer.toString(dataStorage.getOilTemp()));
//            EngTemp.setText(Integer.toString(dataStorage.getEngTemp()));
//            BattVolt.setText(Double.toString(dataStorage.getBattVolt()));
//            OilPres.setText(Integer.toString(dataStorage.getOilPres()));
//            FuelPres.setText(Integer.toString(dataStorage.getFuelPres()));
//            BPresF.setText(Double.toString(currentBrake / 10.0));
//            BTempF.setText(Integer.toString(dataStorage.getBTempF()));
//            Bias.setText(Integer.toString(dataStorage.getBias()));
//            TpBar.setProgress(currentTp);
//            BPresBar.setProgress((int) (currentBrake / MAX_BRAKE_PRESSURE * 100));
//            dataUpdateHandler.post(this);
        }
    };
}
