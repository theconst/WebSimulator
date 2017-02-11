/* 
 * Simulation.js
 */
'use strict';

$(window).on("load", function () {
    var ctx = document.getElementById('temp-1-6');

    /* hard-coded config for chart */
    var chartInstance = new Chart(ctx, {
        type: 'line',
        data: {
            //initialize just empty array
            datasets: []
        },
        options: {
            scales: {
                xAxes: [{
                        type: 'linear',
                        position: 'bottom',
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 't, c'
                        }
                    }],
                yAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'T, C'
                        }
                    }]
            }
        }
    });

    function Point(xVal, yVal) {
        return {x: xVal, y: yVal};
    }

    //default coloring strategy
    function ColoringStrategy() {
        this.index = 0;
        this.colors = ["red", "green", "blue",
            "yellow", "orange", "grey"];
    }

    ColoringStrategy.prototype = {
        nextColor: function () {
            var result = this.colors[this.index];
            this.index = (this.index + 1) % this.colors.length;
            return result;
        }
    };


    //abstract handle for chart.js
    function ChartHandle(chart, coloringStrategy) {
        this.chart = chart;

        //todo: expose only args and rows 
        //hide data and datasets
        //??? getters and setters
        //expose chart and datasets
        this.data = this.chart.data;
        this.datasets = this.data.datasets;
        //arg represents array of not yet rendered data
        //array of rows which will be referenced by original data
        this.arg = []; //set arg to null
        this.rows = {};

        this.fill = false;
        this.pointRadius = 0;

        this.coloring = coloringStrategy;
    }

    ChartHandle.prototype = {

        appendData: function (points, label) {
            var hasData = this.rows.hasOwnProperty(label);
            var row = hasData ? this.rows[label] : [];

            //appendData
            this.rows[label] = $.merge(row, points);
            //handle visibiliety

            if (!hasData) {
                this.datasets.push({
                    label: label,
                    data: this.rows[label],
                    fill: this.fill,
                    pointRadius: this.pointRadius,
                    borderColor: this.coloring.nextColor(),
                    hidden: false
                });
            }
        },

        isEmpty: function () {
            return this.datasets.length === 0;
        },

        //get row or deep copy if needed
        getRow: function (label, deep) {
            return (arguments.length === 1) ? this.rows[label] : deep ?
                    this.rows[label].slice() : this.rows[label];
        },

        appendRow: function (dataRow) {
            var row = dataRow.row;
            var label = dataRow.label;
            var x = this.arg;
            this.appendData(row.map(function (y, i) {
                var delta = x.length - row.length;
                return new Point(x[i + delta], y);
            }), label);
            //return object to allow chaining
            return this;
        },
        //row array of data points
        appendArgument: function (row) {
            if (!row) {
                console.log("useless addition of arguement");
            }

            //deep copy of row
            this.arg = $.merge(this.arg, row);
            return this;
        },

        setVisible: function (label, value) {
            if (this.rows.hasOwnProperty(label)) {
                this.datasets.filter(function (x) {
                    return x.label === label;
                })[0].hidden = !value;
            }
        },

        //simply wraps the internal behaviour
        update: function () {
            //do not update empty canvas (triggers exception in Chart.js)
            if (!this.isEmpty()) {
                return this.chart.update();
            }
        },

        //clears the table
        clear: function () {
            this.arg.length = 0;

            //for each label discard it
            for (var l in this.rows) {
                this.rows[l].length = 0;
            }
        }
    };

    var chartHandle = new ChartHandle(chartInstance, new ColoringStrategy());

    /* Structure holding user interface state */
    var userInputs = {
        sampling: 1,
        speed: 1,
        "valve-1": 0,
        "valve-2": 0,
        "valve-3": 0,
        "pause": false,
        "clear": false,
        "finish": false
    };
    /********************************************/


    /* UI callbacks */
    function replaceText(where, userInputs, param, op) {
//        console.log(userInputs[param]);
//        console.log(param);
        userInputs[param] = op(userInputs[param]);
//        console.log(userInputs[param]);
        where.text(userInputs[param]);
    }

    function up(arg) {
        arg = 0 + arg;
        return arg < 100 ? arg + 1 : arg;
    }

    function down(arg) {
        return arg > 0 ? arg - 1 : arg;
    }

    function id(name, suffix) {
        return "#" + name + suffix;
    }

    var valves = ["valve-1", "valve-2", "valve-3"];

    valves.forEach(function (x) {
        $(id(x, "-pos")).text(0);
    });

