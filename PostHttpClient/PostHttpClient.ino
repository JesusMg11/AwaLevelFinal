/**
   PostHTTPClient.ino

    Created on: 21.11.2016

*/

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

/* this can be run with an emulated server on host:
        cd esp8266-core-root-dir
        cd tests/host
        make ../../libraries/ESP8266WebServer/examples/PostServer/PostServer
        bin/PostServer/PostServer
   then put your PC's IP address in SERVER_IP below, port 9080 (instead of default 80):
*/
//#define SERVER_IP "10.0.1.7:9080" // PC address with emulation on host
#define SERVER_IP "balamcode.000webhostapp.com"

const int TRIG = 5;
const int ECHO = 4;

int tiempo;
int distancia;

#ifndef STASSID
#define STASSID "64AD72" //direccion ssid de red LAN
#define STAPSK  "L21503812614433" //pass
#endif

void setup() {

   pinMode(TRIG,OUTPUT);
    pinMode(ECHO, INPUT);
    Serial.begin(9600);
    digitalWrite(TRIG,LOW);

  Serial.begin(115200);

  Serial.println();
  Serial.println();
  Serial.println();

  WiFi.begin(STASSID, STAPSK);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected! IP address: ");
  Serial.println(WiFi.localIP());

}

void loop() {
  // wait for WiFi connection
  if ((WiFi.status() == WL_CONNECTED)) {

    WiFiClient client;
    HTTPClient http;

    Serial.print("[HTTP] begin...\n");
    // configure traged server and url
    http.begin(client, "http://" SERVER_IP "/servicios-awa/sensor.php"); //HTTP
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");

    Serial.print("[HTTP] POST...\n");
    // start connection and send HTTP header and body
     digitalWrite(TRIG,HIGH);
    delayMicroseconds(100);
    digitalWrite(TRIG,LOW);
    tiempo = pulseIn(ECHO, HIGH);
    distancia = tiempo/58.4;
    Serial.println(distancia);
    String resul = (String) distancia;
    int httpCode = http.POST("porcentaje="+resul);

    // httpCode will be negative on error
    if (httpCode > 0) {
      // HTTP header has been send and Server response header has been handled
      Serial.printf("[HTTP] POST... code: %d\n", httpCode);

      // file found at server
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
        Serial.println("received payload:\n<<");
        Serial.println(payload);
        Serial.println(">>");
      }
    } else {
      Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
  }

  delay(10000);
}
