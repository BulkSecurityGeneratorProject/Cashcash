const helpers = require('./helpers');
const webpack = require('webpack');

let config = {
    entry: helpers.root("/src/main/webapp/js/app.module.js"),

    output: {
        path: helpers.root("/target/www"),
        filename: "dev.[name].js",
        chunkFilename: "[name].js"
    },

    devtool: 'source-map',

    module: {
        rules: [
            {
                // JS LOADER
                test: /\.js$/,
                exclude: /node_modules/,
                use: [
                    {
                        loader: "ng-annotate-loader"
                    },
                    {
                        loader: "babel-loader",
                        options: {
                            presets: ['es2015']
                        }
                    }
                ]
            },
            {
                // HTML LOADER
                test: /\.html$/,
                loader: 'html-loader'
            }
        ]
    },

    plugins: [
        new webpack.ProvidePlugin({
            "$": "jquery",
            "jQuery": "jquery",
            "window.jQuery": "jquery",
            "Selectize": "selectize"
        }),
        new webpack.BannerPlugin("Copyright Samuel GAGNEPAIN"),
        new webpack.optimize.CommonsChunkPlugin({
            name: "vendor",
            minChunks: module => {
                // this assumes your vendor imports exist in the node_modules directory
                return module.context && module.context.indexOf('node_modules') !== -1;
            }
        })
    ]
};

module.exports = config;
