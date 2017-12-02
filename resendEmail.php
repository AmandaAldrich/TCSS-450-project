<?php

ini_set('display_errors', '1');
error_reporting(E_ALL);

	// Connect to the Database
	$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=aldrich7';
	$username = 'aldrich7';
	$password = 'vesyop';
	$userTest = $_POST['userTest'];
	$coder = 459343;
	
	try {
		$db = new PDO($dsn, $username, $password);
		#make a new DB object to interact with
		 $select_sql = 'SELECT username, vericode FROM betterUser WHERE username="$userTest"';  
        #make a query object
        $user_query = $db->query($select_sql);
        #run the query on the DB
        $users = $user_query->fetchAll(PDO::FETCH_ASSOC);
        #check to see if the db returned any values
        if ($users) {
            $results = array("code"=>100, "message" =>"User found");
			sendEmail($userTest, $users['vericode']);
		} else {
			    $result = array("code"=>200, "message"=>"register failed");
            }

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
	
	function sendEmail($email, $coder){
	
		$to = "$email";
		$subject = "Campanion Verification Link";
		$txt = " Here is your link: cssgate.insttech.washington.edu/~aldrich7/verify.php?email=".$email."&code=".$coder;
		$headers = "From: CampanionTeam@Group4.com";
	
		mail($to, $subject, $txt, $headers);
	}

?>