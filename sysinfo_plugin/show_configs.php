<?php
include 'version_reader.php';
function searchInThisFileForToday($file, $needle)
{
    $dateFormat = "Y-m-d";
    $content = get_file_content($file);
    $dateNeedle = stripos($content, date($dateFormat));
    if ($dateNeedle === false) {
        return false;
    }
    $content = substr($content, $dateNeedle, strlen($content) - $dateNeedle);
    $foundNeedle = stripos($content, $needle);
    if ($foundNeedle === false) {
        return false;
    } else {
        return true;
    }
}

function get_file_content($file)
{
    if (file_exists($file)) {
        return utf8_encode(file_get_contents($file));
    } else {
        return "$file not found...";
    }
}

function get_file_xml_content($file)
{
    if (file_exists($file)) {

        $s = utf8_encode(file_get_contents($file));
        $replaced = str_replace(array("<", ">"), array("&lt;", "&gt;"), $s);
        return $replaced;
    } else {
        return "$file not found...";
    }
}

function save_file($file, $content)
{
    return file_put_contents($file, $content);
}

$version_txt = "";
$version_txt .= `rpm --qf "Package: '%{NAME}'  Version: '%{VERSION}' Release: '%{RELEASE}'\n" -q log-rfid-pallet$version_reader`;
$version_txt .= `rpm --qf "Package: '%{NAME}'  Version: '%{VERSION}' Release: '%{RELEASE}'\n" -q OXIT_embi-portcom-rmi-server`;
$etc_path = "/etc/log-rfid-pallet" . $version_reader;
$data_path = "/data/log-rfid-pallet" . $version_reader;
$files = array(
    'application_config' => $etc_path . "/config/application.properties",
    'log4j_config' => $etc_path . "/config/log4j-pallet.properties",
    'rules_config' => "/etc/udev/rules.d/20-pallet" . $version_reader . "-usb-serial.rules",
    'alert_config' => $etc_path . "/config/alert.properties",
    'profiles_config' => $etc_path . "/config/profiles.properties",
    'scanner_config' => $etc_path . "/config/scanner.properties",
    'log4j-reader_config' => $etc_path . "/config/log4j-reader.properties",
);
$titles = array(
    'application_config' => "Application Configuration",
    'log4j_config' => "Logs configuration",
    'rules_config' => "Driver rules",
    'alert_config' => "Alerting configuration",
    'profiles_config' => "Profiles configuration",
    'scanner_config' => "RFID Card configuration",
    'log4j-reader_config' => "Reader log4j configuration",
);

$application_config_txt = get_file_content($files['application_config']);
$log4j_config_txt = get_file_content($files['log4j_config']);
$rules_config_txt = get_file_content($files['rules_config']);
$alert_config_txt = get_file_content($files['alert_config']);
$profiles_config_txt = get_file_content($files['profiles_config']);
$scanner_config_txt = get_file_content($files['scanner_config']);
$log4jreader_config_txt = get_file_content($files['log4j-reader_config']);

$contents = array(
    'application_config' => $application_config_txt,
    'log4j_config' => $log4j_config_txt,
    'rules_config' => $rules_config_txt,
    'alert_config' => $alert_config_txt,
    'profiles_config' => $profiles_config_txt,
    'scanner_config' => $scanner_config_txt,
    'log4j-reader_config' => $log4jreader_config_txt,
);
?>
<div style="float:left;">
    <b>pallet Version:</b>
<pre id="code" class="sh_sh">
<?php echo $version_txt ?>
</pre>

    <?php
    //pallet embarque son reader
    include 'embiReader.php';
    ?>
    <br>
    <br>

    <b>Reader Test:</b>
<pre id="code" class="sh_sh">
<a href="http://<?php echo $_SERVER['SERVER_ADDR'] ?>/sysinfo/plugins/pallet/tools.php"
   onclick="window.open(this.href,'javascriptfr', 'height=500,width=800,resizable=1,location=0,status=0,scrollbars=1,top=0,left=0'); return false;"><b>Click here to test RFID read </b></a>
</pre>
    <br>

    <br>
    <b>View Logs:</b>
<pre id="code" class="sh_sh">
<a href="http://<?php echo $_SERVER['SERVER_ADDR'] ?>/sysinfo/plugins/pallet<?php echo $version_reader ?>/show_logs.php"
   onclick="window.open(this.href,'javascriptfr', 'height=500,width=1038,resizable=1,location=0,status=0,scrollbars=1,top=0,left=0'); return false;"><b>Click here to view log files </b></a>
</pre>
    <br>
    <b>Webservices :</b>
