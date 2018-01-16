function sweetAlertService() {
    this.success = function(title, message) {
        swal(title, message,'success');
    };

    this.error = function(title, message) {
        swal(title, message,'error');
    };

    this.warning = function(title, message) {
        swal(title, message,'warning');
    };

    this.info = function(title, message) {
        swal(title, message,'info');
    };

    this.custom = function (configObject) {
        swal(configObject);
    }
};

/* Create our angular app */
var angularApp = angular.module('app.sidebar');

/* add the service and the controller */
angularApp
    .service('swalService', sweetAlertService)
    