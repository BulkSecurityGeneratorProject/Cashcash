var webpack = require("webpack");
var HtmlWebpackPlugin = require("html-webpack-plugin");
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    entry: __dirname + "/src/main/webapp/js/app.module.js",

    output: {
        path: __dirname + "/target/www",
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
                //CSS LOADER
                test: /\.css$/,
                use: ExtractTextPlugin.extract({
                    fallback: "style-loader",
                    use: ["css-loader", "postcss-loader"]
                })
            },
            {
                //LESS LOADER
                test: /\.less/,
                use: ExtractTextPlugin.extract({
                    fallback: "style-loader",
                    use: ['css-loader', "postcss-loader", 'less-loader']
                })
            },
            {
                // HTML LOADER
                test: /\.html$/,
                loader: 'html-loader'
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
        new HtmlWebpackPlugin({
            template: "./src/main/webapp/resources/index.tmpl.html",
            favicon: './src/main/webapp/resources/favicon.ico'
        }),
        new webpack.HotModuleReplacementPlugin(),
        new ExtractTextPlugin("[name].css"),
        new webpack.optimize.CommonsChunkPlugin({
            name: "vendor",
            minChunks: module =>{
                // this assumes your vendor imports exist in the node_modules directory
                return module.context && module.context.indexOf('node_modules') !== -1;
            }
        }),
        new webpack.LoaderOptionsPlugin({
            debug: true
        })
    ],

    devServer: {
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
    }
};
