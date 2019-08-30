var jsonContents = []
var categories = {};
var g_statistic;
var g_datas = {}
var g_uuid_1;
var g_uuid_2;
var g_summary;
var g_div_id;
var texts;

var dictionnary = {
    'en': {
        "video": "Video",
        "name": "Name",
        "duration": "Duration",
        "step": "Temporal tolerance (seconds)",
        "user": "User",
        "category": "Category",
        "summary": "Summary",
        "total": "Total",
        "matched": "Matched",
        "unmatched": "Unmatched",
        "overall": "Overall",
        "index": "Concordance index"
    },
    'fr': {
        "video": "Vidéo",
         "name": "Nom",
         "duration": "Durée",
         "step": "Tolérance temporelle (secondes)",
         "user": "Utilisateur",
         "category": "Catégorie",
         "summary": "Résumé",
         "total": "Total",
         "matched": "Concordant",
         "unmatched": "Discordant",
         "overall": "Global",
         "index": "Indice de concordance"
     },
    'es': {
        "video": "Vídeo",
        "name": "Nombre",
        "duration": "Tiempo",
        "step": "Paso (segundos)",
        "user": "Usuario",
        "category": "Categoría",
        "summary": "Resumen",
        "total": "Total",
        "matched": "Emparejado",
        "unmatched": "Sin pareja",
        "overall": "Global",
        "index": "Emparejado indice"
    },
}


function setLanguage(ln){
    texts = dictionnary[ln]
}

function reset(){
    jsonContents = []
    g_datas = {}
    g_statistic = []
    g_uuid_1 = null
    g_uuid_2 = null
    g_summary = null
}

function createVideoContainer(index) {
    return `<div id='video-${index}'><div class='info'></div><div class='summary'></div><div class='chart'></div><div>`
}

function addJsonContent(json) {
    jsonContents.push(json)
}

function setInfos() {
    var table = `<table class="table ">
           <thead class="thead-light">
               <tr>
                   <th scope="col">${texts['name']}</th>
                   <th scope="col">${texts['duration']}</th>
                   <th scope="col">${texts['step']}</th>
                   <th scope="col">${texts['user']} 1</th>
                   <th scope="col">${texts['user']} 2</th>
               </tr>
           </thead>
           <tbody>
               <tr>
                   <td>${getName()}</td>
                   <td>${getDuration()}</td>
                   <td>${getStep()}</td>
                   <td>${getUser(1)}</td>
                   <td>${getUser(2)}</td>
               </tr>
            </tbody>
            </table>`

    $(`${g_div_id} .info `).html(table)
}

function getUser(index) {
    var vid = (index === 1) ? "video_1" : "video_2"
    var user = g_statistic['videos'][vid]['user']

    return user['firstName'] + " " + user['lastName']

}

function getStep(){
    return g_statistic['step']
}

function getName() {
    return g_statistic['videos']['video_1']['name'];
}

function getDuration() {
    var totalSeconds = g_statistic['videos']['video_1']['duration'] / 1000;
    var hours = Math.floor(totalSeconds / 3600);
    var minutes = Math.floor((totalSeconds - (3600 * hours)) / 60);
    var seconds = Math.floor((totalSeconds - (3600 * hours)) % 60);

    return `${hours}:${minutes}:${seconds}`;
}


function setGlobals() {
    g_uuid_1 = g_statistic["videos"]["video_1"]["uuid"];
    g_uuid_2 = g_statistic["videos"]["video_2"]["uuid"];
    categories = [];
    g_tooltip = undefined;
}

function parseFile() {
    categories = []

    g_statistic["videos"]["video_1"]["collection"]["categorySet"].forEach(element => {
        categories["Cat_" + element.id] = element
    });

    var summary = {};

    for (var property in categories) {
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

        if (total !== 0) {
            totalSummary = ((total - (video_1['single'] + video_2['single'])) / total * 100).toFixed(2)
        }

        summary[property]['summary'] = {}
        summary[property]['summary']['percent'] = totalSummary

    }
    

    return summary;
}


