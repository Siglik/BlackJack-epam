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
        this.curBid = 0.0;
        let newGame = this.state.game;
        newGame.controls.curBid.value = curBid;
        this.state = {
            "game": newGame
        };
    }
    componentDidMount() {
      console.log("did mount");
        this.startUpdateTick();
    }
    componentWillUnmount() {
        this.stopUpdateTick();
    }

    startUpdateTick(){
      this.updateTicker = setInterval(() => this.tick(), 1000);
    }

    stopUpdateTick() {
        clearInterval(this.updateTicker);
    }

    tick() {
        update();
    }
    update() {
      console.log("update");
        $.getJSON('/blackjack/$/game/getstate').done(function(data) {
            console.log("got data");
            if (data.result !== "ERROR") {
                let newGame = data;
                newGame.controls.curBid.value = this.curBid;
                newGame.controls.balance.value -= this.curBid;
                this.setState((prevState) => {
                    return {game: newGame}
                });
            } else {
                alert(data.result)
            }
        });
    }
    onTimeOff() {
        console.log('time is off!');
        let newGame = this.state.game;
        newGame.controls.balance.value += 1;
        newGame.timer.timeLimit += 2;
        this.setState((prevState) => {
            return {game: newGame}
        });
    }
    onAction(actionName) {
        let newGame = this.state.game;

        this.setState((prevState) => {
            return {game: newGame}
        });
    }
    onBid(value) {
        this.curBid += value;
        update();
    }
    render() {
        let game = this.state.game;
        return (
            <div className="game">
                <Dealer initValues={this.props.initValues.dealer} stateValues={game.dealer}/>
                <GameTimer initValues={this.props.initValues.timer} stateValues={game.timer} onTimeOff={this.onTimeOff.bind(this)}/>
                <Players initValues={this.props.initValues.players} stateValues={game.players}/>
                <Controls initValues={this.props.initValues.controls} stateValues={game.controls}/>
            </div>
        );
    }
}
