import $ from 'jquery';
import React from 'react';
import ReactDOM from 'react-dom';
import BlackJackApp from './components/BlackJackApp.jsx';

import "./dev-data/some_structures.js"

$.getJSON('/blackjack/$/game/getstate', function() {
    //console.log("success");
}).done(function(data) {
    ReactDOM.render(
        <BlackJackApp game={data} initValues={gameinit}/>, document.getElementById('black-jack'));
});
