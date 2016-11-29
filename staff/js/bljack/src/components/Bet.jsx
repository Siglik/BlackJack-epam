import React from 'react';

import './Bet.less'

export default class Bet extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
          <div>
            <figure className="bet">
                <img src="img/chips.png" alt="chips" className="chips"/>
                <figcaption>{this.props.stateValues}$</figcaption>
            </figure>
          </div>
        );
    }
}
