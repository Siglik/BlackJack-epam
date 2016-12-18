import React from 'react';

import './BidButton.less';

export default class BidButton extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
      let className = "button control-key bid";
      if (!this.props.isActive){
        className += " not-active";
      }
      return (
        <a href="#" className={className}>+{this.props.value}$</a>
      );
    }
}
