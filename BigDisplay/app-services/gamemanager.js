/**
 * Created by Manu on 29/06/2016.
 */
(function () {
    'use strict';

    angular
        .module('app')
        .factory('GameManagerService', GameManagerService);

    GameManagerService.$inject = ['$http', '$rootScope'];
    function GameManagerService($http, $rootScope) {
        var service = {};
        var url = $rootScope.url;
        var idBar = $rootScope.globals.currentUser.idBar;
        service.SetCurrentQuestion = SetCurrentQuestion;
        service.SetStatus = SetStatus;
        service.SetElapsedTime = SetElapsedTime;
        service.GetCurrentTrivia = GetCurrentTrivia;
        service.FinishTrivia = FinishTrivia;

        return service;

        function SetCurrentQuestion(question) {
            return $http.put(url + '/game/currentQuestion?idBar=' + idBar, question).then(handleSuccess, handleError('Error setting question'));
        }

        function SetStatus(status) {
            return $http.patch(url + '/game/status?idBar=' + idBar, status).then(handleSuccess, handleError('Error setting status'));
        }

        function SetElapsedTime(elapsedTime) {
            return $http.patch(url + '/game/elapsedTime?idBar=' + idBar, elapsedTime).then(handleSuccess, handleError('Error setting elapsed time'));
        }

        function GetCurrentTrivia() {
            return $http.get(url + '/game/currentTrivia?idBar=' + idBar).then(handleSuccess, handleError('Error getting current trivia'));
        }

        function FinishTrivia() {
            return $http.patch(url + '/game/finishTrivia?idBar=' + idBar).then(handleSuccess, handleError('Error finishing trivia'));
        }

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return {success: false, message: error};
            };
        }
    }

})();
