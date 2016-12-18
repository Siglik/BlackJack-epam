import React from 'react';

import './ActionButton.less'

export default class ActionButton extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        let className = "button control-key";
        if (!this.props.stateValues.isActive) {
            className += " not-active";
        }
        return (
            <a href="#" className={className}>{this.props.children}</a>
        );
    }
}
