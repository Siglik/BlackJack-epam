import React from 'react';

import './Result.less'

export default class Result extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="result pop-up">
              <a href="#" onClick="" className="btn-exit">X</a>
            </div>
        );
    }

}
