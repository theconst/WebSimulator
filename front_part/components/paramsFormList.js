'use strict';

import React from 'react'
import {ParamsForm} from './paramsForm'

export class ParamsFormList extends React.Component {
    constructor(props) {
        super(props);
        this.toggleFormList = this.toggleFormList.bind(this);
        this.handleParamsChanged = this.handleParamsChanged.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
    //TODO: validation inputs  min max
    handleSubmit(e) {
        debugger;
        e.preventDefault();
        let params = {};
        Object.keys(this.state).forEach(prop => {
            let val = parseInt(this.state[prop].trim());
            if (isNaN(val)) throw new TypeError('You need to specify correct type.');
            params[prop] = val;
        });
        debugger;
        this.updateInputs(this.state);
        this.props.onFormSubmit({ params: params });
        return false;
    }
    handleParamsChanged(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    toggleFormList(e) {
        let $hideBtn = $('#hide-button');
        if (($hideBtn).hasClass('glyphicon-arrow-left')) {
            $('.form-submit').hide();
            $('.form-content').addClass('form-content-hidden');
            $($hideBtn).removeClass('glyphicon-arrow-left');
            $($hideBtn).addClass('glyphicon-arrow-right');
        } else {
            $('.form-submit').show();
            $('.form-content').removeClass('form-content-hidden');
            $($hideBtn).removeClass('glyphicon-arrow-right');
            $($hideBtn).addClass('glyphicon-arrow-left');
        }
    }
    updateInputs(data) {
        for (let prop in data) {
            if (prop == 'timespan' || prop == 'sampling') continue;
            // debugger;
            $(`#${prop}-value`)[0].innerHTML = data[prop];
        }
    }
    inputFocus(e) {
        e.preventDefault();
        let $hideBtn = $('#hide-button');
        if ($($hideBtn).hasClass('glyphicon-arrow-right')) {
            $('.form-submit').show();
            $('.form-content').removeClass('form-content-hidden');
            $($hideBtn).removeClass('glyphicon-arrow-right');
            $($hideBtn).addClass('glyphicon-arrow-left');
        }
        let target = e.target;
        $(`[name='${target.id}']`).focus();
    }

    componentDidMount() {
        let inputs = $('.input');
        for(let i = 0; i < inputs.length; i++) {
            inputs[i].onclick = this.inputFocus;
        }
    }
    // shouldComponentUpdate(nextProps, nextState) {
    //     debugger;
    //     this.state = nextState;
    //
    //     return false;
    // }
    render() {
        let paramsForms = '';
        if (this.props.parameters) {
            paramsForms = this.props.parameters.map((name, i) => {
                return(
                    <ParamsForm paramsName={name} paramsChanged={this.handleParamsChanged} key={i}/>
                )
            });
        }
        return(
            <div className="form-content">
                <div className="text-right">
                    <span className="glyphicon glyphicon-arrow-left" id="hide-button" onClick={this.toggleFormList}></span>
                </div>
                <form onSubmit={this.handleSubmit} className="form-submit">
                    {/*Default parameters*/}
                    <div className="form-group">
                        <label htmlFor="sampling">Sampling: </label>
                        <input type="range" name='sampling' defaultValue={1} min="0.02" max="5" step={0.2}
                               onChange={this.handleParamsChanged}/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="timespan">Timespan: </label>
                        <input type="text"  name='timespan'
                               onChange={this.handleParamsChanged}/>
                    </div>
                    {paramsForms}
                    <input type="submit" value="Update" className="btn btn-default"/>
                </form>
            </div>
        )
    }
}