const gameinit = {
    "dealer": {
        "img": "/blackjack/img/dealer.jpg",
        "name": "Дилер"
    },
    "timer": {
        "text": "Времени осталось:"
    },
    "players": {},
    "controls": {
        "balance": {
            "text": "Текущий баланс"
        },
        "actions": {
            "surrender": {
                "text": "Сдаться"
            },
            "split": {
                "text": "Разделить"
            },
            "double": {
                "text": "Удвоить"
            },
            "hit": {
                "text": "Еще"
            },
            "deal": {
                "text": "Ставка"
            },
            "stay": {
                "text": "Хватит"
            },
            "insurance": {
                "text": "Застраховать"
            }
        },
        "bid": {
            "buttonValues": [1, 5, 25, 100]
        }
    }
}


/* TESTING VARIABLE */
let game = {
    "dealer": {
        "hand": {
            "cards": ["/blackjack/img/card/ace.gif", "/blackjack/img/card/back.gif", "/blackjack/img/card/back.gif"]
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
            "img": "/blackjack/img//user/p_one.jpg",
            "hands": [
                {
                    "score": 10,
                    "cards": [
                        "/blackjack/img/card/ace.gif", "/blackjack/img/card/ace.gif"
                    ],
                    "bet": 10,
                    "isActive": false
                }, {
                    "score": 10,
                    "cards": [
                        "/blackjack/img/card/ace.gif", "/blackjack/img/card/ace.gif", "/blackjack/img/card/ace.gif"
                    ],
                    "bet": 75,
                    "isActive": true
                }
            ],
            "isActive": true
        }, {
            "id": 0,
            "name": "Player Two",
            "img": "/blackjack/img/user/pl_three.jpg",
            "hands": [
                {
                    "score": "blackjack",
                    "cards": [
                        "/blackjack/img/card/ace.gif", "/blackjack/img/card/back.gif"
                    ],
                    "bet": 11,
                    "isActive": false
                }
            ],
            "isActive": false
        }, {
            "id": 0,
            "name": "Player Three",
            "img": "/blackjack/img/user/pl_two.png",
            "hands": [
                {
                    "score": 10,
                    "cards": [
                        "/blackjack/img/card/ace.gif", "/blackjack/img/card/back.gif"
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
