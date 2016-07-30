(function () {
    'use strict';

    angular
        .module('app')
        .controller('ModifyUserController', ModifyUserController);

    ModifyUserController.$inject = ['UserService', '$location', '$routeParams', 'FlashService'];
    function ModifyUserController(UserService, $location, $routeParams, FlashService) {
        var vm = this;
        vm.user = null;

        vm.modifyUser = modifyUser;

        initController();

        function initController() {
            loadCurrentUser();
        }

        function loadCurrentUser() {
            UserService.GetById($routeParams.id)
                .then(function (user) {
                    vm.user = user;
                });
        }

        function modifyUser() {
            vm.dataLoading = true;
            UserService.Update(vm.user)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Usuario modificado.', false);
                        $location.path('/user');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }
    }



})();