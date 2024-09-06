<?php
$gradle_properties = file_get_contents('../gradle.properties');
preg_match('/mod_version=(.*)/', $gradle_properties, $matches);
$matches[1] = str_replace(["\r", "\n"], '', $matches[1]);

define('VERSION', $matches[1]);

define('GROUP_ID', 'net.pitan76');
define('ARTIFACT_ID', 'compatdatapacks76');

define('DIRS', array(
    'common' => 'common/build/',
	'fabric' => 'fabric/build/',
	'forge' => 'forge/build/',
	'neoforge' => 'neoforge/build/',
));

foreach (DIRS as $type => $dir) {
    $prefix = ($type == "common" ? '' : '-' . $type);

	$postData = array();
	
	$postData['group_id'] = GROUP_ID;
	$postData['artifact_id'] = ARTIFACT_ID;
	$postData['version'] = VERSION;

	$mavenJava = "maven" . ucfirst($type);


	$files = array(
		$dir . 'libs/' . ARTIFACT_ID . '-' . VERSION . $prefix . '.jar',
		$dir . 'libs/' . ARTIFACT_ID . '-' . VERSION . $prefix . '-sources.jar',
		$dir . 'publications/' . $mavenJava . '/' . ARTIFACT_ID . '.pom',
	);
	
	$pom = '../' . $dir . 'publications/' . $mavenJava . '/' . ARTIFACT_ID . '.pom';



	$pom_str = file_get_contents($pom);
	$pom_str = preg_replace('/' . preg_quote(VERSION . $prefix . '</version>', '/'). '/', VERSION . '</version>', $pom_str, 1);
	file_put_contents($pom, $pom_str);
	
	foreach ($files as $index => $file) {
		$postData['upload[' . $index . ']'] = curl_file_create(
			realpath("../" . $file),
			mime_content_type("../" . $file),
			basename("../" . $file)
		);
		echo "Uploading '" . $file . "'\n";
	}

	$postData['artifact_id'] .= $prefix;
	
	$request = curl_init('http://localhost/maven/maven.php');
	curl_setopt($request, CURLOPT_POST, true);
	curl_setopt($request, CURLOPT_POSTFIELDS, $postData);
	curl_setopt($request, CURLOPT_RETURNTRANSFER, true);
	$result = curl_exec($request);
	
	if ($result === false) {
		error_log(curl_error($request));
	}
	curl_close($request);
}
