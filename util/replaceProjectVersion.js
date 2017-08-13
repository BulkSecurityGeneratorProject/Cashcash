#!/usr/bin/env node
const replace = require('replace-in-file');
const parseString = require('xml2js').parseString;
const fs = require('fs');

let version;
const pomXml = fs.readFileSync('pom.xml', "utf8");

parseString(pomXml, function (err, result) {
    // Look for the version of the project and if not found the version of the parent
    if (result.project.version && result.project.version[0]) {
        version = result.project.version[0];
    } else if (result.project.parent && result.project.parent[0] && result.project.parent[0].version && result.project.parent[0].version[0]) {
        version = result.project.parent[0].version[0]
    } else {
        throw new Error('pom.xml is malformed. No version is defined');
    }
});

const options = {
    files: 'src/main/webapp/js/app.constants.js',
    from: /'VERSION', '[0-9a-zA-Z-.]+'/,
    to: `'VERSION', '${version}'`,
};

replace(options)
    .then(changedFiles => {
        console.log('Modified files:', changedFiles.join(', '));
    }).catch(error => {
    console.error('Error occurred:', error);
});