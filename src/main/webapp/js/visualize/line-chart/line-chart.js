import uiModules from "../../modules";
import * as d3 from "d3";
import nv from "nvd3";

class LineChartController {
    constructor($element) {
        "ngInject";
        this.$element = $element;
    }

    $onInit() {
        this.chart = nv.models.lineChart()
            .options({
                duration: 300,
                useInteractiveGuideline: true
            })
            .x(function (d) {
                return +d.key;
            })
            .y(function (d) {
                return d.values;
            });

        this.chart.xAxis     //Chart x-axis settings
            .axisLabel('Date')
            .tickFormat(function (d) {
                return d3.time.format('%m/%d/%y')(new Date(d));
            });

        this.chart.yAxis     //Chart y-axis settings
            .axisLabel('Amount (EUR)')
            .tickFormat(d3.format('.02f'));

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
        d3.select(this.$element[0])
            .append('svg')//Select the <svg> element you want to render the chart in.
            .style("width", "100%")
            .style("height", "300px")
            .datum(series)         //Populate the <svg> element with chart data...
            .call(this.chart);          //Finally, render the chart!
    }
}

uiModules
    .get('visualize', [])
    .component('lineChart', {
        bindings: {
            series: '<',
            config: '<'
        },
        controller: LineChartController
    });
