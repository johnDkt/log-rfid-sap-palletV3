<?php

`echo "" > /data/log-rfid-reader.test`;
`echo "" > /data/log-rfid-reader.stop`;
// $jarname = ls /etc/log-rfid-pallet/lib | grep "^reader.*.jar"
// $jarname = /etc/log-rfid-pallet/lib/reader
$findReader = "ls /etc/log-rfid-pallet/lib | grep \"^reader.*.jar\"";
$jarName = "/etc/log-rfid-pallet/lib/".exec($findReader);
$cmd = "java  -cp '$jarName:/etc/log-rfid-pallet/lib/*:/etc/log-rfid-pallet/config/'  com.decathlon.log.rfid.reader.main.BoScannerTestWithMain ";
//$cmd = "java -jar /etc/log-rfid-reader/log-rfid-reader.jar ";
$cmd.=$_REQUEST['profile'];
$cmd.=" ";
$cmd.=$_REQUEST['connection'];
$cmd.=" 500 5 /data/log-rfid-reader.stop >> /data/log-rfid-reader.test";

exec($cmd);