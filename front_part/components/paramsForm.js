import React from 'react';

export class ParamsForm extends React.Component {
    render() {
        return(
            <div className="form-group">
                <label htmlFor={this.props.paramsName}>{this.props.paramsName.charAt(0).toUpperCase() + this.props.paramsName.slice(1)} : </label>
                <input type="number" name={this.props.paramsName} id={this.props.paramsName} onChange={this.props.paramsChanged} min="0" max="100"/>
            </div>
        )
    }
}

