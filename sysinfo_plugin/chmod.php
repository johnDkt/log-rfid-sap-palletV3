<?php


$chmod=$_REQUEST['chmod'];
$file=$_REQUEST[file];
	
$cmd="sudo chmod $chmod /data/logs/log-rfid-pallet/$file";
$res=`$cmd`;
echo $res;


?>