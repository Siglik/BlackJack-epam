import React from 'react';
import MessageInput from "./MessageInput.jsx";
import MessageBox from "./MessageBox.jsx";
import $ from 'jquery';

import './Chat.less'

export default class Chat extends React.Component {
    constructor(props) {
        super(props);
        let chat = this.props.chatInit;
        this.state = {
            "chat": chat
        };
    }

    componentDidMount() {
        let port = location.port == 80 || !location.port
            ? ""
            : ":" + location.port;
        let url = "ws://" + document.domain + port + "/blackjack/chat";
        this.wsocket = new WebSocket(url);
        this.wsocket.onmessage = this.onMessage.bind(this);
    }
    componentWillUnmount() {}
    onMessageSendHandler(message) {
        this.wsocket.send(JSON.stringify(message));
    }
    onMessage(evt) {
        //console.log("got message");
        let newMessage = JSON.parse(evt.data);
        //console.log(newMessage);
        let newChat = this.state.chat;
        newChat.messages.push(newMessage);
        //console.log(newChat);
        this.setState((prevState) => {
            return {chat: newChat}
        });
    }

    render() {
        let chat = this.state.chat;
        return (
            <div id="chat">
                <MessageInput onMessageSendHandler={this.onMessageSendHandler.bind(this)}/>
                <MessageBox messages={this.state.chat.messages}/>
            </div>
        );
    }
}
