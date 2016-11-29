import React from 'react';
import Card from './Card.jsx';
import Bet from './Bet.jsx';

export default class Hand extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let bet;
        if (!!this.props.stateValues.bet) {
            bet = (<Bet stateValues={this.props.stateValues.bet}/>);
        } else {
            bet = "";
        }
        return (
            <div className={"hand " + this.props.className +  (this.props.stateValues.isActive ? "active" : "")}>
                {this.props.stateValues.cards.map((card, idx) => {
                    return (<Card key={idx} stateValues={card} className={idx===0 ? "first-card" : ""}/>);
                })}
                {bet}
            </div>
        )
    }
}
