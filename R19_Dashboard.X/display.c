#include "display.h"

void display(int rpm, int oilP, int gasPres, int fuel, int tp, int bspd, int etc, int speed, int gear, int engTemp, int oilTemp, int battVolts) {
    display_start();
    display_labels();
    display_grids();
    display_bottom_section(battVolts, tp);
    display_rpm(rpm);
    display_speed(speed);
    display_waterTemp(engTemp);
    display_oilTemp(oilTemp);
    display_etc(etc);
    // display_gasPres(gasPres);
    display_gear(gear);//
    display_fuel_level(fuel);
    display_bspd(bspd);

    display_end();
}

void display_start() {
    cmd(CMD_DLSTART);
    cmd(CLEAR_COLOR_RGB(0, 0, 0));
    cmd(CLEAR(1, 1, 1));
}

void display_end() {
    cmd(DISPLAY());
    cmd(CMD_SWAP);
    cmd_exec();
}

void display_bottom_section(int volts, int tp) {
    cmd(COLOR_RGB(255, 0, 0));
    cmd(BEGIN(LINES));
    cmd(LINE_WIDTH(30));
    cmd(VERTEX2II(0, 237, 0, 0));
    cmd(VERTEX2II(480, 237, 0, 0));
    cmd(END());

    display_battery(volts);
    display_tp(tp);
}

void display_labels() {
    cmd(COLOR_RGB(255, 255, 255));
    cmd_text(speed_unit_x, speed_unit_y, label_size, 0, "KPH");
    cmd_text(gain_label_x, gain_label_y, SMALL_FONT_SIZE, OPT_CENTER, "Gain/Loss");
    cmd_text(coolant_label_x, coolant_label_y, label_size, OPT_CENTER, "Water");
    cmd_text(oilTemp_label_x, oilTemp_label_y, label_size, OPT_CENTER, "Oil");
    cmd_text(rpm_label_x, rpm_label_y, label_size, OPT_CENTER, "RPM");
    cmd_text(speed_label_x, speed_label_y, label_size, OPT_CENTER, "Speed");
    cmd_text(bias_label_x, bias_label_y, label_size, 0, "Bias");
    cmd_text(bias_unit_x, bias_unit_y, label_size, 0, "%");
    cmd_text(tp_label_x, tp_label_y, label_size, 0, "TP");
    cmd_text(tp_unit_x, tp_unit_y, label_size, 0, "%");
    cmd_text(etc_label_x, etc_label_y, SMALL_FONT_SIZE, OPT_CENTER, "ETC");
    cmd_text(batt_unit_x, batt_unit_y, label_size, 0, "V");
    cmd_text(fuel_label_x, fuel_label_y, label_size, OPT_CENTER, "Fuel");
    // cmd_text(gasPres_label_x, gasPres_label_y, SMALL_FONT_SIZE, OPT_CENTER, "Gas");
    cmd_text(BSPD_label_x, BSPD_label_y, SMALL_FONT_SIZE, OPT_CENTER, "BSPD State");
}

