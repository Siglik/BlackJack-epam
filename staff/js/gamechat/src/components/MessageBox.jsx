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
        var messages = [];
        for (var i = 0; i < this.props.messages.length; i++) {
            messages.push(<Message message={this.props.messages[i]}/>);
        }
        return (
            <div className="messages">
              {messages}
            </div>
        );
    }
}
