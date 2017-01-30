import React from 'react';
import $ from 'jquery';

import './Chat.less'

export default class MessageInput extends React.Component {
  constructor(props) {
      super(props);
  }

  onKeyPressHandler(e) {
      if (e.nativeEvent.keyCode != 13)
          return;

      var input = e.target;
      var text = input.value;

      // if the text is blank, do nothing
      if (text === "")
          return;

      var message = {
          text: text
      }

      let handler = this.props.onMessageSendHandler;
      handler(message);
      input.value = "";
  }

  render() {
      return (
            <div id="send-message">
                <input name="message" placeholder="Press Enter to send" onKeyPress={this.onKeyPressHandler.bind(this)}/>
            </div>
      );
  }
}
