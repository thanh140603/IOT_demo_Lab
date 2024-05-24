import sys
from Adafruit_IO import MQTTClient
import time
import random
#from simple_ai import *
from uart import *

AIO_FEED_IDs = ["nutnhan1", "nutnhan2"]
AIO_USERNAME = "YouGotMeLove"
AIO_KEY = "aio_ytKR98G3xDJnGCcUPOnVzWi3YK3k"

def connected(client):
    print("Ket noi thanh cong ...")
    for topic in AIO_FEED_IDs:
        client.subscribe(topic)
        print("Subscribed to: " + topic)

def subscribe(client , userdata , mid , granted_qos):
    print("Subscribe thanh cong ...")

def disconnected(client):
    print("Ngat ket noi ...")
    sys.exit (1)

def message(client , feed_id , payload):
    print("Nhan du lieu: " + payload + " , feed id:" + feed_id)

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()
counter = 5
counter_ai =5
sensor_type =0
while True:
    # counter = counter - 1
    # if counter <= 0:
    #     counter = 5
    #     print("Random data is publishing....")
    #     if sensor_type == 0:

    #         temp = random.randint(10,50)
    #         print("temp: " + str(temp))
    #         client.publish("cambien1", temp)
    #         sensor_type = 1
    #     elif sensor_type == 1:
    #         light = random.randint(100, 300)
    #         print("light..." + str(light))
    #         client.publish("cambien2", light)
    #         sensor_type = 2
    #     elif sensor_type == 2:
    #         hmi = random.randint(10, 90)
    #         print("hmi..." + str(hmi))

    #         client.publish("cambien3", hmi)
    #         sensor_type = 0

    counter_ai = counter_ai -1
    if counter_ai <= 0:
        counter_ai = 5
        #ai_result = image_detector()
        #print("AI output: ", ai_result)
        #client.publish("ai", ai_result)

    readSerial(client)
    time.sleep(1)
    pass