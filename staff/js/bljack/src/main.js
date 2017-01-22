import React from 'react';
import ReactDOM from 'react-dom';
import BlackJackApp from './components/BlackJackApp.jsx';

/*import GameField from './components/GameField.jsx';
import Player from './components/Player.jsx';
import Hand from './components/Hand.jsx';
import bet from './components/bet.jsx';
import Controls from './components/Controls.jsx';
import ControlKey from './components/ControlKey.jsx'
*/

import "./dev-data/some_structures.js"

ReactDOM.render(
    <BlackJackApp game={game} initValues={gameinit}/>, document.getElementById('black-jack'));
