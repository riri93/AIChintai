(function() {
	'use strict';

	angular.module('app.core').service('CandidateService', CandidateService);
	CandidateService.$inject = [ '$http', '$location', '$localStorage', '$q' ];

	function CandidateService($http, $location, $localStorage, $q) {
		this.getCandidateById = getCandidateById;
		this.saveCandidate = saveCandidate;
		var urlHost = $location.protocol() + "://" + location.host;

		/** ******************************* */

		function getCandidateById(params) {
			var deferred = $q.defer();
			$http({
				method : 'GET',
				url : urlHost + "/getCandidate",
				params : params,
				data : null
			}).then(function makeRequestSuccess(resp) {
				deferred.resolve(resp.data);
			}, function makeRequestFailed(resp) {
				deferred.reject(resp.data);
			});

			return deferred.promise;
		}

		function saveCandidate(data) {
			var deferred = $q.defer();
			$http({
				method : 'POST',
				url : urlHost + "/saveCandidate",
				params : null,
				data : data
			}).then(function makeRequestSuccess(resp) {
				deferred.resolve(resp.data);
			}, function makeRequestFailed(resp) {
				deferred.reject(resp.data);
			});

			return deferred.promise;
		}

	}
})();
