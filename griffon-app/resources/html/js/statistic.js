var videoFile = "07074207.wav";
var statFolder = "/home/david/vifeco/statistic/";
var jsonContents = []
var categories = {};

var g_file;
var g_datas;


function setVideoFile(folder, value) {
    statFolder = folder;
    videoFile = value;
    document.getElementById("test").innerHTML = statFolder + ": " + value;
}

function addJsonContent(json){
    jsonContents.push(json)

    var keys = "";

    for (var property in json){
        keys += property + ": " ;
    }

    var summary = parseFile(json);
    displaySummary(summary);

    // document.getElementById("test").innerHTML = keys
}

function parseFile(video){
    g_file = video;

    video["videos"]["video_1"]["collection"]["categorySet"].forEach(element => {
        categories["Cat_" + element.id] = element
    });

    var summary = {};

    for(var property in categories){
        summary[property] = {};
        var video_1 = {}
        video_1['total'] = getTotal(video["videos"], "video_1", property)
        video_1['single'] = video["tarjan_diff"][property]["Video{" + video["videos"]["video_1"]["uuid"] + "}"]
        video_1['match'] = getMatch(video_1)
        video_1['percent'] = getPercent(video_1)

        var video_2 = {}
        video_2['total'] = getTotal(video["videos"], "video_2", property)
        video_2['single'] = video["tarjan_diff"][property]["Video{" + video["videos"]["video_2"]["uuid"] + "}"]
        video_2['match'] = getMatch(video_2)
        video_2['percent'] = getPercent(video_2)

        summary[property]["video_1"] = video_1
        summary[property]["video_2"] = video_2
    }

    return summary;
}

function getTotal(videos, videoStr, property){
    if(videos[videoStr]["total_category"][property] === undefined){
        return 0
    }

    return videos[videoStr]["total_category"][property];
}

function getMatch(obj){
    return obj['total'] - obj['single'];
}

function getPercent(obj){
    if(obj['total'] === 0){
        return "-";
    } 

    return (obj['total'] - obj['single']) / obj['total'] * 100;
}

function displaySummary(summary){
    var table = `<table class="table table-striped table-bordered">
            <thead class="thead-dark">
                <tr>
                    <th scope="col">Category</th>
                    <th scope="col" colspan="4">Video 1</th>
                    <th scope="col" colspan="3">Video 2</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <th scope="row"></th>
                    <td>Total</td>
                    <td>Matched</td>
                    <td>Singled</td>
                    <td>%</td>
                    <td>Total</td>
                    <td>Singled</td>
                    <td>%</td>

                </tr>`

        for(var property in summary){
            table += `<tr>
                    <th scope="row"><a href='displayChartByCategory(${property})' onclick='return false;'>${categories[property]['name']}</a></th>
                    <td>${summary[property]['video_1']['total']}</td>
                    <td>${summary[property]['video_1']['match']}</td>
                    <td>${summary[property]['video_1']['single']}</td>
                    <td>${summary[property]['video_1']['percent']}</}td>
                    <td>${summary[property]['video_2']['total']}</td>
                    <td>${summary[property]['video_2']['single']}</td>
                    <td>${summary[property]['video_2']['percent']}</td>
                </tr>`

            table += "</tr>";
        }

        table += "</table>"

        $("#summary").html(table)

}

function displayChartByCategory(category){
    console.log("display chart")


}

function displayChart(data){
    var margin = { top: 20, right: 160, bottom: 35, left: 30 };

    var width = 1500 - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom;

    d3.select("#chart").html("");

    var svg = d3.select("#chart")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


    // Transpose the data into layers
    var dataset = d3.layout.stack()(["match", "video_1", "video_2"].map(function (fruit) {
        return data.map(function (d) {
            return { x: (d.time), y: +d[fruit] };
        });
    }));


    // Set x, y and colors
    var x = d3.scale.ordinal()
        .domain(dataset[0].map(function (d) { return d.x; }))
        .rangeRoundBands([10, width - 10], 0.2);

    var y = d3.scale.linear()
        .domain([0, d3.max(dataset, function (d) { return d3.max(d, function (d) { return d.y0 + d.y; }); })])
        .range([height, 0]);

    var colors = ["#d25c4d", "#f2b447", "#d9d574"];


    // Define and draw axes
    var yAxis = d3.svg.axis()
        .scale(y)
        .orient("left")
        .ticks(5)
        .tickSize(-width, 0, 0)
        .tickFormat(function (d) { return d });

    var xAxis = d3.svg.axis()
        .scale(x)
        .orient("bottom");

    svg.append("g")
        .attr("class", "y axis")
        .call(yAxis);

    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);


    // Create groups for each series, rects for each segment 
    var groups = svg.selectAll("g.cost")
        .data(dataset)
        .enter().append("g")
        .attr("class", "cost")
        .style("fill", function (d, i) { return colors[i]; });

    var rect = groups.selectAll("rect")
        .data(function (d) { return d; })
        .enter()
        .append("rect")
        .attr("x", function (d) { return x(d.x); })
        .attr("y", function (d) { return y(d.y0 + d.y); })
        .attr("height", function (d) { return y(d.y0) - y(d.y0 + d.y); })
        .attr("width", x.rangeBand());


    // Draw legend
    var legend = svg.selectAll(".legend")
        .data(colors)
        .enter().append("g")
        .attr("class", "legend")
        .attr("transform", function (d, i) { return "translate(30," + i * 19 + ")"; });

    legend.append("rect")
        .attr("x", width - 18)
        .attr("width", 18)
        .attr("height", 18)
        .style("fill", function (d, i) { return colors.slice().reverse()[i]; });

    legend.append("text")
        .attr("x", width + 5)
        .attr("y", 9)
        .attr("dy", ".35em")
        .style("text-anchor", "start")
        .text(function (d, i) {
            switch (i) {
                case 0: return "Video 2 Singled";
                case 1: return "Video 1 Singled";
                case 2: return "Matched";
            }
        });
}


