(function(){
	var timezonesApp = angular.module('entries', ['ngMessages']);
		
	timezonesApp.factory('findAllService',  function($http) {
		var myService = {
				async: function (userId) {
					var promise = $http.post("/timezones/" + userId, null, {params: {}})
					.then(function(response){
						return response.data;
					});
					return promise;
				}
		};
		return myService;
	});
	
	timezonesApp.factory('addTimezoneService',  function($http) {
		var myService = {
				async: function (userId, timezone) {
					var promise = $http.post("/timezones/add/" + userId, null, 
							{params: {name: timezone.name, city: timezone.city, gmtDifference: timezone.gmtDifference}})
					.then(function(response){
						return response.data;
					});
					return promise;
				}
		};
		return myService;
	});
	
	timezonesApp.factory('removeTimezoneService',  function($http) {
		var myService = {
				async: function (userId, timezoneId) {
					var promise = $http.delete("/timezones/remove/" + userId + "/" + timezoneId)
					.then(function(response){
						return response.data;
					});
					return promise;
				}
		};
		return myService;
	});
	
	timezonesApp.factory('updateTimezoneService',  function($http) {
		var myService = {
				async: function (userId, timezone) {
					var promise = $http.post("/timezones/update/" + userId + "/" + timezone.id, null, 
							{params: {name: timezone.name, city: timezone.city, gmtDifference: timezone.gmtDifference}})
					.then(function(response){
						return response.data;
					});
					return promise;
				}
		};
		return myService;
	});	
	
	timezonesApp.controller('EntriesCtrl', function($scope, $filter, findAllService, addTimezoneService, removeTimezoneService, updateTimezoneService) {
		this.$messages = {}
		this.editedTimezone = {};
		this.editMode = false;
		this.timezones = $scope.$parent.$parent.timezones;
		this.selectedIndex = -1;
		this.filters = {};
		this.list = {};
		this.selectedUser = null;
		this.currentDate = Date.now();
		
		this.setEditMode = function(value) {
			this.init();
			this.editMode = value;
		};
		this.isEditMode = function(timezone) {
			if(!this.editMode) return false;
			if(!timezone) return false;
			
			if( !this.editMode.id && !timezone.id ) {
				return true;
			}
			
			if(this.editMode.id === timezone.id) {
				return true;
			}
			return false;
		};
		this.init = function() {
			findAllService.async(this.selectedUser.id).then(function(result) {
				$scope.editTimezone.list = result;
			});
			this.editMode = false;
			this.editedTimezone = {};
			this.$messages = {};
			this.filters = {};
		};
		this.removeTimezone = function(timezoneToRemove) {
			this.selectedIndex = -1
			var timezonesArray = eval( this.selectedUser.timezones );
			for( var i = 0; i < timezonesArray.length; i++ ) {
				if( timezonesArray[i].id === timezoneToRemove.id ) {
					this.selectedIndex = i;
					break;
				}
			}
			if( this.selectedIndex === -1 ) {
				this.$messages.warning = true;
			}
			removeTimezoneService.async(this.selectedUser.id, timezoneToRemove.id).then(function(removed) {
				if (removed) {
					$scope.editTimezone.selectedUser.timezones.splice( $scope.editTimezone.selectedIndex, 1 );
					$scope.editTimezone.$messages.deleteSuccess = true;
				} else {
					$scope.editTimezone.$messages.warning = true;
				}
			});			
		};
		this.addTimezone = function() {
			addTimezoneService.async(this.selectedUser.id, this.editedTimezone).then(function(d){
				if (d != null) {
					$scope.editTimezone.selectedUser.timezones.push(d);
					$scope.editTimezone.editedTimezone = {};
					$scope.editTimezone.editMode = false;
					$scope.editTimezone.$messages.saveSuccess = true;
				} else {
					$scope.editTimezone.$messages.warning = true;
				}
			});			
		};
		this.updateTimezone = function(timezoneToUpdate) {
			this.selectedIndex = this.selectedUser.timezones.indexOf(timezoneToUpdate);
			updateTimezoneService.async(this.selectedUser.id, timezoneToUpdate).then(function(d){				
				if (d != null) {
					$scope.editTimezone.selectedUser.timezones.splice( $scope.editTimezone.selectedIndex, 1 );
					$scope.editTimezone.selectedUser.timezones.push(d);
					$scope.editTimezone.editMode = false;
					$scope.editTimezone.$messages.updateSuccess = true;
				} else {
					$scope.editTimezone.$messages.warning = true;
				}
			});		
		};		
		$scope.$on("tabSelected", function(event, args){
			if (args.selectedTab === 'timezones') {				
				if($scope.section.selectedUser) {
					$scope.editTimezone.selectedUser = $scope.section.selectedUser;
				} else {
					$scope.editTimezone.selectedUser = $scope.timezones.loggedUser;
				}
				$scope.editTimezone.init();	
			}
		});
		$scope.$on("logout", function(event, args){
			$scope.editTimezone.list = [];
		});

	});
	
})();