<pre id="code" class="sh_sh">
<a href="http://<?php echo $_SERVER['SERVER_ADDR'] ?>/sysinfo/plugins/pallet<?php echo $version_reader ?>/show_ws_stats.php"
   onclick="window.open(this.href,'javascriptfr', 'height=500,width=1038,resizable=1,location=0,status=0,scrollbars=1,top=0,left=0'); return false;"><b>Click here to view webservices statistics </b></a>
</pre>
    <br>
    <b>Validation statistics :</b>
<pre id="code" class="sh_sh">
    <a href="http://<?php echo $_SERVER['SERVER_ADDR'] ?>/sysinfo/plugins/pallet<?php echo $version_reader ?>/show_validation_rate.php"
       onclick="window.open(this.href,'javascriptfr', 'height=500,width=1038,resizable=1,location=0,status=0,scrollbars=1,top=0,left=0'); return false;"><b>Click here to view validation statistics </b></a>
</pre>
</div>
<script type="text/javascript">
    sh_highlightDocument();
</script>
<div
    style="float:left;margin-left:30px;margin-top: 14px;padding: 15px 20px 15px 20px;border:1px #bbbbbb solid;background-color:#f5f5f5">
    <div><b>Antennas Status for today</b></div>
    <div style="margin-left: 5px" ;>
        <div>
            <span><b>Antenna 1 Status:</b></span>
            <span id="a1Status"></span>
        </div>
        <div>
            <span><b>Antenna 2 Status:</b></span>
            <span id="a2Status"></span>
        </div>
        <div>
            <span><b>Antenna 3 Status:</b></span>
            <span id="a3Status"></span>
        </div>
        <div>
            <span><b>Antenna 4 Status:</b></span>
            <span id="a4Status"></span>
        </div>
    </div>
</div>
<?php
$logFile = '/data/logs/log-rfid-pallet/log-rfid-pallet-antenna.log';
$logFilePrevious = '/data/logs/log-rfid-pallet/log-rfid-pallet-antenna.log.1';
//DEV
//$logFile = 'd:/log-rfid-pallet.log';
//$logFilePrevious = 'd:/log-rfid-pallet.log.1';
$antenna1 = searchInThisFileForToday($logFile, "antenna: 11");
if ($antenna1 == false) {
    $antenna1 = searchInThisFileForToday($logFilePrevious, "antenna: 11");
}
$antenna2 = searchInThisFileForToday($logFile, "antenna: 22");
if ($antenna2 == false) {
    $antenna2 = searchInThisFileForToday($logFilePrevious, "antenna: 22");
}
$antenna3 = searchInThisFileForToday($logFile, "antenna: 33");
if ($antenna3 == false) {
    $antenna3 = searchInThisFileForToday($logFilePrevious, "antenna: 33");
}
$antenna4 = searchInThisFileForToday($logFile, "antenna: 44");
if ($antenna4 == false) {
    $antenna4 = searchInThisFileForToday($logFilePrevious, "antenna: 44");
}
?>
<script type='text/javascript'>
    document.getElementById('a1Status').innerHTML = "<img width=32 height=32 src=\"plugins/pallet/img/Tick-32.png\"  />";
    if (<?php if($antenna1!=false){echo("true");}else{echo("false");} ?>) {
        document.getElementById('a1Status').innerHTML = "<img width=32 height=32 src=\"plugins/pallet/img/Tick-32.png\"  />";
    } else {
        document.getElementById('a1Status').innerHTML = "<img width=32 height=32 src=\"plugins/pallet/img/Alert-32.png\"  />";
    }
    if (<?php if($antenna2!=false){echo("true");}else{echo("false");} ?>) {
        document.getElementById('a2Status').innerHTML = "<img width=32 height=32 src=\"plugins/pallet/img/Tick-32.png\"  />";
    } else {
        document.getElementById('a2Status').innerHTML = "<img width=32 height=32 src=\"plugins/pallet/img/Alert-32.png\"  />";
    }
    if (<?php if($antenna3!=false){echo("true");}else{echo("false");} ?>) {
        document.getElementById('a3Status').innerHTML = "<img width=32 height=32 src=\"plugins/pallet/img/Tick-32.png\"  />";
    } else {
        document.getElementById('a3Status').innerHTML = "<img width=32 height=32 src=\"plugins/pallet/img/Alert-32.png\"  />";
    }
    if (<?php if($antenna4!=false){echo("true");}else{echo("false");} ?>) {
        document.getElementById('a4Status').innerHTML = "<img width=32 height=32 src=\"plugins/pallet/img/Tick-32.png\"  />";
    } else {
        document.getElementById('a4Status').innerHTML = "<img width=32 height=32 src=\"plugins/pallet/img/Alert-32.png\"  />";
    }
