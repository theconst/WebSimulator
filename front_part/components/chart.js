// dps = {
//     x: val,
//     y: val
// }
'use strict';
import React from 'react';

const dataLength = 300;
let state = false;

export class ChartBox extends React.Component {
    constructor(props) {
        super(props);
        this.state = { dps: [] };
        this.drawChart = this.drawChart.bind(this);
    }
    drawChart(input) {
        console.log(this.props.dps);
        let chart = new CanvasJS.Chart(input, {
            // backgroundColor: '#000000',
            theme: 'theme1',
            title: {
                text: 'Transient Response'
            },
            axisX: {
                title: 't'
            },
            axisY: {
                title: 'T'
            },
            exportEnabled: true,
            animationEnabled: true,
            legend: {
                cursor: "pointer",
                itemclick: function (e) {
                    if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
                        e.dataSeries.visible = false;
                    } else {
                        e.dataSeries.visible = true;
                    }
                    e.chart.render();
                }
            },
            zoomEnabled: true,
            zoomType: 'xy',
            data: [{
                showInLegend: true,
                type: 'line',
                dataPoints: this.props.dps
            }]
        });
        debugger;
        chart.render();
        this.setState({ chart: chart });
    }

    shouldComponentUpdate(nextProps, nextState) {
        this.state = nextState;
        while(this.state.chart.options.data[0].dataPoints.length > dataLength) {
            this.state.chart.options.data[0].dataPoints.shift();
        }
        if (state) {
            this.state.chart.options.data[0].name = nextProps.name;
            this.state.chart.options.data[0].dataPoints.push(...nextProps.dps);
        } else {
            state = true;
        }

        this.state.chart.render();
        return false;
    }
    render() {
        return(
            <div className="chartContent">
                <div ref={this.drawChart}></div>
            </div>
        )
    }
}


