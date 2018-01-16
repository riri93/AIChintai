(function() {
	'use strict';

	angular.module('app.core').service('CandidateService', CandidateService);
	CandidateService.$inject = [ '$http', '$location', '$localStorage',
			'mySharedService', '$q' ];
	function CandidateService($http, $location, $localStorage, mySharedService,
			$q) {
		this.getCandidateById = getCandidateById;
		this.updateCandidate = updateCandidate;
		this.getCandidateToShowById = getCandidateToShowById;
		this.saveCandidate = saveCandidate;
		var urlHost = $location.protocol() + "://" + location.host;

		/** ******************************* */
		function saveCandidate(candidate, callback) {
			$http.post(urlHost + '/saveCandidate', candidate).then(
					function(response) {
						callback(true, response.data);
					}, function Error(response) {
						callback(false);
					});
		}

	}
})();
