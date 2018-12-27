#include "mcc_generated_files/mcc.h"
#include "FT800.h"

#define SMALL_FONT_SIZE 27
#define LARGE_FONT_SIZE 30
#define label_size 28
#define value_size 31
#define symbol_size 20
#define SPACING 15
#define NEWLINE 25
#define X_START 5
#define Y_START 5
#define X_START_SPEED 360
#define Y_START_SPEED 130
#define GEAR_LINE_WIDTH 96
#define GEAR_POSITION_OFFSET_X 200
#define GEAR_POSITION_OFFSET_Y 45
#define GAUGE_X 240
#define GAUGE_Y 115
#define GAUGE_SIZE 130
#define GAUGE_OPTIONS OPT_NOBACK | OPT_FLAT
#define GAUGE_MAJOR_DIVISION 10
#define GAUGE_MINOR_DIVISION 5
#define GAUGE_RANGE 50
#define BACKGROUND_COLOR_R 60
#define BACKGROUND_COLOR_G 60
#define BACKGROUND_COLOR_B 60   
#define INDICATOR_ACTIVE_COLOR_R 0
#define INDICATOR_ACTIVE_COLOR_G 255
#define INDICATOR_ACTIVE_COLOR_B 0 
#define INDICATOR_ERROR_COLOR_R 255
#define INDICATOR_ERROR_COLOR_G 0
#define INDICATOR_ERROR_COLOR_B 0

#define speed_unit_x 418
#define speed_unit_y 149
#define gain_label_x 410
#define gain_label_y 190
#define coolant_label_x 47
#define coolant_label_y 45
#define oilTemp_label_x 139
#define oilTemp_label_y 45
#define rpm_label_x 385
#define rpm_label_y 45
#define speed_label_x 385
#define speed_label_y 120
#define bias_label_x 65
#define bias_label_y 245
#define bias_unit_x 45
#define bias_unit_y 245
#define tp_label_x 370
#define tp_label_y 245
#define tp_unit_x 459
#define tp_unit_y 245
#define etc_label_x 139
#define etc_label_y 125
#define batt_unit_x 270
#define batt_unit_y 245
#define gasPres_label_x 305
#define gasPres_label_y 190
#define fuel_label_x 47
#define fuel_label_y 125
#define launch_label_x 50
#define launch_label_y 222
#define auto_label_x 140
#define auto_label_y 222
#define clutch_label_x 50
#define clutch_label_y 197
#define radio_label_x 140
#define radio_label_y 197
#define BSPD_label_x 268
#define BSPD_label_y 190

#define coolant_value_x 47
#define coolant_value_y 58
#define oilTemp_value_x 139
#define oilTemp_value_y 58
#define fuel_value_x 175
#define fuel_value_y 165
#define batt_1st_digit_x 240
#define batt_1st_digit_y 236
#define batt_decimal_x 240
#define batt_decimal_y 236
#define batt_2nd_digit_x 248
#define batt_2nd_digit_y 236
#define tp_value_x 455
#define tp_value_y 236
#define etc_value_x 139
#define etc_value_y 146
#define speed_value_x 408
#define speed_value_y 130
#define bias_value_x 5
#define bias_value_y 236
#define rpm_1st_digit_x 380
#define rpm_1st_digit_y 55
#define rpm_decimal_x 380
#define rpm_decimal_y 55
#define rpm_2nd_digit_x 397
#define rpm_2nd_digit_y 55
#define rpm_k_x 425
#define rpm_k_y 74
#define gasPres_1st_digit_x 307
#define gasPres_1st_digit_y 195
#define gasPres_decimal_x 310
#define gasPres_decimal_y 195
#define gasPres_2nd_digit_x 318
#define gasPres_2nd_digit_y 195
#define fuel_level_x 47
#define fuel_level_y 142
#define gain_sign_x 362
#define gain_sign_y 195
#define gain_1st_digit_x 410
#define gain_1st_digit_y 195
#define gain_decimal_x 415
#define gain_decimal_y 195
#define gain_2nd_digit_x 425
#define gain_2nd_digit_y 195
#define BSPD_state_x 268
#define BSPD_state_y 212

#define rpm_point_offset_x 15
#define rpm_point_y 20
#define rpm_offset 25

void display(int rpm, int oilP, int gasPres, int fuel, int tp, int bspd, int etc, int speed, int gear, int engTemp, int oilTemp, int battVolts);
void display_start();
void display_labels(); 
void display_grids();
void display_gear(int gear);
void display_waterTemp(int temp);
void display_oilTemp(int temp);
void display_fuel(int level);
void display_fuel_level(int fuel);
void display_battery(int volts);
void display_oilPres(int pressure);
void display_gasPres(int gasPres);
void display_eThrottle(int etc);
void display_bspd(int bspd);
void display_rpm(int rpm);
void display_end();
void display_speed(int speed);
void display_tp(int tp);
void display_etc(int etc);
void display_laptime(int current_int, int current_dec, int best_int, int best_dec, int last_int, int last_dec, int lap, int best_lap);
void display_status(bool launch, bool autoShift, bool clutch, bool radio);
void display_bottom_section(int volts, int tp);
void display_brake_bias(int bias);
