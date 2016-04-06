(function() {
	var registerApp = angular.module('register', ['login', 'ngMessages']);
	
	registerApp.factory('userAddService', function($http) {
		var myService = {
				async(newUser) {
					var promise = $http.post("/users/regular", newUser)
					.then(function(response){
						return response.data;
					});
					return promise;
				}
		};
		return myService;
	});
	
	registerApp.controller('RegisterCtrl', function($scope, userAddService) {
		this.$messages = {};
		this.newUser = {};
		this.save = function() {
			if(this.newUser.password !== this.newUser.repeatedPassword) {
				this.$messages.warning = true;
				return;
			}			
			userAddService.async(this.newUser).then(function(data) {
				if(data != null) {
					$scope.register.$messages.saveSuccess = true;
					$scope.register.newUser = {};
					$scope.timezones.showLogin(data);
				} else {
					$scope.register.$messages.warning = true;
				}
			});
			this.repeatedPassword = null;
		};
	});
	
})();