import React from 'react';

import './GameTimer.css';

export default class GameTimer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      secondsLeft: this.props.initialTime
    };
  }


  reset(){
    this.state = {
      secondsLeft: this.props.initialTime
    };
  }

  tick() {
    this.setState((prevState) => ({
      secondsLeft: this.decrease(prevState.secondsLeft)
    }));
  }

  decrease(seconds){
    if(seconds === 0){
      this.stopTimer();
    }
    return (seconds - 1 > 0) ? seconds - 1 : 0;
  }

  componentDidMount() {
    this.timer = setInterval(() => this.tick(), 1000);
  }

  componentWillUnmount() {
    this.stopTimer();
  }

  stopTimer(){
    clearInterval(this.timer);
    console.log("time is off");
    this.props.onTimeOff();
  }

  render() {
    return (
      <div className="game-timer">Time left: {Math.floor(this.state.secondsLeft/60)}:{this.state.secondsLeft%60}</div>
    );
  }
}