function getTotal(videoStr, property) {
    if (g_statistic['videos'][videoStr]["total_category"][property] === undefined) {
        return 0
    }

    return g_statistic['videos'][videoStr]["total_category"][property];
}

function getMatch(obj) {
    return obj['total'] - obj['single'];
}

function getPercent(obj) {
    if (obj['total'] === 0) {
        return "-";
    }

    return ((obj['total'] - obj['single']) / obj['total'] * 100).toFixed(2);
}

function displaySummary(summary, index) {
    var table = `<table class="table table-striped table-bordered table-sm border-left">
        <thead class="thead-custom">
            <tr>
                <th></th>
                <th colspan="3">${getUser(1)}</th>
                <th colspan="3">${getUser(2)}</th>  
                <th colspan="3">${texts['overall']}</th>                
            </tr>
        </thead>
        <tbody>
            <tr class="first">
                <td scope="col" class="border-right">${texts['category']}</td>
                <td scope="col" >${texts['total']}</td>
                <td scope="col" >${texts['matched']}</td>
                <td scope="col" class="border-right" >${texts['unmatched']}</td>
                <td scope="col" ">${texts['total']}</td>
                <td scope="col" >${texts['matched']}</td>
                <td scope="col" class="border-right" >${texts['unmatched']}</td>
                <td scope="col" ">${texts['matched']}</td>
                <td scope="col" >${texts['unmatched']}</td>
                <td scope="col" >${texts['index']}</td>
            </tr>`

    let totals = {
        'video_1': {
            'total': 0,
            'match': 0,
            'single': 0
        },
        'video_2': {
            'total': 0,
            'match': 0,
            'single': 0
        },
        'overall': {
            'match': 0,
            'single': 0,
            'index': 0
        }
    }

    for (var property in summary) {
        let resultGlobal = calculateGlobal(summary[property]);

        totals['video_1']['total'] += summary[property]['video_1']['total']
        totals['video_1']['match'] += summary[property]['video_1']['match']
        totals['video_1']['single'] += summary[property]['video_1']['single']
        totals['video_2']['total'] += summary[property]['video_2']['total']
        totals['video_2']['match'] += summary[property]['video_2']['match']
        totals['video_2']['single'] += summary[property]['video_2']['single']

        table += `<tr >
                <td scope="row" class="border-right"><a class='displayChart' href='' data-category='${property}' data-video='video-${index}'>${categories[property]['name']}</a></td>
                <td class="right">${summary[property]['video_1']['total']}</td>
                <td class="right">${summary[property]['video_1']['match']}</td>
                <td class="right border-right">${summary[property]['video_1']['single']}</}td>
                <td class="right">${summary[property]['video_2']['total']}</td>
                <td class="right">${summary[property]['video_2']['match']}</td>
                <td class="right border-right">${summary[property]['video_2']['single']}</td>
                <td class="right">${resultGlobal['match']}</td>
                <td class="right">${resultGlobal['single']}</td>
                <td class="right">${resultGlobal['index']}%</td>
            </tr>`

        table += "</tr>";
    }

    let total_videos =  totals['video_1']['total']  +  totals['video_2']['total'] 

    totals['overall']['match'] = totals['video_1']['match'] + totals['video_2']['match']
    totals['overall']['single'] = totals['video_1']['single'] + totals['video_2']['single']
    totals['overall']['index'] = ((total_videos - totals['overall']['single']) / total_videos * 100).toFixed(2)

    table += `<tr class="last"><td class="border-right"><a class='displayChart' href='' data-category='total' data-video='video-${index}'>${texts['total']}</a></td>`
    table += `<td class="right">${totals['video_1']['total']}</td>`
    table += `<td class="right">${totals['video_1']['match']}</td>`
    table += `<td class="right border-right">${totals['video_1']['single']}</td>`
    table += `<td class="right">${totals['video_2']['total']}</td>`
    table += `<td class="right">${totals['video_2']['match']}</td>`
    table += `<td class="right border-right">${totals['video_2']['single']}</td>`
    table += `<td class="right">${totals['overall']['match']}</td>`
    table += `<td class="right">${totals['overall']['single']}</td>`
    table += `<td class="right">${totals['overall']['index']}%</td>`

    table += "</tr></table><hr />"

    $(`${g_div_id} .summary`).html(table)
}

