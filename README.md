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
### CLI Help
Call Help as follow:

```
$java -jar qtipcli.jar -h                                                                                                                                                             
Expected a command, got -h
Usage: <main class> [options] [command] [command options]
  Options:
    --help

  Commands:
    mqtt      Subscribe and Publish via MQTT Protocol
      Usage: mqtt [options]
        Options:
        * --authgroup, -a
            Authorization Group used in combination with Akamai IoT Edge
            Connect
        * --clientid, -c
            MQTT Client ID
        * --domain, -d
            Domain/Hostname of the mqtt server
        * --key, -k
            Path to the key file
          --message, -m
            Hostname (Property) referenced inside an akamai configuration.
          --publish, -p
            By default we will subscribe, if you like to publish change the
            value to true
            Default: false
          --repeat, -r
            Number of times to repeate the message
            Default: 1
        * --topic, -t
            MQTT Topic

    rns      Akamai IoT Edge Connect: Manage Akamai Reserved Namespace
      Usage: rns [options]
        Options:
          --detail, -d
            Whether to show connection details for the namespaces in the
            user’s account.
            Default: true
          --edgerc
            Full path to the credentials file. Default: <user.home>/.edgerc
            Default: ~/.edgerc
          --global, -g
            Whether to search for reserved namespaces in all accounts or in
            the user’s account only.
            Default: true
          --match, -m
            A search of string matching content. You can use the wildcard
            character * to search for unspecified content.
            Default: *
          --namespace, -n
            The name of a namespace.
          --page, -p
            The number of the page with the number of namespaces to list
            specified in size. Page numbers start at zero.
            Default: 50
          --request-body, -r
            JSON Configuration to be uploaded
          --section
            API Client Name inside the edgerc. Default: api-client
            Default: default
          --size, -s
            The number of namespaces per page.
            Default: 2
        * --type, -t
            Mode (list|reserve|list-all|remove)
            Default: list

    nscv      Akamai IoT Edge Connect: Manage Akamai Reserved Namespace
      Usage: nscv [options]
        Options:
          --activation-state, -a
            Specify activated as a keyword to activate the specified
            configuration version.
            Default: 1
          --edgerc
            Full path to the credentials file. Default: <user.home>/.edgerc
            Default: ~/.edgerc
          --jurisdiction, -j
            By default we will subscribe, if you like to publish change the
            value to true
            Default: <empty string>
          --namespace, -n
            This operation lists all namespaces reserved for the account of
            the user making the request, up to 100 namespaces on a page.
          --request-body, -r
            JSON Configuration to be uploaded
          --section
            API Client Name inside the edgerc. Default: api-client
            Default: default
        * --type, -t
            Mode (list|create|deactivate|activate|list-operations)
            Default: list
          --version, -v
            The configuration version that you want to activate.

    nsc      Akamai IoT Edge Connect: Manage Akamai Namespace
      Usage: nsc [options]
        Options:
          --edgerc
            Full path to the credentials file. Default: <user.home>/.edgerc
            Default: ~/.edgerc
          --jurisdiction, -j
            The name of a jurisdiction. The following options are available:na
            for North America, eu for Europe, jp for Japan, cn for China, sk
            for South Korea, br for Brazil, or xw for the rest of the world.
            Default: <empty string>
          --namespace, -n
            The name of a namespace.
          --request-body, -r
            JSON Configuration to be uploaded
          --section
            API Client Name inside the edgerc. Default: api-client
            Default: default
        * --type, -t
            Mode (list-all|create|get|update|delete)
            Default: list
```
            

### CLI mqtt

```
      Usage: mqtt [options]
        Options:
        * --authgroup, -a
            Authorization Group used in combination with Akamai IoT Edge
            Connect
        * --clientid, -c
            MQTT Client ID
        * --domain, -d
            Domain/Hostname of the mqtt server
        * --key, -k
            Path to the key file
          --message, -m
            Hostname (Property) referenced inside an akamai configuration.
          --publish, -p
            By default we will subscribe, if you like to publish change the
            value to true
            Default: false
          --repeat, -r
            Number of times to repeate the message
            Default: 1
        * --topic, -t
            MQTT Topic
```

