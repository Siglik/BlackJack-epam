import React from 'react';
import Hand from './Hand.jsx'

import './Dealer.less';

export default class BlackJackApp extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
          <div className="dealer container">
              <figure className="column-left player-info">
                  <img src={this.props.initValues.img} alt="dealer" className="dealer-logo" />
                  <figcaption>{this.props.initValues.name}</figcaption>
              </figure>
              <Hand className="column-right" stateValues={this.props.stateValues.hand}/>
          </div>
        );
    }
}
