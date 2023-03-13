#include <Arduino.h>
#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>
#include <Adafruit_MPU6050.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_LSM6DS33.h>
#include <Wire.h>

/*************************
 * BLE : based on 
 * // https://randomnerdtutorials.com/esp32-ble-server-client/#ESP32-BLE-Server
 * 
 * Limits : max 4 clients per server, 3 clients by default
 * To add more clients, a change in the sdk must be made before compiling on a new computer. Follow the link below.
 * https://arduino.stackexchange.com/questions/73288/connect-more-than-4-ble-clients-to-esp32-ble-server
 * 
 * Helpful links :
 * uuid generator : https://www.uuidgenerator.net/
 * ***********************/

//General defines
#define ONBOARD_LED 2
#define BLE_SERVER_NAME "ESP32 BLE SERVER"

// BLE Services
#define SERVICE_UUID "s1afc201-1fb5-459e-8fcc-c5c9c331914b"

// BLE Characteristic
#define CHARAC1_UUID "c1a1d466-344c-4be3-ab3f-189f80dd7518"
#define CHARAC2_UUID "c2a1d466-344c-4be3-ab3f-189f80dd7518"

BLECharacteristic charac_1(CHARAC1_UUID, BLECharacteristic::PROPERTY_NOTIFY | BLECharacteristic::PROPERTY_READ);
BLECharacteristic charac_2(CHARAC2_UUID, BLECharacteristic::PROPERTY_NOTIFY | BLECharacteristic::PROPERTY_READ);

BLEDescriptor descrip_1(BLEUUID((uint16_t)0x2902));
BLEDescriptor descrip_2(BLEUUID((uint16_t)0x2902));

//Variables
int devicesConnected = 0;
bool startAdvertize = true;
BLEServer *pServer;
uint8_t distance = 0;

Adafruit_MPU6050 mpu;
Adafruit_LSM6DS33 sensor;
const int ledPin = 13;
const int BUTTON_PIN = 2;

class Monitor: public BLEServerCallbacks
{
public:
	static int16_t connection_id;

	/* dBm to distance parameters; How to update distance_factor 1.place the
	 * phone at a known distance (2m, 3m, 5m, 10m) 2.average about 10 RSSI
	 * values for each of these distances, Set distance_factor so that the
	 * calculated distance approaches the actual distances, e.g. at 5m. */
	static constexpr float reference_power  = -50; //rssi reffrence 
	static constexpr float distance_factor = 3.5; 
 
	uint8_t get_value() { return value++; }
	esp_err_t get_rssi() { return esp_ble_gap_read_rssi(remote_addr); }
 
	static float get_distance(const int8_t rssi)
	{ return pow(10, (reference_power - rssi)/(10*distance_factor)) * 10; }
 
private:
  void onConnect(BLEServer* pServer, esp_ble_gatts_cb_param_t *param) 
  {
    // Update connection variables
    connection_id = param->connect.conn_id;
    memcpy(&remote_addr, param->connect.remote_bda, sizeof(remote_addr));

    // Install the RSSI callback
    BLEDevice::setCustomGapHandler(&Monitor::rssi_event);

    devicesConnected++;
    if (devicesConnected < 4)
    {
      startAdvertize = true;
    }
  }
 
  void onDisconnect(BLEServer* pServer)
  {
		Serial.printf("Connection #%i closed\n", connection_id);
		BLEDevice::setCustomGapHandler(nullptr);
		connection_id = -1;
    devicesConnected--;
    startAdvertize = true;
    if (devicesConnected < 0)
    {
      devicesConnected = 0;
    }
  }
 
  static void rssi_event(esp_gap_ble_cb_event_t event, esp_ble_gap_cb_param_t *param);
 
	static esp_bd_addr_t remote_addr;
	uint8_t value = 0;
};
 
int16_t Monitor::connection_id = -1;
esp_bd_addr_t Monitor::remote_addr = {};
 
