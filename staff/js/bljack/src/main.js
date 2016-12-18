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

let game = {
    "dealer": {
        "hand": {
            "score": 10,
            "cards": ["img/card/ace.gif", "img/card/back.gif", "img/card/back.gif"]
        }
    },
    "timer": {
        "timeLimit": 5
    },
    "result": null,
    "players": [
        {
            "id": 0,
            "name": "qqq175",
            "img": "img/p_one.jpg",
            "hands": [
                {
                    "score": 10,
                    "cards": [
                        "img/card/ace.gif", "img/card/ace.gif"
                    ],
                    "bet": 10,
                    "isActive": false
                }, {
                    "score": 10,
                    "cards": [
                        "img/card/ace.gif", "img/card/ace.gif", "img/card/ace.gif"
                    ],
                    "bet": 75,
                    "isActive": true
                }
            ],
            "isActive": true
        }, {
            "id": 0,
            "name": "Player Two",
            "img": "img/pl_three.jpg",
            "hands": [
                {
                    "score": 10,
                    "cards": [
                        "img/card/ace.gif", "img/card/back.gif"
                    ],
                    "bet": 11,
                    "isActive": false
                }
            ],
            "isActive": false
        }, {
            "id": 0,
            "name": "Player Three",
            "img": "img/pl_two.png",
            "hands": [
                {
                    "score": 10,
                    "cards": [
                        "img/card/ace.gif", "img/card/back.gif"
                    ],
                    "bet": 17,
                    "isActive": false
                }
            ],
            "isActive": false
        }
    ],
    "controls": {
        "balance": {
            "value": 0.25
        },
        "actions": {
            "surrender": {
                "isActive": false
            },
            "split": {
                "isActive": true
            },
            "double": {
                "isActive": true
            },
            "hit": {
                "isActive": true
            },
            "deal": {
                "isActive": true
            }
        },
        "bid": {
            "isActive": true
        }
    }
}

const gameinit = {
    "dealer": {
        "img": "img/dealer.jpg",
        "name": "Dealer"
    },
    "timer": {
        "text": "Time left:"
    },
    "players": {},
    "controls": {
        "balance": {
            "text": "Current balance"
        },
        "actions": {
            "surrender": {
                "text": "Surrender"
            },
            "split": {
                "text": "Split"
            },
            "double": {
                "text": "Double"
            },
            "hit": {
                "text": "Hit"
            },
            "deal": {
                "text": "Deal"
            }
        },
        "bid": {
            "buttonValues": [1, 5, 25, 100]
        }
    }
}

ReactDOM.render(
    <BlackJackApp game={game} initValues={gameinit}/>, document.getElementById('black-jack'));
