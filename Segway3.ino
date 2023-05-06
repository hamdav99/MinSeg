#include <Adafruit_MPU6050.h>

#define ENCODER_PIN_A 2
#define ENCODER_PIN_B 3
#define OUTPUT_PIN_FORWARD 4
#define OUTPUT_PIN_BACKWARD 5
#define PULSE_PER_REV 720
#define h 20

volatile int encoderPos=0;
volatile unsigned long lastPulseTime=0;
float angularVelocity = 0;

Adafruit_MPU6050 mpu;

double x1,x2,x3,x4,y,y1,y2,y3,y4,r,r1,r2,r3,precalc1,precalc2,precalc3,precalc4;
  int u;
  int AB, ABnew,count,varannan;
  
double hat11 = -3.408;
double hat12 = -36.9924;
double hat13 = -0.2911;
double hat14 = -0.5637;
double hat21 = -0.062;
double hat22 = 0.3069;
double hat23 = -0.0054;
double hat24 = -0.0105;
double hat31 = 53.4426;
double hat32 = 455.3243;
double hat33 = 4.5198;
double hat34 = 6.8184;
double hat41 = 0.9992;
double hat42 = 8.5119;
double hat43 = 0.0858;
double hat44 = 1.1275;
 
double r11 = 0;
double r21 = 0;
double r31 = 0;
double r41 = 0;
 
double L11 = 0.004;
double L21 = -0.7;
double L31 = 0.0003;
double L41 = 0;
 
double LC11 = 0;
double LC12 = 0.004;
double LC13 = 0;
double LC14 = 0;
double LC21 = 0;
double LC22 = -0.7;
double LC23 = 0;
double LC24 = 0;
double LC31 = 0;
double LC32 = 0.0003;
double LC33 = 0;
double LC34 = 0;
double LC41 = 0;
double LC42 = 0;
double LC43 = 0;
double LC44 = 0;
 
double kr11 = 0;
 
//double K11 = -22.7075;
//double K12 = -193.7914;
double K11 = -20;
double K12 = -180;
double K13 = 0.0;
double K14 = 0.0;
//double K13 = -1.9205;
//double K14 = -2.8995;

void setup() {
  // put your setup code here, to run once:
  //Deklarera alla parametrar
  Serial.begin(9600);
  pinMode(ENCODER_PIN_A,INPUT_PULLUP);
  pinMode(ENCODER_PIN_B,INPUT_PULLUP);

  attachInterrupt(digitalPinToInterrupt(ENCODER_PIN_A), hast, CHANGE);
  attachInterrupt(digitalPinToInterrupt(ENCODER_PIN_B), hast, CHANGE);
  
  mpu.setGyroRange(MPU6050_RANGE_500_DEG);
  mpu.setAccelerometerRange(MPU6050_RANGE_4_G);
  mpu.setFilterBandwidth(MPU6050_BAND_21_HZ);
  
  lastPulseTime = micros();

  AB=encoder();

  r=0; //Refernce value
  
  mpu.begin();
  delay(1000); //stabilaze
}

int encoder(){
  int a = digitalRead(ENCODER_PIN_A);
  int b = digitalRead(ENCODER_PIN_B);

  if(a==0 & b==0){
    return 0;
  } else if (a==0 & b==1){
    return 1;
  } else if( a==1 & b==0){
    return 2;
  } else {
    return 3;
  }
}

float hast(){

  ABnew=encoder();
  int temp = encoderPos;
  
  switch(ABnew){
    case 0: if(AB==2) encoderPos++; else encoderPos--; break;
    case 1: if(AB==0) encoderPos++; else encoderPos--; break;
    case 2: if(AB==3) encoderPos++; else encoderPos--; break;
    case 3: if(AB==1) encoderPos++; else encoderPos--; break;
  }

   AB=ABnew;
   
   if(encoderPos == temp+1){
      count++;
   }else{
      count--;
   }
  
}

double limit(double value, int uppLim, int lowLim){
  if(value>uppLim){
    value=uppLim;
  } else if(value<lowLim){
    value=lowLim;
  }
  return value;
}

void loop() {

long time = millis(); //Loopen verkar bara köra var 80ms trots h=10ms
//Serial.println(time);

float anglespeed = ((count * 2 * 3.142*1000)/h)/PULSE_PER_REV;
  count=0;

//Nya värden
sensors_event_t accel, gyro; 
mpu.getAccelerometerSensor()->getEvent(&accel);
mpu.getGyroSensor()->getEvent(&gyro);
float pitch = atan2(accel.acceleration.z, -accel.acceleration.y);//y2
float pitchspeed = gyro.gyro.z; //y1
float angle = 2 * 3.142 * encoderPos/PULSE_PER_REV; // mod360? Bryr vi oss om det är över 360 grader?
//y3

x1=pitchspeed;
x2=pitch;
x3=anglespeed;
x4=angle;

/*Serial.print("x1/pitchspeed: ");
Serial.println(pitchspeed);  
Serial.print("x2/pitch: ");
Serial.println(pitch);
Serial.print("x3/anglespeed: ");
Serial.println(anglespeed); 
Serial.print("x4/angle: ");
Serial.println(angle); */


//Beräkningar med nya värden
u =   -K11*x1 - K12*x2 - K13*x3 - K14*x4; //Ändra första siffran för vinkeloffset


/*Serial.print("u: ");
Serial.println(u); 
Serial.println();*/


//Output

if(u >= 0 && pitch < 0.53 && pitch > -0.53 ){ //om den är mer än 30 grader snnurrar inte hjulen
  u = 70 + 7*u; //Ändra faktorn med u för att motorn har olika gain framåt och bakåt
  u = limit(u, 255, -255);
  analogWrite(OUTPUT_PIN_FORWARD,0);
  analogWrite(OUTPUT_PIN_BACKWARD,u);
} else if (u < 0 && pitch < 0.53 && pitch > -0.53){
  u = 70 - 7*u; //OBS går att ändra golvet också
  u = limit(u, 255, -255);
  analogWrite(OUTPUT_PIN_FORWARD,u);
  analogWrite(OUTPUT_PIN_BACKWARD,0);
} else {
  analogWrite(OUTPUT_PIN_FORWARD,0);
  analogWrite(OUTPUT_PIN_BACKWARD,0);
}


//Waitunitl
time = time + h;
float duration = time - millis();
delay(duration);
}
