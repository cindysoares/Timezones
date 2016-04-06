(function(){
	var app = angular.module('timezones', ['entries', 'login', 'users', 'register', 'ngCookies']);
	
	var visibleTabsRoles = { USER: ['timezones'], 
            USER_MANAGER: ['users'],
            ADMIN_MANAGER: ['timezones', 'users'] };
	
	app.factory('loggedUserFactory', function($http) {
		var myService = {
				getLoggedUser: function() {
					return $http.get('/login');
				}
		};
		return myService;
	});	
	
	app.controller('TimezonesController', function($scope, $http, $cookies, loggedUserFactory){
		$http.defaults.headers.common.Authorization = $cookies.get("authorizationToken");
		this.loggedUser;
		this.loggingShowing = $cookies.get("loggingShowing")==='true';
		this.registeringShowing = $cookies.get("registeringShowing")==='true';
		this.logout = function() {
			this.loggedUser = null;
			this.updateCookies();
			$scope.logout();
		};
		this.showLogin = function() {
			this.loggingShowing = true;
			this.registeringShowing = false;
			this.updateCookies();
		};
		this.showRegister = function() {
			this.registeringShowing = true;
			this.loggingShowing = false;
			this.updateCookies();
		};
		this.login = function(user) {
			this.loggedUser = user;
			this.loggingShowing = false;
			this.registeringShowing = false;
			this.updateCookies();
			$scope.loginSuccess();
		};
		this.updateCookies = function() {
			$cookies.put("registeringShowing", this.registeringShowing);
			$cookies.put("loggingShowing", this.loggingShowing);
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
			loggedUserFactory.getLoggedUser().then(function(response) {				
				if(response.data) {
					$scope.timezones.login(response.data);
				} else {
					$scope.timezones.logout();
				}
			}, function() {
				$scope.timezones.logout();
			});	
		}		
	});
	
	app.controller('SectionController', function($scope, $cookies) {
		this.selectedTab = $cookies.get("selectedTab");
		this.visibleTabs = [];
		this.selectedUser = null;
		this.setTab = function(newTab) {
			this.selectedTab = newTab;
			this.updateCookies();
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
		this.updateCookies = function() {
			if(this.selectedTab !== null && this.selectedTab !== 'null') {
				$cookies.put("selectedTab", this.selectedTab);
			} else {
				$cookies.remove("selectedTab");
			}
		}
		$scope.tabSelected = function(){
		   $scope.$broadcast("tabSelected", {selectedTab: $scope.section.selectedTab});
		};
		$scope.$on("loginSuccess", function(event, args){
			var userProfile = args.loggedUser.profile;
			$scope.section.visibleTabs = visibleTabsRoles[userProfile];
			$scope.section.setTab($scope.section.selectedTab?$scope.section.selectedTab:$scope.section.visibleTabs[0]);
		});
		$scope.$on("logout", function(){
			$scope.section.selectedUser = null;
			$scope.section.setTab(null)
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