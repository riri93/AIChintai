
(function() {
	'use strict';

	angular.module('app.core').filter('date', function($filter) {
		return function(input) {
			if (input == null) {
				return "";
			}
			var _date = $filter('date')(new Date(input), 'yyyy-MM-dd');
			return _date.toUpperCase();
		};
	});
})();
