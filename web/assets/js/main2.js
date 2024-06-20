(function ($) {
    "use strict";

    function validateForm(formClass) {
        var form = $(formClass);
        var inputs = form.find('.validate-input .input100');

        form.on('submit', function() {
            var check = true;

            for (var i = 0; i < inputs.length; i++) {
                if (validate(inputs[i]) == false) {
                    showValidate(inputs[i]);
                    check = false;
                }
            }

            return check;
        });

        inputs.each(function() {
            $(this).focus(function() {
                hideValidate(this);
            });
        });

        function validate(input) {
            if ($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
                if ($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                    return false;
                }
            } else {
                if ($(input).val().trim() == '') {
                    return false;
                }
            }
        }

        function showValidate(input) {
            var thisAlert = $(input).parent();
            $(thisAlert).addClass('alert-validate');
        }

        function hideValidate(input) {
            var thisAlert = $(input).parent();
            $(thisAlert).removeClass('alert-validate');
        }
    }

    // Apply validation to each form separately
    validateForm('.form-otp');
    validateForm('.form-password');

})(jQuery);
