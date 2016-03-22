(function(){
	var app = angular.module('timezones', ['entries', 'login', 'users', 'register', 'ngCookies']);
	
	var visibleTabsRoles = { USER: ['timezones'], 
            USER_MANAGER: ['users'],
            ADMIN_MANAGER: ['timezones', 'users'] };
	
	app.factory('loggedUserFactory', function($http) {
		var myService = {
				getLoggedUser: function() {
					return $http.get('/login')
						.then(function(response){
							return response.data;
						});
				}
		};
		return myService;
	});	
	
	app.controller('TimezonesController', function($scope, $http, $cookies, loggedUserFactory){
		$http.defaults.headers.common.Authorization = $cookies.get("authorizationToken");
		this.loggedUser;
		this.loggingShowing = false;
		this.registeringShowing = false;
		this.logout = function() {
			this.loggedUser = null;
			$scope.logout();
		};
		this.showLogin = function() {
			this.loggingShowing = true;
			this.registeringShowing = false;
		};
		this.showRegister = function() {
			this.registeringShowing = true;
			this.loggingShowing = false;
		};
		this.login = function(user) {
			this.loggedUser = user;
			this.loggingShowing = false;
			this.registeringShowing = false;
			$scope.loginSuccess();
		};
		$scope.loginSuccess = function(){
		   $scope.$broadcast("loginSuccess", {loggedUser: $scope.timezones.loggedUser});
		};		
		$scope.logout = function(){
		   $scope.$broadcast("logout", {loggedUser: $scope.timezones.loggedUser});
		   delete $http.defaults.headers.common.Authorization;
		   $cookies.remove("authorizationToken");
		};
		if($http.defaults.headers.common.Authorization) {
			loggedUserFactory.getLoggedUser().then(function(d) {				
				if(d) {
					$scope.timezones.login(d);
				} else {
					$scope.timezones.logout();
				}
			}, function(error) {
				$scope.timezones.logout();
			});	
		}		
	});
	
	app.controller('SectionController', function($scope) {
		this.selectedTab = {};
		this.visibleTabs = [];
		this.selectedUser = null;
		this.setTab = function(newTab) {
			this.selectedTab = newTab;
			$scope.tabSelected();	
		};
		this.setSelectedUser = function(user) {
			this.selectedUser = user;
		};
		this.isSelected = function(tab) {
			return this.selectedTab === tab;
		};
		this.tabIsVisible = function(tab) {
			if(this.visibleTabs.indexOf(tab) >= 0) return true;
			return false;
		}
		$scope.tabSelected = function(){
		   $scope.$broadcast("tabSelected", {selectedTab: $scope.section.selectedTab});
		};
		$scope.$on("loginSuccess", function(event, args){
			var userProfile = args.loggedUser.profile;
			$scope.section.visibleTabs = visibleTabsRoles[userProfile];
			$scope.section.setTab($scope.section.visibleTabs[0]);
		});
		$scope.$on("logout", function(event, args){
			$scope.section.selectedUser = null;
		});		
		
	});
	
	app.directive('login', function() {
		return { restrict: 'E', templateUrl: 'login.html' };
	});
		
	app.directive('register', function() {
		return { restrict: 'E', templateUrl: 'register.html' };
	});

	app.directive('timezones', function(){
		return { restrict: 'E', templateUrl: 'timezones.html' };
	});
	
	app.directive('users', function(){
		return { restrict: 'E', templateUrl: 'users.html' };
	});

	
})();