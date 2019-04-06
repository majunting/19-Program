package com.example.fsae.a19telemetryapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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

import com.jjoe64.graphview.GraphView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final double MAX_BRAKE_PRESSURE = 40.0;

    private static final String REGISTER_USER_SUCCESSFUL = "Registered";
    private static final String AUDIO_TRANSMIT_ACCEPTED = "Granted";
    private static final String AUDIO_TRANSMIT_REJECTED = "Channel in use";
    private static final String AUDIO_RECEIVE_ACTIVE= "Transmit";
    private static final String AUDIO_TERMINATE = "Terminate";

    private UdpListener udpListener;
    private DataStorage dataStorage;
    private TcpClient tcpClient;
    private TcpClient.OnMessageReceived tcpDelegate;
    private GraphUpdate mGraph;

    private Handler dataUpdateHandler;

    protected String ARG_SECTION_NUMBER;

    private static boolean registered = false;

    private TextView Speed;
    private TextView TPPercent;
    private TextView ThrottlePos;
    private TextView ThrottlePed;
    private TextView BPresF;
    private TextView BPresR;
    private TextView BrakePercent;
    private TextView BPresPercent;
    private TextView BTempF;
    private TextView BTempR;
    private TextView AirTemp;
    private TextView Gear;
    private TextView OilTemp;
    private TextView EngTemp;
    private TextView FuelPres;
    private TextView OilPres;
    private TextView Bias;
    private TextView RPM;
    private TextView Lambda;
    private TextView FuelAim;
    private TextView LambdaDiff;
    private TextView BattVolt;
    private TextView DamperFL;
    private TextView DamperFR;
    private TextView DamperRL;
    private TextView DamperRR;
    private TextView LatG;
    private TextView LongG;
    private TextView Auto;
    private TextView Clutch;
    private TextView Launch;
    private TextView Radio;
    private TextView TPMode;
    private ProgressBar TpBar;
    private ProgressBar BPresBar;

    private GraphView graph1;
    private GraphView graph2;
    private GraphView graph3;
    private GraphView graph4;

    private int tab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
        TPPercent = (TextView) findViewById(R.id.tp);
        TpBar = (ProgressBar) findViewById(R.id.tpProgressBar);
//        BrakePercent = (TextView) findViewById(R.id.brakePercent);
        BPresBar = (ProgressBar) findViewById(R.id.brakeProgressBar);
        AirTemp = (TextView) findViewById(R.id.airTemp);
        EngTemp = (TextView) findViewById(R.id.EngTemp);
        OilTemp = (TextView) findViewById(R.id.OilTemp);
        ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
        ThrottlePos = (TextView) findViewById(R.id.throttlePos);
        OilPres = (TextView) findViewById(R.id.oilPres);
        FuelPres = (TextView) findViewById(R.id.fuelPres);
        Lambda = (TextView) findViewById(R.id.lambda);
        TPMode = (TextView) findViewById(R.id.TpMode);
        BattVolt = (TextView) findViewById(R.id.battVolt);
        Bias = (TextView) findViewById(R.id.bias);
        BPresPercent = (TextView) findViewById(R.id.brakePercent);
        DamperFL = (TextView) findViewById(R.id.damperFL);
        DamperFR = (TextView) findViewById(R.id.damperFR);
        DamperRL = (TextView) findViewById(R.id.damperRL);
        DamperRR = (TextView) findViewById(R.id.damperRR);
        LatG = (TextView) findViewById(R.id.LatG);
        LongG = (TextView) findViewById(R.id.LongG);
        LambdaDiff = (TextView) findViewById(R.id.lambda);
        ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
        BPresF = (TextView) findViewById(R.id.BrakePres);
        BTempF = (TextView) findViewById(R.id.BrakeTemp);

        graph1 = (GraphView) findViewById(R.id.Graph1);
        graph2 = (GraphView) findViewById(R.id.Graph2);
        graph3 = (GraphView) findViewById(R.id.Graph3);
        graph4 = (GraphView) findViewById(R.id.Graph4);

        initializeUdpListener();
        dataUpdateHandler = new Handler();
        dataUpdateHandler.postDelayed(updateUI, 250);
        connectToServer();
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
            tab = 0;
            // Handle the camera action
            setContentView(R.layout.activity_general);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

