<?php

function tailLogs($file, $nbLines) {
	if (file_exists($file)){
		$cmd="tail -n $nbLines $file";
		$content = utf8_encode(`$cmd`);	
		$content = str_replace(array("<",">"),array("&lt;", "&gt;"),$content);
       
		return $content;
	} else {
		return "$file not found...";
	}
}
$file = $_GET['file']; // get the requested page
$nbLines = $_GET['nbLines']; // get how many rows we want to have into the grid
$grep = $_GET['filter']; // get index row - i.e. user click to sort
$grepV = $_GET['filterV']; // get the direction
$level = $_GET['level'];
$start = $_GET['start'];
$end = $_GET['end'];
$suffix = $_GET['suffix'];

if (!file_exists($file.$suffix)){
	$content="$file$suffix not found...";
} else {
	

	$filter="";

	if (isset($grep) && !empty($grep)) {
		$filter.=" | grep -E ";
		$filter.=$grep;
	}

	if (isset($grepV ) && !empty($grepV)) {
		$filter.=" | grep -v ";
		$filter.=$grepV;
	}

	if (isset($level) && !empty($level)) {
		$filter.=" | grep -E ";
		$filter.=$level;
	}

	if (isset($start) && !empty($start)) {
		$filter.=" | awk 'BEGIN{comp=\"".$start."\"; }{date=substr($0,0,23); if( (date >= comp) ){print $0;}}' ";

	}

	if (isset($end) && !empty($end)) {
		$filter.=" | awk 'BEGIN{comp=\"".$end."\"; }{date=substr($0,0,23); if( (date <= comp) ){print $0;}}' ";

	}

	if (!isset($nbLines) || empty($nbLines)) {
		$cmd="cat $file$suffix";
	} else {
		$cmd="tail -n $nbLines $file$suffix";
	}

	if (!empty($filter)) {
		$cmd.=$filter;
	}
	
	$content = utf8_encode(`$cmd`);
	$content = str_replace(array("<",">"),array("&lt;", "&gt;"),$content);
}

$responce->rows[0]['id']=0;
$cell=array();$cell[0]=$content;
$responce->rows[0]['cell']=$cell;

echo json_encode($responce);


?>
