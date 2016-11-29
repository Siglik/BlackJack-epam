import React from 'react';
import Balance from './Balance.jsx';

import './Controls.less';

export default class Controls extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div className="controls container">
                <div className="column-left">
                    <Balance initValues={this.props.initValues.balance} stateValues={this.props.stateValues.balance}/>
                </div>
                <ul className="gorizontal-menu column-center">
                    <li>
                        <a href="#" className="button control-key">Surrender</a>
                    </li>
                    <li>
                        <a href="#" className="button control-key not-active">Split</a>
                    </li>
                    <li>
                        <a href="#" className="button control-key">Double</a>
                    </li>
                    <li>
                        <a href="#" className="button control-key">Hit</a>
                    </li>
                    <li>
                        <a href="#" className="button control-key">Deal</a>
                    </li>
                </ul>
                <ul className="gorizontal-menu column-right">
                    <li>
                        <a href="#" className="button control-key bid">+1$</a>
                    </li>
                    <li>
                        <a href="#" className="button control-key bid">+5$</a>
                    </li>
                    <li>
                        <a href="#" className="button control-key bid">+25$</a>
                    </li>
                    <li>
                        <a href="#" className="button control-key bid">+100$</a>
                    </li>
                </ul>
            </div>
        );
    }
}
