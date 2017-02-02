import React from 'react';
import Message from './Message.jsx'
import $ from 'jquery';

import './Chat.less'

export default class MessageBox extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidUpdate(){
      $("div.messages").scrollTop($("div.messages")[0].scrollHeight);
    }

    render() {
        var messages =  this.props.messages.map((message) =>
        <Message message={message} key={message.id}/>
      );
        return (
            <div className="messages">
              {messages}
            </div>
        );
    }
}
