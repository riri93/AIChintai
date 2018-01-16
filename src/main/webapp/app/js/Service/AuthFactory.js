(function() {
	'use strict';

	angular.module('app.routes').factory('authHttpResponseInterceptor', authHttpResponseInterceptor);
	authHttpResponseInterceptor.$inject = [  '$location','$q','$localStorage', '$window' ];
	function authHttpResponseInterceptor($q, $location, $localStorage, $window) {
		return {
			response : function(response) {
				if (response.status === 401
						|| response.status === 406) {
					console.log("Response 401");
				}
				return response || $q.when(response);
			},
			request : function(config) {
				if ($localStorage.currentUser) {
					config.headers.authorization = 'Bearer '
							+ $localStorage.currentUser.token;

				}

				return config;

			},
			responseError : function(rejection) {
				console.log("Response");
				if (rejection.status === 401
						|| rejection.status === 406) {
					console.log("Response Error 401",
							$location.$$path);

					function aa(localStorage, callback) {
						localStorage.currentUser = null;
						callback();

					}
					;
					if ($location.$$path !== '/login') {
						aa($localStorage, function() {

							$window.location.assign('/login');
						});
					}

				}
				return $q.reject(rejection);
			}
		}
	}
})();
