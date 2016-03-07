(function(){
	var mealsApp = angular.module('entries', ['ngMessages']);
		
	mealsApp.controller('EntriesCtrl', function($scope, $filter, addTimezoneService, removeTimezoneService, updateTimezoneService) {
		this.$messages = {}
		this.editedTimezone = {};
		this.editMode = false;
		this.calories = $scope.$parent.$parent.calories;
		this.selectedIndex = -1;
		this.dailyCaloriesCount = {};
		this.filters = {};
		this.list = {};
		this.selectedUser = null;
		
		this.setEditMode = function(value) {
			this.init();
			this.editMode = value;
		};
		this.isEditMode = function(meal) {
			if(!this.editMode) return false;
			if(!meal) return false;
			
			if( !this.editMode.id && !meal.id ) {
				return true;
			}
			
			if(this.editMode.id === meal.id) {
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
					$scope.editTimezone.selectedUser = $scope.calories.loggedUser;
				}
				$scope.editTimezone.init();	
			}
		});
		$scope.$on("logout", function(event, args){
			$scope.editTimezone.list = [];
		});

	});
	
})();