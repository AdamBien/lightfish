if(typeof lightfish == "undefined"){
    lightfish = {};
}

lightfish.view = {
    config: {
        baseUri: "/lightfish"
    },
    _charts: {},
    _poolChartNames: {
        connections: 'Connections Free/Used',
        waitQueueLength: 'Wait Queue Length',
        potentialLeak: 'Potential Connection Leak #'
    },
    _previousData: null,
    _poll: function(){
        $.ajax({
            url: lightfish.view.config.baseUri + "/live", 
            success: function(rawData){
                var data = lightfish.view._parseData(rawData);
                
                lightfish.view._calculateAdditionalData(data, lightfish.view._previousData);
                lightfish.view._updateCharts(data);
                
                lightfish.view._previousData = data;

            }, 
            dataType: "xml", 
            complete: lightfish.view._poll, 
            timeout: 30000
        });
    },
    _parseData: function(rawData){
        var $data = $(rawData);
        var parsed = {};
        parsed.time = new Date($data.find("monitoringTime").text())
        parsed.heapSize = Number($data.find("usedHeapSize").text());
        parsed.threadCount = Number($data.find("threadCount").text());
        parsed.peakThreadCount = Number($data.find("peakThreadCount").text());
        parsed.txCommitCount = Number($data.find("committedTX").text());
        parsed.txRollbackCount = Number($data.find("rolledBackTX").text());
        parsed.queuedConnectionCount = Number($data.find("queuedConnections").text());
        parsed.errorCount = Number($data.find("totalErrors").text());
        parsed.currentBusyThreadCount = Number($data.find("currentThreadBusy").text());
        parsed.sessionCount = Number($data.find("activeSessions").text());
        parsed.expiredSessionCount = Number($data.find("expiredSessions").text());
        parsed.pools = {};
        $data.find("pools").each(function(){
            var poolData = $(this);
            var poolName = poolData.find("jndiName").text();
            parsed.pools[poolName]= {};
            parsed.pools[poolName].freeConnections = Number(poolData.find("numconnfree").text());
            parsed.pools[poolName].usedConnections = Number(poolData.find("numconnused").text());
            parsed.pools[poolName].waitQueueLength = Number(poolData.find("waitqueuelength").text());
            parsed.pools[poolName].potentialLeak = Number(poolData.find("numpotentialconnleak").text());
        });
        
        return parsed;
    },
    _calculateAdditionalData: function(newData, previousData){
        if(previousData==null){
            return;
        }
        var timeDifference = (newData.time - previousData.time)/1000;
        
        var newCommits = newData.txCommitCount - previousData.txCommitCount;
        newData.commitsPerSecond = newCommits/timeDifference;
        
        var newRollbacks = newData.txRollbackCount - previousData.txRollbackCount;
        newData.rollbacksPerSecond = newRollbacks/timeDifference;
        
        var newErrors = newData.errorCount - previousData.errorCount;
        newData.errorsPerSecond = newErrors/timeDifference;
    },
    _updateCharts: function(data){
        lightfish.view._charts.heap.appendData([[data.time,data.heapSize]]);
        lightfish.view._charts.threads.appendData([[data.time,data.threadCount],[data.time,data.peakThreadCount]]);
        
        lightfish.view._charts.txCommit.appendData([[data.time,data.txCommitCount]]);
        lightfish.view._charts.txRollback.appendData([[data.time,data.txRollbackCount]]);
        
        lightfish.view._charts.errors.appendData([[data.time,data.errorCount]]);
        lightfish.view._charts.errorsPerSecond.appendData([[data.time,data.errorsPerSecond]]);
        lightfish.view._charts.queuedConnections.appendData([[data.time,data.queuedConnectionCount]]);
        
        lightfish.view._charts.busyThreads.appendData([[data.time,data.currentBusyThreadCount]]);
        
        lightfish.view._charts.commitsPerSecond.appendData([[data.time,data.commitsPerSecond]]);
        lightfish.view._charts.rollbacksPerSecond.appendData([[data.time,data.rollbacksPerSecond]]);
        
        lightfish.view._charts.sessions.appendData([[data.time,data.sessionCount]]);
        lightfish.view._charts.expiredSessions.appendData([[data.time,data.expiredSessionCount]]);
        
        for(var poolKey in data.pools){
            if(typeof lightfish.view._charts.pool[poolKey] == "undefined"){
                lightfish.view._createPoolCharts(poolKey)
            }
            
            for(var chartKey in lightfish.view._poolChartNames){
                if(chartKey!= "connections"){
                    lightfish.view._charts.pool[poolKey][chartKey].appendData([[data.time,data.pools[poolKey][chartKey]]]);
                }else{
                    lightfish.view._charts.pool[poolKey][chartKey].appendData([
                        [data.time,data.pools[poolKey].usedConnections],
                        [data.time,data.pools[poolKey].freeConnections]
                        ]);
                }
            }
        }
    },
    _createPoolCharts: function(poolKey){
        lightfish.view._charts.pool[poolKey] = {};
        
        $('<li><a href="#' + poolKey + '"><span>Resource: ' + poolKey + '</span></a></li>')
        .appendTo("#tabIndex");
        
        var poolChartsElement = $('<div id="' + poolKey + '" />')
        .addClass('chart-subsection').appendTo('#tabs');
        poolChartsElement.append($('<h1>Resource: ' + poolKey + '</h1>'));
        
        for(var chartKey in lightfish.view._poolChartNames){
            var currentChartContainer = $('<div class="chart-container" />')
            .appendTo(poolChartsElement);
            currentChartContainer.append($('<h1>' + lightfish.view._poolChartNames[chartKey] + '</h1>'));
            var currentChartElement = $('<div id="' + poolKey + '_' + chartKey + 'Chart" class="chart" />')
            .appendTo(currentChartContainer);
            
            if(chartKey != "connections"){
                lightfish.view._charts.pool[poolKey][chartKey] = new Chart({
                    element: currentChartElement,
                    chartOptions: {
                        yaxis:{
                            min:0
                        }
                    }
                })
            }else{
                lightfish.view._charts.pool[poolKey][chartKey] = new Chart({
                    element: currentChartElement,
                    labels:['used', 'free'],
                    chartOptions: {
                        series:{
                            stack:true
                        },
                        legend: {
                            position: "se"
                        }
                    }
                })
            }
            
        }
        
        poolChartsElement.append($('<div style="clear:both" />'));
        $("#tabs").tabs("refresh");
    },
    redrawVisibleCharts: function(){
        var allCharts = [];
        for(var chartKey in lightfish.view._charts){
            if(chartKey=="pool"){
                continue;
            }
            allCharts.push(lightfish.view._charts[chartKey]);
        }
        
        for(var poolKey in lightfish.view._charts.pool){
            var pool = lightfish.view._charts.pool[poolKey];
            for(var chartKey in pool){
                allCharts.push(pool[chartKey]);
            }
        }
        
        for(var index in allCharts){
            var currChart = allCharts[index];
            if($(currChart.element).is(":visible")){
                currChart.updateChart();
            }
        }
        
    },
    setup: function(){
        lightfish.view._charts.heap = new Chart({
            element: '#heapChart',
            chartOptions: {
                yaxis: {
                    min: 0,
                    tickFormatter: function(val,axis){
                        return val/1000000;
                    }
                }
            }
        });
    
        lightfish.view._charts.threads = new Chart({
            element: '#threadChart',
            chartOptions: {
                lines: {
                    fill:null
                }
            }
        });
        
        lightfish.view._charts.txCommit = new Chart({
            element: '#txCommitChart'
        });
        
        lightfish.view._charts.txRollback = new Chart({
            element: '#txRollbackChart'
        });
        
        lightfish.view._charts.queuedConnections = new Chart({
            element: '#queuedConnectionChart',
            chartOptions: {
                yaxis: {
                    min:0
                }
            }
        });
        
        lightfish.view._charts.errors = new Chart({
            element: '#errorChart',
            chartOptions: {
                yaxis: {
                    minTickSize:1
                }
            }
        });
        
        lightfish.view._charts.errorsPerSecond = new Chart({
            element: '#errorPerSecondChart',
            chartOptions: {
                yaxis: {
                    minTickSize:null,
                    min:0
                }
            }
        });
        
        lightfish.view._charts.busyThreads = new Chart({
            element: '#busyThreadChart'
        });
        
        lightfish.view._charts.commitsPerSecond = new Chart({
            element: '#commitsPerSecondChart',
            chartOptions: {
                yaxis: {
                    minTickSize:null,
                    min:0
                }
            }
        });
        
        lightfish.view._charts.rollbacksPerSecond = new Chart({
            element: '#rollbacksPerSecondChart',
            chartOptions: {
                yaxis: {
                    minTickSize:null,
                    min:0
                }
            }
        });
        
        lightfish.view._charts.sessions = new Chart({
            element: '#sessionsChart'
        });
        
        lightfish.view._charts.expiredSessions = new Chart({
            element: '#expiredSessionsChart'
        });
    
        lightfish.view._charts.pool = {};
    
        lightfish.view._poll();
    }
}

