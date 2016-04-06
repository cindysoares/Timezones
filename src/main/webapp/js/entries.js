(function(){
	var timezonesApp = angular.module('entries', ['ngMessages']);
		
	timezonesApp.factory('timezonesService',  function($http) {
		var myService = {
				findAll: function () {
					return $http.get("/timezones");
				},
				add: function (timezone) {
					return $http.post("/timezones", timezone);
				},
				remove: function (timezoneId) {
					return $http.delete("/timezones/" + timezoneId);
				},
				update: function (timezone) {
					return $http.put("/timezones/" + timezone.id, timezone);
				}
		};
		return myService;
	});
	
	timezonesApp.filter('filterByName', function(){
		return function(timezones, filterName) {
			var filteredTimezones = [];
			var filterNameToUpperCase = filterName? filterName.toUpperCase():null;
			angular.forEach(timezones, function(timezone) {
			      if (!filterName || timezone.name.toUpperCase().includes(filterNameToUpperCase)) {
			    	  filteredTimezones.push(timezone);
			      }			      
			});
			return filteredTimezones;
		}
	});
	
	timezonesApp.controller('EntriesCtrl', function($scope, timezonesService) {
		this.$messages = {}
		this.editedTimezone = {};
		this.editMode = false;
		this.timezones = $scope.$parent.$parent.timezones;
		this.selectedIndex = -1;
		this.list = {};
		this.selectedUser = null;
		this.currentDate = Date.now();
		
		this.setEditMode = function(value) {
			this.init();
			this.editMode = value;
		};
		this.isEditMode = function(timezone) {
			if(!this.editMode || !timezone) return false;			
			if( !this.editMode.id && !timezone.id ) return true;			
			return (this.editMode.id === timezone.id);
		};
		this.init = function() {
			timezonesService.findAll().then(function(result) {
				$scope.editTimezone.list = result.data;
			}, function(error) {
				if(error.status===401) {
					$scope.timezones.logout(); 
					return;
				}
				$scope.editTimezone.list = null;
				$scope.editTimezone.$messages.error = true;
			});
			this.editMode = false;
			this.editedTimezone = {};
			this.$messages = {};
			this.filters = {};
		};
		this.removeTimezone = function(timezoneToRemove) {
			this.selectedIndex = -1
			for( var i = 0; i < this.list.length; i++ ) {
				if( this.list[i].id === timezoneToRemove.id ) {
					this.selectedIndex = i;
					break;
				}
			}
			if( this.selectedIndex === -1 ) {
				this.$messages.warning = true;
			}
			timezonesService.remove(timezoneToRemove.id).then(function(response) {
				if (response.data) {
					$scope.editTimezone.list.splice( $scope.editTimezone.selectedIndex, 1 );
					$scope.editTimezone.$messages.deleteSuccess = true;
				} else {
					$scope.editTimezone.$messages.warning = true;
				}
			}, function(error){
				$scope.editTimezone.$messages.error = true;
			});			
		};
		this.addTimezone = function() {
			this.editedTimezone.userId = $scope.timezones.loggedUser.id;
			timezonesService.add(this.editedTimezone).then(function(response){
				if (response.data != null) {
					$scope.editTimezone.list.push(response.data);
					$scope.editTimezone.editedTimezone = {};
					$scope.editTimezone.editMode = false;
					$scope.editTimezone.$messages.saveSuccess = true;
				} else {
					$scope.editTimezone.$messages.warning = true;
				}
			}, function(error){
				$scope.editTimezone.$messages.error = true;
			});			
		};
		this.updateTimezone = function(timezoneToUpdate) {
			this.selectedIndex = this.list.indexOf(timezoneToUpdate);
			timezonesService.update(timezoneToUpdate).then(function(response){				
				if (response.data != null) {
					$scope.editTimezone.list.splice( $scope.editTimezone.selectedIndex, 1 );
					$scope.editTimezone.list.push(response.data);
					$scope.editTimezone.editMode = false;
					$scope.editTimezone.$messages.updateSuccess = true;
				} else {
					$scope.editTimezone.$messages.warning = true;
				}
			}, function(error){
				$scope.editTimezone.$messages.error = true;
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