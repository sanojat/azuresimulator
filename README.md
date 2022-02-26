
1##########################
Rest endpoint to thread list configured in configuration.json
URL: 
http://localhost:8080/simulator/thread/list
METHOD : GET

2###########################
Rest endpoint to understand Thread status
URL: 
http://localhost:8080/simulator/thread/status
METHOD : GET
	
3##########################
Rest endpoint to run and stop threads

URL 
http://localhost:8080/simulator/thread/action
METHOD : POST

To get threadName,you can run "http://localhost:8080/simulator/thread/list" and frame message body as per requirement,

MESSAGE BODY eg:

	[
	{
		"threadName": "device1-patient1-temperature",
		"action": "RUN"
	},
	{
		"threadName": "device1-patient1-humidity",
		"action": "STOP"
	},
	{
		"threadName": "device2-patient2-temperature",
		"action": "RUN"
	},
	{
		"threadName": "device2-patient2-humidity",
		"action": "STOP"
	}
   ]