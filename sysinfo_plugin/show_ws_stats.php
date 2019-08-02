<?php include 'version_reader.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en_EN" lang="en_EN">
<head>
<meta content="text/html; charset=UTF-8" http-equiv="content-type"/>
<title>Webservices</title>

<link type="text/css" href="/sysinfo/js/jquery-ui/css/custom-theme/jquery-ui-1.8rc1.custom.css" rel="stylesheet" />
<link type="text/css" href="/sysinfo/themes/oxylane/layout.css" rel="stylesheet" />

<script type="text/javascript" src="/sysinfo/js/jquery/jquery-1.4.2.min.js" ></script>
<script type="text/javascript" src="/sysinfo/js/jquery-ui/js/jquery-ui-1.8rc1.custom.min.js" ></script>

<script type="text/javascript" src="/sysinfo/js/jquery.autoheight.js" ></script>

<script type="text/javascript" src="/sysinfo/js/jquery.timer.js" ></script>
<script type="text/javascript" src="/sysinfo/js/ui_ajax.js" ></script>

<!-- Load CSS/JS Of all plugins -->

<!-- Load css 'reader' ('plugins/pallet/css/css.php') -->
<link type='text/css' href='/sysinfo/plugins/pallet<?php echo $version_reader;?>/css/sh_style.css' rel='stylesheet'/>
<link type='text/css' href='/sysinfo/plugins/pallet<?php echo $version_reader;?>/css/sh_stats.css' rel='stylesheet'/>
<link type='text/css' href='/sysinfo/plugins/pallet<?php echo $version_reader;?>/js/jqGrid/css/ui.jqgrid.css' rel='stylesheet'/>
<link type='text/css' href='/sysinfo/plugins/pallet<?php echo $version_reader;?>/css/eas.css' rel='stylesheet'/>

<!-- END -->

</head>

<body style="background: none repeat scroll 0% 0% rgb(255, 255, 255);">
 <div class="page_margins"><div class="page">
 
 <script type='text/javascript'>
<?php echo "$(document).stopTime()"; ?>
</script>


<?php

$start="<input type=\"text\" id='start' size=24 >";
$end="<input type=\"text\" id='end' size=24>";


?>

<b>Filter :</b>
<div id="code" style="width:990px">
	<table style="float:center;">
		<tbody>
			<tr><td align=right>
				<td align=right style="padding-left:10px;">Start Date:</td><td><?php echo $start; ?></td> 
				<td align=right style="padding-left:10px;">End Date:  </td><td><?php echo $end; ?></td>
				<td style="padding-left:10px;" rowspan=2 valign=center><input type=submit value="Filter" onclick="filter(); return false;"></td>
				<td style="padding-left:10px;" rowspan=2 valign=center><input type=submit value="Clean" onclick="cleanFilter(); return false;"></td>
			</tr>
		</tbody>
	</table>
</div>
<br>

<div style="position:relative;  left: 10%;">
	<table border=1>
		<thead>
			<tr align=center bgcolor="#DDD"><th width=300px >Webservice Name</th><th width=100px>Min (ms)</th><th width=100px>Max (Ms)</th><th width=100px>Avg (ms)</th><th width=100px>Count</th></tr>
		</thead>
		<tbody id='result'>
			
		</tbody>
	</table>
</div>
<br>

<script language='Javascript'>

function filterDate(start, end) {

	$.ajax({
	       type: "POST",
	   	url: "/sysinfo/plugins/pallet<?php echo $version_reader;?>/show_ws_stats_ajax.php",
	       data: "file=/data/logs/log-rfid-pallet<?php echo $version_reader;?>/log-rfid-pallet<?php echo $version_reader;?>-ws-stats.log&start="+start+"&end="+end,
		success: function(msg){
			$("#result").html(msg);
	     		   	}
	    });
	}

filterDate('','');

function filter() {
	var start=document.getElementById('start').value;
	var end=document.getElementById('end').value;
	filterDate(start,end);
}

function cleanFilter() {
	document.getElementById('start').value='';
	document.getElementById('end').value='';
	filter();
}

</script>


</script>
</div></div>
</body>
</html>