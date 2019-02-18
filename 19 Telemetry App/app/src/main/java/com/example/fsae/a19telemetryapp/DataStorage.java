package com.example.fsae.a19telemetryapp;

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
        ThrottlePos = 40;
        ThrottlePed = 40;
        BPresF = 0;
        BPresR = 0;
        BTempF = 120;
        BTempR = 60;
        Gear = 3;
        OilTemp = 55;
        EngTemp = 55;
        FuelPres = 325;
        OilPres = 500;
        Bias = 56;
        RPM = 6.0;
        Lambda = 0.86;
        FuelAim = 0.86;
        BattVolt = 13.2;
        Auto = false;
        Clutch = false;
        Launch = false;
        Radio = false;
    }

    public void storeData(byte[] data) {
        switch(data[0]) {
            case 0x40:
                synchronized (lock1) {

                }
                break;
            case 0x41:
                synchronized (lock2) {

                }
                break;
            case 0x42:
                synchronized (lock3) {

                }
                break;
            case 0x43:
                synchronized (lock4) {

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
        synchronized (lock1) {
            return Gear;
        }
    }

    public int getOilTemp() {
        synchronized (lock1) {
            return OilTemp;
        }
    }

    public int getEngTemp() {
        synchronized (lock1) {
            return EngTemp;
        }
    }

    public int getFuelPres() {
        synchronized (lock2) {
            return FuelPres;
        }
    }

    public int getOilPres() {
        synchronized (lock2) {
            return OilPres;
        }
    }

    public int getBias() {
        synchronized (lock3) {
            return Bias;
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

    public double getBattVolt() {
        synchronized (lock3) {
            return BattVolt;
        }
    }

    public boolean getAuto() {
        synchronized (lock3) {
            return Auto;
        }
    }

    public boolean getClutch() {
        synchronized (lock3) {
            return Clutch;
        }
    }

    public boolean getLaunch() {
        synchronized (lock3) {
            return Launch;
        }
    }

    public boolean getRadio() {
        synchronized (lock3) {
            return Radio;
        }
    }
}
