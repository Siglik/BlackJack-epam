import React from 'react';
import Balance from './Balance.jsx';
import ActionButton from './ActionButton.jsx';
import BidButton from './BidButton.jsx';

import './Controls.less';

export default class Controls extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        let balance = this.props.stateValues.balance;
        return (
            <div className="controls container">
                <div className="column-left">
                    <Balance initValues={this.props.initValues.balance} stateValues={balance}/>
                </div>
                <ul className="gorizontal-menu column-center">
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.surrender}>
                            {this.props.initValues.actions.surrender.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.split}>
                            {this.props.initValues.actions.split.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.double}>
                            {this.props.initValues.actions.double.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.hit}>
                            {this.props.initValues.actions.hit.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.deal}>
                            {this.props.initValues.actions.deal.text}
                        </ActionButton>
                    </li>
                </ul>
                <ul className="gorizontal-menu column-right">
                    {this.props.initValues.bid.buttonValues.map((value, idx) => {
                        let isActive = this.props.stateValues.bid.isActive;
                        if (balance.value < value) {
                            isActive = false;
                        }
                        return (
                            <li key={idx}><BidButton value={value} isActive={isActive}/></li>
                        );
                    })}
                </ul>
            </div>
        );
    }
}
