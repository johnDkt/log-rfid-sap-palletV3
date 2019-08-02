<?php

class AutoValidationBean {
    private $dateStart;
    private $dateEnd;

    private $analyzedLines;

    private $nbSearch;
    private $nbValid;
    private $nbQuit;
    private $nbReset;
    private $nbOtherProblem;

    private $analyzedFiles;

    public function AutoValidationBean() {}

    public function getAnalyzedFiles() {
        return $this->analyzedFiles;
    }

    public function setAnalyzedFiles($analyzedFile) {
        $this->analyzedFiles = $analyzedFile;
    }

    public function getNbSearch() {
        return $this->nbSearch;
    }

    public function setNbSearch($nbSearch) {
        $this->nbSearch = $nbSearch;
    }

    public function getNbValid() {
        return $this->nbValid;
    }

    public function setNbValid($nbValid) {
        $this->nbValid = $nbValid;
    }

    public function getNbQuit() {
        return $this->nbQuit;
    }

    public function setNbQuit($nbQuit) {
        $this->nbQuit = $nbQuit;
    }

    public function getNbReset() {
        return $this->nbReset;
    }

    public function setNbReset($nbReset) {
        $this->nbReset = $nbReset;
    }

    public function getNbOtherProblem() {
        return $this->nbOtherProblem;
    }

    public function setNbOtherProblem($nbOtherProblem) {
        $this->nbOtherProblem = $nbOtherProblem;
    }

    public function getDateStart() {
        return $this->dateStart;
    }

    public function getDateEnd() {
        return $this->dateEnd;
    }

    public function setDateStart($dateStart) {
        $this->dateStart = $dateStart;
    }

    public function setDateEnd($dateEnd) {
        $this->dateEnd = $dateEnd;
    }

    public function setAnalyzedLines($analyzedLines) {
        $this->analyzedLines = $analyzedLines;
    }

    public function getAnalyzedLines() {
        return $this->analyzedLines;
    }
}

class FileValidator {
    private $EXTENSION_AVAILABLE = array('log');

    private function checkErrorFiles(array $filesContent) {
        foreach($filesContent as $filename => $content) {
            if (empty($content)) {
                unset($filesContent[$filename]);
            }
        }
        return $filesContent;
    }

    private function checkExtension($file) {
        $pathInfo = pathinfo($file);
        foreach($this->EXTENSION_AVAILABLE as $ext) {
            if ($pathInfo['extension'] === $ext) {
                return true;
            }
        }
        return false;
    }

    private function getFile($file) {
        if (file_exists($file) && $this->checkExtension($file)) {
            try {
                return utf8_encode(file_get_contents($file));
            } catch (Exception $e) {
                echo $e->getFile() . "<br>" . $e->getMessage();
                return '';
            }
        } else {
            return '';
        }
    }

    public function getFiles(array $files) {
        $filesContent = array();
        foreach($files as $filename => $file) {
            $filesContent[$filename] = $this->getFile($file);
        }
        return $this->checkErrorFiles($filesContent);
    }
}

class AutoValidationService {

    const RESET = "RESET";
    const SEARCH = "SEARCH";
    const VALID = "VALID";
    const QUIT = "QUIT";
    const DELIMITER = "|";
    const DATE_FORMAT = "Y-m-d";

    private $fileValidator;

    public function AutoValidationService() {
        $this->fileValidator = new FileValidator();
    }

    public function getFileValidator() {
        return $this->fileValidator;
    }

    private function extractLogFromPeriod($content, $dateStart, $dateEnd) {
        $arrayLines = $this->transformFileContentToArray($this->createMainStringFromFilesContent($content));
        $logFilesContent = array();
        $dateCurrent = $dateEnd;
        while (strtotime($dateCurrent) < strtotime($dateStart)) {
            $logFilesContent = array_merge($logFilesContent, preg_grep("/$dateCurrent/", $arrayLines));
            $dateCurrent = date($this::DATE_FORMAT, strtotime($dateCurrent.' +1day'));
        }
        return $logFilesContent;
    }

    private function createMainStringFromFilesContent(array $filesContent) {
        $string = '';
        foreach($filesContent as $content) {
            $string .= $content;
        }
        return $string;
    }

    private function transformFileContentToArray($content) {
        return preg_split("/\n/", $content);
    }

    private function isStrInOtherOne($line, $str) {
        return preg_match("/$str/", $line);
    }

