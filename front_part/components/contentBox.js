'use strict';
import React from 'react';
import {ParamsFormList} from './paramsFormList';
import {ChartList} from './chartList';

// const socket = io('ws://localhost:8084/WebSimulator/model');
// const socket = new WebSocket("ws://localhost:8081/WebSimulator/model");
const socket = new WebSocket("ws://localhost:3000/", 'echo-protocol');
const defaultParams = {
    timespan: 10,
    sampling: 1,
    input1: 1,
    input2: 1,
    input3: 1
};

socket.onclose = function (event) {
    if (event.wasClean) {
        alert('Соединение закрыто чисто');
    } else {
        alert('Обрыв соединения');
    }
    alert('Код: ' + event.code + ' причина: ' + event.reason);
};
//TODO: default params

export class ContentBox extends React.Component {
    constructor(props) {
        super(props);
        this.state = { charts: []};
        this.emiterForData = this.emiterForData.bind(this);
        //TODO: test if request works
        // If doesn't work try to do in componentWillMount
        // this.getParameters();
    }
    getParameters() {
        //TODO: type right url
        fetch('/params')
            .then(response => {
                return response.json();
            })
            .then(names => {
                // debugger;
                for (let prop in names.params) {
                    defaultParams[prop] = null;
                }
                this.setState({parameters: names.params});
            })
            .catch(err => {
                console.log(err);
            });
    }
    /**
     * Structures input data to necessary format for multiple trends
     * on one chart
     * @param {object} data
     * @returns {{dps: Array, multiple: boolean}}
     */
    static structuringMultipleData(data) {
        if (!data.hasOwnProperty('ticks')) {
            throw new Error('Server did not give time data');
        }
        let ticks = data.ticks;
        delete data.ticks;
        let arrOfdps = [], i = 0;
        for (let prop in data) {
            arrOfdps.push({ showInLegend: true, type: 'line', dataPoints: [] });
            for (let j = 0; j < ticks.length; j++) {
                arrOfdps[i].dataPoints.push({ x: ticks[j], y: data[prop][j]});
            }
            i++;
        }
        return { dps: arrOfdps, multiple: false };
    }
    /**
     * Structures data for multiple charts
     * @param {object} data
     * @returns {Array} charts consist of {{dps: Array}}
     */
    static structuringDataForMultipleCharts(data) {
        if (!data.hasOwnProperty('ticks')) {
            throw new Error('Server did not give time data');
        }
        let time = data.ticks;
        delete data.ticks;
        let charts = [], i = 0;
        for (let prop in data) {
            charts.push({dps: [], name: prop });
            for (let j = 0; j < time.length; j++) {
                charts[i].dps.push({ x: time[j], y: data[prop][j]});
            }
            i++;
        }
        // debugger;
        return charts;
    }

    static updateDisplayParams(params) {
        // debugger;
        let data = params;
        for (let prop in data) {
            if (prop == 'ticks') continue;
            // debugger;
            $(`#${prop}-value`)[0].innerHTML = data[prop][data[prop].length - 1].toFixed(2);
        }
    }
    dataListener() {
        socket.onmessage = data => {
            console.log('Listen data:');
            console.log(data.data);
            // debugger;
            let newData = JSON.parse(data.data);
            if (newData !== undefined && Object.keys(newData).length > 1) {
                console.log('Validation passed');
                // debugger;
                ContentBox.updateDisplayParams(newData);
                this.setState({ charts: ContentBox.structuringDataForMultipleCharts(newData)});
            }
        };
    }
    emiterForData(data) {
        console.log('emit data');
        let message = {};
        // debugger;
        if (data !== undefined) {
            for (let prop in data.params) {
                message[prop] = data.params[prop];
                defaultParams[prop] = data.params[prop];
            }
        } 
        for (let props in defaultParams) {
            if (!message[props]) {
                message[props] = defaultParams[props];
            }
        }
        // debugger;
        socket.send(JSON.stringify(message));
    }
    // componentWillMount() {
    //     this.getParameters();
    // }
    componentDidMount() {
        this.setState({parameters: ['input1', 'input2', 'input3']});
        console.log('start emiter');
        this.dataListener();
        setInterval(this.emiterForData, this.props.interval);
    }
    render() {
        return(
            <div>
                <ParamsFormList onFormSubmit={this.emiterForData} parameters={this.state.parameters}/>
                <ChartList charts={this.state.charts}/>
            </div>
        )
    }
}

