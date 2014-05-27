app.directive('idle', ['$idle', '$timeout', '$interval', function($idle, $timeout, $interval){
  return {
    restrict: 'A',
    link: function(scope, elem, attrs) {
      var timeout;
      var timestamp = localStorage.lastEventTime;

      // Watch for the events set in ng-idle's options
      // If any of them fire (considering 500ms debounce), update localStorage.lastEventTime with a current timestamp
      elem.on($idle._options().events, function(){
        if (timeout) { $timeout.cancel(timeout); }
        timeout = $timeout(function(){
          localStorage.setItem('lastEventTime', new Date().getTime());
        }, 500);
      });

      // Every 5s, poll localStorage.lastEventTime to see if its value is greater than the timestamp set for the last known event
      // If it is, reset the ng-idle timer and update the last known event timestamp to the value found in localStorage
      $interval(function() {
        if (localStorage.lastEventTime > timestamp) {
          $idle.watch();
          timestamp = localStorage.lastEventTime;
        } 
      }, 5000);
    }
  }
}])

app.factory('SessionTimeout', ['$http', function($http) {
    return $http.get(SERVICE_URL_BASE + "session/maxinactiveinterval");
}])

app.controller('SessionExpiresCtrl', ['$idle', '$scope', '$modalInstance', '$window', '$rootScope', function( $idle, $scope, $modalInstance, $window, $rootScope) {
    $scope.timeoutMessage = function() {
	var duration = $rootScope.sessionIdleTimeMaxDuration ? Math.floor($rootScope.sessionIdleTimeMaxDuration / 60) : 30;
	return jQuery.i18n.prop('session.expired.text1.part1') + " " + duration +  " " + jQuery.i18n.prop('session.expired.text1.part2');
    }
    
    $scope.okConfirm = function() {
	$idle.watch();
	$modalInstance.close();
    }
    $scope.redirectToLogin = function() {
	$window.location.reload();
    }
}])

app.controller('EventsCtrl', ['$scope','$idle', 'SessionTimeout', '$modal', '$rootScope', function($scope, $idle, SessionTimeout, $modal, $rootScope) {
    openModal = function(template) {
	return $modal.open({
		templateUrl: template,
		controller: 'SessionExpiresCtrl',
		keyboard: false,
		backdrop: 'static'
	    });
    }

    $scope.$on('$idleWarn', function(e, countdown) {
	if (!$scope.sessionWarning || angular.element('#sessionWarning').length < 1) {
	    $scope.sessionWarning = openModal('sessionWarning.html');
	}
    });

    $scope.$on('$idleTimeout', function() {
	$scope.sessionWarning.close();
	$scope.sessionWarning = openModal('sessionExpired.html');
    })

    $scope.$on('$keepalive', function() {
	SessionTimeout.success(function(result){
	    if (!$rootScope.sessionIdleTimeMaxDuration) {
		$rootScope.sessionIdleTimeMaxDuration = result;
	    }
	});
    })

}])
.config(['$idleProvider', '$keepaliveProvider', function($idleProvider, $keepaliveProvider) {    
    var warningDuration = 300;
    jQuery.get(SERVICE_URL_BASE + "session/maxinactiveinterval", function( result ) {
	console.log(result);
	$idleProvider.idleDuration((result < warningDuration) ? (1800 - warningDuration) : (result - warningDuration));
    });
    $idleProvider.warningDuration(warningDuration);
    $keepaliveProvider.interval(SESSION_KEEPALIVE_INTERVAL_IN_SECONDS);
}])
.run(['$idle', function($idle){
    $idle.watch();
}]);