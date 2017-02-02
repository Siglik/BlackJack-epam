import React from 'react';

import './Balance.less';

export default class Balance extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <figure className="balance">
                <p className="currency cur-balance">
                    {this.props.stateValues.value.toFixed(2)}
                </p>
                <figcaption>{this.props.initValues.text}</figcaption>
            </figure>
        );
    }
}
