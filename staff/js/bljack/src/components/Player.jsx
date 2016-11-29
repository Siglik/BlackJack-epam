import React from 'react';
import Hand from "./Hand.jsx";

import './Player.less'

export default class Player extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className={"player " + this.props.className}>
              {!!this.props.stateValues ?
                  this.props.stateValues.hands.map((hand, idx) => {
                      return (<Hand key={idx} stateValues={hand} className=""/> );
                  }) : ""
              }

                <figure className={"player-info " + (this.props.stateValues.isActive ? "active" : "")}>
                    <img src={this.props.stateValues.img} alt="player" className="player-logo"/>
                    <figcaption>{this.props.stateValues.name}</figcaption>
                </figure>
            </div>
        )
    }
}
