<?php
require_once "conexion.php";
if($_SERVER["REQUEST_METHOD"] == "GET"){
        $c = conexion();
        $s = $c->prepare("SELECT * FROM nivel");
        $s->execute();
        $s->setFetchMode(PDO::FETCH_ASSOC);
        $r = $s->fetch();
        if($r){
            $r = array("datos"=>$r); 
        }else{
        }
        echo json_encode($r);
}

if($_SERVER["REQUEST_METHOD"] == "POST"){
    if(isset($_POST['porcentaje'])){
    try{
    $c = conexion();
    $s = $c->prepare("INSERT INTO nivel values (0, :p, :f)");
    $s->bindValue(":p", $_POST['porcentaje']);
    $s->bindValue(":f", date("Y-m-d H:i:s"));
    $s->execute();
    $s->setFetchMode(PDO::FETCH_ASSOC);
    $r = $s->fetch();
    if($r){
        $r = array("insersion"=>"si"); 
    }else{
        $r = array("insersion"=>"no"); 
    }
    echo json_encode($r);
    print_r($r);
}catch(PDOException $e){
$r = array("insersion"=>"si"); 
}
}
}