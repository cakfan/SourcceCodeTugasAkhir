#include <ESP8266WiFi.h>
#include <Time.h>
#include <FirebaseArduino.h>
#include "MFRC522.h"

#define FIREBASE_HOST "absensi-a3df8.firebaseio.com"
#define FIREBASE_AUTH "BwYOAy3krYttDE2xCzPljobtQlCWk1qoeQpKZUHd"

int timezone = 7;
int dst = 0;

const char* ssid     = "TFHM";
const char* password = "12345678T";
const int BUZZER_PIN = D8;

//const int buzzer = 5;
#define RST_PIN D4
#define SS_PIN  D2
MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance
int getHari,getBulan,getTanggal,getWaktu,getTahun,getX;

boolean reconnect() {
  digitalWrite(D0, HIGH);
  WiFi.begin(ssid, password);
  int retry = 51;
  while (WiFi.status() != WL_CONNECTED) {
    if (retry > 50) {
      Serial.println("");
      Serial.printf("Trying connect to %s", ssid);
      retry = 0;
    }
    delay(500);
    Serial.print(".");
    retry++;
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  digitalWrite(D0, LOW);
  configTime(timezone * 3600, 0, "pool.ntp.org", "time.nist.gov");
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void setbaca(){
  Firebase.setInt("baca_rfid",1);
  if (Firebase.failed()) {
        Serial.print("setting /message failed:");
        Serial.println(Firebase.error());  
        return;
   }
}
void jumlahHadir(int n){
  int jml = Firebase.getInt("jumlah");
  Firebase.setInt("jumlah",jml+n);
}
void cekstatusNya(String a, String b, String c){
  int cek = Firebase.getInt("rfid_list/"+a+"/status");
  if(cek == 0){
    statusNya(a,1);
    jumlahHadir(1);
    absenMasuk(a,b,c);
  }
  if(cek == 1){
    statusNya(a,0);
    jumlahHadir(-1);
    absenPulang(a,b,c);
  }
  
}
void statusNya(String cardid, int statusnya){
  Firebase.setInt("rfid_list/"+cardid+"/status",statusnya);
  if (Firebase.failed()) {
        Serial.print("Gagal set status!!!");
        bunyiBeep(2,50000);
        Serial.println(Firebase.error());  
        return;
   }
}
void absenMasuk(String a, String b, String c){
  Firebase.setString("absensi/"+a+"/"+b, c);
  if (Firebase.failed()) {
        Serial.print("Gagal masuk!!!");
        bunyiBeep(2,50000);
        Serial.println(Firebase.error());  
        return;
   }
   bunyiBeep(1,30000);
}

void absenPulang(String a, String b, String c){
  Firebase.setString("pulang/"+a+"/"+b, c);
  if (Firebase.failed()) {
        Serial.print("Gagal pulang!!!");
        bunyiBeep(2,50000);
        Serial.println(Firebase.error());  
        return;
  }
  bunyiBeep(1,20000);
}

String sentFirebase(unsigned long cardUID) {
  String aa = String(cardUID);
  Firebase.setString("notifikasi",aa);
  time_t now = time(nullptr);
  Serial.println(ctime(&now));
  String tm = ctime(&now);
  getHari = tm.indexOf(' ');
  getBulan = tm.indexOf(' ', getHari + 1);
  getTanggal = tm.indexOf(' ',getBulan + 1);
  getWaktu = tm.indexOf(' ', getTanggal + 1);
  getTahun = tm.indexOf(' ', getWaktu + 1);
  getX = tm.indexOf(' ', getTahun+1);
  String hari = tm.substring(0,getHari);
  String bulan = tm.substring(getHari+1,getBulan);
  String bb = tm.substring(getBulan+1,getTanggal);
  String tanggal = tm.substring(getTanggal+1,getWaktu);
  String waktu = String(tm.substring(getWaktu+1,getTahun));
  String tahun = String(tm.substring(getTahun+1));
  String hr;
  if(hari == "Sat"){hr="Sabtu";}
  else if(hari == "Sun"){hr="Minggu";}
  else if(hari == "Mon"){hr="Senin";}
  else if(hari == "Tue"){hr="Selasa";}
  else if(hari == "Wed"){hr="Rabu";}
  else if(hari == "Thu"){hr="Kamis";}
  else {hr="Jumat";}
  Serial.println("Hari: "+String(getHari)+" = "+ hr+"\nTanggal: "+String(getTanggal)+" = "+tanggal+"\nJam: "+String(getWaktu)+" = "+waktu+"\nBulan: "+String(getBulan)+" = "+bulan+"\nTahun: "+String(getTahun)+" = "+tahun);
  cekstatusNya(aa,hr,tm);
  setbaca();
  delay(1000);
}

void setup() {
  pinMode(D0, OUTPUT);
  pinMode(D1, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);
  Serial.begin(115200);
  Serial.println("");
  delay(1000);
  reconnect();
  SPI.begin();        // Init SPI bus
  mfrc522.PCD_Init(); // Init MFRC522 card
  mfrc522.PCD_SetAntennaGain(mfrc522.RxGain_max);
}

unsigned long getCardUID() {
  if ( !mfrc522.PICC_ReadCardSerial()) {
    return -1;
  }
  unsigned long hex_num;
  hex_num =  mfrc522.uid.uidByte[0] << 24;
  hex_num += mfrc522.uid.uidByte[1] << 16;
  hex_num += mfrc522.uid.uidByte[2] <<  8;
  hex_num += mfrc522.uid.uidByte[3];
  mfrc522.PICC_HaltA(); // Stop reading
  return hex_num;
}
int wait = 51;
void loop() {
  if (WiFi.status() != WL_CONNECTED)
    reconnect();
  if (wait > 50) {
    Firebase.setInt("baca_rfid",0);
    Firebase.setString("notifikasi","");
    Serial.println("");
    Serial.print("Wait for new Card");
    wait = 0;
  }
  Serial.print(".");
  wait++;
  if (wait % 2 == 0)
    digitalWrite(D0, HIGH);
  else
    digitalWrite(D0, LOW);
  // Look for new cards
  if ( ! mfrc522.PICC_IsNewCardPresent()) {
    delay(100);
    return;
  }
  unsigned long cardUID = getCardUID();
  for (int i = 0; i < 3; i++) {
    digitalWrite(D0, HIGH);
    delay(100);
    digitalWrite(D0, LOW);
    delay(100);
  }
  if (cardUID == -1) {
    Serial.println("Failed to get UID");
    return;
  }
  Serial.printf("\nCard UID is %u\n", cardUID);
  for (int i = 0; i < 3; i++) {
    digitalWrite(D1, HIGH);
    delay(250);
    digitalWrite(D1, LOW);
    delay(250);
  }
  sentFirebase(cardUID);
  wait = 51;
}
void bunyiBeep(int n, int t) {
for (int i = 0; i < n; i++) {
 tone(BUZZER_PIN,t);
 delay(100);
 noTone(BUZZER_PIN);
 delay(100);
}
}