void Monitor::rssi_event(esp_gap_ble_cb_event_t event, esp_ble_gap_cb_param_t *param)
{
  static int16_t rssi_average = 0;
  if (event == ESP_GAP_BLE_READ_RSSI_COMPLETE_EVT)
  {
    // Adjust damping_factor to lower values to have a more reactive response
    const float damping_factor = 0.8;
    rssi_average = rssi_average * damping_factor + param->read_rssi_cmpl.rssi * (1 - damping_factor);
    distance = get_distance(rssi_average);
  }
}
 
Monitor monitor;
	
void setup()
{
  //Create server
  BLEDevice::init(BLE_SERVER_NAME);
  pServer = BLEDevice::createServer();
  pServer->setCallbacks(&monitor);

  // Create the BLE Services
  BLEService *service_1 = pServer->createService(SERVICE_UUID);

  service_1->addCharacteristic(&charac_1);
  service_1->addCharacteristic(&charac_2);

  descrip_1.setValue("charac 1");
  charac_1.addDescriptor(&descrip_1);
  descrip_2.setValue("charac 2");
  charac_2.addDescriptor(&descrip_2);

  service_1->start();

  // Start advertising services
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);

  ///Centrale Inertielle:
  Serial.begin(9600);
  while (!Serial)
    delay(10); // will pause Zero, Leonardo, etc until serial console opens

  Serial.println("Adafruit MPU6050 test!");
  pinMode(BUTTON_PIN, INPUT_PULLUP);
  pinMode(ledPin, OUTPUT);

  if (!mpu.begin()) {
    Serial.println("Failed to find MPU6050 chip");
    while (1) {
      delay(10);
    }
  }
  Serial.println("MPU6050 Found!");
  
  mpu.setAccelerometerRange(MPU6050_RANGE_8_G);
  mpu.setGyroRange(MPU6050_RANGE_500_DEG);
  mpu.setFilterBandwidth(MPU6050_BAND_21_HZ);

}

unsigned long time1 = 0;
unsigned long time2 = 0;
static uint8_t choc = 0;
ulong live = 0;

void loop()
{
	static const uint32_t REFRESH_DELAY = 100;
	static uint32_t next_detection;

  if (startAdvertize)
  {
    pServer->getAdvertising()->start();
    startAdvertize = false;
  }

  if (devicesConnected > 0)
  {
    //charac 1 blinks at 0.25sec
    if ((millis() - time1) > 1000) {
      //mode1 = !mode1; 
      charac_1.setValue(&choc, 1);
      charac_1.notify();
      time1 = millis();
    }
    if (millis() - next_detection >= REFRESH_DELAY)
		{
			// Prepare for the next detection
			next_detection += REFRESH_DELAY;

			// Update the internal value (what for?)
			auto value = monitor.get_value();
			//Serial.printf("*** NOTIFY: %d ***\n", value);
			charac_2.setValue(&distance, sizeof(distance));
			charac_2.notify();
	 
			// Request RSSI from the remote address
			if (monitor.get_rssi() != ESP_OK)
				Serial.println("RSSI request failed");
		}

  }

  //// Centrale Inertielle:
  sensors_event_t a, g, temp;
  mpu.getEvent(&a, &g, &temp);

  /* Print out the values */
  // Serial.print("Acceleration X: ");
  // Serial.print(a.acceleration.x);
  // Serial.print(", Y: ");
  // Serial.print(a.acceleration.y);
  // Serial.print(", Z: ");
  // Serial.print(a.acceleration.z);
  // Serial.println(" m/s^2");

  if (a.acceleration.x > 24 || a.acceleration.y > 24 || a.acceleration.z > 50 ) {
    //Serial.println("too much G's");
    digitalWrite(ledPin, HIGH);
    choc = 1;
    live = millis();
  }

  if (millis() - live >= 3000) {  
    digitalWrite(ledPin, LOW);
    choc = 0;
  }

  //Serial.println(digitalRead(BUTTON_PIN));
  delay(10);


}



