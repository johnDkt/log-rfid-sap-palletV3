<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en_EN" lang="en_EN">
<head>
<meta content="text/html; charset=UTF-8" http-equiv="content-type"/>
<title>Reader Test</title>

<link type="text/css" href="/sysinfo/js/jquery-ui/css/custom-theme/jquery-ui-1.8rc1.custom.css" rel="stylesheet" />
<link type="text/css" href="/sysinfo/themes/oxylane/layout.css" rel="stylesheet" />

<script type="text/javascript" src="/sysinfo/js/jquery/jquery-1.4.2.min.js" ></script>
<script type="text/javascript" src="/sysinfo/js/jquery-ui/js/jquery-ui-1.8rc1.custom.min.js" ></script>

<script type="text/javascript" src="/sysinfo/js/jquery.autoheight.js" ></script>

<script type="text/javascript" src="/sysinfo/js/jquery.timer.js" ></script>
<script type="text/javascript" src="/sysinfo/js/ui_ajax.js" ></script>

<!-- Load CSS/JS Of all plugins -->

<!-- Load css 'reader' ('plugins/reader/css/css.php') -->
<link type='text/css' href='/sysinfo/plugins/pallet/css/sh_style.css' rel='stylesheet'/>
<link type='text/css' href='/sysinfo/plugins/pallet/css/sh_stats.css' rel='stylesheet'/>
<link type='text/css' href='/sysinfo/plugins/pallet/js/jqGrid/css/ui.jqgrid.css' rel='stylesheet'/>
<link type='text/css' href='/sysinfo/plugins/pallet/css/eas.css' rel='stylesheet'/>

<!-- END -->

</head>

<body style="background: none repeat scroll 0% 0% rgb(255, 255, 255);">
 <div class="page_margins"><div class="page">
<b>TEST RFID READ :</b>
<br>


<div style="border:1px solid #BBB; width:750px;">
	<div style="margin-left:20px; margin-top:5px; margin-bottom:5px;">
		<b> - Profile : </b> <input type="text" value="dev" id="profile" size="5"> <b> - Connection : </b> <select id="connection" ><option value="COM">COM</option><option value="IP">IP</option></select>
	</div>
	<div style="margin-left:20px; margin-top:5px; margin-bottom:5px;">
		<b> - Press start button to start RFID read : </b><input type="submit" id="startReadBtn" onclick="startRead(); return false;" value="START" >
	</div>
	<div style="margin-left:20px; margin-top:5px; margin-bottom:5px;">
		<b> - Press stop button to stop RFID read: </b><input type="submit" id="stopReadBtn" onclick="stopRead(); return false;" value="STOP" >
	</div>
	
	<pre style="margin-left:20px;margin-bottom:5px;" id="readResult">
	</pre>

</div>

<script type="text/javascript">

$(document).stopTime();

document.getElementById('startReadBtn').disabled=false;
document.getElementById('stopReadBtn').disabled=true;

function startRead() {

	var profile = document.getElementById('profile').value;
	var connection = document.getElementById('connection').value;

	$.ajax({
   		type: "POST",
   		url: "/sysinfo/plugins/pallet/startRead.php",
   		data: "profile="+profile+"&connection="+connection,
		success: function(msg){  
			refreshRead();
     		$(document).stopTime('refresh_read');
     		document.getElementById('startReadBtn').disabled=false;
			document.getElementById('stopReadBtn').disabled=true;

   		}
 	});
	$(document).everyTime(1000, 'refresh_read', refreshRead );
	document.getElementById('startReadBtn').disabled=true;
	document.getElementById('stopReadBtn').disabled=false;
}

function stopRead() {

	$.ajax({
   		type: "POST",
   		url: "/sysinfo/plugins/pallet/stopRead.php",
		success: function(msg){	
	
   		}
 	});

}

function refreshRead() {
	$.ajax({
   		type: "POST",
   		url: "/sysinfo/plugins/pallet/getFile.php",
   		success: function(msg){   
     			document.getElementById('readResult').innerHTML=msg;
		}
 	});
}


</script>
</div></div>
</body>
</html>
