import uiModules from "../../modules";
import * as d3 from "d3";
import nv from "nvd3";

class BarChartController {
    constructor($element) {
        "ngInject";
        this.$element = $element;
    }

    $onInit() {
        this.chart = nv.models.multiBarChart()
            .duration(300)
            .showLegend(true)
            .stacked(true);

        // chart sub-models (ie. xAxis, yAxis, etc) when accessed directly, return themselves, not the parent chart, so need to chain separately
        this.chart.xAxis
            .axisLabel('Date')
            .tickFormat(function (d) {
                return d3.time.format('%m/%y')(new Date(d));
            })
            .showMaxMin(false);

        this.chart.yAxis
            .axisLabel('Amount (EUR)')
            .tickFormat(d3.format('.02f'));

        this.chart.showXAxis(true);

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
            .datum(series)
            .call(this.chart);
    }
}

uiModules
    .get('visualize', [])
    .component('barChart', {
        bindings: {
            series: '<',
            config: '<'
        },
        controller: BarChartController
    });
