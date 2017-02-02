import React from 'react';

import './Bet.less'

export default class Bet extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        let insurance;
        if (!!this.props.stateValues.insurance) {
            insurance = (<p className='insurance'>${this.props.stateValues.insurance}</p>);
        } else {
            insurance = "";
        }
        return (
          <div>
            {insurance}
            <figure className="bet">
                <img src="/blackjack/img/chips.png" alt="chips" className="chips"/>
                <figcaption>${this.props.stateValues.bet}</figcaption>
            </figure>
          </div>
        );
    }
}
