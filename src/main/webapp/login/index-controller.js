(function() {
	'use strict';

	angular.module('app').controller('Login.IndexController', Controller);

	function Controller($location, AuthenticationService, $scope) {
		var vm = this;

		vm.login = login;
		// vm.verifEmail = verifEmail;

		initController();

		$scope.verifEmail = function verifEmail() {

			AuthenticationService.verifyEmail(vm.username, function(result) {
				if (result === true) {

				} else {

				}
			});
		}

		function initController() {
			// reset login status
			AuthenticationService.Logout();
		}
		;

		function login() {

			vm.loading = true;
			AuthenticationService.Login(vm.username, vm.password, function(
					result, data) {
				if (result === true) {
					console.log("done");
					$location.path('/createSalon');
				} else {
					if (data.error === 'unauthorized') {
						console.log("unauthorized");
						vm.error = 'Your  account  is not  Verified Yet ';
						vm.loading = false;
					} else {
						
						vm.error = 'Username or password is incorrect';
						vm.loading = false;
					}

				}
			});
		}
		;

	}
})();