//todo: refactor valves to have classes, use jquery selectors

//register callbacks for each of the functions
    valves.forEach(function (x) {
        $(id(x, "-up-arrow")).mousedown(function (event) {
            event.preventDefault();
            console.log('up-arrow');
            replaceText($(id(x, "-pos")), userInputs, x, up);
        });
        $(id(x, "-down-arrow")).mousedown(function (event) {
            event.preventDefault();
            console.log('down-arrow');
            replaceText($(id(x, "-pos")), userInputs, x, down);
        });
    });

    [1, 2, 3, 4, 5, 6].forEach(function (x) {
        $(id("display-temp-", x)).change(function (event) {
            event.preventDefault();
            var val = $(this).is(':checked');
            chartHandle.setVisible("T" + x, val);
        });
    });

    $('#clear-canvas').click(function (event) {
        event.preventDefault();
        userInputs["clear"] = true;
    });

    $('#pause').click(function (event) {
        event.preventDefault();
        userInputs["pause"] = !userInputs["pause"];
    });

    $('#finish-btn').click(function (event) {
        event.preventDefault();
        userInputs["finish"] = true;
    });

    $('#sim-speed').on("change mousemove", function () {
        userInputs["speed"] = $(this).val();
    });
    /************************************************************************/

    /* TODO: encapsulate loop as an object and perform callback init there */


    //tricky bug, chart js didn't want to draw null chart without exception

//    var firstDrawn = false;

    /* Control loop of the UI */
    /* Controller interacts with ui and updates view */
    //acts as data source has send function to query messages
    function loop(inputs, handle, dataSource) {    
        //process text message from data source
        function processTextMessage(message) {
            var data = JSON.parse(message.data);
//                console.log("Data:" + data);
            handle.appendArgument(data["ticks"]).
                    appendRow({label: "T1", row: data["param1"]}).
                    appendRow({label: "T2", row: data["param2"]}).
                    appendRow({label: "T3", row: data["param3"]}).
                    appendRow({label: "T4", row: data["param4"]}).
                    appendRow({label: "T5", row: data["param5"]}).
                    appendRow({label: "T6", row: data["param6"]});
        }

        //read inputs
        if (!inputs["pause"]) {
            //generate message
            //
            var sampling = inputs.sampling;
            var speed = inputs.speed;
            
            var message = JSON.stringify({
                "input1": inputs["valve-1"],
                "input2": inputs["valve-2"],
                "input3": inputs["valve-3"],
                "sampling": sampling,
                "timespan": Math.ceil(speed * sampling)
            });
            //setup dataSource according to WS interface
            dataSource.send(message);
            //set callback on message completion
            dataSource.onmessage = processTextMessage;
        }
        if (inputs["clear"]) {
            handle.clear();
            inputs["clear"] = false;
        }
        if (inputs["finish"]) {
            dataSource.close();
            //! show history goes to the source callback
        }
        //update view       --- update view on each frame
        handle.update();
    }
    /*********************************************/

    /* logic and messaging */
    var URL = "ws://localhost:8084/WebSimulator/model";
   
    /* Establishing the control loop based on connection with server */
    //global var - in order not to put it to WS / wrap it for the sake of one var
    var socket = new WebSocket(URL);
    var loopHandle;
    
     /* configurator of the control loop */
    function setUpLoop() {
        return window.setInterval(function () {
            //user inputs acts as structure that stores state
            //handle is view
            //socket is datasource
            loop.apply(null, [userInputs, chartHandle, socket]);
        }, userInputs.sampling * 1000);
    }  
    
    socket.onopen = function () {
        console.log("connected ");
        loopHandle = setUpLoop();
    };

    socket.onclose = function (event) {
        console.log("closed, reason("
                + event.eventCode + "," + event.reason + ")");

        //temporary workaround
        $('#history-ref').show();
        window.clearInterval(loopHandle);
    };
});