function calculateGlobal(datas){
    let result = {
        'total': 0,
        'index': 100.00,
        'match': 0,
        'single': 0
    }

    result['total'] = datas['video_1']['total'] + datas['video_2']['total']
    result['match'] = datas['video_1']['match'] + datas['video_2']['match']
    result['single'] = datas['video_1']['single'] + datas['video_2']['single']

    if(result['total'] !== 0){
        result['index'] = ((result['total'] - result['single'])/ result['total'] * 100)
    }

    result['index']  = result['index'].toFixed(2)

    return result
}


function parseData() {
    var data = {};

    for (category in categories) {
        data[category] = [];

        var duration = g_statistic['videos']['video_1']['duration'];
        var total = Math.ceil(duration / (1000 * 60))

        for (var i = 0; i <= total; i++) {
            var obj = { 'time': i.toString(), 'match': 0, 'video_1': 0, 'video_2': 0 }
            data[category].push(obj);
        }

        var edges = g_statistic['tarjan_edge'][category];

        if (Array.isArray(edges)) {
            for (var i = 0; i < edges.length; i++) {
                var startPoint = Math.round(edges[i]['start']['point']['startDouble'] / 60000);

                data[category][startPoint]['match'] += 1;
            }

            var singles = g_statistic['lonely_points'][category];

            for (var i = 0; i < singles.length; i++) {
                var point = singles[i]['point'];
                var startPoint = Math.round(point['startDouble'] / 60000);

                if (isVideo1(point['videoId'])) {
                    data[category][startPoint]['video_1'] += 1
                } else {
                    data[category][startPoint]['video_2'] += 1
                }
            }
        }

    }

    //Total
    let tmpCat = Object.keys(categories)
    total = data[tmpCat[0]].length
    console.log(total)
    data['total'] = []


    for(let j = 0; j < total; j++){
        data['total'][j] = {time: `${j}`, match: 0, video_1: 0, video_2: 0}
    }

    for(let j = 0; j < total; j++){
        for(let i = 0; i < tmpCat.length; i++){
            let c = tmpCat[i]
            data['total'][j]['match'] += data[c][j]['match']
            data['total'][j]['video_1'] += data[c][j]['video_1']
            data['total'][j]['video_2'] += data[c][j]['video_2']
        }

    }
    
    
    console.log(data)

    categories['total'] = {
        'name': 'Total'
    }

    return data;
}


function isVideo1(uuid) {
    return uuid === g_uuid_1;
}

function getMaxRounded(max){
    let result = 0
    switch(true) {
        case (max <= 5):
        return 5
        case (max <= 10):
        return 10
        default:
            result  = (Math.floor(max / 10) + 1) * 10
            return result
   }
}

function getPrimes(max){
    let result = []
    let index = 2
    while(max > 1){
        while(max % index !== 0){
            index++;
        }
        max /= index
        result.push(index)
        index = 2;
    }

    return result
}

function getNumberTicks(max){
    let result = 1
    let list = {
        'n': [],
        'p': [],
        'maxRound': 0,
        'nb_ticks' : 0
    }

    let roundMax = getMaxRounded(max)
    let primes = getPrimes(roundMax)

    if(roundMax <= 10){

        result = roundMax
    } else {
        while(result < 3 && primes.length > 0){
            result *= primes.shift()
        }
    }

    list['maxRound'] = roundMax
    list['nb_ticks'] = result

    let ratioN = roundMax / result
    let ratioP = 100 / result

    for(let i = 0; i <= result ; i++ ){
        list['n'].push(i * ratioN)
        list['p'].push((i * ratioP).toFixed(1))
    }

    return list
}



