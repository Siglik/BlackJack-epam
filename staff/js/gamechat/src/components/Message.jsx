import React from 'react';
import $ from 'jquery';

import './Chat.less'

export default class Message extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        var message = this.props.message;
        return (
            <p className="message">
                <span className="sender">
                  <img src={message.senderImg} alt={message.senderName} className="chat-logo"/>{message.senderName}
                </span>:
                <span className="message-text">
                    {message.text}
                </span>
            </p>
        );
    }
}
