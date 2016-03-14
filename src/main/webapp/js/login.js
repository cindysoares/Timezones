(function() {
	
	var loginApp = angular.module('login', ['ngMessages']);	
	
	loginApp.factory('loginFactory', function($http) {
		var myService = {
				async: function (emailValue, passwordValue) {
					var promise = $http.post('/login', {email: emailValue, password: passwordValue}, 
							{headers: {'Content-Type': "application/x-www-form-urlencoded"},
							 transformRequest: function(obj) {
					                var str = [];
					                for(var p in obj)
					                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
					                return str.join("&");
					            }
							})
							.then(function(response){
								return response.data;
							});
					return promise;
				} // FIXME Don´t pass password as a param.
		};
		return myService;
	});
	
	loginApp.controller('LoginCtrl', function(loginFactory, $scope) {
		this.email = "cindy@email.com"; // FIXME remove this value 
		this.password = "senha"; // FIXME remove this value 
		this.$messages = {};
		this.submit = function() {
			loginFactory.async(this.email, this.password).then(function(d) {				
				if(!d) {
					$scope.login.$messages.invalidLogin = true;
				} else {
					$scope.timezones.login(d);
					$scope.login.$messages = {}
				}
			});
		};
	});  
	
})();