#### CLI mqtt example subscribe
```
$ java -jar qtipcli.jar mqtt -c client1 -a "measures:sub" -t "catchpoint/test" -k ./private/qtip.a2s.ninja.key -d qtip-eu.a2s.ninja
START subscribe
client1
measures:sub
catchpoint/test
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
In case you would like to use MQTT Wildcard just use the "#" as follow:

```
$ java -jar qtipcli.jar mqtt -c client1 -a "measures:sub" -t "catchpoint/#" -k ./private/qtip.a2s.ninja.key -d qtip-eu.a2s.ninja
```


#### CLI mqtt example Publish
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

### CLI support of Akamai IoT Edge Connect
The Akamai IoT Edge Connect
IoT Edge Connect uses the Akamai Platform to provide communication and data processing for millions of connected devices. It is a scalable, reliable, and distributed real-time publish-subscribe mechanism that organizes data into logical categories called topics. [more](https://developer.akamai.com/api/web_performance/iot_edge_connect/v1.html#overview).

#### CLI mqtt example rns (Manage Akamai Reserved Namespace)

##### Example: List reserved namespace iec_hebe_io

```
java -jar qtipcli.jar rns --edgerc /Users/cpinotos/.edgerc  -t list-all -m iec_hebe_io                                                                                               ⏎
[
  {
    "namespace": "iec_hebe_io",
    "reserved": "2018-08-08T13:48:17Z",
    "connections": {
      "eu": [
        "iec.hebe.io",
        "eu.iec.hebe.io"
      ]
    }
  }
]
```

####CLI mqtt example nsc (Manage Akamai Reserved Namespace)
##### List all jurisdiction of Namespace Configuration "iec_hebe_io"
```
java -jar qtipcli.jar nsc --edgerc /Users/cpinotos/.edgerc -t list-all -n iec_hebe_io
[
  {
    "id": 824,
    "namespace": "iec_hebe_io",
    "jurisdiction": "eu",
    "storage": 200000,
    "topic-expiry": 7,
    "topics": [
      "topic1",
      "topic2/*",
      "topic3/**",
      "test/**",
      "result/**",
      "result_store"
    ],
    "acls": [
      {
        "path": "topic1",
        "publishers": [
          "pub"
        ],
        "subscribers": [
          "sub"
        ]
      },
      {
        "path": "topic2/*",
        "publishers": [
          "pub"
        ],
        "subscribers": [
          "sub"
        ]
      },
      {
        "path": "topic3/*",
        "publishers": [
          "pub"
        ],
        "subscribers": []
      },
      {
        "path": "test/*",
        "publishers": [
          "client-pub"
        ],
        "subscribers": []
      },
      {
        "path": "result/*",
        "publishers": [
          "agent-pub"
        ],
        "subscribers": []
      },
      {
        "path": "result_store",
        "publishers": [
          "collector-pub"
        ],
        "subscribers": [
          "collector-sub"
        ]
      }
    ]
  }
]
```

##### List jurisdiction "eu" of Namespace Configuration "iec_hebe_io"
```
java -jar qtipcli.jar nsc --edgerc /Users/cpinotos/.edgerc -t get -n iec_hebe_io -j eu
{
  "id": 824,
  "namespace": "iec_hebe_io",
  "jurisdiction": "eu",
  "storage": 200000,
  "topic-expiry": 7,
  "topics": [
    "topic1",
    "topic2/*",
    "topic3/**",
    "test/**",
    "result/**",
    "result_store"
  ],
  "acls": [
    {
      "path": "topic1",
      "publishers": [
        "pub"
      ],
      "subscribers": [
        "sub"
      ]
    },
    {
      "path": "topic2/*",
      "publishers": [
        "pub"
      ],
      "subscribers": [
        "sub"
      ]
    },
    {
      "path": "topic3/*",
      "publishers": [
        "pub"
      ],
      "subscribers": []
    },
    {
      "path": "test/*",
      "publishers": [
        "client-pub"
      ],
      "subscribers": []
    },
    {
      "path": "result/*",
      "publishers": [
        "agent-pub"
      ],
      "subscribers": []
    },
    {
      "path": "result_store",
      "publishers": [
        "collector-pub"
      ],
      "subscribers": [
        "collector-sub"
      ]
    }
  ]
}
```
##### List all Versions of jurisdiction "eu" of Namespace Configuration "iec_hebe_io"
```
java -jar qtipcli.jar nscv --edgerc /Users/cpinotos/.edgerc -t list -n iec_hebe_io -j eu
[
  {
    "createdBy": "cpinotos",
    "createdAt": 1533736288868,
    "namespace": "iec_hebe_io",
    "jurisdiction": "eu",
    "version": "1",
    "activationStatus": "DEPLOYED",
    "operationType": "ACTIVATION"
  },
  {
    "createdBy": "cpinotos",
    "createdAt": 1537784596309,
    "namespace": "iec_hebe_io",
    "jurisdiction": "eu",
    "version": "2",
    "activationStatus": "DEPLOYED",
    "operationType": "ACTIVATION"
  },
  {
    "createdBy": "cpinotos",
    "createdAt": 1537972425158,
    "namespace": "iec_hebe_io",
    "jurisdiction": "eu",
    "version": "3",
    "activationStatus": "DEPLOYED",
    "operationType": "ACTIVATION"
  },
  {
    "createdBy": "cpinotos",
    "createdAt": 1538040514667,
    "namespace": "iec_hebe_io",
    "jurisdiction": "eu",
    "version": "4",
    "activationStatus": "DEPLOYED",
    "operationType": "ACTIVATION"
  },
  {
    "createdBy": "cpinotos",
    "createdAt": 1538055720610,
    "namespace": "iec_hebe_io",
    "jurisdiction": "eu",
    "version": "5",
    "activationStatus": "DEPLOYED",
    "operationType": "ACTIVATION"
  },
  {
    "createdBy": "cpinotos",
    "createdAt": 1538060063980,
    "namespace": "iec_hebe_io",
    "jurisdiction": "eu",
    "version": "6",
    "activationStatus": "DEPLOYED",
    "operationType": "ACTIVATION"
  }
]
```



