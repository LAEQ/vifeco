var videoFile = "07074207.wav";
var statFolder = "/home/david/vifeco/statistic/";
var jsonContents = []
var categories = {};

var g_statistic;
var g_datas;
var g_uuid_1;
var g_uuid_2;
var g_summary;


function setVideoFile(folder, value) {
    statFolder = folder;
    videoFile = value;
    document.getElementById("test").innerHTML = statFolder + ": " + value;
}

function addJsonContent(json){
    jsonContents.push(json)

    g_statistic = json;

    setGlobals();

    infos();

    g_summary = parseFile();
    displaySummary(g_summary);
    g_datas = parseData();
    activateCategoryBtn();
}

function getUser(index){
    var vid = (index === 1)? "video_1" : "video_2"
    var user = g_statistic['videos'][vid]['user']

    return user['firstName'] + " " + user['lastName']

}

function infos(){
   var table = `<table class="table">
           <thead class="">
               <tr>
                   <th scope="col">Name</th>
                   <th scope="col">Duration</th>
                   <th scope="col">User 1</th>
                   <th scope="col">User 2</th>
               </tr>
           </thead>
           <tbody>
               <tr>
                   <td>${getName()}</td>
                   <td>${getDuration()}</td>
                   <td>${getUser(1)}</td>
                   <td>${getUser(2)}</td>
               </tr>
            </tbody>
            </table>`

    $("#info").html(table)
}

function getName(){
    return g_statistic['videos']['video_1']['name'];
}

function getDuration(){
    var totalSeconds = g_statistic['videos']['video_1']['duration'] / 1000;
    var hours = Math.floor(totalSeconds / 3600);
    var minutes = Math.floor((totalSeconds - (3600 * hours)) / 60);
    var seconds = Math.floor((totalSeconds - (3600 * hours)) % 60);

    return `${hours}:${minutes}:${seconds}`;
}


function setGlobals(){
    g_uuid_1 = g_statistic["videos"]["video_1"]["uuid"];
    g_uuid_2 = g_statistic["videos"]["video_2"]["uuid"];
    g_datas = undefined;
    categories = [];
    d3.select("#chart").html("");
    d3.select("#info").html("");
}

function parseFile(){
    categories = []

    g_statistic["videos"]["video_1"]["collection"]["categorySet"].forEach(element => {
        categories["Cat_" + element.id] = element
    });

    var summary = {};

    for(var property in categories){
        var video_1 = {}
        video_1['total'] = getTotal("video_1", property)
        video_1['single'] = g_statistic["tarjan_diff"][property]["Video{" + g_uuid_1 + "}"]
        video_1['match'] = getMatch(video_1)
        video_1['percent'] = getPercent(video_1)

        var video_2 = {}
        video_2['total'] = getTotal("video_2", property)
        video_2['single'] = g_statistic["tarjan_diff"][property]["Video{" + g_uuid_2 + "}"]
        video_2['match'] = getMatch(video_2)
        video_2['percent'] = getPercent(video_2)

        summary[property] = {};
        summary[property]["video_1"] = video_1
        summary[property]["video_2"] = video_2

        var total = video_1['total'] + video_2['total']
        var totalSummary = 100;

        if(total !== 0){
            totalSummary = (total - (video_1['single'] + video_2['single'])) / total * 100
        }

        summary[property]['summary'] = {}
        summary[property]['summary']['percent'] = totalSummary

    }

    return summary;
}



