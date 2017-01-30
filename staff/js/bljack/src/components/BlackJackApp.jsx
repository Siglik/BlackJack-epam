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
        let game = this.props.game;
        game.controls.curBid = this.curBid;
        this.state = {
            "game": game
        };
    }
    componentDidMount() {
        //console.log("did mount");
        this.startUpdateTick();
    }
    componentWillUnmount() {
        this.stopUpdateTick();
    }

    startUpdateTick() {
        this.updateTicker = setInterval(() => this.tick(), 1200);
    }

    stopUpdateTick() {
        clearInterval(this.updateTicker);
    }

    tick() {
        this.update();
    }
    update() {
        var inst=this;
        $.getJSON('/blackjack/$/game/getstate', function() {
          //  console.log("success got data");
        }).done(function(data) {
            if (data.result !== "ERROR") {
                let newGame = data;
                newGame.controls.curBid = inst.curBid;
                newGame.controls.balance.value -= inst.curBid;
                inst.setState((prevState) => {
                    return {game: newGame}
                });
            } else {
                alert(data.result)
            }
        }).fail(function() {
            //console.log("error update");
        });
    }
    onTimeOff() {
        //console.log('time is off!');
        let newGame = this.state.game;
        this.setState((prevState) => {
            return {game: newGame}
        });
    }
    onAction() {
      this.curBid = 0;
      this.update();
    }
    onBid(value) {
        this.curBid += value;
        this.update();
    }
    render() {
        let game = this.state.game;
        /*<GameTimer initValues={this.props.initValues.timer} stateValues={game.timer} onTimeOff={this.onTimeOff.bind(this)}/>*/
        return (
            <div className="game">
              <Dealer initValues={this.props.initValues.dealer} stateValues={game.dealer}/>
              <Players initValues={this.props.initValues.players} stateValues={game.players}/>
              <Controls initValues={this.props.initValues.controls} stateValues={game.controls} doOnAction={this.onAction.bind(this)} doOnBid={this.onBid.bind(this)}/>
            </div>
        );
    }
}
