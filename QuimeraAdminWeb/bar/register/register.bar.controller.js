(function () {
    'use strict';

    angular
        .module('app')
        .controller('RegisterBarController', RegisterBarController);

    RegisterBarController.$inject = ['BarService', '$location', 'FlashService', 'BannerService'];
    function RegisterBarController(BarService, $location, FlashService, BannerService) {
        var vm = this;
        vm.allBanners = [];
        vm.bannersSelected = [];
        vm.registerBar = registerBar;
        vm.loadAllBanners = loadAllBanners;

        initController();

        function initController() {
            loadAllBanners();
        }

        function registerBar() {
            vm.dataLoading = true;
            vm.bar.banners = vm.bannersSelected;

            BarService.Create(vm.bar)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Bar registrado.', false);
                        $location.path('/bar');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }

        function loadAllBanners(){
            BannerService.GetAll()
                .then(function (banners) {
                    vm.allBanners = banners;
                });
        }
    }




})();