function displayChart(data, id, cat) {
    var margin = { top: 60, right: 250, bottom: 35, left: 30 };

    var width = 1330 - margin.left - margin.right,
        height = 430 - margin.top - margin.bottom;

    d3.select(id).html("");

    var svg = d3.select(id)
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var dataset = d3.layout.stack()(["match", "video_1", "video_2"].map(function (fruit) {
        return data.map(function (d) {
            return { x: (d.time), y: + d[fruit] };
        });
    }));

    var coefs = data.map(d => {
        let total_discordant = d['video_1'] + d['video_2']
        let total  = (d.match * 2)  + total_discordant

        if(total === 0){
            return 100
        }
        
        return (total - total_discordant) / total * 100
    })


    var x = d3.scale.ordinal()
        .domain(dataset[0].map(function (d) { return d.x; }))
        .rangeRoundBands([10, width - 10], 0.2);

    var maxY = d3.max(dataset, function (d) { return d3.max(d, function (d) { return d.y0 + d.y; }); });

    var ticksValues = getNumberTicks(maxY)

    var y = d3.scale.linear()
        .domain([0, ticksValues.maxRound])
        .range([height, 0]);

    var y_coef = d3.scale.linear().domain([0, 100]).range([height, 0])

    var colors = ["#3182bd", "#de2d26", "#feb24c"];

    var yAxis = d3.svg.axis()
        .scale(y)
        .orient("left")
        .tickSize(-width, 0, 0)
        .tickFormat(function (d) { return d })
        .tickValues(ticksValues.n);

    var yAxisCoef = d3.svg.axis()
        .scale(y_coef)
        .orient("right")
        .tickSize(width, 0, 0)
        .tickFormat(function (d) { return d })
        .tickValues(ticksValues.p);

    var xAxis = d3.svg.axis()
        .scale(x)
        .orient("bottom")
        .tickFormat(function(d) { return `${d}`})

    svg.append("g")
        .attr("class", "y axis")
        .call(yAxis);

    svg.append("g")
        .attr("class", "y axis")
        .call(yAxisCoef);

    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);

    svg.append("text")
        .attr("x", width)
        .attr("y", height + margin.top - 5)
        .text("minutes")

    var groups = svg.selectAll("g.cost")
        .data(dataset)
        .enter().append("g")
        .attr("class", "cost")
        .style("fill", function (d, i) { return colors[i]; })

    groups.selectAll("rect")
        .data(function (d) { return d; })
        .enter()
        .append("rect")
        .attr("x", function (d) { return x(d.x); })
        .attr("y", function (d) { return y(d.y0 + d.y); })
        .attr("height", function (d) { return y(d.y0) - y(d.y0 + d.y); })
        .attr("width", x.rangeBand())
        .on("mouseover", function() { tooltip.style("display", null); })
        .on("mouseout", function() { tooltip.style("display", "none"); })
        .on("mousemove", function(d, i) {
            var xPosition = d3.mouse(this)[0] - 15;
            var yPosition = d3.mouse(this)[1] - 25;
            tooltip.attr("transform", "translate(" + xPosition + "," + yPosition + ")");
            tooltip.select("text").text(d.y);
        });

    var linesCoefs= []

    for(let i = 1; i < coefs.length; i++){
        linesCoefs.push({
            "x1": x(i - 1) + 12.5,
            "x2": x(i) + 12.5,
            "y1": y_coef(coefs[i - 1]),
            "y2": y_coef(coefs[i])
        })
    }

    var coefsLines = svg.selectAll("g.coefsLine")
        .data(linesCoefs)
        .enter()
        .append("line")
        .attr("x1", d => d.x1)
        .attr("x2", d => d.x2)
        .attr("y1", d => d.y1)
        .attr("y2", d => d.y2)
        .attr("stroke", 'black')
        .attr("stroke-width", 1.1)

    var coefDots = svg.selectAll("g.coefs")
        .data(coefs)
        .enter()
        .append("circle")
        .attr("cx", (d, i) => x(i) + 12.5)
        .attr("cy", d =>  y_coef(d))
        .attr("r", 5)
        .style("fill", "#3182bd")
        .style("stroke", 'black')
        .on("mouseover", function() { tooltip.style("display", null); })
        .on("mouseout", function() { tooltip.style("display", "none"); })
        .on("mousemove", function(d, i) {
            var xPosition = d3.mouse(this)[0] - 15;
            var yPosition = d3.mouse(this)[1] - 25;
            tooltip.attr("transform", "translate(" + xPosition + "," + yPosition + ")");
            tooltip.select("text").text(d.toFixed(2));
        });

    

    svg.append("text")
        .attr("x", 30)
        .attr("y", -30)
        .attr("class", "h6")
        .text(categories[cat].name)

    svg.append("text")
        .attr("x", -10)
        .attr("y", -15)
        .attr('class', 'axis-text')
        .text("N")

    svg.append("text")
        .attr("x", width-30)
        .attr("y", -10)
        .attr('class', 'axis-text')
        .text(texts['index'] + " (%)")

    var legend = svg.selectAll(".legend")
        .data(colors)
        .enter().append("g")
        .attr("class", "legend")
        .attr("transform", function (d, i) { return "translate(80," + (i + 1) * 27 + ")"; });

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
                case 0: return `${getUser(2)} `;
                case 1: return `${getUser(1)} `;
                case 2: return `${texts['matched']}`;
            }
        });

    svg.append("line")
        .attr("x1", width + 60)
        .attr("x2", width + 90)
        .attr("y1", 120)
        .attr("y2", 120)
        .attr("stroke", 'black')
        .attr("stroke-width", 1.1)

    svg.append("circle")
        .attr("cx", width + 75)
        .attr("cy", 120)
        .attr("r", 5)
        .style("fill", "#3182bd")
        .style("stroke", 'black')

    svg.append("text")
        .attr("x", width + 100)
        .attr("y", 120)
        .attr("dy", ".35em")
        .style("text-anchor", "start")
        .text(texts['index'])


    var tooltip = svg.append("g")
        .attr("class", "tooltip-2")
        .style("display", "none");
          
      tooltip.append("rect")
        .attr("width", 30)
        .attr("height", 20)
        .attr("fill", "white")
        .style("opacity", 0.5);
      
      tooltip.append("text")
        .attr("x", 15)
        .attr("dy", "1.2em")
        .style("text-anchor", "middle")
        .attr("font-size", "12px")
        .attr("font-weight", "bold");
}

