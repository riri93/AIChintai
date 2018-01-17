/**
 * Apply Controller
 */

(function() {
	'use strict';

	angular.module('app.core').controller('ApplyFormController',
			ApplyFormController);

	function ApplyFormController($http, $scope, $state, RoomService,
			CandidateService, $location, $localStorage, $routeParams,
			swalService, $window, $translate, $filter, $q) {
		var vm = this;
		vm.candidateToPreview = null;
		vm.roomToPreview = null;
		vm.applyForRoom = applyForRoom;
		vm.executeApply = executeApply;
		vm.exist = false;
		vm.preferedDate = null;
		$scope.IsVisible = false;

		vm.preferedTime = [ "午前中", "昼", "午後", "夜", "いつでも " ];

		var url = $location.path().split('/');
		initController();
		vm.data = {};
		vm.params = {};
		function initController() {
			console.log("idRoom: " +url[2]);
			console.log("idCandidate : " +url[3]);
			vm.params={
					"idRoom" : url[2],
					"idCandidate" : url[3]
			},
			
			
			
			RoomService
					.initApplyRoom(vm.params)
					.then(
							function(res) {
								console
										.log('resultat+++++++++++++++++++++++++++++ '
												+ res);
								if (!res.error) {
									console
											.log('exit **************************'
													+ res.exist);
									if (res.exist) {
										vm.candidateToPreview = res.candidateRoomRelation.candidate;
										vm.preferedDate = res.candidateRoomRelation.preferedDate;
										vm.preferedTime = res.candidateRoomRelation.preferedTime;
										console
												.log("******************candidate************: "
														+ vm.candidateToPreview);
										vm.roomToPreview = res.candidateRoomRelation.room;
										console
												.log("******************room************: "
														+ vm.roomToPreview);
										vm.code = vm.roomToPreview.postCode;
										console
												.log("******************postCode************: "
														+ vm.roomToPreview.postCode);
										vm.latitude = vm.roomToPreview.latitudeStation;
										vm.longitude = vm.roomToPreview.longitudeStation;
									} else {
										vm.candidateToPreview = null;
										vm.roomToPreview = res.candidateRoomRelation.room;
										console
												.log("******************room************: "
														+ vm.roomToPreview);
										vm.code = res.candidateRoomRelation.room.postCode;
										console
												.log("******************postCode************: "
														+ vm.code);
										vm.latitude = vm.roomToPreview.latitudeStation;
										vm.longitude = vm.roomToPreview.longitudeStation;
									}
								} else {
									console
											.log("******************ERRRRORRRR********************")
								}
							});

		}
		;

//		 function brToNewLine(str) {
//		 return str.replace(/<br ?\/?>/g, "\n");
//		 }

		// function mobilecheck() {
		// var check = false;
		// (function(a) {
		// if
		// (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge
		// |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm(
		// os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows
		// ce|xda|xiino/i
		// .test(a)
		// || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a
		// wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r
		// |s
		// )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1
		// u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp(
		// i|ip)|hs\-c|ht(c(\-|
		// |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac(
		// |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt(
		// |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg(
		// g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-|
		// |o|v)|zz)|mt(50|p1|v
		// )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v
		// )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-|
		// )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i
		// .test(a.substr(0, 4)))
		// check = true;
		// })(navigator.userAgent || navigator.vendor || window.opera);
		// return check;
		// }
		// ;

		function executeApply() {

			if (vm.applyForm.$valid) {
				CandidateService
						.saveCandidate(vm.candidateToPreview)
						.then(
								function(res) {
									console.log("--Candidate updated-----");
									console.log("id candidate=========" +vm.candidateToPreview.idUserInformation);
									console.log("id room ============" +vm.roomToPreview.idRoom);
							
									
									vm.data = {
										"idRoom" : vm.roomToPreview.idRoom,
										"idCandidate" : vm.candidateToPreview.idUserInformation,
										"preferedDate" : vm.preferedDate,
										"preferedTime" : vm.preferedTime
									},
									console.log("idRoom: " +vm.data.idRoom);
									console.log("idCandidate: " +vm.data.idCandidate);
									RoomService
											.applyForRoom(vm.data)
											.then(
													function(result) {
														if (result) {
															console
																	.log('---------------------'
																			+ result);
															if (result.exist) {
																console
																		.log("-----checkApplyToRoom----------true");
																swalService
																		.info(
																				"info",
																				"You have already applied for this room!");
															} else {
																console
																		.log("--------------false");
																vm.loading = true;
																swalService
																		.success(
																				"応募成功",
																				"応募しました！");
																
															}
															
														} else {
															console
																	.log("--apply for room--ERROR----");
															vm.loading = false;
														}
														vm.loading = false;
													})
									/** ********************* */
								})
			} else {
				console.log('dirty data');
				vm.applyForm.userName.$dirty = true;
				vm.applyForm.userPhone.$dirty = true;
				vm.applyForm.preferedTime.$dirty = true;
				vm.applyForm.preferedDate.$dirty = true;
			}

		}

		function applyForRoom() {
			vm.exist = false;
			console.log("room " + vm.roomToPreview.idRoom);
			console.log("cand " + vm.candidateToPreview.idUserInformation);
			vm.executeApply();
			console.log('aplyyyy');
		}

		function tConv24(time24) {
			var ts = time24;
			var H = +ts.substr(0, 2);
			var h = (H % 12) || 12;
			h = (h < 10) ? ("0" + h) : h; // leading 0 at the left for 1 digit
			// hours
			var ampm = H < 12 ? " AM" : " PM";
			ts = h + ts.substr(2, 3) + ampm;
			return ts;
		}
		;

		$scope.today = function() {
			$scope.dt = new Date();
		};
		$scope.today();

		$scope.clear = function() {
			$scope.dt = null;
		};

		$scope.inlineOptions = {
			customClass : getDayClass,
			minDate : new Date(),
			showWeeks : true
		};

		 $scope.dateOptions = {
			formatYear : 'yy',
			maxDate : new Date(2020, 5, 22),
			minDate : new Date(),
			startingDay : 1,
			initDate : new Date(1997, 1, 1)
		};
		

				 // Disable weekend selection
		function disabled(data) {
			var date = data.date, mode = data.mode;
			return mode === 'day'
					&& (date.getDay() === 0 || date.getDay() === 6);
		}
		


		 $scope.toggleMin = function() {
			$scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null
					: new Date();
			$scope.dateOptions.minDate = $scope.inlineOptions.minDate;
		};
		
		 $scope.toggleMin();
		
		 $scope.openBirthday = function() {
		 $scope.popupBirthday.opened = true;
		 };
		
		 $scope.popupBirthday = {
		 opened : false
		 };
		
		 $scope.open1 = function() {
		 $scope.popup1.opened = true;
		 };
		
		 $scope.open2 = function() {
		 $scope.popup2.opened = true;
		 };
		
		 $scope.setDate = function(year, month, day) {
		 $scope.dt = new Date(year, month, day);
		 };
		
		 $scope.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy',
		 'shortDate' ];
		 $scope.format = $scope.formats[0];
		 $scope.altInputFormats = [ 'M!/d!/yyyy' ];
		
		 $scope.popup1 = {
		 opened : false
		 };
		
		 $scope.popup2 = {
		 opened : false
		 };
		
		 var tomorrow = new Date();
		 tomorrow.setDate(tomorrow.getDate() + 1);
		 var afterTomorrow = new Date();
		 afterTomorrow.setDate(tomorrow.getDate() + 1);
		 $scope.events = [ {
		 date : tomorrow,
		 status : 'full'
		 }, {
		 date : afterTomorrow,
		 status : 'partially'
		 } ];
		
		 function getDayClass(data) {
		 var date = data.date, mode = data.mode;
		 if (mode === 'day') {
		 var dayToCheck = new Date(date).setHours(0, 0, 0, 0);
		
		 for (var i = 0; i < $scope.events.length; i++) {
		 var currentDay = new Date($scope.events[i].date).setHours(
		 0, 0, 0, 0);
		
		 if (dayToCheck === currentDay) {
		 return $scope.events[i].status;
		 }
		 }
		 }
		
		 return '';
		 }
		
		 $scope.markers = [];
		
		 var infoWindow = new google.maps.InfoWindow();
		 /** ****************** */
		 var createMarker = function(info) {
		 console.log('marker');
		 var marker = new google.maps.Marker({
		 map : $scope.map,
		 position : new google.maps.LatLng(info.lat, info.long),
		 title : info.place
		 });
		
		 marker.content = '<div class="infoWindowContent">' + info.desc
		 + '<br />' + info.lat + ' E,' + info.long + ' N, </div>';
		
		 google.maps.event.addListener(marker, 'click', function() {
		 infoWindow.setContent('<h2>' + marker.title + '</h2>'
		 + marker.content);
		 infoWindow.open($scope.map, marker);
		 });
		
		 google.maps.event.addListenerOnce($scope.map, 'idle', function() {
		 google.maps.event.trigger($scope.map, 'resize');
		 $scope.map.setCenter(marker.getPosition());
		 });
		
		 $scope.markers.push(marker);
		
		 }
		
		 function initMap() {
		 console.log('ini');
		 $scope.markers = [];
		 var mapOptions = {
		 zoom : 16,
		 center : new google.maps.LatLng(vm.latitude, vm.longitude),
		 mapTypeId : google.maps.MapTypeId.ROADMAP
		 }
		
		 var address = {
		 place : vm.code,
		 desc : 'code',
		 lat : vm.latitude,
		 long : vm.longitude
		 };
		
		 console.log('code: ' + vm.code);
		
		 $scope.map = new google.maps.Map(document.getElementById('map'),
		 mapOptions);
		
		 createMarker(address);
		
		 // google.maps.event.addListener($scope.map, 'click',
		 // function(event) {
		 // placeMarker($scope.map, event.latLng);
		 // });
		
		 }
		
		 function openMapModal(modalID) {
		 console.log('hi');
		 $scope.markers = [];
		 initMap();
		 ModalService.Open(modalID);
		 }
		
		 $scope.ShowHide = function() {
		 // If DIV is visible it will be hidden and vice versa.
		 $scope.markers = [];
		 initMap();
		 $scope.IsVisible = $scope.IsVisible ? false : true;
		 }
		
	}

})();