void display_grids() {
    COLOR_RGB(255, 255, 255);
    //water
    cmd(BEGIN(LINE_STRIP));
    cmd(LINE_WIDTH(10));
    cmd(VERTEX2II(15, 45, 0, 0));
    cmd(VERTEX2II(5, 45, 0, 0));
    cmd(VERTEX2II(5, 110, 0, 0));
    cmd(VERTEX2II(90, 110, 0, 0));
    cmd(VERTEX2II(90, 45, 0, 0));
    cmd(VERTEX2II(80, 45, 0, 0));
    cmd(END());

    //oil
    cmd(BEGIN(LINE_STRIP));
    cmd(VERTEX2II(123, 45, 0, 0));
    cmd(VERTEX2II(98, 45, 0, 0));
    cmd(VERTEX2II(98, 110, 0, 0));
    cmd(VERTEX2II(180, 110, 0, 0));
    cmd(VERTEX2II(180, 45, 0, 0));
    cmd(VERTEX2II(155, 45, 0, 0));
    cmd(END());

    //fuel
    cmd(BEGIN(LINE_STRIP));
    cmd(LINE_WIDTH(10));
    cmd(VERTEX2II(20, 125, 0, 0));
    cmd(VERTEX2II(5, 125, 0, 0));
    cmd(VERTEX2II(5, 180, 0, 0));
    cmd(VERTEX2II(90, 180, 0, 0));
    cmd(VERTEX2II(90, 125, 0, 0));
    cmd(VERTEX2II(80, 125, 0, 0));
    cmd(END());

    //etc
    cmd(BEGIN(LINE_STRIP));
    cmd(LINE_WIDTH(10));
    cmd(VERTEX2II(120, 125, 0, 0));
    cmd(VERTEX2II(98, 125, 0, 0));
    cmd(VERTEX2II(98, 180, 0, 0));
    cmd(VERTEX2II(180, 180, 0, 0));
    cmd(VERTEX2II(180, 125, 0, 0));
    cmd(VERTEX2II(158, 125, 0, 0));
    cmd(END());

    //rpm
    cmd(BEGIN(LINE_STRIP));
    cmd(LINE_WIDTH(10));
    cmd(VERTEX2II(360, 45, 0, 0));
    cmd(VERTEX2II(300, 45, 0, 0));
    cmd(VERTEX2II(300, 105, 0, 0));
    cmd(VERTEX2II(470, 105, 0, 0));
    cmd(VERTEX2II(470, 45, 0, 0));
    cmd(VERTEX2II(410, 45, 0, 0));
    cmd(END());

    //speed
    cmd(BEGIN(LINE_STRIP));
    cmd(LINE_WIDTH(10));
    cmd(VERTEX2II(350, 120, 0, 0));
    cmd(VERTEX2II(300, 120, 0, 0));
    cmd(VERTEX2II(300, 180, 0, 0));
    cmd(VERTEX2II(470, 180, 0, 0));
    cmd(VERTEX2II(470, 120, 0, 0));
    cmd(VERTEX2II(420, 120, 0, 0));
    cmd(END());

    //gas
    // cmd(BEGIN(LINE_STRIP));
    // cmd(LINE_WIDTH(10));
    // cmd(VERTEX2II(285, 190, 0, 0));
    // cmd(VERTEX2II(265, 190, 0, 0));
    // cmd(VERTEX2II(265, 230, 0, 0));
    // cmd(VERTEX2II(345, 230, 0, 0));
    // cmd(VERTEX2II(345, 190, 0, 0));
    // cmd(VERTEX2II(325, 190, 0, 0));
    // cmd(END());

    //BSPD
    cmd(BEGIN(LINE_STRIP));
    cmd(LINE_WIDTH(10));
    cmd(VERTEX2II(220, 190, 0, 0));
    cmd(VERTEX2II(190, 190, 0, 0));
    cmd(VERTEX2II(190, 230, 0, 0));
    cmd(VERTEX2II(345, 230, 0, 0));
    cmd(VERTEX2II(345, 190, 0, 0));
    cmd(VERTEX2II(315, 190, 0, 0));
    cmd(END());

    //gain
    cmd(BEGIN(LINE_STRIP));
    cmd(LINE_WIDTH(10));
    cmd(VERTEX2II(365, 190, 0, 0));
    cmd(VERTEX2II(355, 190, 0, 0));
    cmd(VERTEX2II(355, 230, 0, 0));
    cmd(VERTEX2II(465, 230, 0, 0));
    cmd(VERTEX2II(465, 190, 0, 0));
    cmd(VERTEX2II(455, 190, 0, 0));
    cmd(END());
}

