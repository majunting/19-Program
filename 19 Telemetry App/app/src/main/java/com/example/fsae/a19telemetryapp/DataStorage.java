package com.example.fsae.a19telemetryapp;

import java.nio.charset.Charset;
import java.util.Arrays;

public class DataStorage {

    private Object lock1 = new Object();
    private Object lock2 = new Object();
    private Object lock3 = new Object();
    private Object lock4 = new Object();

    private static String ServerIP = null;

    // sensor readings from vehicle
    private static int Speed;
    private static int ThrottlePos;
    private static int ThrottlePed;
    private static int BPresF;
    private static int BPresR;
    private static int BTempF;
    private static int BTempR;
    private static int Gear;
    private static int OilTemp;
    private static int EngTemp;
    private static int FuelPres;
    private static int OilPres;
    private static int Bias;
    private static int IAT;
    private static int damperFL;
    private static int damperFR;
    private static int damperRL;
    private static int damperRR;
    private static double latG;
    private static double longG;
    private static double RPM;
    private static double Lambda;
    private static double FuelAim;
    private static double BattVolt;
    private static boolean Auto;
    private static boolean Clutch;
    private static boolean Launch;
    private static boolean Radio;

    public DataStorage() {
        Speed = 20;
        ThrottlePos = 0;
        ThrottlePed = 40;
        BPresF = 0;
        BPresR = 0;
        BTempF = 120;
        BTempR = 60;
        Gear = 3;
        OilTemp = 55;
        EngTemp = 55;
        IAT = 35;
        FuelPres = 325;
        OilPres = 500;
        Bias = 56;
        RPM = 6.0;
        Lambda = 0.86;
        FuelAim = 0.86;
        BattVolt = 13.2;
        latG = 0.0;
        longG = 0.0;
        damperFL = 22;
        damperFR = 21;
        damperRL = 33;
        damperRR = 32;
        Auto = false;
        Clutch = false;
        Launch = false;
        Radio = false;
    }

    public void storeData(byte[] data) {
        switch(data[0]) {
            case 0x40:
                synchronized (lock1) {
                    int rpm_temp = (data[1] << 8 | (data[2] & 0xFF)) / 100;
                    RPM = rpm_temp / 10.0;
                    OilPres = (data[3] << 8 | (data[4] & 0xFF)) / 10;
                    FuelPres = (data[5] << 8 | (data[6] & 0xFF)) / 10;
                    ThrottlePos = data[7];
                    Speed = data[8];
                }
                break;
            case 0x41:
                synchronized (lock2) {
                    BPresF = data[1];
                    BTempF = (data[3] << 8 | (data[4]&0xFF));
                    Gear = data[7];
                    Bias = data[8];
                }
                break;
            case 0x42:
                synchronized (lock3) {
                    EngTemp = data[1];
                    OilTemp = data[2];
                    BattVolt = (data[3] & 0xFF) / 10.0;
                    IAT = data[4];
                }
                break;
            case 0x43:
                synchronized (lock4) {

                }
                break;
            case (byte)0xFE:
                if(ServerIP == null) {
                    int zeroIndex =  data.length;
                    for (int i=0; i<data.length; ++i) {
                        if(data[i] == 0) {
                            zeroIndex = i;
                        }
                    }
                    ServerIP = new String(Arrays.copyOfRange(data,1,zeroIndex), Charset.forName("UTF-8"));
                }
                break;
        }
    }

    public String getServerIP() {
        if(ServerIP == null)    return "";
        else    return ServerIP;
    }

    public int getSpeed() {
        synchronized (lock1) {
            return Speed;
        }
    }

    public int getThrottlePos() {
        synchronized (lock1) {
            return ThrottlePos;
        }
    }

    public int getThrottlePed() {
        synchronized (lock1) {
            return ThrottlePed;
        }
    }

    public int getBPresF() {
        synchronized(lock2) {
            return BPresF;
        }
    }

    public int getBPresR() {
        synchronized (lock2) {
            return BPresR;
        }
    }

    public int getBTempF() {
        synchronized (lock2) {
            return BTempF;
        }
    }

    public int getBTempR() {
        synchronized (lock2) {
            return BTempR;
        }
    }

    public int getGear() {
        synchronized (lock2) {
            return Gear;
        }
    }

    public int getOilTemp() {
        synchronized (lock3) {
            return OilTemp;
        }
    }

    public int getEngTemp() {
        synchronized (lock3) {
            return EngTemp;
        }
    }

    public int getFuelPres() {
        synchronized (lock1) {
            return FuelPres;
        }
    }

    public int getOilPres() {
        synchronized (lock1) {
            return OilPres;
        }
    }

    public int getIAT() {
        synchronized (lock3) {
            return IAT;
        }
    }

    public int getBias() {
        synchronized (lock2) {
            return Bias;
        }
    }

    public int getDamperFL() {
        synchronized (lock4) {
            return damperFL;
        }
    }

    public int getDamperFR() {
        synchronized (lock4) {
            return damperFR;
        }
    }

    public int getDamperRL() {
        synchronized (lock4) {
            return damperRL;
        }
    }

    public int getDamperRR() {
        synchronized (lock4) {
            return damperRR;
        }
    }

    public double getRPM() {
        synchronized (lock1) {
            return RPM;
        }
    }

    public double getLambda() {
        synchronized (lock3) {
            return Lambda;
        }
    }

    public double getFuelAim() {
        synchronized (lock3) {
            return FuelAim;
        }
    }

    public double getLambdaDiff() {
        synchronized (lock3) {
            return Lambda - FuelAim;
        }
    }

    public double getLatG() {
        synchronized (lock4) {
            return latG;
        }
    }

    public double getLongG() {
        synchronized (lock4) {
            return longG;
        }
    }

    public double getBattVolt() {
        synchronized (lock3) {
            return BattVolt;
        }
    }

    public boolean getAuto() {
        synchronized (lock4) {
            return Auto;
        }
    }

    public boolean getClutch() {
        synchronized (lock4) {
            return Clutch;
        }
    }

    public boolean getLaunch() {
        synchronized (lock4) {
            return Launch;
        }
    }

    public boolean getRadio() {
        synchronized (lock4) {
            return Radio;
        }
    }
}
