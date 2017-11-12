# FakeGenerator

Aplicación que simula tanto la generación automática y aleatoria como el envío del estado del rango de sensores pasados por parámetro.

En este ejemplo el programa creará 25.000 sensores que estarán enviando continuamente su estado, el cual está compuesto por los campos movimiento, humo, temperatura actual y temperatura máxima:

java -jar fakeGen-fakeGen-1.0-SNAPSHOT.jar 1 25000

Genera números aleatorios con los que activa alarmas en estos sensores según una frecuencia establecida en la aplicación. 

La salida del programa será:

Sending 'POST' request to URL : http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/ENTRY/messages

Post parameters : 1,false,false,10,50

Response Code : 200

Aleatorio : 80.0


Sending 'POST' request to URL : http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/ENTRY/messages

Post parameters : 2,false,false,20,30

Response Code : 200

Aleatorio : 24.0


Sending 'POST' request to URL : http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/ENTRY/messages

Post parameters : 3,true,false,8,36

Response Code : 200

Aleatorio : 38.0


Sending 'POST' request to URL : http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/ENTRY/messages

Post parameters : 4,false,false,18,19

Response Code : 200

Aleatorio : 66.0


...

Sending 'POST' request to URL : http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/ENTRY/messages

Post parameters : 23832,false,false,30,21

Response Code : 200

Aleatorio : 14.0


Sending 'POST' request to URL : http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/ENTRY/messages

Post parameters : 23833,false,true,8,50

Response Code : 200

Aleatorio : 73.0


Sending 'POST' request to URL : http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/ENTRY/messages

Post parameters : 23834,false,false,10,44

Response Code : 200

Aleatorio : 39.0


...
...


