"use strict";

//create a module
var app = angular.module('myRestApp', []);

//add a controller to the module
app.controller('myCtrl', ['$scope', function($scope) {
	$scope.firstName = "";
	$scope.lastName = "";
	$scope.address = "";
	$scope.company = "";
	$scope.showDB = false;
	
	//erase 
  $scope.erase = function() {
  	$scope.firstName = "";
  	$scope.lastName = "";
  	$scope.address = "";
  	$scope.company = "";
  	$scope.showDB = false;
  };
  
  //create a json string for ajax call
  $scope.buildJsonStr = function () {
  	var json = {
  			"firstName": $scope.firstName,
  			"lastName": $scope.lastName,
  			"address": $scope.address,
  			"company": $scope.company	
  	}
  	return JSON.stringify(json);
  };
  
  //call rest api to save data
  $scope.save = function() {
  	var jsonStr = $scope.buildJsonStr();
  	
  	var xhttp = new XMLHttpRequest();
  	xhttp.onreadystatechange = function() {
  		if (this.readyState == 4) {
  			if (this.status == 200) {
  				$scope.processData(this.responseText);
  				$scope.$apply();
  			} else {
  				alert("Something is wrong: " + this.status);
  			}			
  		} 
  	};
  	xhttp.open("POST", "/myRestApp/rest/cioapi", true);
  	xhttp.send(jsonStr);
  };
  
  //update model using the response data
  $scope.processData = function(jsonStr) {
  	var jsonObj = JSON.parse(jsonStr);
  	$scope.firstName = jsonObj.firstName[0];
  	$scope.lastName = jsonObj.lastName[0];
  	$scope.address = jsonObj.address[0];
  	$scope.company = jsonObj.company[0];	
  	$scope.showDB = true;
  };
  
}]);
