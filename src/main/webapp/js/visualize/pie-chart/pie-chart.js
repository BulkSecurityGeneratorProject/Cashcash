import uiModules from "../../modules";
import * as d3 from "d3";
import nv from "nvd3";

class PieChartController {
    constructor($element) {
        "ngInject";
        this.$element = $element;
    }

    $onInit() {
        this.chart = nv.models.pieChart()
            .x(function (d) {
                return d.label
            })
            .y(function (d) {
                return d.value
            })
            .showLabels(true);

        //Update the chart when window resizes.
        nv.utils.windowResize(this.chart.update);

        if (this.series) {
            this.update(this.series);
        }
    }

    $onChanges(changeObject) {
        // if (changeObject.series.currentValue) {
        //     this.update(changeObject.series.currentValue);
        // }
    }

    update(series) {
        var myData = series[0].values;

        d3.select(this.$element[0])
            .append('svg')//Select the <svg> element you want to render the chart in.
            .style("width", "100%")
            .style("height", "300px")
            .datum(myData)
            .transition()
            .duration(350)
            .call(this.chart);
    }

    exampleData() {
        return [
            {
                "label": "One",
                "value": 29.765957771107
            },
            {
                "label": "Two",
                "value": 0
            },
            {
                "label": "Three",
                "value": 32.807804682612
            },
            {
                "label": "Four",
                "value": 196.45946739256
            },
            {
                "label": "Five",
                "value": 0.19434030906893
            },
            {
                "label": "Six",
                "value": 98.079782601442
            },
            {
                "label": "Seven",
                "value": 13.925743130903
            },
            {
                "label": "Eight",
                "value": 5.1387322875705
            }
        ];
    }
}

uiModules
    .get('visualize', [])
    .component('pieChart', {
        bindings: {
            series: '<',
            config: '<'
        },
        controller: PieChartController
    });
