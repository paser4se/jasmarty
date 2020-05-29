/*
   Wipf           12.06.2016 Init
                  14.07.2016
                  07.11.2016
                  20.01.2017 V4 Laufzeitverbesserungeng
                  23.01.2017 int zu byte
                  27.01.2017 Tasterverhalten optimiert
                  09.02.2017
               15/18.02.2017 V5 Funktionsaufteilung - Taster gehen nun nicht nur bei der Bildschirmaktualisierung
                  07.06.2017 Kleine Aenderungen - weniger Speicher noetig
                  08.06.2017 Grosser Umbau auf Arduino Mini - negative Eingaenge
                  17.07.2017 Offlinemenue
                  22.10.2017 Bug gefunden bei þ zeichen
                  01.02.2019 LCD Display von 0X3F auf 0X27
                  30.06.2019 customCharLoad1
                  02.04.2020 ueberarbeiten
                  29.05.2020 Pro-Micro sonderfunktionen

        Pin 2 -> SDA
        Pin 3 -> SCL
        Pin 4 -> LED System
        Pin 5 -> LED Button

    Arduino Mini/Nano ->  Pro-Micro 
*/
#include <LiquidCrystal_I2C.h>
#include "HID-Project.h"

#define ADDRESS 0X3F
#define VERSION " v2.0"
#define LED_A 4
#define LED_B 5

byte pin;
byte nIn;
byte incoming;
byte rxbyte;
byte col;
byte row;
int wartespezial = 0;
int in_buttons[] = {6,7,8,9,10,16,14,15,18,19,20,21};

LiquidCrystal_I2C lcd(ADDRESS, 20, 4);

void setup() {
  pinMode(LED_A, OUTPUT);
  digitalWrite(LED_A, HIGH);
  pinMode(LED_B, OUTPUT);
  
  Serial.begin(9600);
  Consumer.begin();
  
  for (int nPin: in_buttons) {
    pinMode(nPin, INPUT_PULLUP);
  }

  lcd.begin();
  lcd.clear();
  lcd.backlight();

  byte customCharLoad0[] = {
    B10000,
    B10000,
    B10000,
    B10000,
    B10000,
    B10000,
    B11111,
    B10000
  };
  byte customCharLoad1[] = {
    B11100,
    B11100,
    B11100,
    B11100,
    B11100,
    B11100,
    B11111,
    B11100
  };
  // customChar 2 - 7 nicht genutzt

  lcd.createChar(0, customCharLoad0);
  lcd.createChar(1, customCharLoad1);

  lcd.setCursor(2, 0);
  lcd.print("Smartie by Wipf");
  lcd.setCursor(0, 3);
  lcd.print("LEO");
  lcd.print(" Tasten ");
  lcd.setCursor(15, 3);
  lcd.print(VERSION);
  digitalWrite(LED_A, LOW);
}

byte serial_getch() {
  while (Serial.available() == 0) {
    taster();
  }
  incoming = Serial.read(); // read the incoming byte:
  return (byte)(incoming & 0xff);
}

