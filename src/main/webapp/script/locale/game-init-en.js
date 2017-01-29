const gameinit = {
    "dealer": {
        "img": "/blackjack/img/dealer.jpg",
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
                "text": "Surrender",
                "command": "/blackjack/$/game/surrender"
            },
            "split": {
                "text": "Split",
                "command": "/blackjack/$/game/split"
            },
            "double": {
                "text": "Double",
                "command": "/blackjack/$/game/double"
            },
            "hit": {
                "text": "Hit",
                "command": "/blackjack/$/game/hit"
            },
            "deal": {
                "text": "Deal",
                "command": "/blackjack/$/game/deal"
            },
            "stay": {
                "text": "Stay",
                "command": "/blackjack/$/game/stay"
            },
            "insurance": {
                "text": "Insurance",
                "command": "/blackjack/$/game/insurance"
            }
        },
        "bid": {
            "buttonValues": [0.5, 1, 5, 25, 100]
        }
    }
}


/* TESTING VARIABLE */
let game = {
	    "dealer": {
	        "hand": {
	            "cards": []
	        }
	    },
	    "timer": {
	        "timeLimit": 120
	    },
	    "result": null,
	    "players": [
	        {
	            "id": 0,
	            "name": "PLAYER",
	            "img": "/blackjack/img/user/noimage.png",
	            "hands": [
	                {
	                    "score": 0,
	                    "cards": [
	                    ],
	                    "bet": 0,
	                    "isActive": true
	                }, {
	                    "score": 0,
	                    "cards": [ ],
	                    "bet": 0,
	                    "isActive": false
	                }
	            ],
	            "isActive": true
	        }, {
	            "id": 0,
	            "name": "PLAYER",
	            "img": "/blackjack/img/user/noimage.png",
	            "hands": [
	                {
	                    "score": 0,
	                    "cards": [ ],
	                    "bet": 0,
	                    "isActive": false
	                }
	            ],
	            "isActive": false
	        }, {
	            "id": 0,
	            "name": "PLAYER",
	            "img": "/blackjack/img/user/noimage.png",
	            "hands": [
	                {
	                    "score": 0,
	                    "cards": [ ],
	                    "bet": 0,
	                    "isActive": false
	                }
	            ],
	            "isActive": false
	        }
	    ],
	    "controls": {
	        "balance": {
	            "value": 1000.0
	        },
	        "actions": {
	            "surrender": {
	                "isActive": true
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
	            },
	            "stay": {
	                "isActive": true
	            },
	            "insurance": {
	                "isActive": true
	            }
	        },
	        "bid": {
	            "isActive": true
	        }
	    }
	}
