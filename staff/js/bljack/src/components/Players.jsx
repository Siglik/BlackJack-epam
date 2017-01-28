import React from 'react';
import Player from "./Player.jsx"

import './Players.less'

export default class Players extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="players container">
              <Player className="column-left" stateValues={this.props.stateValues[0]}/>
              <Player className="column-center" stateValues={this.props.stateValues[1]}/>
              <Player className="column-right" stateValues={this.props.stateValues[2]}/>
            </div>
        );
    }

}
