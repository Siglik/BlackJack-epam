import React from 'react';
import Player from "./Player.jsx"

import './Score.less'

export default class Players extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let postfix = "";
        if (this.props.stateValues !== "blackjack"){
          postfix = " pts";
        }
        return (
            <div className="score">
                {this.props.stateValues}{postfix}
            </div>
        );
    }

}
