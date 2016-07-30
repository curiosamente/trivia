(function () {
    'use strict';

    angular
        .module('app')
        .controller('BarController', BarController);

    BarController.$inject = ['BarService', '$location', 'FlashService'];
    function BarController(BarService, $location, FlashService) {
        var vm = this;

        vm.allBars = [];
        vm.deleteBar = deleteBar;

        initController();

        function initController() {
            loadAllBars();
        }

        function loadAllBars() {
            BarService.GetAll()
                .then(function (bars) {
                    vm.allBars = bars;
                });
        }

        function deleteBar(bar) {
            BarService.Delete(bar)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Bar eliminado.', true);
                        loadAllBars();
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                    $location.path('/bar');
                });
        }

    }

})();