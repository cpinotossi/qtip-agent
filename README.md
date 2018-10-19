# Qtip-Agent
Qtip Agent is a Framwork which will help to execut Performance Test based on MQTT.


## Status
Work in progess. The current release does only work in combination with the Akamai MQTT Service "IoT Edge Connect".

## Team
* Anthony Hogg (Enterprise Architect @ Akamai)
* Guillaume Moissaing (Enterprise Architect @ Akamai)
* Alexandre Darcherif (Solutions Engineer @ Akamai)
* Philon Terving (Engagment Manager @ Akamai)
* Christian Pino Tossi (Web Architect @ Akamai)

## CLI
### mqtt

```
$ java -jar qtipcli.jar mqtt                                                                                                                                                           ⏎
Was passed main parameter 'mqtt' but no main parameter was defined in your arg class
Usage: <main class> [options]
  Options:
  * --authgroup, -a
      API Client Name inside the edgerc. Default: api-client
  * --clientid, -c
      API Client Name inside the edgerc. Default: api-client
    --help

  * --key, -k
      Path to the key file
    --message, -m
      Hostname (Property) referenced inside an akamai configuration.
    --publish, -p
      By default we will subscribe, if you like to publish change the value to
      true
      Default: false
  * --topic, -t
      MQTT Topic
```
#### Subscribe
```
$ java -jar qtipcli.jar mqtt -c client1 -a "measures:sub" -t "catchpoint/#" -k ./private/qtip.a2s.ninja.key -d qtip-eu.a2s.ninja
START subscribe
client1
measures:sub
catchpoint/#
ssl://qtip-eu.a2s.ninja:8883
connect
MESSAGE FROM catchpoint/test: Example payload with this qos: 0
MESSAGE FROM catchpoint/test: [1539962313788]:huhu with this qos: 1
MESSAGE FROM catchpoint/test: [1539962313788]:huhu with this qos: 1
MESSAGE FROM catchpoint/test: [1539962313788]:huhu with this qos: 1
MESSAGE FROM catchpoint/test: [1539962313788]:huhu with this qos: 1
MESSAGE FROM catchpoint/test: [1539962313788]:huhu with this qos: 1
MESSAGE FROM catchpoint/test: [1539962313788]:huhu with this qos: 1
MESSAGE FROM catchpoint/test: [1539962313788]:huhu with this qos: 1
MESSAGE FROM catchpoint/test: [1539962313788]:huhu with this qos: 1
MESSAGE FROM catchpoint/test: [1539962313788]:huhu with this qos: 1
```
#### Publish
```
$ java -jar qtipcli.jar mqtt -c client2 -a "measures:pub" -t "catchpoint/test" -p -m "huhu" -k ./private/qtip.a2s.ninja.key -r 10 -d qtip-eu.a2s.ninja
START publish
client2
measures:pub
catchpoint/test
ssl://qtip-eu.a2s.ninja:8883
connect
Repeat#0 msg:[1539962313788]:huhu
Repeat#1 msg:[1539962313788]:huhu
Repeat#2 msg:[1539962313788]:huhu
Repeat#3 msg:[1539962313788]:huhu
Repeat#4 msg:[1539962313788]:huhu
Repeat#5 msg:[1539962313788]:huhu
Repeat#6 msg:[1539962313788]:huhu
Repeat#7 msg:[1539962313788]:huhu
Repeat#8 msg:[1539962313788]:huhu
Repeat#9 msg:[1539962313788]:huhu
```
