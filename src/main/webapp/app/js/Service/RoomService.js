(function() {
	'use strict';

	angular.module('app.core').service('RoomService', RoomService);
	RoomService.$inject = [ '$http', '$location', '$localStorage', '$q' ];

	function RoomService($http, $location, $localStorage, $q) {
		this.applyForRoom = applyForRoom;
		this.initApplyRoom = initApplyRoom;
		var urlHost = $location.protocol() + "://" + location.host;

		function initApplyRoom(params) {
			var deferred = $q.defer();
			$http({
				method : 'GET',
				url : urlHost + "/initApply",
				params : params,
				data : null

			}).then(function makeRequestSuccess(resp) {
				deferred.resolve(resp.data);
			}, function makeRequestFailed(resp) {
				deferred.reject(resp.data);
			});

			return deferred.promise;
		}

		function applyForRoom(idRoom, idCandidate, callback) {
			var deferred = $q.defer();
			$http.post(
					urlHost + '/apply?idRoom=' + idRoom + '&idCandidate='
							+ idCandidate).then(
					function makeRequestSuccess(resp) {
						deferred.resolve(resp.data);
					}, function makeRequestFailed(resp) {
						deferred.reject(resp.data);
					});
			return deferred.promise;
		}

	}
})();