void display_status(bool launch, bool autoShift, bool clutch, bool radio) {
    cmd(BEGIN(RECTS));
    if(launch) {
        cmd(COLOR_RGB(INDICATOR_ACTIVE_COLOR_R, INDICATOR_ACTIVE_COLOR_G, INDICATOR_ACTIVE_COLOR_B));
    } else {
        cmd(COLOR_RGB(BACKGROUND_COLOR_R, BACKGROUND_COLOR_G, BACKGROUND_COLOR_B));
    }
    cmd(VERTEX2II(10, 215, 0, 0));
    cmd(VERTEX2II(90, 230, 0, 0));

    if (autoShift) {
        cmd(COLOR_RGB(INDICATOR_ACTIVE_COLOR_R, INDICATOR_ACTIVE_COLOR_G, INDICATOR_ACTIVE_COLOR_B));
    } else {
        cmd(COLOR_RGB(BACKGROUND_COLOR_R, BACKGROUND_COLOR_G, BACKGROUND_COLOR_B));
    }
    cmd(VERTEX2II(100, 215, 0, 0));
    cmd(VERTEX2II(180, 230, 0, 0));

    if (clutch) {
        cmd(COLOR_RGB(INDICATOR_ACTIVE_COLOR_R, INDICATOR_ACTIVE_COLOR_G, INDICATOR_ACTIVE_COLOR_B));
    } else {
        cmd(COLOR_RGB(BACKGROUND_COLOR_R, BACKGROUND_COLOR_G, BACKGROUND_COLOR_B));
    }
    cmd(VERTEX2II(10, 190, 0, 0));
    cmd(VERTEX2II(90, 205, 0, 0));

    if (radio) {
        cmd(COLOR_RGB(INDICATOR_ACTIVE_COLOR_R, INDICATOR_ACTIVE_COLOR_G, INDICATOR_ACTIVE_COLOR_B));
    } else {
        cmd(COLOR_RGB(BACKGROUND_COLOR_R, BACKGROUND_COLOR_G, BACKGROUND_COLOR_B));
    }
    cmd(VERTEX2II(100, 190, 0, 0));
    cmd(VERTEX2II(180, 205, 0, 0));

    if (launch) {
        cmd(COLOR_RGB(0, 0, 0));
    } else {
        cmd(COLOR_RGB(255, 255, 255));
    }
    cmd_text(launch_label_x, launch_label_y, label_size, OPT_CENTER, "Launch");

    if (autoShift) {
        cmd(COLOR_RGB(0, 0, 0));
    } else {
        cmd(COLOR_RGB(255, 255, 255));
    }
    cmd_text(auto_label_x, auto_label_y, label_size, OPT_CENTER, "Auto");

    if (clutch) {
        cmd(COLOR_RGB(0, 0, 0));
    } else {
        cmd(COLOR_RGB(255, 255, 255));
    }
    cmd_text(clutch_label_x, clutch_label_y, label_size, OPT_CENTER, "Clutch");
    
    if (radio) {
        cmd(COLOR_RGB(0, 0, 0));
    } else {
        cmd(COLOR_RGB(255, 255, 255));
    }
    cmd_text(radio_label_x, radio_label_y, label_size, OPT_CENTER, "Radio");
}

void display_laptime(int current_int, int current_dec, int best_int, int best_dec, int last_int, int last_dec, int lap, int best_lap) {
    //lap time labels
    // cmd(COLOR_RGB(255, 125, 0));
//    cmd_text(5, 207, 27, 0, "LAST");
//    cmd_number(66, 217, 27, OPT_CENTER, lap);
//    cmd_text(448, 207, 27, OPT_RIGHTX, "BEST");
//    cmd_number(475, 207, 27, OPT_RIGHTX, best_lap);
    // cmd_text(240, 165, 27, OPT_CENTER, "LAP");
    // cmd_number(240, 190, 29, OPT_CENTER, lap+1);

    int time_difference_integer = last_int - best_int;
    int time_difference_decimal = last_dec - best_dec;

    if(time_difference_integer > 0) {
        cmd(COLOR_RGB(255, 0, 0));  //red colour if timing increased
        cmd_text(gain_sign_x, gain_sign_y, LARGE_FONT_SIZE, 0, "+");
    } else {
        cmd(COLOR_RGB(0, 255, 0));  //green colour if timing decreased
        cmd_text(gain_sign_x, gain_sign_y, LARGE_FONT_SIZE, 0, "-");
    }
    cmd_text(gain_decimal_x, gain_decimal_y, LARGE_FONT_SIZE, 0, ".");
    cmd_number(gain_1st_digit_x, gain_1st_digit_y, LARGE_FONT_SIZE, OPT_RIGHTX, time_difference_integer);
    cmd_number(gain_2nd_digit_x, gain_2nd_digit_y, LARGE_FONT_SIZE, 0, time_difference_decimal);

//    // last lap time
//    cmd(COLOR_RGB(255, 255, 255));
//    cmd_number(36, 222, 30, OPT_RIGHTX, last_int);
//    cmd_text(39, 238, 28, OPT_CENTER, ":");
//    cmd_number(42, 222, 30, OPT_NODL, last_dec);
//
//
//    // best lap time
//    cmd_number(434, 222, 30, OPT_RIGHTX, best_int);
//    cmd_text(437, 239, 28, OPT_CENTER, ":");
//    cmd_number(440, 222, 30, OPT_NODL, best_dec);
//
//
//    // current lap time
//    cmd_number(233, 235, 30, OPT_RIGHTX, current_int);
//    cmd_text(237, 252, 28, OPT_CENTER, ":");
//    cmd_number(240, 235, 30, OPT_NODL, current_dec);
}