function getTotal(videoStr, property){
    if(g_statistic['videos'][videoStr]["total_category"][property] === undefined){
        return 0
    }

    return g_statistic['videos'][videoStr]["total_category"][property];
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
                <th scope="col" >Summary</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <th scope="row"></th>
                <td>Total</td>
                <td>Matched</td>
                <td>Singled</td>
                <td>%</td>
                <td>Total</td>summary
                <td>Singled</td>
                <td>%</td>
                <td>% total</td>
            </tr>`

    for(var property in summary){
        table += `<tr>
                <th scope="row"><a class='displayChart' href='' data-category='${property}' onclick='return false;'>${categories[property]['name']}</a></th>
                <td>${summary[property]['video_1']['total']}</td>
                <td>${summary[property]['video_1']['match']}</td>
                <td>${summary[property]['video_1']['single']}</td>
                <td>${summary[property]['video_1']['percent']}</}td>
                <td>${summary[property]['video_2']['total']}</td>
                <td>${summary[property]['video_2']['single']}</td>
                <td>${summary[property]['video_2']['percent']}</td>
                <td>${summary[property]['summary']['percent']}</td>
            </tr>`

        table += "</tr>";
    }
    table += "</table>"
    $("#summary").html(table)
}


function parseData(){
    var data = {};

    for(category in categories){
        data[category] = [];

        var duration = g_statistic['videos']['video_1']['duration'];
        var total = Math.ceil(duration / (1000 * 60)) 

        for(var i = 0; i < total; i++) {
            var obj = { 'time': i.toString(), 'match': 0, 'video_1': 0, 'video_2': 0}
            data[category].push(obj);
        }

        var edges = g_statistic['tarjan_edge'][category];

        if(Array.isArray(edges)){
            for(var i = 0; i < edges.length; i++ ){
                var startPoint = Math.round(edges[i]['start']['point']['startDouble'] / 60000);
                data[category][startPoint]['match'] += 1;
            }

            var singles = g_statistic['lonely_points'][category];

            for(var i = 0; i < singles.length; i++){
                var point = singles[i]['point'];
                var startPoint = Math.round(point['startDouble'] / 60000);

                if(isVideo1(point['videoId'])){
                    data[category][startPoint]['video_1'] += 1
                } else {
                    data[category][startPoint]['video_2'] += 1
                }
            }
        }

    }

    return data;
}


function isVideo1(uuid){
    return uuid === g_uuid_1;
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

    var dataset = d3.layout.stack()(["match", "video_1", "video_2"].map(function (fruit) {
        return data.map(function (d) {
            return { x: (d.time), y: +d[fruit] };
        });
    }));


    var x = d3.scale.ordinal()
        .domain(dataset[0].map(function (d) { return d.x; }))
        .rangeRoundBands([10, width - 10], 0.2);

    var y = d3.scale.linear()
        .domain([0, d3.max(dataset, function (d) { return d3.max(d, function (d) { return d.y0 + d.y; }); })])
        .range([height, 0]);

    var colors = ["#d25c4d", "#f2b447", "#d9d574"];

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

function activateCategoryBtn(){
    $('.displayChart').click(function(){
        var category = event.srcElement.getAttribute('data-category');
        displayChart(g_datas[category]);
    })
}



function donut(){  
    // Default settings
    var $el = d3.select("body")
    var data = {};
    // var showTitle = true;
    var width = 960,
        height = 400,
        radius = Math.min(width, height) / 2;
  
    var currentVal;
    var color = d3.scale.category20();
    var pie = d3.layout.pie()
      .sort(null)
      .value(function(d) { return d.value; });
  
    var svg, g, arc; 
  
  
    var object = {};
  
    // Method for render/refresh graph
    object.render = function(){

    var colors = ["#766A47", "#97885B", "#D1C5A3", '#0E4762', '#3C748F', '#6793A8']

      if(!svg){
        arc = d3.svg.arc()
        .outerRadius(radius)
        .innerRadius(radius - (radius/2.5));
  
        svg = $el.append("svg")
          .attr("width", width)
          .attr("height", height)
        .append("g")
          .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
  
        g = svg.selectAll(".arc")
          .data(pie(d3.entries(data)))
          .enter().append("g")
          .attr("class", "arc"); 
  
        g.append("path")
          // Attach current value to g so that we can use it for animation
          .each(function(d) { 
              this._current = d;
         })
          .attr("d", arc)
          .style("fill", function(d) { return colors[d.data.key]; });
        g.append("text")
            .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
            .attr("dy", ".35em")
            .style("text-anchor", "middle");
        g.select("text").text(function(d) { return d.data.key; });
  
        svg.append("text")
            .datum(data)
            .attr("x", 0 )
            .attr("y", 0 + radius/10 )
            .attr("class", "text-tooltip")        
            .style("text-anchor", "middle")
            .attr("font-weight", "bold")
            .style("font-size", radius/2.5+"px");
  
        // g.on("mouseover", function(obj){
        //   console.log(obj)
        //   svg.select("text.text-tooltip")
        //   .attr("fill", function(d) { return color(obj.data.key); })
        //   .text(function(d){
        //     return d[obj.data.key];
        //   });
        // });
  
        // g.on("mouseout", function(obj){
        //   svg.select("text.text-tooltip").text("");
        // });
  
      }else{
        g.data(pie(d3.entries(data))).exit().remove();
  
        g.select("path")
        .transition().duration(200)
        .attrTween("d", function(a){
          var i = d3.interpolate(this._current, a);
          this._current = i(0);
          return function(t) {
              return arc(i(t));
          };
        })
  
        g.select("text")
        .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; });
  
        svg.select("text.text-tooltip").datum(data);
      }      
      return object;
    };
  
    // Getter and setter methods
    object.data = function(value){
      if (!arguments.length) return data;
      data = value;
      return object;
    };
  
    object.$el = function(value){
      if (!arguments.length) return $el;
      $el = value;
      return object;
    };
  
    object.width = function(value){
      if (!arguments.length) return width;
      width = value;
      radius = Math.min(width, height) / 2;
      return object;
    };
  
    object.height = function(value){
      if (!arguments.length) return height;
      height = value;
      radius = Math.min(width, height) / 2;
      return object;
    };
  
    return object;
  };

$(document).ready(function () {
    g_statistic = file1;

    setGlobals();

    infos();

    g_summary = parseFile();
    displaySummary(g_summary);
    g_datas = parseData();
    activateCategoryBtn();

    var getData = function(){
        var size = 3;
        var data = {};
        var text = "";
        for(var i=0; i<size; i++){
          data[i] = Math.round(Math.random() * 100);
          text += "data-"+ (i+1) +" = " + data["data-"+(i+1)] + "<br/>";
        };
        d3.select("#data").html(text);
        return data;
      };
      
      var chart = donut()
                    .$el(d3.select("#chart"))
                    .data(getData())
                    .render();
      


});