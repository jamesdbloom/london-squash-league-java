var validation = validation || {};

(function () {
    "use strict";

    var i,
        setClass = function (element, value) {
            element.setAttribute('class', value)
        },
        filled = function (e) {
            if (this.value.length >= 1) {
                setClass(this, 'filled show_validation');
            } else {
                setClass(this, 'empty show_validation');
            }
        },
        changed = function (e) {
            setClass(this, 'filled show_validation');
        },
        matches = function (id) {
            return function (e) {
                if (this.value.length == 0 || this.value == document.getElementById(id).value) {
                    setClass(this, 'valid show_validation');
                } else {
                    setClass(this, 'invalid show_validation');
                }
            };
        },
        setValidation = function () {
            if (validation.filled) {
                for (i = 0; i < validation.filled.length; i++) {
                    document.getElementById(validation.filled[i]).onblur = filled;
                }
            }
            if (validation.changed) {
                for (i = 0; i < validation.changed.length; i++) {
                    document.getElementById(validation.changed[i]).onblur = changed;
                }
            }
            if (validation.matches) {
                for (i = 0; i < validation.matches.length; i++) {
                    document.getElementById(validation.matches[i].id).onblur = matches(validation.matches[i].matches);
                }
            }
            if (validation.onload) {
                if (validation.filled) {
                    for (var f = 0; f < validation.filled.length; f++) {
                        filled.call(document.getElementById(validation.filled[f]));
                    }
                }
                if (validation.changed) {
                    for (i = 0; i < validation.changed.length; i++) {
                        filled.call(document.getElementById(validation.changed[i]));
                    }
                }
                if (validation.matches) {
                    for (i = 0; i < validation.matches.length; i++) {
                        matches(validation.matches[i].matches).call(document.getElementById(validation.matches[i].id));
                    }
                }
            }
        };
    setValidation();
})();