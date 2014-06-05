#include <Servo.h>
#include <AccelStepper.h>

Servo myservo01,myservo02,myservo03,myservo04;
int servoPin01 = 10;
int servoPin02 = 9;
int servoPin03 = 8;

int directionPin01 = 2;
int directionPin02 = 4;
int directionPinTable = 0;

int stepPin01 = 3;
int stepPin02 = 5;
int stepPinTable = 1;


AccelStepper stepper01(1, stepPin01, directionPin01);
AccelStepper stepper02(1, stepPin02, directionPin02);
AccelStepper rotatingTable(1, stepPinTable, directionPinTable);


void setup() {
  Serial.begin(19200);
  myservo01.attach(servoPin01);
  myservo02.attach(servoPin02);
  myservo03.attach(servoPin03);
  myservo01.write(92);
  myservo02.write(97 + 35);
  myservo03.write(65 + 24);
  
  
  stepper01.setAcceleration(20000);
  stepper02.setAcceleration(20000);
  rotatingTable.setAcceleration(20000);
}

void loop() {
  static int v = 0;

  if (Serial.available()) {

    char ch = Serial.read();
    switch(ch){

    case '0'...'9':
      v = v * 10 + ch - '0';
      break;  

    case 'a': 
      myservo01.write(v);
      v = 0;
      break;

    case 'b':
      myservo02.write(v);
      v = 0;
      break;   

    case 'c':
      myservo03.write(v);
      v = 0;
      break;

    case 'd':
      if(v < 12000){
        stepper01.moveTo(-50000000); 
        stepper01.setMaxSpeed(abs(v-12000));
      }
      else if (v >12000){
        stepper01.moveTo(50000000);
        stepper01.setMaxSpeed(abs(v-12000));
      }
      else {
        stepper01.move(0);
        stepper01.setMaxSpeed(0);
      }
      v = 0;
      break;

    case 'e':
      if(v < 12000){
        stepper02.moveTo(-50000000); 
        stepper02.setMaxSpeed(abs(v-12000));
      }
      else if (v >12000){
        stepper02.moveTo(50000000);
        stepper02.setMaxSpeed(abs(v-12000));
      }
      else {
        stepper02.move(0);
        stepper02.setMaxSpeed(0);
      }
      v = 0;
      break;
      
      case 't':
      if(v < 12000){
        rotatingTable.moveTo(-50000000); 
        rotatingTable.setMaxSpeed(abs(v-12000));
      }
      else if (v >12000){
        rotatingTable.moveTo(50000000);
        rotatingTable.setMaxSpeed(abs(v-12000));
      }
      else {
        rotatingTable.move(0);
        rotatingTable.setMaxSpeed(0);
      }
      v = 0;
      break;
    }  
  }

  Serial.flush();

  stepper01.run();
  stepper02.run();
  rotatingTable.run();
}

