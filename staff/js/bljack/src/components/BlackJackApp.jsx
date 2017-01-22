import React from 'react';
import Dealer from './Dealer.jsx';
import GameTimer from './GameTimer.jsx';
import Players from './Players.jsx';
import Controls from './Controls.jsx';
import $ from 'jquery';

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
      let newGame = this.state.game;
      newGame.controls.balance.value += 1;
      newGame.timer.timeLimit += 2;
      this.setState((prevState) => {
        return {
          game: newGame
        }
      });
    }
    render() {
        let game = this.state.game;
        return (
            <div className="game">
                <Dealer initValues={this.props.initValues.dealer} stateValues={game.dealer}/>
                <GameTimer initValues={this.props.initValues.timer}  stateValues={game.timer} onTimeOff={this.onTimeOff.bind(this)}/>
                <Players initValues={this.props.initValues.players} stateValues={game.players}/>
                <Controls initValues={this.props.initValues.controls} stateValues={game.controls}/>
            </div>
        );
    }
}
