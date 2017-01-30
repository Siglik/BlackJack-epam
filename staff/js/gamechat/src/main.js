import $ from 'jquery';
import React from 'react';
import ReactDOM from 'react-dom';
import Chat from './components/Chat.jsx';

var chatInit = {
    messages: []
}

ReactDOM.render(
    <Chat chatInit={chatInit}/>, document.getElementById('gamechat'));
