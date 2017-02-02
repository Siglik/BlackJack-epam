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
      let handler = (e)=>{
         e.preventDefault();
         this.props.doOnBid(this.props.value);
       };
      return (
        <a href="#" className={className} onClick={handler}>+{this.props.value}$</a>
      );
    }
}
