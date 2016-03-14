(function() {
	
	var loginApp = angular.module('login', ['ngMessages']);	
	
	loginApp.factory('loginFactory', function($http) {
		var myService = {
				login: function (emailValue, passwordValue) {
					var promise = $http.post('/login', {email: emailValue, password: passwordValue},
							{transformResponse: function(obj) {
					                return obj;
					            }
							})
							.then(function(response){
								var token = response.data;
								$http.defaults.headers.common.Authorization = token;
								return token;
							});
					return promise;
				},
				getLoggedUser: function() {
					return $http.get('/login')
						.then(function(response){
							return response.data;
						});
				}
		};
		return myService;
	});
	
	loginApp.controller('LoginCtrl', function(loginFactory, $scope) {
		this.email = "cindy@email.com"; // FIXME remove this value 
		this.password = "senha"; // FIXME remove this value 
		this.$messages = {};
		this.submit = function($http) {
			loginFactory.login(this.email, this.password).then(function(d) {				
				if(!d) {
					$scope.login.$messages.invalidLogin = true;
				} else {
					$scope.login.$messages = {};
					loginFactory.getLoggedUser().then(function(d) {				
						if(!d) {
							$scope.login.$messages.invalidLogin = true;
						} else {
							$scope.timezones.login(d);
							$scope.login.$messages = {}
						}
					});					
				}
			}, function(error) {
				$scope.login.$messages.invalidLogin = true;
			});
		};
	});  
	
})();