void display_waterTemp(int temp) {
    cmd(COLOR_RGB(255, 255, 255));
    //cmd_number(64, 7, 28, 0, temp);
    cmd_number(coolant_value_x, coolant_value_y, value_size, OPT_CENTERX, temp);
}

void display_oilTemp(int temp) {
    //cmd_number(66, 48, 28, 0, temp);
    cmd_number(oilTemp_value_x, oilTemp_value_y, value_size, OPT_CENTERX, temp);
}

void display_fuel(int level) {
    cmd_number(fuel_value_x, fuel_value_y, label_size, 0, level);
}

void display_bspd(int bspd) {
    switch (bspd) {
        case 0:
        cmd(COLOR_RGB(0, 255, 0));
        cmd_text(BSPD_state_x, BSPD_state_y, label_size, OPT_CENTER, "Normal");
        break;
        case 1:
        cmd(COLOR_RGB(255, 255, 0));
        cmd_text(BSPD_state_x, BSPD_state_y, label_size, OPT_CENTER, "Warning");
        break;
        case 2:
        cmd(COLOR_RGB(255, 0, 0));
        cmd_text(BSPD_state_x, BSPD_state_y, label_size, OPT_CENTER, "Error");
        break;
        default:
        break;
    }
}

void display_battery(int volts) {
    cmd(COLOR_RGB(255, 255, 255));
    cmd_number(batt_1st_digit_x, batt_1st_digit_y, LARGE_FONT_SIZE, OPT_RIGHTX, volts/10);
    cmd_text(batt_decimal_x, batt_decimal_y, LARGE_FONT_SIZE, 0, ".");
    cmd_number(batt_2nd_digit_x, batt_2nd_digit_y, LARGE_FONT_SIZE, 0, volts%10);
}

// void display_oilPres(int pressure) {
//     cmd_number(oilPres_value_x, oilPres_value_y, label_size, 0, pressure);
// }

void display_fuel_level(int fuel) {
    if(fuel == 1) {
        cmd(COLOR_RGB(255, 255, 255));
        cmd_text(fuel_level_x, fuel_level_y, label_size, OPT_CENTERX, "Normal");
    }
    else {
        cmd(COLOR_RGB(255, 0, 0));
        cmd_text(fuel_level_x, fuel_level_y, label_size, OPT_CENTERX, "Warning");
    }
} 

void display_gasPres(int gasPres) {
    cmd_number(gasPres_1st_digit_x, gasPres_1st_digit_y, LARGE_FONT_SIZE, OPT_RIGHTX, gasPres/10);
    cmd_text(gasPres_decimal_x, gasPres_decimal_y, LARGE_FONT_SIZE, 0, ".");
    cmd_number(gasPres_2nd_digit_x, gasPres_2nd_digit_y, LARGE_FONT_SIZE, 0, gasPres%10);
}

void display_tp(int tp) {
    //cmd_number(455, 166, 30, OPT_RIGHTX, tp);
    cmd_number(tp_value_x, tp_value_y, LARGE_FONT_SIZE, OPT_RIGHTX, tp);
}

