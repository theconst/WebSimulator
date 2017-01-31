const path = require('path'),
    webpack = require('webpack');

const APP_DIR = path.resolve(__dirname, 'public/');

module.exports = {
    entry: path.resolve(__dirname, 'main.js'),
    output: { path: APP_DIR, filename: 'bundle.js'},
    module: {
        loaders: [
            {
                test: /.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                query: {
                    presets: ['es2015', 'react']
                }
            }
        ]
    }
};