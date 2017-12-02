<?php
ini_set('display_errors', '1');
error_reporting(E_ALL);
	// Connect to the Database
	$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=aldrich7';
	$username = 'aldrich7';
	$password = 'vesyop';
	$userTest = $_POST['userTest'];
	$passTest = $_POST['passTest'];
	
	try {
		#make a new DB object to interact with
		$db = new PDO($dsn, $username, $password);
		#build a SQL statement to query the DB
		$select_sql = "SELECT username, pwd FROM betterUser WHERE username = '$userTest' AND pwd = '$passTest'"; #fix this
		#make a query object
		$user_query = $db->query($select_sql);
		#run the query on the DB
		$users = $user_query->fetchAll(PDO::FETCH_ASSOC);
		#check to see if the db returned any values
		if ($users) {
			#start an array to hold the results
			$result = array("code"=>100, "message" =>"Login Successful", "size"=>count($users));
			$user_array = array();
			#iterate through the results
			
			$second_select_sql = "SELECT username, pwd FROM betterUser WHERE username = '$userTest' AND pwd = '$passTest' AND active = 'y'";
			 
			$second_user_query = $db->query($second_select_sql);

			$second_user = $second_user_query->fetchAll(PDO::FETCH_ASSOC);
			
			if($second_user){
				
				$result = array("code" => 400, "message" =>"They are verified", "size"=>count($second_user));
			}
			
			else{
			
				$result = array("code" => 500, "message" =>"They are not verified", "size"=>count($second_user));
			
			}
			
		} else {
			$result = array("code"=>200, "message"=>"Username or Password not found");
		}
		echo json_encode($result);
		$db = null;
	} catch (PDOException $e) {
		$error_message = $e->getMessage();
		$result = array("code"=>300, "message"=>"There was an error connecting to
							the database: $error_message");
		echo json_encode($result);
		exit();
	}
?>