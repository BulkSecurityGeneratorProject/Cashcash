const HtmlWebpackPlugin = require("html-webpack-plugin");
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const helpers = require('./helpers');
const autoprefixer = require('autoprefixer');
const UglifyJsPlugin = require('webpack/lib/optimize/UglifyJsPlugin');

webpackConfig.output.filename = "prod.[name].[chunkhash].js";
webpackConfig.output.chunkFilename = "[name].[chunkhash].js";

const extractCss = new ExtractTextPlugin({
    filename: 'css/[name].[contenthash].css',
    disable: process.env.NODE_ENV === 'development'
});

webpackConfig.module.rules = [...webpackConfig.module.rules,
    {
        //CSS LOADER
        test: /\.css$/,
        use: extractCss.extract({
            use: [
                {
                    loader: "css-loader",
                    options: {
                        minimize: true,
                        sourceMap: true,
                        importLoaders: 2
                    }
                },
                {
                    loader: "postcss-loader",
                    options: {
                        plugins: () => [autoprefixer]
                    }
                }
            ],
            // use style-loader in development
            fallback: 'style-loader'
        })
    },
    {
        //LESS LOADER
        test: /\.less/,
        use: extractCss.extract({
            use: [
                {
                    loader: "css-loader",
                    options: {
                        minimize: true,
                        sourceMap: true,
                        importLoaders: 2
                    }
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
            ],
            // use style-loader in development
            fallback: 'style-loader'
        })
    },
    {
        //IMAGE LOADER
        test: /\.(ico|png|jpe|jpg|woff|woff2|eot|ttf|svg)(\?.*$|$)/,
        loader: 'url-loader',
        options: {
            limit: 3000,
            name: '[name].[hash].[ext]'
        }
    }
];

webpackConfig.plugins = [...webpackConfig.plugins,
    new HtmlWebpackPlugin({
        inject: true,
        template: helpers.root('/src/main/webapp/resources/index.tmpl.html'),
        favicon: helpers.root('/src/main/webapp/resources/favicon.ico'),
        minify: {
            removeComments: true,
            collapseWhitespace: true,
            removeRedundantAttributes: true,
            useShortDoctype: true,
            removeEmptyAttributes: true,
            removeStyleLinkTypeAttributes: true,
            keepClosingSlash: true,
            minifyJS: true,
            minifyCSS: true,
            minifyURLs: true
        }
    }),
    new UglifyJsPlugin({
        include: /\.min\.js$/,
        minimize: true
    }),
    new ExtractTextPlugin("styles.[contenthash].css")
];


module.exports = webpackConfig;