</script>
<script type='text/javascript'>

    function showOther(links) {
        var all = ["tunnel_config", "log4j_config"];

        for (var i = 0; i < all.length; i++) {
            if (all[i] != links) {
                showFile(all[i]);
            }
        }

    }

    function editFile(links) {
        if (document.getElementById(links + "_edit")) document.getElementById(links + "_edit").style.display = "none";
        if (document.getElementById(links + "_save")) document.getElementById(links + "_save").style.display = "block";
        if (document.getElementById(links + "_cancel")) document.getElementById(links + "_cancel").style.display = "block";

        if (document.getElementById(links + "_code")) document.getElementById(links + "_code").style.display = "none";
        if (document.getElementById(links + "_input")) document.getElementById(links + "_input").style.display = "block";

    }

    function showFile(links) {
        if (document.getElementById(links + "_edit")) document.getElementById(links + "_edit").style.display = "block";
        if (document.getElementById(links + "_save")) document.getElementById(links + "_save").style.display = "none";
        if (document.getElementById(links + "_cancel")) document.getElementById(links + "_cancel").style.display = "none";

        if (document.getElementById(links + "_code")) document.getElementById(links + "_code").style.display = "block";
        if (document.getElementById(links + "_input")) document.getElementById(links + "_input").style.display = "none";
    }


    function saveFile(file, node) {

        var c = document.getElementById(node + "_area").value;

        $.ajax({
            type: "POST",
            url: "plugins/pallet/save_file.php",
            data: "file=" + file + "&content=" + c,
            success: function (msg) {
                alert(msg);
                document.getElementById('li-pallet').firstElementChild.onclick();
            }
        });
    }

</script>

<div style="float:left;">
    <?php
    $login = "/var/www/html/sysinfo/plugins/config-mgt/login.php";
    include $login;
    $secure = file_exists($login);
    foreach ($files as $id => $info){
    ?>
    <?php if (!$secure || (isset ($_SESSION['auth']) && $_SESSION['auth']->auth == "OK"))  { ?>
    <div style="width:700px;">
        <b><?php echo $titles[$id] ?>: (<?php echo $info ?>)</b>
        <?php } ?>
        <?php if (!$secure || (isset ($_SESSION['auth']) && $_SESSION['auth']->canRead($info) && $_SESSION['auth']->canWrite($info))) { ?>
            <span id="<?php echo $id ?>_span">
        <a id="<?php echo $id ?>_edit" href="#"
           onclick="showOther('<?php echo $id ?>'); editFile('<?php echo $id ?>'); return false;"
           style="display:block;float:right;color:#00F;"> Edit </a>
        <a id="<?php echo $id ?>_save" href="#"
           onclick="saveFile('<?php echo $info ?>','<?php echo $id ?>'); return false;"
           style="display:none;float:right;color:#00F;"> Save </a>
        <a id="<?php echo $id ?>_cancel" href="#" onclick="showFile('<?php echo $id ?>'); return false;"
           style="display:none;float:right;color:#F00;"> Cancel &nbsp;</a>
        </span>
        <?php } ?>
        <?php if (!$secure || (isset ($_SESSION['auth']) && $_SESSION['auth']->canRead($info)))  { ?>
    </div>
<?php } ?>
    <?php if (!$secure || (isset ($_SESSION['auth']) && $_SESSION['auth']->canRead($info))) { ?>
        <div id="<?php echo $id ?>_code">
            <pre id="code" class="sh_sh"><?php echo $contents[$id] ?></pre>
        </div>
    <?php } ?>
    <?php if (!$secure || (isset ($_SESSION['auth']) && $_SESSION['auth']->canRead($info) && $_SESSION['auth']->canWrite($info))) { ?>
        <div id="<?php echo $id ?>_input" style="display:none">
            <TEXTAREA id="<?php echo $id ?>_area" wrap=off
                      style="width:700px;height:300px;"><?php echo $contents[$id] ?></TEXTAREA>
            <script type='text/javascript'>
                var height = document.getElementById('<?php echo $id?>_code').firstElementChild.offsetHeight;
                document.getElementById('<?php echo $id?>_area').style.height = height + "px";
            </script>
        </div>
    <?php } ?>
    <br>
    <?php if (!$secure || (isset ($_SESSION['auth']) && $_SESSION['auth']->auth == "OK"))   { ?>
    <br>
</div>
<?php } ?>
<?php
}
?>
</div>