function activateCategoryBtn() {
    $('.displayChart').click(function () {
        event.preventDefault();

        var video_id = event.srcElement.getAttribute('data-video');
        var category = event.srcElement.getAttribute('data-category');
        var divId = `#${video_id} .chart`;
        var chart = displayChart(g_datas[video_id][category], divId, category);
        

        

        return false;
    })
}

function sortStatistic(a, b){
    if(a['step'] > b['step']){
        return 1;
    }

    return -1;
}

function render() {
    $('#container').html("");

    jsonContents.sort(sortStatistic)

    for (var i = 0; i < jsonContents.length; i++) {
        var div = createVideoContainer(i);
        $('#container').append(div)

        g_statistic = jsonContents[i]
        g_div_id = `#video-${i}`
        setGlobals()
        setInfos();
        g_summary = parseFile()
        g_datas[`video-${i}`] = parseData()
        displaySummary(g_summary, i)
    }

    activateCategoryBtn();

    // var video_id = 'video-0'
    // var category = 'Cat_1'
    // var divId = `#${video_id} .chart`;
    // var chart = displayChart(g_datas[video_id][category], divId, category);
}

$(document).ready(function () {
    setLanguage('en')
    addJsonContent(file1)
    // addJsonContent(file2)
    render();    
    console.log(g_datas)
});