import React from 'react';

export default class Hand extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
                  <img src={this.props.stateValues} alt="card" className={"card " + this.props.className} />
        );
    }
}
