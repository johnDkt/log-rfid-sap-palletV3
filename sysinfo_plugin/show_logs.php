<?php include 'version_reader.php'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en_EN" lang="en_EN">
<head>
<meta content="text/html; charset=UTF-8" http-equiv="content-type"/>
<title>pallet Logs</title>

<link type="text/css" href="/sysinfo/js/jquery-ui/css/custom-theme/jquery-ui-1.8rc1.custom.css" rel="stylesheet" />
<link type="text/css" href="/sysinfo/themes/oxylane/layout.css" rel="stylesheet" />

<script type="text/javascript" src="/sysinfo/js/jquery/jquery-1.4.2.min.js" ></script>
<script type="text/javascript" src="/sysinfo/js/jquery-ui/js/jquery-ui-1.8rc1.custom.min.js" ></script>

<script type="text/javascript" src="/sysinfo/js/jquery.autoheight.js" ></script>

<script type="text/javascript" src="/sysinfo/js/jquery.timer.js" ></script>
<script type="text/javascript" src="/sysinfo/js/ui_ajax.js" ></script>

<!-- Load CSS/JS Of all plugins -->

<!-- Load css 'pallet' ('plugins/pallet/css/css.php') -->
<link type='text/css' href='/sysinfo/plugins/pallet<?php echo $version_reader;?>/css/sh_style.css' rel='stylesheet'/>
<link type='text/css' href='/sysinfo/plugins/pallet<?php echo $version_reader;?>/css/sh_stats.css' rel='stylesheet'/>
<link type='text/css' href='/sysinfo/plugins/pallet<?php echo $version_reader;?>/js/jqGrid/css/ui.jqgrid.css' rel='stylesheet'/>
<link type='text/css' href='/sysinfo/plugins/pallet<?php echo $version_reader;?>/css/eas.css' rel='stylesheet'/>


<!-- Load js 'pallet' ('plugins/pallet/js/js.php') -->
<script type='text/javascript' src='/sysinfo/plugins/pallet<?php echo $version_reader;?>/js/sh_main.min.js' ></script>
<script type='text/javascript' src='/sysinfo/plugins/pallet<?php echo $version_reader;?>/js/sh_sh.min.js' ></script>
<script type='text/javascript' src='/sysinfo/plugins/pallet<?php echo $version_reader;?>/js/sh_log.js' ></script>
<script type='text/javascript' src='/sysinfo/plugins/pallet<?php echo $version_reader;?>/js/jqGrid/jquery.jqGrid.js' ></script>


<!-- END -->

</head>

<body style="background: none repeat scroll 0% 0% rgb(255, 255, 255);">
 <div class="page_margins"><div class="page">
 
<script type='text/javascript'>
<?php echo "$(document).stopTime()"; ?>
</script>



<?php



$defaultNbLines=100;

