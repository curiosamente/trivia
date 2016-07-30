(function () {
    'use strict';

    angular
        .module('app')
        .controller('BannerController', BannerController);

    BannerController.$inject = ['BannerService', '$location', 'FlashService'];
    function BannerController(BannerService, $location, FlashService) {
        var vm = this;

        vm.allBanners = [];
        vm.deleteBanner = deleteBanner;

        initController();

        function initController() {
            loadAllBanners();
        }

        function loadAllBanners() {
            BannerService.GetAll()
                .then(function (banners) {
                    vm.allBanners = banners;
                });
        }

        function deleteBanner(banner) {
            BannerService.Delete(banner)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Banner eliminado.', true);
                        loadAllBanners();
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                    $location.path('/banner');
                });
        }

    }

})();