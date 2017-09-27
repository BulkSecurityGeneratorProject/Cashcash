const webpack = require("webpack");
const webpackConfig = require('./webpack.config.base');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const autoprefixer = require('autoprefixer');

webpackConfig.module.rules = [...webpackConfig.module.rules,
    {
        //CSS LOADER
        test: /\.css$/,
        use: [
            {
                loader: 'style-loader'
            },
            {
                loader: "css-loader"
            },
            {
                loader: "postcss-loader",
                options: {
                    plugins: () => [autoprefixer]
                }
            }
        ]
    },
    {
        //LESS LOADER
        test: /\.less/,
        use: [
            {
                loader: 'style-loader'
            },
            {
                loader: "css-loader"
            },
            {
                loader: "postcss-loader",
                options: {
                    plugins: () => [autoprefixer]
                }
            },
            {
                loader: 'less-loader'
            }
        ]
    },
    {
        //IMAGE LOADER
        test: /\.(ico|png|jpe|jpg|woff|woff2|eot|ttf|svg)(\?.*$|$)/,
        loader: 'url-loader',
        options: {
            limit: 3000,
            name: '[name].[ext]'
        }
    }
];

webpackConfig.plugins = [...webpackConfig.plugins,
    new HtmlWebpackPlugin({
        template: "./src/main/webapp/resources/index.tmpl.html",
        favicon: './src/main/webapp/resources/favicon.ico'
    }),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.LoaderOptionsPlugin({
        debug: true
    }),
    new ExtractTextPlugin("[name].css")
];

webpackConfig.devServer = {
    contentBase: "./target/www",
    port: 8081,
    historyApiFallback: true,
    inline: true,
    hot: true,
    proxy: {
        '/api': {
            target: 'http://localhost:8080',
            secure: false
        }
    }
};

module.exports = webpackConfig;
