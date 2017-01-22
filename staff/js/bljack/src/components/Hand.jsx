import React from 'react';
import Card from './Card.jsx';
import Bet from './Bet.jsx';
import Score from './Score.jsx';

export default class Hand extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let bet,
            score;
        if (!!this.props.stateValues.bet) {
            bet = (<Bet stateValues={this.props.stateValues.bet}/>);
        } else {
            bet = "";
        }
        if (!!this.props.stateValues.score) {
            score = (<Score stateValues={this.props.stateValues.score}/>);
        } else {
            score = "";
        }
        return (
            <div className={"hand " + this.props.className + (this.props.stateValues.isActive
                ? "active"
                : "")}>
                {score}
                {this.props.stateValues.cards.map((card, idx) => {
                    return (<Card key={idx} stateValues={card} className={idx === 0
                        ? "first-card"
                        : ""}/>);
                })}

                {bet}
            </div>
        )
    }
}
