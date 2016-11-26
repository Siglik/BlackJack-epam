import React from 'react';
import ReactDOM from 'react-dom';
import BlackJackApp  from './components/BlackJackApp.jsx';
/*import GameField from './components/GameField.jsx';
import Player from './components/Player.jsx';
import Hand from './components/Hand.jsx';
import Bid from './components/Bid.jsx';
*/
import GameTimer from './components/GameTimer.jsx';
/*
import Controls from './components/Controls.jsx';
import ControlKey from './components/ControlKey.jsx'
*/
ReactDOM.render(
    <BlackJackApp />,
    document.getElementById('black-jack')
);