    private function countNbStr(array $array, $str) {
        $nb = 0;
        foreach($array as $line) {
            if ($this->isStrInOtherOne($line, $str)) {
                ++$nb;
            }
        }
        return $nb;
    }

    private function extractNbReset(array $content) {
        return $this->countNbStr($content, $this::RESET);
    }

    private function extractNbSearch(array $content) {
        return $this->countNbStr($content, $this::SEARCH);
    }

    private function extractNbValid(array $content) {
        return $this->countNbStr($content, $this::VALID);
    }

    private function extractNbQuit(array $content) {
        return $this->countNbStr($content, $this::QUIT);
    }

    public function calculateAverageBetweenNbSearchAndNbValid(AutoValidationBean $autoValidationBean) {
        $nbSearch = $autoValidationBean->getNbSearch() - $autoValidationBean->getNbReset();
        $nbValid = $autoValidationBean->getNbValid();
        if ($nbValid === 0) return 0;
        else return $nbValid /$nbSearch;
    }

    public function calculatePercentBetweenNbSearchAndNbValid(AutoValidationBean $autoValidationBean) {
        $nbSearch = $autoValidationBean->getNbSearch() - $autoValidationBean->getNbReset();
        $nbValid = $autoValidationBean->getNbValid();
        if ($nbValid === 0) return 0;
        else return ($nbValid / $nbSearch) * 100;
    }

    private function extractDate($line) {
        preg_match("/^[0-9-: ]+/", $line, $array);
        return strtotime($array[0]);
    }

    public function calculateAverageValidationTime(AutoValidationBean $autoValidationBean) {
        $content = $autoValidationBean->getAnalyzedLines();
        $average = 0;
        $countValidation = 0;
        $searchDate = "";
        $searchStart = false;
        foreach($content as $line) {
            if ($this->isStrInOtherOne($line, $this::SEARCH)) {
                $searchDate = $this->extractDate($line);
                $searchStart = true;
            } else if ($this->isStrInOtherOne($line, $this::VALID) && $searchStart) {
                $validDate = $this->extractDate($line);
                $average += $validDate - $searchDate;
                ++$countValidation;
                $searchStart = false;
            } else {
                $searchStart = false;
            }
        }

        return ($countValidation !== 0)?($average / $countValidation):(0);
    }

    public function printInformationAboutAnalyzedLines(AutoValidationBean $autoValidationBean) {
        $arrayRes = array();
        $content = $autoValidationBean->getAnalyzedLines();
        foreach($content as $line) {
            preg_match('/^([0-9-: ]+).*?([0-9]+)\|(.+)$/', $line, $arrayInformation);
            $arrayRes[] = array(
                'TIME' => $arrayInformation[1],
                'UAT' => $arrayInformation[2],
                'STATUS' => $arrayInformation[3]);
        }
        return $arrayRes;
    }

    public function extractAutoValidationInformationFromLogFile(array $file, $dateStart = null, $dateEnd = null) {
        if (!isset($dateStart)) {
            $dateStart = date($this::DATE_FORMAT, strtotime("today"));
        }

        if (!isset($dateEnd)) {
            $dateEnd = date($this::DATE_FORMAT, strtotime($dateStart." -1month"));
        }

        $filesContent = $this->getFileValidator()->getFiles($file);
        if(empty($filesContent)) {
            return null;
        }

        $content = $this->extractLogFromPeriod($filesContent, $dateStart, $dateEnd);

        $autoValidationBean = new AutoValidationBean();
        $autoValidationBean->setDateStart($dateStart);
        $autoValidationBean->setDateEnd($dateEnd);
        $autoValidationBean->setAnalyzedLines($content);
        $autoValidationBean->setAnalyzedFiles(array_keys($filesContent));
        $autoValidationBean->setNbSearch($this->extractNbSearch($content));
        $autoValidationBean->setNbReset($this->extractNbReset($content));
        $autoValidationBean->setNbValid($this->extractNbValid($content));
        $autoValidationBean->setNbQuit($this->extractNbQuit($content));

        return $autoValidationBean;
    }

}

$log_files = array('log-rfid-pallet-actions.log' => '/data/logs/log-rfid-pallet/log-rfid-pallet-actions.log',
    'log-rfid-pallet-actions.log.1' => '/data/logs/log-rfid-pallet/log-rfid-pallet-actions.log.1',
    'log-rfid-pallet-actions.log.2' => '/data/logs/log-rfid-pallet/log-rfid-pallet-actions.log.2');
