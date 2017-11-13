<?php

ini_set('display_errors', '1');
error_reporting(E_ALL);
	// Connect to the Database
	$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=aldrich7';
	$username = 'aldrich7';
	$password = 'vesyop';
	$email = $_GET['email'];
	$code = $_GET['code'];
	
	$db = new PDO($dsn, $username, $password);
	
	$select_sql = "SELECT username, pwd FROM betterUser WHERE username = '$email' AND vericode = '$code'";
	$user_query = $db->query($select_sql);
	$users = $user_query->fetchAll(PDO::FETCH_ASSOC);
	try {
		if ($users) {
			#start an array to hold the results
			$result = array("code"=>100, "message" =>"UserName Found", "size"=>count($users));
			$user_array = array();
			#iterate through the results
			for ($i = 0; $i < count($users); $i++) { 
				$update_sql = "UPDATE betterUser SET active = 'y' WHERE username = '$email'";
				$user_query2 = $db->query($update_sql);
				$users2 = $user_query2->fetchAll(PDO::FETCH_ASSOC);
				if ($users2) {
					#start an array to hold the results
					$result = array("code"=>300, "message" =>"UserName Found", "size"=>count($users));
					$user_array = array();
				}
			}
			
		} else {
		
			$result = array("code"=>200, "message" =>"Not Verified", "size"=>count($users));
		}
		echo json_encode($result);
		$db = null;
	} catch (PDOException $e) {
		$error_message = $e->getMessage();
		$result = array("code"=>400, "message"=>"There was an error connecting to
							the database: $error_message");
		echo json_encode($result);
		exit();
	}
?>
