<?php
$username = $_SERVER['PHP_AUTH_USER'];
$password = $_SERVER["PHP_AUTH_PW"];

if($username == "username" && $password == "password"){
    echo "true";
}
else{
    echo"false";
}