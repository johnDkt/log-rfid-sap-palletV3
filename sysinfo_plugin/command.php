<?php

$service=$_REQUEST['service'];
$command=$_REQUEST['command'];

$result=`sudo $service $command`;

echo $result;