(function() {
	var usersApp = angular.module('users', ['ngMessages']);
	
	usersApp.factory('usersFindAllService', function($http) {
		var myService = {
				async: function () {
					var promise = $http.get("/users")
					.then(function(response){
						return response.data;
					});
					return promise;
				}
		};
		return myService;
	});
	
	usersApp.factory('usersAddService', function($http) {
		var myService = {
				async: function (newUser) {
					var promise = $http.post("/users", newUser)
					.then(function(response){
						return response.data;
					});
					return promise;
				}
		};
		return myService;
	});
	
	usersApp.factory('usersRemoveService', function($http) {
		var myService = {
				async: function (userId) {
					var promise = $http.delete("/users/"+userId)
					.then(function(response){
						return response.data;
					});
					return promise;
				}
		};
		return myService;
	});
	
	usersApp.factory('usersUpdateService', function($http) {
		var myService = {
				async: function (user) {
					var promise = $http.put("/users/"+user.id, user)
					.then(function(response){
						return response.data;
					});
					return promise;
				}
		};
		return myService;
	});	
	
	usersApp.controller('UsersCtrl', function($scope, usersFindAllService, usersAddService, usersRemoveService, usersUpdateService) {
		this.list = [];
		this.editMode = false;
		this.newUser = {}
		this.$messages = {};
		this.setEditMode = function(value) {
			this.editMode = value;
		};
		this.isEditMode = function(user) {
			if(!this.editMode || !user) return false;			
			if( !this.editMode.id && !user.id ) return true;			
			return (this.editMode.id === user.id);
		};		
		this.init = function() {
			usersFindAllService.async().then(function(d) {
				$scope.users.list = d;
			});
			this.$messages = {};
			this.sortList();
		};
		this.sortList = function() {
			this.list.sort(function(a, b){return a.name.localeCompare(b.name)});
		};
		this.addUser = function() {
			if(this.newUser.newPassword !== this.repeatedPassword) {
				this.$messages.warning = true;
				return;
			}
			usersAddService.async(this.newUser).then(function(data) {
				if(data != null) {
					$scope.users.list.push(data);
					$scope.users.$messages.saveSuccess = true;
					$scope.users.newUser = {};
					$scope.users.setEditMode(false);
					$scope.users.sortList();
				} else {
					$scope.users.$messages.warning = true;
				}
			}, function errorCallback(response){
				if(response.status===401) {
					$scope.timezones.logout(); 
					return;
				}
				$scope.users.$messages.error = true;
			});
			this.repeatedPassword = null;
		};
		this.removeUser = function(userToRemove) {
			this.selectedIndex = -1
			for( var i = 0; i < this.list.length; i++ ) {
				if( this.list[i].id === userToRemove.id ) {
					this.selectedIndex = i;
					break;
				}
			}
			if( this.selectedIndex === -1 ) {
				this.$messages.warning = true;
			}
			usersRemoveService.async(userToRemove.id).then(function(removed) {
				if (removed) {
					$scope.users.list.splice( $scope.users.selectedIndex, 1 );
					$scope.users.$messages.deleteSuccess = true;
				} else {
					$scope.users.$messages.warning = true;
				}
			}, function errorCallback(response){
				if(response.status===401) {
					$scope.timezones.logout(); 
					return;
				}
				$scope.users.$messages.error = true;
			});			
		};
		this.updateUser = function(userToUpdate) {
			if(userToUpdate.password !== userToUpdate.repeatedPassword) {
				this.$messages = {};
				this.$messages.warning = true;
				return;
			}

			this.selectedIndex = this.list.indexOf(userToUpdate);
			usersUpdateService.async(userToUpdate).then(function(d){				
				if (d != null) {
					$scope.users.list.splice( $scope.users.selectedIndex, 1 );
					$scope.users.list.push(d);
					$scope.users.editMode = false;
					$scope.users.$messages.updateSuccess = true;
					$scope.users.sortList();
				} else {
					$scope.users.$messages = {};
					$scope.users.$messages.warning = true;
				}
			}, function errorCallback(response){
				if(response.status===401) {
					$scope.timezones.logout(); 
					return;
				}
				$scope.users.$messages.error = true;
			});		
		};
		this.listMeals = function(user) {
			$scope.section.setSelectedUser(user);
			$scope.section.setTab('meals');
		}
		
		$scope.$on("tabSelected", function(event, args){
			if (args.selectedTab === 'users') {
				$scope.users.init();	
			}
		});
	});
	
})();