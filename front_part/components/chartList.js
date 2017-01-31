'use strict';

import {ChartBox} from './chart'

export class ChartList extends React.Component {
    render() {
        console.log('CHARTS: ' + this.props.charts);
        let chartNodes = this.props.charts.map((chart, i) => {
            return(
                <div className="multiple-charts thumbnail" key={i}>
                    <ChartBox dps={chart.dps} name={chart.name}/>
                </div>
            )
        });
        return(
            <div className="row">
                {chartNodes}
            </div>
        )
    }
}