//            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            });

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
            TPPercent = (TextView) findViewById(R.id.tp);
            TpBar = (ProgressBar) findViewById(R.id.tpProgressBar);
//            BrakePercent = (TextView) findViewById(R.id.brakePercent);
            BPresBar = (ProgressBar) findViewById(R.id.brakeProgressBar);
            AirTemp = (TextView) findViewById(R.id.airTemp);
            EngTemp = (TextView) findViewById(R.id.EngTemp);
            OilTemp = (TextView) findViewById(R.id.OilTemp);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            ThrottlePos = (TextView) findViewById(R.id.throttlePos);
            OilPres = (TextView) findViewById(R.id.oilPres);
            FuelPres = (TextView) findViewById(R.id.fuelPres);
            Lambda = (TextView) findViewById(R.id.lambda);
            TPMode = (TextView) findViewById(R.id.TpMode);
            BattVolt = (TextView) findViewById(R.id.battVolt);
            Bias = (TextView) findViewById(R.id.bias);
            BPresPercent = (TextView) findViewById(R.id.brakePercent);
            DamperFL = (TextView) findViewById(R.id.damperFL);
            DamperFR = (TextView) findViewById(R.id.damperFR);
            DamperRL = (TextView) findViewById(R.id.damperRL);
            DamperRR = (TextView) findViewById(R.id.damperRR);
            LatG = (TextView) findViewById(R.id.LatG);
            LongG = (TextView) findViewById(R.id.LongG);
            LambdaDiff = (TextView) findViewById(R.id.lambda);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            BPresF = (TextView) findViewById(R.id.BrakePres);
            BTempF = (TextView) findViewById(R.id.BrakeTemp);
        } else if (id == R.id.nav_engine) {
            tab = 1;
            setContentView(R.layout.activity_engine);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

//            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            });

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
            TPPercent = (TextView) findViewById(R.id.tp);
            TpBar = (ProgressBar) findViewById(R.id.tpProgressBar);
//            BrakePercent = (TextView) findViewById(R.id.brakePercent);
            BPresBar = (ProgressBar) findViewById(R.id.brakeProgressBar);
            AirTemp = (TextView) findViewById(R.id.airTemp);
            EngTemp = (TextView) findViewById(R.id.EngTemp);
            OilTemp = (TextView) findViewById(R.id.OilTemp);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            ThrottlePos = (TextView) findViewById(R.id.throttlePos);
            OilPres = (TextView) findViewById(R.id.oilPres);
            FuelPres = (TextView) findViewById(R.id.fuelPres);
            Lambda = (TextView) findViewById(R.id.lambda);
            TPMode = (TextView) findViewById(R.id.TpMode);
            BattVolt = (TextView) findViewById(R.id.battVolt);
            Bias = (TextView) findViewById(R.id.bias);
            BPresPercent = (TextView) findViewById(R.id.brakePercent);
            DamperFL = (TextView) findViewById(R.id.damperFL);
            DamperFR = (TextView) findViewById(R.id.damperFR);
            DamperRL = (TextView) findViewById(R.id.damperRL);
            DamperRR = (TextView) findViewById(R.id.damperRR);
            LatG = (TextView) findViewById(R.id.LatG);
            LongG = (TextView) findViewById(R.id.LongG);
            LambdaDiff = (TextView) findViewById(R.id.lambda);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            BPresF = (TextView) findViewById(R.id.BrakePres);
            BTempF = (TextView) findViewById(R.id.BrakeTemp);
        } else if (id == R.id.nav_suspension) {
            tab = 2;
            setContentView(R.layout.activity_suspension);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

//            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            });

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
            TPPercent = (TextView) findViewById(R.id.tp);
            TpBar = (ProgressBar) findViewById(R.id.tpProgressBar);
//            BrakePercent = (TextView) findViewById(R.id.brakePercent);
            BPresBar = (ProgressBar) findViewById(R.id.brakeProgressBar);
            AirTemp = (TextView) findViewById(R.id.airTemp);
            EngTemp = (TextView) findViewById(R.id.EngTemp);
            OilTemp = (TextView) findViewById(R.id.OilTemp);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            ThrottlePos = (TextView) findViewById(R.id.throttlePos);
            OilPres = (TextView) findViewById(R.id.oilPres);
            FuelPres = (TextView) findViewById(R.id.fuelPres);
            Lambda = (TextView) findViewById(R.id.lambda);
            TPMode = (TextView) findViewById(R.id.TpMode);
            BattVolt = (TextView) findViewById(R.id.battVolt);
            Bias = (TextView) findViewById(R.id.bias);
            BPresPercent = (TextView) findViewById(R.id.brakePercent);
            DamperFL = (TextView) findViewById(R.id.damperFL);
            DamperFR = (TextView) findViewById(R.id.damperFR);
            DamperRL = (TextView) findViewById(R.id.damperRL);
            DamperRR = (TextView) findViewById(R.id.damperRR);
            LatG = (TextView) findViewById(R.id.LatG);
            LongG = (TextView) findViewById(R.id.LongG);
            LambdaDiff = (TextView) findViewById(R.id.lambda);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            BPresF = (TextView) findViewById(R.id.BrakePres);
            BTempF = (TextView) findViewById(R.id.BrakeTemp);
        } else if (id == R.id.nav_misc) {
            tab = 3;
            setContentView(R.layout.activity_misc);        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

//            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            });

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
            TPPercent = (TextView) findViewById(R.id.tp);
            TpBar = (ProgressBar) findViewById(R.id.tpProgressBar);
//            BrakePercent = (TextView) findViewById(R.id.brakePercent);
            BPresBar = (ProgressBar) findViewById(R.id.brakeProgressBar);
            AirTemp = (TextView) findViewById(R.id.airTemp);
            EngTemp = (TextView) findViewById(R.id.EngTemp);
            OilTemp = (TextView) findViewById(R.id.OilTemp);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            ThrottlePos = (TextView) findViewById(R.id.throttlePos);
            OilPres = (TextView) findViewById(R.id.oilPres);
            FuelPres = (TextView) findViewById(R.id.fuelPres);
            Lambda = (TextView) findViewById(R.id.lambda);
            TPMode = (TextView) findViewById(R.id.TpMode);
            BattVolt = (TextView) findViewById(R.id.battVolt);
            Bias = (TextView) findViewById(R.id.bias);
            BPresPercent = (TextView) findViewById(R.id.brakePercent);
            DamperFL = (TextView) findViewById(R.id.damperFL);
            DamperFR = (TextView) findViewById(R.id.damperFR);
            DamperRL = (TextView) findViewById(R.id.damperRL);
            DamperRR = (TextView) findViewById(R.id.damperRR);
            LatG = (TextView) findViewById(R.id.LatG);
            LongG = (TextView) findViewById(R.id.LongG);
            LambdaDiff = (TextView) findViewById(R.id.lambda);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            BPresF = (TextView) findViewById(R.id.BrakePres);
            BTempF = (TextView) findViewById(R.id.BrakeTemp);
        } else if (id == R.id.nav_graph) {
            tab = 4;
            setContentView(R.layout.activity_graph);        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

//            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            });

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
            TPPercent = (TextView) findViewById(R.id.tp);
            TpBar = (ProgressBar) findViewById(R.id.tpProgressBar);
            BrakePercent = (TextView) findViewById(R.id.brakePercent);
            BPresBar = (ProgressBar) findViewById(R.id.brakeProgressBar);
            AirTemp = (TextView) findViewById(R.id.airTemp);
            EngTemp = (TextView) findViewById(R.id.EngTemp);
            OilTemp = (TextView) findViewById(R.id.OilTemp);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            ThrottlePos = (TextView) findViewById(R.id.throttlePos);
            OilPres = (TextView) findViewById(R.id.oilPres);
            FuelPres = (TextView) findViewById(R.id.fuelPres);
            Lambda = (TextView) findViewById(R.id.lambda);
            TPMode = (TextView) findViewById(R.id.TpMode);
            BattVolt = (TextView) findViewById(R.id.battVolt);
            Bias = (TextView) findViewById(R.id.bias);
            BPresPercent = (TextView) findViewById(R.id.brakePercent);
            DamperFL = (TextView) findViewById(R.id.damperFL);
            DamperFR = (TextView) findViewById(R.id.damperFR);
            DamperRL = (TextView) findViewById(R.id.damperRL);
            DamperRR = (TextView) findViewById(R.id.damperRR);
            LatG = (TextView) findViewById(R.id.LatG);
            LongG = (TextView) findViewById(R.id.LongG);
            LambdaDiff = (TextView) findViewById(R.id.lambda);
            ThrottlePed = (TextView) findViewById(R.id.throttlePedal);
            BPresF = (TextView) findViewById(R.id.BrakePres);
            BTempF = (TextView) findViewById(R.id.BrakeTemp);

            graph1 = (GraphView) findViewById(R.id.Graph1);
            graph2 = (GraphView) findViewById(R.id.Graph2);
            graph3 = (GraphView) findViewById(R.id.Graph3);
            graph4 = (GraphView) findViewById(R.id.Graph4);

            graph1.addSeries(mGraph.rpmSeries);
            graph1.getViewport().setXAxisBoundsManual(true);
            graph1.getViewport().setMinX(0);
            graph1.getViewport().setMaxX(500);

            graph2.addSeries(mGraph.speedSeries);
            graph2.getViewport().setXAxisBoundsManual(true);
            graph2.getViewport().setMinX(0);
            graph2.getViewport().setMaxX(500);

            graph3.addSeries(mGraph.tpSeries);
            graph3.getViewport().setXAxisBoundsManual(true);
            graph3.getViewport().setMinX(0);
            graph3.getViewport().setMaxX(500);

            graph4.addSeries(mGraph.brakeSeries);
            graph4.getViewport().setXAxisBoundsManual(true);
            graph4.getViewport().setMinX(0);
            graph4.getViewport().setMaxX(500);

        } else if (id == R.id.nav_comms) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeUdpListener() {
        dataStorage = new DataStorage();
        mGraph = new GraphUpdate();
        mGraph.setDataStorage(dataStorage);
        udpListener = new UdpListener(dataStorage);
        udpListener.setPriority(Thread.NORM_PRIORITY);
        udpListener.start();
    }

    private void connectToServer () {
        tcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {
                // process the received message from TCP server
                if(message.equals(REGISTER_USER_SUCCESSFUL)) {
                    registered = true;
//                } else if(message.equals(AUDIO_TRANSMIT_ACCEPTED)) {   //grant audio access command
//                    audio_transmit_enabled = true;
//                    activateAudioStream();
//                    Log.e("onMessageReceived","audio_transmit_enabled" );
//                } else if(message.equals(AUDIO_TRANSMIT_REJECTED)) {
//                    audio_transmit_rejected = true;
//                } else if(message.contains(AUDIO_RECEIVE_ACTIVE)) {
//                    audio_transmit_enabled = false;
//                    audio_transmit_rejected = false;
//                    audio_receive_active = true;
//                    transmitting_client_ID = message.substring(AUDIO_RECEIVE_ACTIVE.length()); //extract the userID
//                    activateAudioReceive();
//                } else if (message.equals(AUDIO_TERMINATE)) {
//                    if(audioReceiveThread != null && !audioReceiveThread.isInterrupted()) {
//                        audio_receive_active = false;
//                        deactivateAudioReceive();
//                    }
                }
            }
        });
        new ConnectTcpServerTask().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(audioThread != null && audioThread.isAlive()) {
//            audioThread.interrupt();
//        }
//        if(audioReceiveThread != null) {
//            audioReceiveThread.interrupt();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(tcpClient != null) {
            tcpClient.stopClient();
        }
    }

    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            switch(tab) {
                case(0):
                    Speed.setText(Integer.toString(dataStorage.getSpeed()));
                    Gear.setText(Integer.toString(dataStorage.getGear()));
                    RPM.setText(Double.toString(dataStorage.getRPM()));
                    TPPercent.setText(Integer.toString(dataStorage.getThrottlePos()));
                    BPresPercent.setText(Integer.toString((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100)));
                    TpBar.setProgress(dataStorage.getThrottlePos());
                    BPresBar.setProgress((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100));
                    break;
                case(1):
                    Speed.setText(Integer.toString(dataStorage.getSpeed()));
                    Gear.setText(Integer.toString(dataStorage.getGear()));
                    RPM.setText(Double.toString(dataStorage.getRPM()));
                    TPPercent.setText(Integer.toString(dataStorage.getThrottlePos()));
                    ThrottlePos.setText(Integer.toString(dataStorage.getThrottlePos()));
                    BPresPercent.setText(Integer.toString((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100)));
                    AirTemp.setText(Integer.toString(dataStorage.getIAT()));
                    OilTemp.setText(Integer.toString(dataStorage.getOilTemp()));
                    EngTemp.setText(Integer.toString(dataStorage.getEngTemp()));
                    BattVolt.setText(Double.toString(dataStorage.getBattVolt()));
                    OilPres.setText(Integer.toString(dataStorage.getOilPres()));
                    FuelPres.setText(Integer.toString(dataStorage.getFuelPres()));
                    TpBar.setProgress(dataStorage.getThrottlePos());
                    BPresBar.setProgress((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100));
                    LambdaDiff.setText(Double.toString(dataStorage.getLambdaDiff()));
                    ThrottlePed.setText(Integer.toString(dataStorage.getThrottlePed()));
                    break;
                case(2):
                    Speed.setText(Integer.toString(dataStorage.getSpeed()));
                    Gear.setText(Integer.toString(dataStorage.getGear()));
                    RPM.setText(Double.toString(dataStorage.getRPM()));
                    TPPercent.setText(Integer.toString(dataStorage.getThrottlePos()));
                    BPresPercent.setText(Integer.toString((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100)));
                    TpBar.setProgress(dataStorage.getThrottlePos());
                    BPresBar.setProgress((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100));
                    Bias.setText(Integer.toString(dataStorage.getBias()));
                    LatG.setText(Double.toString(dataStorage.getLatG()));
                    LongG.setText(Double.toString(dataStorage.getLongG()));
                    BPresF.setText(Double.toString(dataStorage.getBPresF()/10));
                    BTempF.setText(Integer.toString(dataStorage.getBTempF()));
                    break;
                case(3):
                    Speed.setText(Integer.toString(dataStorage.getSpeed()));
                    Gear.setText(Integer.toString(dataStorage.getGear()));
                    RPM.setText(Double.toString(dataStorage.getRPM()));
                    TPPercent.setText(Integer.toString(dataStorage.getThrottlePos()));
                    BPresPercent.setText(Integer.toString((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100)));
                    TpBar.setProgress(dataStorage.getThrottlePos());
                    BPresBar.setProgress((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100));
                    break;
                case(4):
                    mGraph.updateData(dataStorage.getSpeed(), dataStorage.getRPM(), dataStorage.getThrottlePos(), dataStorage.getBPresF());
//                    mGraph.onResume();
                    break;
                default:
                    break;
            }
