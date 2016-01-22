var path = require('path');
var webpack = require('webpack');

module.exports = {
    entry: {
    },
    output: {
        path: path.join(__dirname, '..', 'resources', 'js'),
        filename: '[name].js'
    },
    module: {
        loaders: [
            { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader' }
        ]
    }
};
