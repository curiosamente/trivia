(function () {
    'use strict';

    angular
        .module('app')
        .controller('ModifyBarController', ModifyBarController);

    ModifyBarController.$inject = ['BarService', '$location', '$routeParams', 'FlashService', 'BannerService'];
    function ModifyBarController(BarService, $location, $routeParams, FlashService, BannerService) {
        var vm = this;
        vm.bar = null;
        vm.bannersSelected = [];
        vm.modifyBar = modifyBar;

        initController();

        function initController() {
            loadAllBanners();
            loadCurrentBar();
        }

        function loadCurrentBar() {
            BarService.GetById($routeParams.id)
                .then(function (bar) {
                    vm.bar = bar;
                    vm.bar.banners.forEach(function (outerBanner) {
                        vm.allBanners.forEach(function (innerBanner) {
                            if (outerBanner.idBanner === innerBanner.idBanner) {
                                vm.bannersSelected.push(innerBanner);
                            }
                        });
                    });
                });
        }

        function modifyBar() {
            vm.dataLoading = true;
            vm.bar.banners = vm.bannersSelected;

            BarService.Update(vm.bar)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Bar modificado.', false);
                        $location.path('/bar');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }

        function loadAllBanners() {
            BannerService.GetAll()
                .then(function (banners) {
                    vm.allBanners = banners;
                });
        }

    }



})();