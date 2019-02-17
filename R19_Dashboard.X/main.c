#include<xc.h>
#include <string.h>
#include "mcc_generated_files/mcc.h"
#include "FT800.h"
#include "display.h"
#include "mcc_generated_files/ecan.h"

#define FT_ACTIVE  0x00
#define FT_STANDBY 0x41
#define FT_SLEEP   0x42
#define FT_PWRDOWN 0x50
#define FT_CLKEXT  0x44
#define FT_CLK48M  0x62
#define FT_CLK36M  0x61
#define FT_CORERST 0x68
#define UP_SHIFT_RPM 10000
#define MESSAGE_SIZE 20

uCAN_MSG canMessage;
bool refresh_screen = false;
bool up_blink = false;

typedef struct {
    int current, current_number, current_int, current_dec, last, last_int, last_dec, best, best_number, best_int, best_dec;
} Lap_time;

void wait2secs(){
    __delay_ms(2000);
}

void refresh() {
    refresh_screen = true;
}

void LED_blink() {
    INTERRUPT_PeripheralInterruptDisable();
    up_shift_LAT = !up_shift_GetValue();
    down_shift_LAT = !down_shift_GetValue();
    INTERRUPT_PeripheralInterruptEnable();
}

void main(void) {   
    Lap_time lap_time= {0,1,0,0,0,0,0,0,0,0,0};
    bool timer_started = false;
    bool launch, autoShift, clutch, drs, radio = false;
    char message[MESSAGE_SIZE] = {'\0'};
    int rpm = 0, oilP = 0, fuel = 0, tp = 0, speed = 0, gear = 0, engTemp = 0, oilTemp = 0, battVolts = 0, bias = 0;
    int gasP = 0;
    int fuelP = 0;
    int etc = 0;
    int BSPD_counter = 0;
    int bspd = 0;
    int brake_p = 0;
    bool bspd_flag = false;
    wait2secs(); 
    
    // Initialize the device
    SYSTEM_Initialize();
    
    // CAN Configuration
    CIOCONbits.CLKSEL = 1;
    CIOCONbits.ENDRHI = 1;
    CIOCONbits.TX2SRC = 0;
    CIOCONbits.TX2EN = 1; 
    RXB0CONbits.RXM0 = 1;
    RXB0CONbits.RXM1 = 0; //receive only standard messages
    RXB0CONbits.RB0DBEN = 0;
    PIE5bits.RXB0IE = 0;
    PIE5bits.RXB1IE = 0;
    PIR5bits.ERRIF = 0;
    PIR5bits.WAKIF = 0;
    
    // CAN FILTER CONFIG
    // RXF5 & 6 not available in mode 0
//    RXF4SIDH = 0xC8;
//    RXF4SIDL = 0x00; //Filter for ID 0x640
//    RXF3SIDH = 0xC8;
//    RXF3SIDL = 0x20; //Filter for ID 0x641
    RXF2SIDH = 0xC8;
    RXF2SIDL = 0x40; //Filter for ID 0x642
    RXM0SIDH = 0xFE;
    RXM0SIDL = 0b00011111;
    RXM1SIDH = 0xFE;
    RXM1SIDL = 0x00011111;
    
    // SPI Configuration for LCD
    SSPSTATbits.SMP = 0;
    SSPSTATbits.CKE = 1;
    SSPCON1bits.SSPEN = 1;
    SSPCON1bits.CKP = 1;
    SSPCON1bits.SSPM = 0b0010; //FOSC/64    
    FT800_Init();
     
    wait2secs(); 
    SSPCON1bits.SSPM = 0b0001; //FOSC/16  

    // up_shift_SetLow();
    // down_shift_SetLow();
    // warning_1_SetLow();
    // warning_2_SetHigh();
    // warning_3_SetLow();
    // warning_4_SetHigh();
    
    display(rpm, oilP, fuelP, fuel, tp, bspd, etc, speed, gear, engTemp, oilTemp, battVolts);
    
    
    TMR1_SetInterruptHandler(&refresh); 
    // TMR0_SetInterruptHandler(*LED_blink);
    TMR0_StopTimer();
    INTERRUPT_GlobalInterruptEnable();
    INTERRUPT_PeripheralInterruptEnable();
    
    while (1) {  
        if (CAN_receive(&canMessage)) {
            if (canMessage.frame.id == 0x640) {
                rpm = ((canMessage.frame.data0 << 8) | canMessage.frame.data1);
                oilP = ((canMessage.frame.data2 << 8) | canMessage.frame.data3) / 10;
                fuelP = ((canMessage.frame.data4 << 8) | canMessage.frame.data5);
                tp = canMessage.frame.data6;
                speed = canMessage.frame.data7;
            } else if (canMessage.frame.id == 0x641) {
                brake_p = ((canMessage.frame.data0 << 8) | canMessage.frame.data1) / 10;
                gear = canMessage.frame.data6;
                bias = canMessage.frame.data7;
            } else if (canMessage.frame.id == 0x642) {
                engTemp = canMessage.frame.data0;
                oilTemp = canMessage.frame.data1;
                battVolts = canMessage.frame.data2;
            } else if (canMessage.frame.id == 0x643) {
                radio = canMessage.frame.data0 >> 7;
                drs = canMessage.frame.data0 >> 6 & 0b1;
                etc = canMessage.frame.data0 >> 3 & 0b111; // E-throttle state
//                fuel = canMessage.frame.data0 >> 2 & 0b1; // fuel pressure
                launch = canMessage.frame.data1 >> 7 & 0b1;
                autoShift = canMessage.frame.data1 >> 6 & 0b1;
                clutch = canMessage.frame.data1 >> 5 & 0b1;
            } else if (canMessage.frame.id == 0x644) {
                lap_time.last = ((canMessage.frame.data0 << 8) | canMessage.frame.data1);
                lap_time.current = ((canMessage.frame.data2 << 8) | canMessage.frame.data3);
                lap_time.current_number = ((canMessage.frame.data4 << 8) | canMessage.frame.data5);
                lap_time.last_int = lap_time.last/100;
                lap_time.last_dec = lap_time.last%100;
                lap_time.current_int = lap_time.current/100;
                lap_time.current_dec = lap_time.current%100;
                if(lap_time.current < lap_time.best) {
                    lap_time.best = lap_time.current; 
                    lap_time.best_int = lap_time.current_int;
                    lap_time.best_dec = lap_time.current_dec;
                }
            }
        }
        if(refresh_screen) {
            if(!bspd_flag) {
                if(tp > 21 && brake_p > 1.44) {
                    BSPD_counter++;
                    bspd = 1;
                }
                else {
                    BSPD_counter = 0;
                    bspd = 0;
                }
            }
            if(BSPD_counter >= 33)  bspd_flag = true;
            if(bspd_flag)   bspd = 2;
            if(rpm > 1000 && fuelP < 320)  fuel = 0;
            else    fuel = 1;
            display_start();
            display_labels();
            display_grids();
            display_bottom_section(battVolts, tp);
            display_rpm(rpm);
            display_speed(speed);
            display_waterTemp(engTemp);
            display_oilTemp(oilTemp);
            display_etc(etc);
            display_brake_bias(bias);
//            display_gasPres(gasP);
            display_bspd(bspd);
            display_gear(gear);
            display_fuel_level(fuel);
            display_status(launch, autoShift, clutch, radio);
            display_laptime(lap_time.current_int, lap_time.current_dec, lap_time.best_int, lap_time.best_dec,
                             lap_time.last_int, lap_time.last_dec, lap_time.current_number, lap_time.best_number);
            display_end();
            // if(rpm > UP_SHIFT_RPM && !timer_started) {
            //     TMR0_StartTimer();
            //     timer_started = true;
            // } else {
            //     TMR0_StopTimer();
            //     timer_started = false;
            //     up_shift_SetLow();
            //     down_shift_SetLow();
            // }
            refresh_screen = false;
            TMR1_Reload();
        }
    }
}


/**
 End of File
*/