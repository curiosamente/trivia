(function () {
    'use strict';

    angular
        .module('app')
        .controller('CategoryRegisterController', CategoryRegisterController);

    CategoryRegisterController.$inject = ['CategoryService', '$location', 'FlashService', '$scope'];
    function CategoryRegisterController(CategoryService, $location, FlashService,$scope) {
        var vm = this;
        vm.category = {};
        //vm.category.subCategories = [];

        vm.registerCategory = registerCategory;

        function registerCategory() {
            vm.dataLoading = true;
            CategoryService.Create(vm.category)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Categoría registrada.', false);
                        $location.path('/category');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }
        //$scope.addItem = function(item){
        //    $scope.vm.category.subCategories.push(item);
        //    $scope.newItem = null;
        //}
    }

})();