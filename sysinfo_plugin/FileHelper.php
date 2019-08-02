<?php
    class FileHelper {

        static function get_file_content($file){
            if (file_exists($file)){
                return utf8_encode(file_get_contents($file));
            }else{
                return "$file not found...";
            }
        }

        static function get_file_xml_content($file){
            if (file_exists($file)){
                $s = utf8_encode(file_get_contents($file));
                $replaced = str_replace(array("<",">"),array("&lt;", "&gt;"),$s);
                return $replaced;
            }else{
                return "$file not found...";
            }

        }

        static function save_file($file, $content) {
            return file_put_contents($file, $content);
        }

    }