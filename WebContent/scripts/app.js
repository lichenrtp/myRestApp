"use strict";

function buildJsonStr() {
	var json = {
			"firstName": $("#fName").val(),
			"lastName": $("#lName").val(),
			"address": $("#address").val(),
			"company": $("#company").val()	
	}
	return JSON.stringify(json);
}

function save() {
	var jsonStr = buildJsonStr();
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if (this.status == 200) {
				var result = processData(this.responseText);
				$("#results").html(result);
			} else {
				alert("Something is wrong: " + this.status);
			}			
		} 
	};
	//xhttp.open("POST", "./mock/save.txt", true);
	//xhttp.send(jsonStr);
	xhttp.open("POST", "/myRestApp/rest/cioapi", true);
	xhttp.send(jsonStr);
}

function erase() {
	$("#fName").val("");
	$("#lName").val("");
	$("#address").val("");
	$("#company").val("");
	$("#results").html("");
}

function processData(jsonStr) {
	var jsonObj = JSON.parse(jsonStr);
	var rtnStr = "First Name: " + jsonObj.firstName[0] + "<br>" +
						"Last Name: " + jsonObj.lastName[0] + "<br>" +
						"Address: " + jsonObj.address[0] + "<br>" +
						"Company: " + jsonObj.company[0] + "<br>";
	return rtnStr;							
}
