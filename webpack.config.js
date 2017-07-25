var path = require('path');
var webpack = require('webpack')
var ROOT = path.resolve(__dirname, 'src/main/webapp');
var SRC = path.resolve(ROOT, 'javascript');
var DEST = path.resolve(__dirname, 'src/main/webapp/dist');
 
module.exports = {
  devtool: 'source-map',
  entry: {
    app: SRC + '/index.js',
  },
  resolve: {
    modules: [
      path.join(__dirname, 'src/main/webapp/javascript'),
      path.join(__dirname, 'src/main/webapp/css'),
      "node_modules"
    ],
    extensions: ['.js', '.jsx']
  },
  output: {
    path: DEST,
    filename: 'bundle.js',
    publicPath: '/dist/'
  },
  module: {
    loaders: [
      {
        test: /\.jsx?$/,  // Notice the regex here. We're matching on js and jsx files.
        loaders: ['babel-loader'],
        include: path.join(__dirname, 'src')
      },
      {test: /\.css$/, loader: 'style-loader!css-loader'},
      {test: /\.less$/, loader: 'style!css!less'},
 
      // Needed for the css-loader when [bootstrap-webpack](https://github.com/bline/bootstrap-webpack)
      // loads bootstrap's css.
      {test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&amp;mimetype=application/font-woff'},
      {test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&amp;mimetype=application/octet-stream'},
      {test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: 'file'},
      {test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&amp;mimetype=image/svg+xml'}
    ]
  }
};