$(function(){
    lightfish.view.setup();
    $("#tabs").tabs({
        activate: lightfish.view.redrawVisibleCharts
    });
    
    $("#applicationsContainer").accordion({
        heightStyle:"content"
    });
});


var _Chart = {
    element: null,
    maxDataLength: 15,
    labels: [],
    chartOptions: {
        lines: {
            show: true, 
            fill:true
        },
        yaxis: {
            minTickSize: 1,
            tickFormatter: function(val,axis){
                if(val<10){
                    return val.toFixed(2);
                }else if(val<100){
                    return val.toFixed(3);
                }else{
                    return val.toFixed(0);
                }
            }
        },
        xaxis: {
            mode:"time"
        }
    },
    _data: null,
    _chart: null,
    setData: function(data){
        this.data = data;
    },
    updateChart: function(){
        if(this._chart!=null){
            var labeledData = [];
            for(var index in this._data){
                var dataSet = {data:this._data[index]};
                if(typeof this.labels[index] != "undefined"){
                    dataSet.label = this.labels[index];
                }
                labeledData.push(dataSet);
            }
            this._chart.setData(labeledData);
            this._chart.setupGrid();
            this._chart.draw();
        }
    },
    appendData: function(newData){
        for(var lineIndex in newData){
            var dataLine = this._data[lineIndex];
            if(typeof dataLine == "undefined"){
                dataLine = [];
                this._data[lineIndex] = dataLine;
            }
            dataLine.push(newData[lineIndex]);
            if(dataLine.length > this.maxDataLength){
                dataLine.shift(1);
            }
        }
        if($(this.element).is(':visible')){
            this.updateChart();
        }
    },
    construct: function(){
        this._chart = $.plot($(this.element),this._data, this.chartOptions);
    }
    
}

function Chart(userOptions){
    $.extend(true,this,_Chart,userOptions);
    this._data = [];
    this.construct()
    
}