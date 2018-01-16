/**
 * modal directive
 */
(function() {
	'use strict';

	angular.module('app.core').directive('modal', Directive);

	function Directive(ModalService) {
		return {
			link : function(scope, element, attrs) {
				// ensure id attribute exists
				if (!attrs.id) {
					console.error('modal must have an id');
					return;
				}

				// move element to bottom of page (just before </body>) so it
				// can be displayed above everything else
				element.appendTo('body');

				// close modal on background click

				// add self (this modal instance) to the modal service so it's
				// accessible from controllers
				var modal = {
					id : attrs.id,
					open : Open,
					close : Close
				};
				ModalService.Add(modal);

				// remove self from modal service when directive is destroyed
				scope.$on('$destroy', function() {
					ModalService.Remove(attrs.id);

					element.remove();
				});

				// open modal
				function Open() {
					console.log("sdfsdf");
					$("#" + attrs.id).modal();
					// element.show();
					// $('body').addClass('modal-open');
				}

				// close modal
				function Close() {
					$("#" + attrs.id).modal('hide');

					// element.hide();
					// $('body').removeClass('modal-open');
				}
			}
		};
	}

})();
