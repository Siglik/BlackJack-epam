import React from 'react';

import './GameTimer.less';

export default class GameTimer extends React.Component {
    constructor(props) {
        super(props);
        this.reset();
    }

    reset() {
        this.state = {
            secondsLeft: this.props.stateValues.timeLimit,
            classNames: "game-timer"
        };
    }

    tick() {
        let classNames = "game-timer";
        if (this.state.secondsLeft <= 15) {
            classNames += " time-almost-off"
        }
        this.setState((prevState) => ({
            secondsLeft: this.decrease(prevState.secondsLeft),
            classNames: classNames
        }));
    }

    decrease(seconds) {
        if (seconds === 0) {
            this.stopTimer();
        }
        return (seconds - 1 > 0)
            ? seconds - 1
            : 0;
    }

    componentDidMount() {
        this.timer = setInterval(() => this.tick(), 1000);
    }

    componentWillUnmount() {
        this.stopTimer();
    }

    stopTimer() {
        clearInterval(this.timer);
        this.props.onTimeOff();
    }

    render() {
        return (
            <div className={this.state.classNames} ref="timer">{this.props.initValues.text} {Math.floor(this.state.secondsLeft / 60)}:{this.state.secondsLeft % 60}</div>
        );
    }
}
