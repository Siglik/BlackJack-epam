import React from 'react';
import GameTimer from './GameTimer.jsx';

import './BlackJackApp.css';

export default class BlackJackApp extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: null
        }
    }
    render() {
        const onTimeOff = () => {
            alert('time is off!');
        }
        return (
            <div>
                <p>
                    test
                </p>
                <GameTimer initialTime="10" onTimeOff={onTimeOff}/>
            </div>
        );
    }
}
