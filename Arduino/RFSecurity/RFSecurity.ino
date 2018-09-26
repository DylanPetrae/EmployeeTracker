 #include <SoftwareSerial.h>

#define rxPin 4                           // define SoftwareSerial rx data pin  
#define txPin 2                           // define SoftwareSerial tx data pin  
#define TONE_PIN 6                        // Piezo buzzer is on Pin 11
#define ledPin 7                          // Green LED light that indicates Security Mode is on Pin 7
#define ledTriggerPin 13                  // Blue LED indicating that the RF sensor has been triggered
#define rfSensorPin 8                     // RF Proximity Sensor is on Pin 8

SoftwareSerial blueTooth(rxPin, txPin);   // create instance of SoftwareSerial

int ledState = 0;                         // Start LED OFF
int securityState = 0;                    // Start Security Mode OFF
int securityTripped = 0;                  // Holds value of whether an object has triggered the security

unsigned long startMillis;
unsigned long currentMillis;
const unsigned long period = 10000;

void setup(){
  pinMode(ledPin, OUTPUT);                // LED pin (8) for Security Mode status indicator is OUTPUT
  digitalWrite(ledPin, LOW);              // Start Security Mode status LED as OFF
  pinMode(TONE_PIN, OUTPUT);              // Piezo Buzzer pin (11) is OUTPUT

  pinMode(rfSensorPin, INPUT);
  pinMode(ledTriggerPin, OUTPUT);

  Serial.begin(9600);                     // Begin Hardware Serial
  blueTooth.begin(9600);                  // Begin SoftwareSerial

  startMillis = millis();
}

void loop(){
  char c;                               // This char will hold value of the reading FROM the Arduino
  char setState;                        // This char will hold value of the reading FROM the Mobile Device

  // Reading FROM Phone
  if ( blueTooth.available() > 0){      // If there is data on the serial line
    setState = blueTooth.read();        // Store value FROM the MOBILE DEVICE in the char setState
    //Serial.write(setState);             // Write setState to the Serial Monitor

    if (setState == '0') {              // If a command of 0 was sent from the mobile device, turn OFF Security Mode
      digitalWrite(ledPin, LOW);        // LED indicator for Security Mode reset to OFF
      securityState = 0;                // State variable for Security Mode reset to 0
    }
    if (setState == '1') {              // If a command of 1 was sent from the mobile device, turn ON Security Mode
      digitalWrite(ledPin, HIGH);       // LED indicator for Security Mode set to ON
      securityState = 1;                // State variable for Security Mode set to 1
    }
  }

  while (securityState == 1) {         // While the Security Mode state variable is set to 1:

    if ( blueTooth.available() > 0){      // If a command appears on the serial line:
      setState = blueTooth.read();        // Read the command and store it in the setState variable
      //Serial.write(setState);             // Write setState to Serial monitor

      if (setState == '0') {              // If the command was 0, turn OFF Security Mode and exit the while loop:
        digitalWrite(ledPin, LOW);        // LED indicator for Security Mode reset to 0
        digitalWrite(ledTriggerPin, LOW); // LED indicator for RF Sensor being triggered reset to 0
        securityState = 0;                // State variable for Security Mode set to 0
      }
    }
    
    int sensorValue = digitalRead(rfSensorPin);
    if(sensorValue == HIGH){
      currentMillis = millis();
      digitalWrite(ledTriggerPin, HIGH);
      securityTripped = 1;
      if(currentMillis - startMillis >= period){
      blueTooth.print("2");
      Serial.write("2");
      startMillis = currentMillis;
      }
//      if(Serial.available() > 0){
//        int bytes = Serial.available();
//        blueTooth.print((char)Serial.read());
//      }
    }
    else{
      digitalWrite(ledTriggerPin, LOW);
      securityTripped = 0;
    }
   
  }
  digitalWrite(ledTriggerPin, LOW);
}