$autoValidationService = new AutoValidationService();
$autoValidationBean = $autoValidationService->extractAutoValidationInformationFromLogFile($log_files);

?>

<!DOCTYPE html>
<html xml:lang="en_EN" lang="en_EN">
<head>
    <meta charset=utf-8 />
    <title>Validation statistics</title>
    <link type='text/css' href='css/bootstrap.min.css' rel='stylesheet'/>
    <link type='text/css' href='css/bootstrap-table.min.css' rel='stylesheet'/>
    <link type='text/css' href='css/table.css' rel='stylesheet'/>
</head>

<body style="background: none repeat scroll 0% 0% rgb(255, 255, 255);">
<section>
    <header>Analyzed files</header>
    <article>
        <p>
            <?php
            $resTmp = $autoValidationBean->getAnalyzedFiles();
            foreach($resTmp as $analyzedFile) {
                echo $analyzedFile."\t";
            }
            ?>
        </p>
    </article>
</section>
<section>
    <header>Information about reading's statistics</header>
    <article>
        <p>From <?php echo $autoValidationBean->getDateEnd()?> to <?php echo $autoValidationBean->getDateStart()?></p>
        <p>Number of pallets' reading : <mark><?php echo $autoValidationBean->getNbSearch(); ?></mark></p>
        <p>Number of cancellation  of pallets' reading :
            <mark><?php echo $autoValidationBean->getNbReset(); ?></mark>
            &nbsp;&nbsp;<span class="text-muted"><em>Number of reset of the reading</em></span>
        </p>
        <p>Number of interruption of pallets' reading :
            <mark><?php echo $autoValidationBean->getNbQuit(); ?></mark>
            &nbsp;&nbsp;<span class="text-muted"><em>Number of shutdown of the application</em></span>
        </p>
        <p>Validation rate : <mark><?php
                $resTmp = $autoValidationService->calculatePercentBetweenNbSearchAndNbValid($autoValidationBean);
                echo number_format($resTmp, 2, ',', ' ');
                unset($resTmp);
                ?> %
            </mark></p>
        <p>Average validation time: <mark><?php
                $resTmp = $autoValidationService->calculateAverageValidationTime($autoValidationBean);
                echo number_format($resTmp, 2, ',', ' ');
                unset($resTmp);
                ?> s
            </mark></p>
    </article>
</section>
<section>
    <header>Details of reading</header>
    <article>
        <button type="button" id="showAnalyzedLines" class="">Show details</button>
        <div id="analyzedLines">
            <table class="table table-bordered table-hover table-striped" data-toggle="table" data-sort-name="table-time" data-sort-order="asc"
                   data-pagination="true" data-page-size="25" data-page-list="[25, 50, 75, 100]" data-search="true">
                <caption>Analyzed lines from files' log</caption>
                <thead>
                <tr>
                    <th scope="col" data-field="table-time" data-sortable="true">Time</th>
                    <th scope="col" data-field="table-uat" data-sortable="true">UAT</th>
                    <th scope="col" data-field="table-status" data-sortable="true">Status</th>
                </tr>
                </thead>
                <tbody>
                <?php
                $resTmp = $autoValidationService->printInformationAboutAnalyzedLines($autoValidationBean);
                foreach($resTmp as $arrayInformationAboutAnalyzedLine) { ?>
                    <tr>
                        <td><?php echo $arrayInformationAboutAnalyzedLine['TIME'] ?></td>
                        <td><?php echo $arrayInformationAboutAnalyzedLine['UAT'] ?></td>
                        <td><?php echo $arrayInformationAboutAnalyzedLine['STATUS'] ?></td>
                    </tr>
                <?php }
                unset($resTmp);
                ?>
                </tbody>
            </table>
        </div>
    </article>
</section>
<script type='text/javascript' src="js/jquery-1.11.3.min.js" ></script>
<script type='text/javascript' src="js/bootstrap.min.js" ></script>
<script type='text/javascript' src="js/bootstrap-table.min.js" ></script>
<script type='text/javascript' src="js/bootstrap-table-en-US.min.js" ></script>
<script type="text/javascript">
    $('#analyzedLines').hide();
    $('#showAnalyzedLines').click(function () {
        $('#analyzedLines').toggle();
    });
</script>
</body>

</html>