$(document).ready(function () {

    var summary = parseFile(file1);
    var datas = parseData(file1);

    displaySummary(summary);
    displayChart(datas['Cat_1']);

    // Setup svg using Bostock's margin convention

    // var margin = { top: 20, right: 160, bottom: 35, left: 30 };

    // var width = 1500 - margin.left - margin.right,
    //     height = 500 - margin.top - margin.bottom;

    // var svg = d3.select("#chart")
    //     .append("svg")
    //     .attr("width", width + margin.left + margin.right)
    //     .attr("height", height + margin.top + margin.bottom)
    //     .append("g")
    //     .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


    // /* Data in strings like it would be if imported from a csv */
    // var data = [
    //     { time: "0", match: "10", mcintosh: "15", oranges: "9", pears: "6" },
    //     { time: "1", match: "12", mcintosh: "18", oranges: "9", pears: "4" },
    //     { time: "2", redDelicious: "05", mcintosh: "20", oranges: "8", pears: "2" },
    //     { time: "3", redDelicious: "01", mcintosh: "15", oranges: "5", pears: "4" },
    //     { time: "4", redDelicious: "02", mcintosh: "10", oranges: "4", pears: "2" }
    // ];

    // data = datas['Cat_1'];


    // var parse = d3.time.format("%Y").parse;


    // // Transpose the data into layers
    // var dataset = d3.layout.stack()(["match", "video_1", "video_2"].map(function (fruit) {
    //     return data.map(function (d) {
    //         return { x: (d.time), y: +d[fruit] };
    //     });
    // }));


    // // Set x, y and colors
    // var x = d3.scale.ordinal()
    //     .domain(dataset[0].map(function (d) { return d.x; }))
    //     .rangeRoundBands([10, width - 10], 0.2);

    // var y = d3.scale.linear()
    //     .domain([0, d3.max(dataset, function (d) { return d3.max(d, function (d) { return d.y0 + d.y; }); })])
    //     .range([height, 0]);

    // var colors = ["#d25c4d", "#f2b447", "#d9d574"];


    // // Define and draw axes
    // var yAxis = d3.svg.axis()
    //     .scale(y)
    //     .orient("left")
    //     .ticks(5)
    //     .tickSize(-width, 0, 0)
    //     .tickFormat(function (d) { return d });

    // var xAxis = d3.svg.axis()
    //     .scale(x)
    //     .orient("bottom");

    // svg.append("g")
    //     .attr("class", "y axis")
    //     .call(yAxis);

    // svg.append("g")
    //     .attr("class", "x axis")
    //     .attr("transform", "translate(0," + height + ")")
    //     .call(xAxis);


    // // Create groups for each series, rects for each segment 
    // var groups = svg.selectAll("g.cost")
    //     .data(dataset)
    //     .enter().append("g")
    //     .attr("class", "cost")
    //     .style("fill", function (d, i) { return colors[i]; });

    // var rect = groups.selectAll("rect")
    //     .data(function (d) { return d; })
    //     .enter()
    //     .append("rect")
    //     .attr("x", function (d) { return x(d.x); })
    //     .attr("y", function (d) { return y(d.y0 + d.y); })
    //     .attr("height", function (d) { return y(d.y0) - y(d.y0 + d.y); })
    //     .attr("width", x.rangeBand());


    // // Draw legend
    // var legend = svg.selectAll(".legend")
    //     .data(colors)
    //     .enter().append("g")
    //     .attr("class", "legend")
    //     .attr("transform", function (d, i) { return "translate(30," + i * 19 + ")"; });

    // legend.append("rect")
    //     .attr("x", width - 18)
    //     .attr("width", 18)
    //     .attr("height", 18)
    //     .style("fill", function (d, i) { return colors.slice().reverse()[i]; });

    // legend.append("text")
    //     .attr("x", width + 5)
    //     .attr("y", 9)
    //     .attr("dy", ".35em")
    //     .style("text-anchor", "start")
    //     .text(function (d, i) {
    //         switch (i) {
    //             case 0: return "Video 2 Singled";
    //             case 1: return "Video 1 Singled";
    //             case 2: return "Matched";
    //         }
    //     });
});