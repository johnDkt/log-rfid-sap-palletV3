<?php

function save_file($file, $content) {
	return file_put_contents($file, $content);
}

if (isset($_REQUEST['file'])) {
	if (isset($_REQUEST['content'])) {
		$response = save_file($_REQUEST['file'], $_REQUEST['content']);
		if ($response > 0) {
			echo $_REQUEST['file'] . " saved successfully";
		} else {
			echo "Error while saving " . $_REQUEST['file'];

		}			
		
	} 
}


?>