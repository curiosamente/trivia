(function () {
    'use strict';

    angular
        .module('app')
        .controller('UserRegisterController', UserRegisterController);

    UserRegisterController.$inject = ['UserService', '$location', 'FlashService'];
    function UserRegisterController(UserService, $location, FlashService) {
        var vm = this;

        vm.registerUser = registerUser;

        function registerUser() {
            vm.dataLoading = true;
            UserService.Create(vm.user)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Usuario registrado.', false);
                        $location.path('/user');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }
    }

})();