(function() {
	
	var loginApp = angular.module('login', ['ngMessages', 'ngCookies']);	
	
	loginApp.factory('loginFactory', function($http) {
		var myService = {
				login(emailValue, passwordValue) {
					var promise = $http.post('/login', {email: emailValue, password: passwordValue},
							{transformResponse(obj) {
								return obj;
							}})
							.then(function(response){
								var token = response.data;
								$http.defaults.headers.common.Authorization = token;
								return token;
							});
					return promise;
				},
				getLoggedUser() {
					return $http.get('/login')
						.then(function(response){
							return response.data;
						});
				}
		};
		return myService;
	});
	
	loginApp.controller('LoginCtrl', function(loginFactory, $scope, $cookies) {
		this.email; 
		this.password;  
		this.$messages = {};
		this.submit = function() {
			loginFactory.login(this.email, this.password).then(function(d) {				
				if(!d) {
					$scope.login.$messages.invalidLogin = true;
				} else {
					$cookies.put("authorizationToken", d);
					$scope.login.$messages = {};
					loginFactory.getLoggedUser().then(function(d) {				
						if(!d) {							
							$scope.login.$messages.invalidLogin = true;
						} else {
							$scope.timezones.login(d);
							$scope.login.$messages = {};							
						}
					});					
				}
			}, function() {
				$scope.login.$messages.invalidLogin = true;
			});
		};
	});  
	
}());