void loop() {
  //Auswertung
  {
    rxbyte = serial_getch();
    if (rxbyte == 254) //Matrix Orbital uses 254 prefix for commands
    {
      switch (serial_getch())
      {
        case 40:
          Consumer.write(MEDIA_VOL_UP);
          break;
        case 41:
          Consumer.write(MEDIA_VOL_DOWN);
          break;
        case 42:
          Consumer.write(MEDIA_VOL_MUTE);
          break;
        case 66: //backlight on (at previously set brightness)
          digitalWrite(LED_B, HIGH);
          break;
        case 70:
          digitalWrite(LED_B, LOW);
          break;
        case 71:                      //set cursor position
          col = (serial_getch() - 1); //get column byte
          row = (serial_getch());
          lcd.setCursor(col, row - 1);
          break;
        case 72: //cursor home (reset display position)
          lcd.setCursor(0, 0);
          break;
        //      case 86: //BEENDEN Des Programmes
        //        lcd.clear();
        //        lcd.setCursor(0, 0);
        //        lcd.print("Smartie wurde Beendet");
        //        delay(5000);
        case 88: //clear display, cursor home
          lcd.clear();
          lcd.setCursor(0, 0);
          break;
      }
      return;
    }

    switch (rxbyte) { //Zeichen anpassen
      case 0x02:
        //rxbyte = 0xC6; //  1/3  Block Altanativ: 0xA4
        rxbyte = 0;
        break;
      case 0x03:
        rxbyte = 1;
        //rxbyte = 0xDB; // 2/3 Block
        break;
      case 0x01:
        rxbyte = 0xFF; // 3/3 block
        break;
      /////////////LEERZEICHEN das alle Chars defined sind;
      case 0x04:
      case 0x05:
      case 0x06:
      case 0x07:
      case 0x08:
        //   rxbyte = NULL;
        rxbyte = 0x20;
        break;
      //////////Ende Leerzeichen

      case 0xE4: //ASCII "a" umlaut
        rxbyte = 0xE1;
        break;
      case 0xF1: //ASCII "n" tilde
        rxbyte = 0xEE;
        break;
      case 0xF6: //ASCII "o" umlaut
        rxbyte = 0xEF;
        break;
      case 0xFC: //ASCII "u" umlaut
        rxbyte = 0xF5;
        break;
      case 0xA3: //sterling (pounds)--liere
        rxbyte = 0xED;
        break;
      case 0xB0: //degrees symbol
        rxbyte = 0xDF;
        break;
      case 0xB5: //mu
        rxbyte = 0xE4;
        break;
      case 0xC4: //"A" Umlaut gross
        rxbyte = 0xE1;
        break;
      case 0xC0: //"A" variants
      case 0xC1:
      case 0xC2:
      case 0xC3:
      case 0xC5:
        rxbyte = 0x41;
        break;
      case 0xC8: //"E" variants
      case 0xC9:
      case 0xCA:
      case 0xCB:
        rxbyte = 0x45;
        break;
      case 0xCC: //"I" variants
      case 0xCD:
      case 0xCE:
      case 0xCF:
        rxbyte = 0x49;
        break;
      case 0xD1: //"N" tilde -> plain "N"
        rxbyte = 0x43;
        break;
      case 0xD6: //"O" Umlaut gross
        rxbyte = 0xEF;
        break;
      case 0xD2: //"O" variants
      case 0xD3:
      case 0xD4:
      case 0xD5:
      case 0xD8:
        rxbyte = 0x4F;
        break;
      case 0xDC: //"U" Umlaut gross
        rxbyte = 0xF5;
        break;
      case 0xD9: //"U" variants
      case 0xDA:
      case 0xDB:
        rxbyte = 0x55;
        break;
      case 0xDD: //"Y" acute -> "Y"
        rxbyte = 0x59;
        break;
      /*    case 0xDF: //beta  //mucks up LCDSmartie's degree symbol??
              rxbyte = 0xE2;
              break;
      */
      case 0xE0: //"a" variants except umlaut
      case 0xE1:
      case 0xE2:
      case 0xE3:
      case 0xE5:
        rxbyte = 0x61;
        break;
      case 0xE7: //"c" cedilla -> "c"
        rxbyte = 0x63;
        break;
      case 0xE8: //"e" variants
      case 0xE9:
      case 0xEA:
      case 0xEB:
        rxbyte = 0x65;
        break;
      case 0xEC: //"i" variants
      case 0xED:
      case 0xEE:
      case 0xEF:
        rxbyte = 0x69;
        break;
      case 0xF2: //"o" variants except umlaut
      case 0xF3:
      case 0xF4:
      case 0xF5:
      case 0xF8:
        rxbyte = 0x6F;
        break;
      case 0xF7: //division symbol
        rxbyte = 0xFD;
        break;
      case 0xF9: //"u" variants except umlaut
      case 0xFA:
      case 0xFB:
        rxbyte = 0x75;
        break;
      default:
        // FEHLERHAFTE AUSGABEN !
        break;
    }
    lcd.write(rxbyte); //otherwise a plain char so we print it to lcd
    // lcd.print(rxbyte);
  }
}

void taster(void) {
    for (int nPin: in_buttons) {
      nIn = digitalRead(nPin);
      if (nIn == LOW) {
        digitalWrite(LED_A, HIGH);
        Serial.write(nPin + 47); //pin 2 == 50 == ASCII "2"
        Serial.flush();
        digitalWrite(LED_A, LOW);
        break;
      }
    }
    //  Serial.flush();
}