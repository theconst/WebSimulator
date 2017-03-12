'use strict';

//TODO: resolve pause buttons 
//fix bugs

/**
 *  Simle view for handling the simulation output 
 *  Processes and gets out the output
 */
;
var view = (function($) {
    function View(valveInitial, speedCalculationStrategy) {
        var _self = this;

        //initialize inputs and chart by default,
        //can be easily passed as constructor parameters or getters/setters;
        this.calculateSpeed = speedCalculationStrategy;

        //initialize
        this._paused = true;
        this._finished = false;

        //stores data nessesary for simulation
        this.inputs = {
            sampling: 1,
            speed: 1,
            "valve-1": valveInitial["input1"],
            "valve-2": valveInitial["input2"],
            "valve-3": valveInitial["input3"],
        };

        //create chart
        var colors = ["red", "green", "blue", "yellow", "orange", "grey"];
        this.handle = new charts.LinearChart($('#temp-1-6'), 't, c', 'T, C', colors);
        var temperaturesValuesIds = ["temp-1", "temp-2", "temp-3", "temp-4", "temp-5", "temp-6"];
        var valveIds = ["valve-1", "valve-2", "valve-3"];

        var valvePosition = valveIds.map(function(valveId) {
            return $(id(valveId, "-pos"));
        });
        var upArrow = valveIds.map(function(valveId) {
            return $(id(valveId, "-up-arrow"));
        });
        var downArrow = valveIds.map(function(valveId) {
            return $(id(valveId, "-down-arrow"));
        });

        valveIds.forEach(function(valveId, i) {
            valvePosition[i].text(_self.inputs[valveId].value);
        });

        //initialize original properties of the valves
        valveIds.forEach(function(valveId, i) {

            upArrow[i].mousedown(function(event) {
                var valve = _self.inputs[valveId];
                var current = +valve.value;
                var max = valve.max;
                var next = current < max ? current + 1 : max;

                valve.value = next;
                valvePosition[i].text(next);

                ((valve.value == max) && upArrow[i].hide()) || downArrow[i].show();
                return false;
            });

            downArrow[i].mousedown(function(event) {
                var valve = _self.inputs[valveId];
                var current = +valve.value;
                var min = valve.min;
                var next = current > min ? current - 1 : min;

                valve.value = next;
                valvePosition[i].text(next);

                ((valve.value == min) && downArrow[i].hide()) || upArrow[i].show();
                return false;
            });
        });


        //initialize temperature displays
        temperaturesValuesIds.forEach(function(val, no) {
            $(id("display-", val)).change(function() {
                var val = $(this).is(':checked');
                _self.handle.setVisible("T" + (no + 1), val);
                _self.handle.update();
                return false;
            });
        });

        //initialize general controls
        $('#clear-canvas').click(function(event) {
            _self.handle.clear();
            _self.handle.update();
            return false;
        });

        $('#sim-speed').on("change mousemove", function(event) {
            _self.inputs["speed"] = $(this).val();
            return false;
        });

        $('#pause').click(function(event) {
            _self._paused = !_self._paused;
            return false;
        });

        $('#finish-btn').click(function(event) {
            _self._finished = true;
            return false;
        });


        //helper function for genrating the id
        function id(name, suffix) {
            return "#" + name + suffix;
        }
    }

    View.prototype.getUserInput = function() {
        //calculate timespan based on sampling and speed
        return {
            "inputs": [this.inputs["valve-1"].value,
                     this.inputs["valve-2"].value,
                     this.inputs["valve-3"].value],
            "names": ["input1", "input2", "input3"],
            "sampling": this.inputs.sampling,
            "timespan": this.calculateSpeed(this.inputs.speed,
                this.inputs.sampling)
        };
    }

    //Wait fo user to resolve
    View.prototype.isPaused = function() {
        return this._paused;
    }

    View.prototype.isFinished = function() {
        return this._finished;
    }

    // plot values on screen
    View.prototype.plot = function(data) {
        //refresh all the values
        this.handle.appendArgument(data["ticks"])
            .appendRow({ label: "T1", row: data["param1"] })
            .appendRow({ label: "T2", row: data["param2"] })
            .appendRow({ label: "T3", row: data["param3"] })
            .appendRow({ label: "T4", row: data["param4"] })
            .appendRow({ label: "T5", row: data["param5"] })
            .appendRow({ label: "T6", row: data["param6"] })
            .update();
    }

    View.prototype.showAll = function() {
        $('.hidden').removeClass('hidden').addClass('visible');
    }

    View.prototype.hideAll = function() {
        $('.visible').removeClass('visible').addClass('hidden');
    }

    View.prototype.printMessage = function(message) {
        $('#status-bar').text(message);
    }

    return {
        View: View
    }
})(jQuery);