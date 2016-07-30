(function () {
    'use strict';

    angular
        .module('app')
        .controller('ControlPanelController', ControlPanelController);

    ControlPanelController.$inject = ['GameManagerService', '$location', 'FlashService', '$interval', '$scope'];
    function ControlPanelController(GameManagerService, $location, FlashService, $interval, $scope) {
        var vm = this;
        var interval = null;
        vm.games = [];

        vm.stopTrivia = stopTrivia;

        function stopTrivia(idBar) {
            GameManagerService.FinishTrivia(idBar)
                .then(function (response) {
                    if (response.status == "200") {
                        FlashService.Success('Trivia finalizada.', false);
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }

        function getAllTriviasRunning() {
            GameManagerService.GetGames()
                .then(function (response) {
                    if (response != "") {
                        vm.dataLoading = true;
                        vm.games = response.data;
                    } else {
                        vm.games = {};
                        vm.dataLoading = false;
                    }
                });
        }

        $(function () {
            interval = $interval(function () {
                getAllTriviasRunning();
            }, 1000);

        });

        $scope.$on('$destroy', function () {
            $interval.cancel(interval);
        });
    }

})();