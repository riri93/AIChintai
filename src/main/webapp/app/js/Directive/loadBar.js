/**
 * modal directive
 */
(function() {
	'use strict';

	angular.module('app.core').directive('showDuringResolve', Directive);

	function Directive($rootScope) {
		
		
		  return {
			    link: function(scope, element) {

			      element.addClass('ng-hide');

			      var unregister = $rootScope.$on('$routeChangeStart', function() {
			    	  console.log("change eeeeeeeeeeeeeeee route ")
			        element.removeClass('ng-hide');
			      });

			      scope.$on('$destroy', unregister);
			    }
			  };
		
		
	
	}

})();
