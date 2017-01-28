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
                <ul className="gorizontal-menu column-center contr-actions">
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.insurance} command={this.props.initValues.actions.insurance.command} doOnAction={this.props.doOnAction}>
                            {this.props.initValues.actions.insurance.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.surrender} command={this.props.initValues.actions.surrender.command} doOnAction={this.props.doOnAction}>
                            {this.props.initValues.actions.surrender.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.split} command={this.props.initValues.actions.split.command} doOnAction={this.props.doOnAction}>
                            {this.props.initValues.actions.split.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.double} command={this.props.initValues.actions.double.command} doOnAction={this.props.doOnAction}>
                            {this.props.initValues.actions.double.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.hit} command={this.props.initValues.actions.hit.command} doOnAction={this.props.doOnAction}>
                            {this.props.initValues.actions.hit.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.deal} command={this.props.initValues.actions.deal.command} curBid={this.props.stateValues.curBid} doOnAction={this.props.doOnAction}>
                            {this.props.initValues.actions.deal.text}
                        </ActionButton>
                    </li>
                    <li>
                        <ActionButton stateValues={this.props.stateValues.actions.stay} command={this.props.initValues.actions.stay.command} doOnAction={this.props.doOnAction}>
                            {this.props.initValues.actions.stay.text}
                        </ActionButton>
                    </li>
                </ul>
                <ul className="gorizontal-menu column-right contr-bid">
                    {this.props.initValues.bid.buttonValues.map((value, idx) => {
                        let isActive = this.props.stateValues.bid.isActive;
                        if (balance.value < value) {
                            isActive = false;
                        }
                        return (
                            <li key={idx}><BidButton value={value} isActive={isActive} doOnBid={this.props.doOnBid}/></li>
                        );
                    })}
                </ul>
            </div>
        );
    }
}