void display_etc(int etc) {
    switch(etc) {
        case 1: 
        cmd_text(etc_value_x, etc_value_y, SMALL_FONT_SIZE, OPT_CENTERX, "Idle");
        break;
        case 2:
        cmd_text(etc_value_x, etc_value_y, SMALL_FONT_SIZE, OPT_CENTERX, "Anti Lag");
        break;
        case 3:
        cmd_text(etc_value_x, etc_value_y, SMALL_FONT_SIZE, OPT_CENTERX, "Limit");
        break;
        case 4:
        cmd_text(etc_value_x, etc_value_y, SMALL_FONT_SIZE, OPT_CENTERX, "Gear Shift");
        break;
        case 5:
        cmd_text(etc_value_x, etc_value_y, SMALL_FONT_SIZE, OPT_CENTERX, "Test");
        break;
        default:
        cmd_text(etc_value_x, etc_value_y, SMALL_FONT_SIZE, OPT_CENTERX, "Normal");
        break;
    }
}

void display_speed(int speed) {
    cmd(COLOR_RGB(255, 255, 255));
    cmd_number(speed_value_x, speed_value_y, value_size, OPT_RIGHTX, speed);
}

void display_brake_bias(int bias) {
    cmd(COLOR_RGB(255, 255, 255));
    if(bias < 100)    cmd_number(bias_value_x, bias_value_y, LARGE_FONT_SIZE, 0, bias);
}

void display_rpm(int rpm) {   
    int first_digit = rpm/1000;
    int second_digit = (rpm/100)%10;
    int diff = 12500/19;

    int n = rpm/diff;

    cmd_number(rpm_1st_digit_x, rpm_1st_digit_y, value_size, OPT_RIGHTX, first_digit);
    cmd_number(rpm_2nd_digit_x, rpm_2nd_digit_y, value_size, 0, second_digit);
    cmd_text(rpm_decimal_x, rpm_decimal_y, value_size, 0, ".");
    cmd_text(rpm_k_x, rpm_k_y, label_size, 0, "k");

    cmd(BEGIN(POINTS));
    cmd(POINT_SIZE(150));
    cmd(COLOR_RGB(0, 255, 0));
    for(int i = 0; i<n; i++) {
        if(i >= 10)  cmd(COLOR_RGB(255, 255, 0));
        if(i >= 15) cmd(COLOR_RGB(255, 0, 0));
        cmd(VERTEX2II(rpm_point_offset_x + (i * rpm_offset), rpm_point_y, 0, 0));
    }
}

void display_gear(int gear) {
    cmd(LINE_WIDTH(GEAR_LINE_WIDTH));
    cmd(COLOR_RGB(202, 125, 0));
    cmd(BEGIN(LINES));
    switch (gear) {
        case 0:
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y, 0, 0));
            break;
        case 1:
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+40, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+40, GEAR_POSITION_OFFSET_Y+119, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+39, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+20, GEAR_POSITION_OFFSET_Y+10, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+20, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+60, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            break;
        case 2:
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+1, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+59, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+79, GEAR_POSITION_OFFSET_Y+60, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+60, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+61, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+1, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            break;
        case 3:
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+79, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+79, GEAR_POSITION_OFFSET_Y+60, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+60, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+79, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            break;
        case 4:
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+59, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+79, GEAR_POSITION_OFFSET_Y+60, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+1, GEAR_POSITION_OFFSET_Y+60, 0, 0));
            break;
        case 5:
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+1, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+59, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+79, GEAR_POSITION_OFFSET_Y+60, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+60, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+61, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+119, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+1, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            break;
        case 6:
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+1, GEAR_POSITION_OFFSET_Y, 0, 0));
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y, 0, 0));
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+1, 0, 0));
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+119, 0, 0));
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+1, GEAR_POSITION_OFFSET_Y+60, 0, 0));
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+60, 0, 0));
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+61, 0, 0));
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+80, GEAR_POSITION_OFFSET_Y+119, 0, 0));
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X, GEAR_POSITION_OFFSET_Y+120, 0, 0));
             cmd(VERTEX2II(GEAR_POSITION_OFFSET_X+79, GEAR_POSITION_OFFSET_Y+120, 0, 0));
            break;
        default:
            
            break;
    }
}