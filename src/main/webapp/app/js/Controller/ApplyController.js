/**
 * Apply Controller
 */

(function() {
	'use strict';

	angular.module('app.core').controller('ApplyFormController',
			ApplyFormController);

	function ApplyFormController($http, $scope, $state, RoomService,
			CandidateService, $location, $localStorage, $routeParams,
			swalService, $window, ModalService, $filter, $q) {
		var vm = this;
		vm.candidateToPreview = null;
		vm.roomToPreview = null;
		vm.applyForRoom = applyForRoom;
		vm.executeApply = executeApply;
		vm.exist = false;
		vm.preferedDate = null;
		vm.preferedTime = "午前中";
		$scope.IsVisible = false;
		vm.openMapModal = openMapModal;

		vm.preferedTimeList = [ "午前中", "昼", "午後", "夜", "いつでも " ];

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
										vm.candidateToPreview = res.candidate;
										vm.roomToPreview = res.room;
										
										console.log("---------------No Relation --------------");
									}
								} else {
									console
											.log("******************ERRRRORRRR********************");
								}
							});

		}
		;


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
			console.log('applyyyy');
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
			dateDisabled: disabled,
			formatYear : 'yy',
			maxDate : new Date(2020, 5, 22),
			minDate : new Date(Date.now()),
			startingDay : 1,
			initDate : new Date(Date.now())
		};
		

		// Disable weekend selection
		 function disabled(data) {
			var date = data.date, mode = data.mode;
			var now = new Date(Date.now());
			return mode === 'day'
					&& (date <= now)
//			(date.getDay() === 0 || date.getDay() === 6)
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
		 console.log("vm.code: " +vm.code );
			 var address = {
					 address : "Beppu Tower, Beppu, Oita Prefecture, Japan ",
					 desc : "address",
					 lat : 33.281712,
					 long : 131.505981
			 };
		 

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
		 
		 
//		 function getAddress(location){
//			 console.log("hellooo");
//			console.log(location);
//			 $.getJSON("https://maps.googleapis.com/maps/api/geocode/json?address="+location+"&key=AIzaSyA-X7GGbSw0m0PvQ8_ibHBCQW1vew8_fMs",function(data, textStatus){
//				 return data.results[0].formatted_address;
//				 // $.each(data.Placemark, function(key, val) {
//// console.log("val.address: " +val.address)
//// if(val.address !=null){
//// return val.address;
//// }else{
//// return null;
//// }
////
//// });
//			 });
//			 }
		 
		 
		 
		
	}

})();