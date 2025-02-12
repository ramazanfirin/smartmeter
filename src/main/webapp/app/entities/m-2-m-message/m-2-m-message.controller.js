(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('M2mMessageController', M2mMessageController);

    M2mMessageController.$inject = ['DataUtils', 'M2mMessage'];

    function M2mMessageController(DataUtils, M2mMessage) {

        var vm = this;

        vm.m2mMessages = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            M2mMessage.query(function(result) {
                vm.m2mMessages = result;
                vm.searchQuery = null;
            });
        }
    }
})();
