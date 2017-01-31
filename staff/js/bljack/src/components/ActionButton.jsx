import React from 'react';

import './ActionButton.less'

export default class ActionButton extends React.Component {
    constructor(props) {
        super(props);
        this.handleDeal = this.handleDeal.bind(this);
        this.handleAction = this.handleAction.bind(this);
    }
    handleAction(e){
      var inst = this;
      e.preventDefault();
      $.getJSON(this.props.command, function() {
          //console.log(inst.props.command +" EXECUTED");
      }).done(function(data) {
          if (data.result === "OK") {
              inst.props.doOnAction();
          } else {
            console.log(data.result);
            console.log(data.message);
          }
      })
    }
    handleDeal(bid){
      var inst = this;
      $.getJSON(this.props.command+"?bid="+bid, function() {
        //console.log(inst.props.command +"?bid="+bid+" EXECUTED");
      }).done(function(data) {
          if (data.result === "OK") {
              inst.props.doOnAction();
          } else {
              console.log(data.result);
              console.log(data.message);
          }
      })
    }
    render() {
        let className = "button control-key";
        let bid = "";
        let handler = this.handleAction;
        if (!this.props.stateValues.isActive) {
            className += " not-active hidden";
        }
        if (!!this.props.curBid) {
            bid = " $" + this.props.curBid;
            handler = (e) => {
              e.preventDefault();
              this.handleDeal(this.props.curBid);
            };
        }
        return (
            <a href="#" className={className} onClick={handler}>{this.props.children}{bid}</a>
        );
    }
}
