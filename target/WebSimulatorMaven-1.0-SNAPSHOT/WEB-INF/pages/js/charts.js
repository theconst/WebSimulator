; //custom interface for chart.js
var charts = (function(window) {

    function Point(xVal, yVal) {
        return { x: xVal, y: yVal };
    }

    function ColoringStrategy(colors) {
        this.index = 0;
        this.colors = colors;
    }

    ColoringStrategy.prototype = {
        nextColor: function() {
            var result = this.colors[this.index];
            this.index = (this.index + 1) % this.colors.length;
            return result;
        }
    };

    function LinearChart(ctx, xLabel, yLabel, colors) {
        this.chart = new Chart(ctx, {
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
                            labelString: xLabel
                        }
                    }],
                    yAxes: [{
                        ticks: {
                            beginAtZero: false
                        },
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: yLabel
                        }
                    }]
                }
            }
        });

        //provide data and datasets as for ordinary chart
        this.data = this.chart.data;
        this.datasets = this.data.datasets;
        //arg represents array of not yet rendered data
        //array of rows which will be referenced by original data
        this.arg = []; //set arg to null
        this.rows = {};

        this.fill = false;
        this.pointRadius = 0;

        this.coloring = new ColoringStrategy(colors);
    }

    //provide custom interation interface
    LinearChart.prototype = {

        appendData: function(points, label) {
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

        isEmpty: function() {
            return this.datasets.length === 0;
        },

        //get row or deep copy if needed
        getRow: function(label, deep) {
            return (arguments.length === 1) ? this.rows[label] : deep ?
                this.rows[label].slice() : this.rows[label];
        },

        appendRow: function(dataRow) {
            var row = dataRow.row;
            var label = dataRow.label;
            var x = this.arg;
            this.appendData(row.map(function(y, i) {
                var delta = x.length - row.length;
                return new Point(x[i + delta], y);
            }), label);
            //return object to allow chaining
            return this;
        },
        //row array of data points
        appendArgument: function(row) {
            if (!row) {
                console.log("useless addition of argument");
            }

            //deep copy of row
            this.arg = $.merge(this.arg, row);
            return this;
        },

        setVisible: function(label, value) {
            if (this.rows.hasOwnProperty(label)) {
                this.datasets.filter(function(x) {
                    return x.label === label;
                })[0].hidden = !value;
            }
        },

        //simply wraps the internal behaviour
        update: function() {
            //do not update empty canvas (triggers exception in Chart.js)
            if (!this.isEmpty()) {
                return this.chart.update();
            }
        },

        //clears the table
        clear: function() {
            this.arg.length = 0;

            //for each label discard it
            for (var l in this.rows) {
                this.rows[l].length = 0;
            }
        }
    };


    return {
        LinearChart: LinearChart
    }
})(window);