//            Speed.setText(Integer.toString(dataStorage.getSpeed()));
//            Gear.setText(Integer.toString(dataStorage.getGear()));
//            RPM.setText(Double.toString(dataStorage.getRPM()));
//            TPPercent.setText(Integer.toString(dataStorage.getThrottlePos()));
//            ThrottlePos.setText(Integer.toString(dataStorage.getThrottlePos()));
//            BPresPercent.setText(Integer.toString((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100)));
//            IAT.setText(Integer.toString(dataStorage.getIAT()));
//            OilTemp.setText(Integer.toString(dataStorage.getOilTemp()));
//            EngTemp.setText(Integer.toString(dataStorage.getEngTemp()));
//            BattVolt.setText(Double.toString(dataStorage.getBattVolt()));
//            OilPres.setText(Integer.toString(dataStorage.getOilPres()));
//            FuelPres.setText(Integer.toString(dataStorage.getFuelPres()));
//            BPresF.setText(Double.toString(dataStorage.getBPresF() / 10));
//            Bias.setText(Integer.toString(dataStorage.getBias()));
//            TpBar.setProgress(dataStorage.getThrottlePos());
//            BPresBar.setProgress((int) (dataStorage.getBPresF() / MAX_BRAKE_PRESSURE * 100));
            dataUpdateHandler.post(this);

        }
    };

    private class ConnectTcpServerTask extends AsyncTask<String, String, TcpClient> {
        @Override
        protected TcpClient doInBackground(String... message) {
            while(dataStorage.getServerIP().isEmpty()); // wait for serverIP to be received
            tcpClient.setServerIP(dataStorage.getServerIP());
            tcpClient.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
    }
}
