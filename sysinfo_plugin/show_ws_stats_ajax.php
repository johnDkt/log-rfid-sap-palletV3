<?php

$file = $_REQUEST['file']; // get the requested page
$start = $_REQUEST['start'];
$end = $_REQUEST['end'];

if (!file_exists($file)){
	$content="<tr><td>$file not found...</td><td></td><td></td><td></td><td></td></tr>";
} else {
	

	$filter="";


	if (isset($start) && !empty($start)) {
		$filter.=" | awk 'BEGIN{FS=\";\"; comp=\"".$start."\"; }{date=substr($2,0,23); if( (date >= comp) ){print $0;}}' ";

	}

	if (isset($end) && !empty($end)) {
		$filter.=" | awk 'BEGIN{FS=\";\"; comp=\"".$end."\"; }{date=substr($2,0,23); if( (date <= comp) ){print $0;}}' ";

	}

	
	$cmd="cat $file*";
	
$count=" | awk ' 
BEGIN{FS=\";\";} 
{
	tabCount[$1]=tabCount[$1]+1; 
	tabSum[$1]=tabSum[$1]+$3; 
	if (tabMin[$1]==\"\") {tabMin[$1]=$3;} else if (tabMin[$1]>$3){tabMin[$1]=$3;} 
	if (tabMax[$1]==\"\") {tabMax[$1]=$3;} else if (tabMax[$1]<$3) {tabMax[$1]=$3;}
} 
END { 
	for(ws in tabCount) {
		if (ws!=\"\") print \"<tr><td>\"ws\"</td><td>\"tabMin[ws]\"</td><td>\"tabMax[ws]\"</td><td>\"tabSum[ws]/tabCount[ws]\"</td><td>\"tabCount[ws]\"</td></tr>\";
	} 
}'";
	$cmd.=$filter.$count;
	$content = utf8_encode(`$cmd`);
	
}

echo $content;


?>
