(function(){
	var timezonesApp = angular.module('entries', ['ngMessages']);
		
	timezonesApp.controller('EntriesCtrl', function($scope, $filter) {
		this.$messages = {}
		this.editedTimezone = {};
		this.editMode = false;
		this.timezones = $scope.$parent.$parent.timezones;
		this.selectedIndex = -1;
		this.dailyCaloriesCount = {};
		this.filters = {};
		this.list = {};
		this.selectedUser = null;
		
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
			this.list = this.selectedUser.timezones;
			this.editMode = false;
			this.editedTimezone = {};
			this.$messages = {};
			this.filters = {};
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