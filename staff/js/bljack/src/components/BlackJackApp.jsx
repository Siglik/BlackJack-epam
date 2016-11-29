import React from 'react';
import Dealer from './Dealer.jsx';
import GameTimer from './GameTimer.jsx';
import Players from './Players.jsx';
import Controls from './Controls.jsx';

import './BlackJackApp.less';

export default class BlackJackApp extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            "game": this.props.game
        };
    }
    onTimeOff () {
      console.log('time is off!');
      this.setState((prevState) => {
        let newGame = prevState.game;
        newGame.timer.timeLimit = 45;
        return {
          game: newGame
        }
      });
    }
    render() {
        return (
            <div className="game">
                {this.state.game.timer.timeLimit}
                <Dealer initValues={this.props.initValues.dealer} stateValues={this.state.game.dealer}/>
                <GameTimer initValues={this.props.initValues.timer}  stateValues={this.state.game.timer} onTimeOff={this.onTimeOff.bind(this)}/>
                <Players initValues={this.props.initValues.players} stateValues={this.state.game.players}/>
                <Controls initValues={this.props.initValues.controls} stateValues={this.state.game.controls}/>
            </div>
        );
    }
}
