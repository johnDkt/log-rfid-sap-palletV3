<!-- pallet  have reader module embedded, so we have reader module in the same tab than pallet's tab -->
<b>Embi reader service :</b>
<br>
<div style="border:1px solid #BBB; width:710px">
	<div style="margin-left:20px; margin-top:5px; margin-bottom:5px;">
		<b> - Status : </b> <span id="status"></span><input type="submit" onclick="refreshStatus(); return false;" value="Refresh Status" >
	</div>

	<div style="margin-left:20px;margin-bottom:5px;">
		<b> - Command : </b> <input type="submit" onclick="stop(); return false;" value="Stop" > <input type="submit" onclick="start(); return false;" value="Start" > <span id="message"></span>
	</div>

</div>

<script type="text/javascript">


function refreshStatus () {

	$.ajax({
   		type: "POST",
   		url: "plugins/pallet/command.php",
   		data: "service=/etc/init.d/embi-portcom-rmi-deamon&command=status",
   		success: function(msg){    
     			document.getElementById('status').innerHTML=msg;
   		}
 	});
}

function start() {
	$.ajax({
   		type: "POST",
   		url: "plugins/pallet/command.php",
   		data: "service=/etc/init.d/embi-portcom-rmi-deamon&command=start",
   		success: function(msg){   
     		document.getElementById('message').innerHTML="Command /etc/init.d/embi-portcom-rmi-deamon start send";
			refreshStatus ();
   		}
 	});
	
}

function stop() {
	$.ajax({
   		type: "POST",
   		url: "plugins/pallet/command.php",
   		data: "service=/etc/init.d/embi-portcom-rmi-deamon&command=stop",
   		success: function(msg){    
     		document.getElementById('message').innerHTML="Command /etc/init.d/embi-portcom-rmi-deamon stop send";
			refreshStatus ();
   		}
 	});
	refreshStatus();
}
refreshStatus ();
</script>