$show_logs['pallet_log'] = array(
	     'file' => '/data/logs/log-rfid-pallet'.$version_reader.'/log-rfid-pallet'.$version_reader.'.log',
);



 
foreach ($show_logs as $id => $info){

	$json_url = '/sysinfo/plugins/pallet'.$version_reader.'/show_logs_ajax.php?file='.$info['file'].'&nbLines='.$defaultNbLines;
	$autoRefresh="<b>Auto Refresh</b>:<select id=\'auto_refresh".$id."\'><OPTION VALUE=0>NO</OPTION><OPTION VALUE=5000>5 sec</OPTION><OPTION VALUE=10000>10 sec</OPTION> <OPTION VALUE=30000>30 sec</OPTION><OPTION VALUE=60000>60 sec</OPTION></select><br>";
	$level="<select id='level".$id."'><OPTION VALUE=''></OPTION><OPTION VALUE='DEBUG'>DEBUG</OPTION><OPTION VALUE='INFO'>INFO</OPTION><OPTION VALUE='WARN'>WARN</OPTION><OPTION VALUE='ERROR'>ERROR</OPTION></select>";
	$lines="<input type=\"text\" id='nbLines".$id."' value=$defaultNbLines size=3 >";
	$filter="<input type=\"text\" id='filter".$id."' size=60 >";
	$filterV="<input type=\"text\" id='filterV".$id."' size=60>";
	$start="<input type=\"text\" id='start".$id."' size=24 >";
	$end="<input type=\"text\" id='end".$id."' size=24>";
	$refresh="<a href=# onclick=jQuery(\'#".$id."\').trigger(\'reloadGrid\');>[ Refresh Log File ]</a>";
	$suffix="<input type=\"text\" id='suffix".$id."' size=3>";
	
	?>	

	<?php
	echo "<table id='".$id."'></table><div class='navGrid' id='pager_".$id."'>";
	?>
<table style="float:center;">
			<tbody>
				<tr>					
					<td align=right>Level:   </td><td><?php echo $level; ?></td>
					<td align=right style="padding-left:10px;">Grep:      </td><td><?php echo $filter; ?></td>
					<td align=right style="padding-left:10px;">Start Date:</td><td><?php echo $start; ?></td> 
					<td style="padding-left:10px;" rowspan=2 valign=center><input type=submit value="Filter" onclick="filter<?php echo $id;?>(); return false;"></td>
					<td style="padding-left:10px;" rowspan=2 valign=center><input type=submit value="Clean" onclick="cleanFilter<?php echo $id;?>(); return false;"></td>
					<td style="padding-left:10px;" rowspan=2 valign=center>Suffix: <?php echo $suffix; ?></td>
				</tr>
				<tr>
					<td align=right>Nb Lines:</td><td><?php echo $lines; ?></td> 
					<td align=right style="padding-left:10px;">Grep -V: </td><td><?php echo $filterV; ?></td>
				    <td align=right style="padding-left:10px;">End Date:  </td><td><?php echo $end; ?></td>
				</tr>
			</tbody>
		</table>
<?php
	echo "</div>";

	echo "<script type='text/javascript'>$.jgrid.useJSON = true;";

// Grid
    echo "
$('#".$id."').jqGrid({
        url: \"".$json_url."\",
        datatype: 'json',
        height: '100%',
        colNames:['".$refresh."--".$autoRefresh."'],
        colModel:[{align:'left', width:'1000px', sortable:false}],        
	 height:300,
	 rowNum: 1,        
        pager: '#pager_".$id."', 
	 pgbuttons: false,
        pginput: false,
	 headertitles:false,
	hoverrows:false,
        multiselect: false,	 
        caption:\"".$info['file']."\" ,
		 gridComplete: function() {
			document.getElementById('".$id."').parentNode.parentNode.scrollTop=document.getElementById('".$id."').clientHeight;
			document.getElementById('".$id."').firstElementChild.lastElementChild.firstElementChild.style.overflow='auto';
			document.getElementById('".$id."').firstElementChild.lastElementChild.firstElementChild.title='';			
			document.getElementById('".$id."').style.width='auto' ;
	 }, 
});


$('#".$id."').jqGrid('navGrid','#pager_".$id."',{edit:false,add:false,del:false,search:false,pager:true});

";


/// Auto refresh
echo "var refresh_logs_".$id." = function(i){";
    echo "  jQuery('#".$id."').trigger('reloadGrid');";
echo "}";

echo "

$('#auto_refresh".$id."').change(function(){
   	$(document).stopTime('show_logs".$id."');
    	var value = $('#auto_refresh".$id."').val();    	
	if (value > 0) {
		$(document).everyTime(parseInt(value), 'show_logs".$id."', refresh_logs_".$id." );
	} 
});
";
?>

var filter<?php echo $id;?> = function() {
	
	var lines = document.getElementById('nbLines<?php echo $id;?>').value;
	var level = document.getElementById('level<?php echo $id;?>').value;
	var filter = document.getElementById('filter<?php echo $id;?>').value;
	var filterV = document.getElementById('filterV<?php echo $id;?>').value;
	var start = document.getElementById('start<?php echo $id;?>').value;
	var end = document.getElementById('end<?php echo $id;?>').value;
	var suffix = document.getElementById('suffix<?php echo $id;?>').value;
	
	var newUrl="<?php echo $json_url?>&nbLines="+lines+"&level="+level+"&filter="+filter+"&filterV="+filterV+"&start="+start+"&end="+end+"&suffix="+suffix ;

 	jQuery('#<?php echo $id;?>').setGridParam( 
		{
		url:newUrl,
		page:1
		} ).trigger("reloadGrid");
 
}

var cleanFilter<?php echo $id;?> = function() {
	document.getElementById('nbLines<?php echo $id;?>').value='<?php echo $defaultNbLines;?>';
	document.getElementById('level<?php echo $id;?>').value='';
	document.getElementById('filter<?php echo $id;?>').value='';
	document.getElementById('filterV<?php echo $id;?>').value='';
	document.getElementById('start<?php echo $id;?>').value='';
	document.getElementById('end<?php echo $id;?>').value='';
	document.getElementById('suffix<?php echo $id;?>').value='';
	
	filter<?php echo $id;?>();
 
}
document.getElementById('pager_<?php echo $id;?>').style.height="48px";
document.getElementById('auto_refresh<?php echo $id;?>').parentNode.style.height="22px";

<?php
	echo "</script>";	
echo "<br>";
}
?>

<script type='text/javascript'>
function chmod() {
	var chmod = document.getElementById('chmod').value;
	var file = document.getElementById('file').value;

$.post(
		"/sysinfo/plugins/pallet<?php echo $version_reader;?>/chmod.php", 
		{ chmod : chmod , file : file  } , 
		function(msg) {
			alert( msg );
			
			}
		);
}
</script>

<?php
$cmd = "ls -l /data/logs/log-rfid-pallet".$version_reader."/log-rfid-pallet*";
$perm=`$cmd`;
?>

<b>Log files permissions:</b> 
<br>
<pre id=code style="color:#000;">
<?php echo $perm; ?>
</pre>
<i>&nbsp chmod : <input type=text size=3 id="chmod"> file : <input type=text size=30 id="file"> <input type=submit value="CHMOD" onclick="chmod(); return false;"> </i>

</script>
</div></div>
</body>
</html>
