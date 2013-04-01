if(typeof lightfish == "undefined"){
    lightfish = {};
}

if (!Object.keys) {
    Object.keys = function (obj) {
        var keys = [],
        k;
        for (k in obj) {
            if (Object.prototype.hasOwnProperty.call(obj, k)) {
                keys.push(k);
            }
        }
        return keys;
    };
}

function formatTime(inTime){
    var time = new Date(inTime);
    return time.toLocaleTimeString();
}

function formatTimeMs(inTime){
    var time = new Date(inTime);
    return time.toLocaleTimeString() + "." + time.getMilliseconds();
}


function formatBytes(bytes){
    var precision = 1;
    var kilobyte = 1024;
    var megabyte = kilobyte * 1024;
    var gigabyte = megabyte * 1024;
    var terabyte = gigabyte * 1024;
	
    if ((bytes >= 0) && (bytes < kilobyte)) {
        return bytes + ' B';

    } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
        return (bytes / kilobyte).toFixed(precision) + ' KB';

    } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
        return (bytes / megabyte).toFixed(precision) + ' MB';

    } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
        return (bytes / gigabyte).toFixed(precision) + ' GB';

    } else if (bytes >= terabyte) {
        return (bytes / terabyte).toFixed(precision) + ' TB';
    } else {
        return bytes + ' B';
    }
}

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
                    return val.toFixed(0);
                }else{
                    return val.toFixed(0);
                }
            }
        },
        xaxis: {
            mode:"time",
            tickFormatter: function(val,axis){
                return formatTime(val);
            }
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
                var dataSet = {
                    data:this._data[index]
                };
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
        
        if(this._data[0] != undefined && this._data[0].length > 1){
            $(this.element).removeClass("loading");
        }
    },
    construct: function(){
        this._chart = $.plot($(this.element),this._data, this.chartOptions);
        $(this.element).addClass("loading");
    }
    
}

function Chart(userOptions){
    $.extend(true,this,_Chart,userOptions);
    this._data = [];
    this.construct()
    
}

var _SnapshotData = {
    purgeOld:function(){
        var objectKeys = Object.keys(lightfish.view._data)
        if(objectKeys.length > lightfish.view.config.snapshots.maxDataLength+1){
            var idToRemove = 0;
            if(objectKeys[idToRemove]=="purgeOld"){
                idToRemove++;
            }
            lightfish.view._data[objectKeys[idToRemove]].element.remove();
            delete lightfish.view._data[objectKeys[idToRemove]];
        }

    }
}

function SnapshotData(userOptions){
    $.extend(true,this,_SnapshotData,userOptions);
}

lightfish.view = {
    config: {
        baseUri: "/lightfish",
        currentInstance: "",
        refreshInterval: 2,
        snapshots: {
            maxDataLength: 100,
            importantData: {
                heapSize:"Used Heap", 
                threadCount:"Thread Count"
            },
            dialogOptions: {
                height: "auto",
                width:900,
                maxHeight: 600,
                resizable: true
            }
        },
        charts: {
            maxDataLength: 20
        }
    },
    _data: new SnapshotData({}),
    _snapshotPoll: function(){
        $.ajax({
            url: lightfish.view.config.baseUri + "/live/" + lightfish.view.config.currentInstance, 
            success: function(rawData){
                lightfish.view._snapshotPoll();
                var data = lightfish.view._parseSnapshot(rawData);
                lightfish.view._data[data.id] = data;
                lightfish.view._data.purgeOld();
                
                lightfish.view.snapshots._addEntry(data);
                
                lightfish.view.charts._calculateAdditionalData(data, lightfish.view.charts._previousData);
                lightfish.view.charts._updateCharts(data);
                
                lightfish.view.charts._previousData = data;
            }, 
            error:function(){
                lightfish.view._snapshotPoll();
            },
            dataType: "xml", 
            timeout: lightfish.view.config.refreshInterval * 2000
        });
    },
    _parseSnapshot: function(rawData){
        var $data = $(rawData);
        var parsed = {};
        parsed.id = Number($data.find("id:first").text());
        parsed.time = new Date($data.find("snapshot > monitoringTime").text())
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

        parsed.escalated = false;
        parsed.escalatedBy = {};

        return parsed;
    },
    setup: function(){
        if(document.location.hash.length>0){
            lightfish.view.config.currentInstance = document.location.hash.substr(1);
            $("#instance-selection-dropdown").val(lightfish.view.config.currentInstance);
        }
        
        $("#instance-selection-dropdown").change(lightfish.view.changeInstance)
        
        $("#applicationsContainer").accordion({
            heightStyle:"content"
        });

        lightfish.view.snapshots.setup();
        lightfish.view.charts.setup();
        lightfish.view.escalations.setup();
    },
    changeInstance:function(){
        document.location.assign("#" + $("#instance-selection-dropdown").val());
        document.location.reload();
    },
    snapshots:{
        _snapshotTemplate: null,
        _isOdd:false,
        _follow:true,
        formatters:{
            time: formatTime,
            monitoringTime: formatTimeMs,
            heapSize: formatBytes,
            escalatedBy: function(val){
                if(val==undefined || val == null){
                    return "";
                }
                var outVal = ""
                for(var key in val){
                    outVal += key + "  ";
                }
                return outVal;
            }
        },
        _formatData:function(key, inValue){
            var outValue = inValue;
            if(lightfish.view.snapshots.formatters[key]){
                outValue = lightfish.view.snapshots.formatters[key](outValue);
            }
            return outValue;
        },
        _addEntry:function(data, header){
            var snapshotEntry = lightfish.view.snapshots._snapshotTemplate.clone();
            snapshotEntry.addClass(lightfish.view.snapshots._idOdd?"odd":"even");
            snapshotEntry.attr('data-snapshot-id',data.id);
            snapshotEntry.dblclick(lightfish.view.snapshots._snapshotDblClick);
            for(var datumKey in data){
                var datumValue = data[datumKey];
                if(!header){
                    datumValue = lightfish.view.snapshots._formatData(datumKey, datumValue);
                }
                snapshotEntry.find(".snapshot-data-" + datumKey).append(datumValue);
            }
            if(header){
                snapshotEntry.appendTo("#snapshotHeader");
            }else{
                snapshotEntry.appendTo("#snapshotContainer");
            }
            snapshotEntry.show();
            
            data.element = snapshotEntry;
            lightfish.view.snapshots._idOdd = ! lightfish.view.snapshots._idOdd;
            if(lightfish.view.snapshots._follow){
                var container = $("#snapshotContainer")[0];
                container.scrollTop = container.scrollHeight;
            }
            return snapshotEntry;
        },
        _snapshotDblClick: function(){
            var snapshotId = $(this).attr('data-snapshot-id');
            lightfish.view.snapshots.snapshotDialog.show(snapshotId);
        },
        setup:function(){
            var snapshotTemplate = $("#snapshot-template");
            lightfish.view.snapshots._snapshotTemplate = snapshotTemplate;
            for(var key in lightfish.view.config.snapshots.importantData){
                snapshotTemplate.append('<div class="snapshot-datum snapshot-data-'+ key +'" />');
            }
            lightfish.view.snapshots._snapshotTemplate.remove();
            
            var headers = $.extend({
                time:"Time",
                id:"ID"
            }, lightfish.view.config.snapshots.importantData);
            lightfish.view.snapshots._addEntry(headers,true).addClass("snapshot-header").removeClass("snapshot-line");
            lightfish.view.snapshots.snapshotDialog.setup();
            $("#toggleFollow").click(lightfish.view.snapshots.toggleFollow)
            
        },
        toggleFollow:function(e){
            e.preventDefault();
            lightfish.view.snapshots._follow = !lightfish.view.snapshots._follow;
            $("#toggleFollow").html(lightfish.view.snapshots._follow?"Stop Following":"Follow");
        },
        snapshotDialog:{
            _template: null,
            _poolTemplate: null,
            _logRecordTemplate: null,
            setup: function(){
                var template = $("#snapshot-dialog-template");
                lightfish.view.snapshots.snapshotDialog._template = template;
                template.remove();
                
                var poolTemplate = $("#snapshot-dialog-pool-template");
                lightfish.view.snapshots.snapshotDialog._poolTemplate = poolTemplate;
                poolTemplate.remove();
                
                var logRecordTemplate = $("#snapshot-dialog-log-record-template");
                lightfish.view.snapshots.snapshotDialog._logRecordTemplate = logRecordTemplate;
                logRecordTemplate.remove();
            },
            show: function(snapshotId){
                var data = lightfish.view._data[snapshotId];
                
                if(data==undefined){
                    return;
                }
                var template = lightfish.view.snapshots.snapshotDialog._template;
                var dialogElem = template.clone();
                var dialogId = "snapshot-" + snapshotId;
                dialogElem.attr({
                    id:dialogId
                });
                
                if(!data.escalated){
                    dialogElem.find("#snapshot-datum-escalatedBy").parent().hide();
                }
                
                for(var key in data){
                    if(key=="pools" || key=="logs"){
                        continue;
                    }
                    var datumValue = lightfish.view.snapshots._formatData(key,data[key]);
                    
                    dialogElem.find("#snapshot-datum-" + key)
                    .attr('id',dialogId + '-datum-' + key)
                    .html(datumValue)
                }
                
                
                var poolsElem = dialogElem.find("#snapshot-datum-pools")
                for(var poolKey in data.pools){
                    var poolData = data.pools[poolKey];
                    var poolElem = lightfish.view.snapshots.snapshotDialog._poolTemplate.clone();
                    var poolElemId = dialogId + '-' + poolKey;
                    poolElem.attr('id',poolElemId)
                    poolElem.find("#snapshot-datum-pool-name")
                    .attr('id',poolElemId + '-datum-name')
                    .html(poolKey)

                    for(var key in poolData){
                        var datumValue = lightfish.view.snapshots._formatData(key,poolData[key]);
                        poolElem.find("#snapshot-datum-pool-" + key)
                        .attr('id',poolElemId + '-datum-' + key)
                        .html(datumValue)
                    }
                    poolElem.appendTo(poolsElem)
                }
                
                var logsElem = dialogElem.find("#snapshot-datum-logs")
                var logsElemId = dialogId + '-logs'
                logsElem.attr('id',logsElemId);
                
                var logsLoadingElem = logsElem.find("#snapshot-datum-logs-loading");
                logsLoadingElem.attr('id', logsElemId + "-loading");
                
                
                $.getJSON(lightfish.view.config.baseUri + "/resources/logs/for/snapshot/" + snapshotId,null,function(data){
                    if(data!=null){
                        for(var logIndex in data.logRecord){
                        
                            var logData = data.logRecord[logIndex];
                            var logRecordElem = lightfish.view.snapshots.snapshotDialog._logRecordTemplate.clone();
                            var logElemId = logsElemId + '-' + logIndex;
                            logRecordElem.attr('id',logElemId);

                            for(var key in logData){
                                var datumValue = lightfish.view.snapshots._formatData(key,logData[key]);
                                logRecordElem.find("#snapshot-datum-log-" + key)
                                .attr('id',logElemId + '-datum-' + key)
                                .html(datumValue)
                            }
                            logRecordElem.appendTo(logsElem)
                        }
                    }
                    logsLoadingElem.hide();
                })

                

                
                dialogElem.appendTo("#snapshotdialogContainer").dialog(
                    $.extend(true,
                    {
                        title: 'Snapshot ' + snapshotId + ' Details'
                    },
                    lightfish.view.config.snapshots.dialogOptions
                    )
                    );
            }
        }
        
    },
    escalations:{
        _escalations:[],
        _disableNotifications: false,
        _escalationPoll: function(){
            $.ajax({
                url: lightfish.view.config.baseUri + "/escalations/", 
                success: function(rawData){
                    lightfish.view.escalations._escalationPoll();
                    var $rawData = $(rawData);
                    var entries = $rawData.find("escalations > entry");
                    entries.each(function(){
                        var escalationKey = $(this).find("> key").text();
                        var escalationSnapshot = lightfish.view._parseSnapshot($(this).find("> value > snapshot"));
                        var escalationMessage = $(this).find("> value > basicMessage").text();
                        
                        var storedData = lightfish.view._data[escalationSnapshot.id];
                        if(storedData!=undefined){
                            storedData.escalated = true;
                            if(!storedData.escalatedBy[escalationKey]){
                                storedData.escalatedBy[escalationKey] = true;
                                storedData.element.addClass("escalated");
                                if(!lightfish.view.escalations._disableNotifications){
                                    lightfish.view.charts._addNotification(
                                        "Escalation Notification: " + escalationKey,
                                        escalationMessage);
                                }
                            }
                        }
                    })
                }, 
                error:function(){
                    lightfish.view.escalations._escalationPoll();
                },
                dataType: "xml", 
                timeout: lightfish.view.config.refreshInterval * 2000
            });
        },
        setup:function(){
            $("#notificationContainer").notify();
            lightfish.view.escalations._escalationPoll();
            $("#toggleNotifications").click(lightfish.view.escalations.toggleNotifcations)
        },
        toggleNotifcations:function(e){
            e.preventDefault();
            lightfish.view.escalations._disableNotifications = !lightfish.view.escalations._disableNotifications;
            $("#toggleNotifications").html(
                lightfish.view.escalations._disableNotifications?"Enable Notifications":"Disable Notifications");
        }
    },
    charts:{
        _charts: {},
        _poolChartNames: {
            connections: 'Connections Free/Used',
            waitQueueLength: 'Wait Queue Length',
            potentialLeak: 'Potential Connection Leak #'
        },
        _previousData: null,
        _addNotification: function(inTitle, inBody){
            $("#notificationContainer").notify("create", {
                title: inTitle,
                body: inBody
            })
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
            lightfish.view.charts._charts.heap.appendData([[data.time,data.heapSize]]);
            lightfish.view.charts._charts.threads.appendData([[data.time,data.threadCount],[data.time,data.peakThreadCount]]);
        
            lightfish.view.charts._charts.txCommit.appendData([[data.time,data.txCommitCount]]);
            lightfish.view.charts._charts.txRollback.appendData([[data.time,data.txRollbackCount]]);
        
            lightfish.view.charts._charts.errors.appendData([[data.time,data.errorCount]]);
            lightfish.view.charts._charts.errorsPerSecond.appendData([[data.time,data.errorsPerSecond]]);
            lightfish.view.charts._charts.queuedConnections.appendData([[data.time,data.queuedConnectionCount]]);
        
            lightfish.view.charts._charts.busyThreads.appendData([[data.time,data.currentBusyThreadCount]]);
        
            lightfish.view.charts._charts.commitsPerSecond.appendData([[data.time,data.commitsPerSecond]]);
            lightfish.view.charts._charts.rollbacksPerSecond.appendData([[data.time,data.rollbacksPerSecond]]);
        
            lightfish.view.charts._charts.sessions.appendData([[data.time,data.sessionCount]]);
            lightfish.view.charts._charts.expiredSessions.appendData([[data.time,data.expiredSessionCount]]);
        
            for(var poolKey in data.pools){
                if(typeof lightfish.view.charts._charts.pool[poolKey] == "undefined"){
                    lightfish.view.charts._createPoolCharts(poolKey)
                }
            
                for(var chartKey in lightfish.view.charts._poolChartNames){
                    if(chartKey!= "connections"){
                        lightfish.view.charts._charts.pool[poolKey][chartKey].appendData([[data.time,data.pools[poolKey][chartKey]]]);
                    }else{
                        lightfish.view.charts._charts.pool[poolKey][chartKey].appendData([
                            [data.time,data.pools[poolKey].usedConnections],
                            [data.time,data.pools[poolKey].freeConnections]
                            ]);
                    }
                }
            }
        },
        _createPoolCharts: function(poolKey){
            lightfish.view.charts._charts.pool[poolKey] = {};
        
            $('<li><a href="#' + poolKey + '"><span>Resource: ' + poolKey + '</span></a></li>')
            .appendTo("#tabIndex");
        
            var poolChartsElement = $('<div id="' + poolKey + '" />')
            .addClass('chart-subsection').appendTo('#tabs');
            poolChartsElement.append($('<h1>Resource: ' + poolKey + '</h1>'));
        
            for(var chartKey in lightfish.view.charts._poolChartNames){
                var currentChartContainer = $('<div class="chart-container" />')
                .appendTo(poolChartsElement);
                currentChartContainer.append($('<h1>' + lightfish.view.charts._poolChartNames[chartKey] + '</h1>'));
                var currentChartElement = $('<div id="' + poolKey + '_' + chartKey + 'Chart" class="chart" />')
                .appendTo(currentChartContainer);
            
                if(chartKey != "connections"){
                    lightfish.view.charts._charts.pool[poolKey][chartKey] = new Chart({
                        element: currentChartElement,
                        chartOptions: {
                            yaxis:{
                                min:0
                            }
                        }
                    })
                }else{
                    lightfish.view.charts._charts.pool[poolKey][chartKey] = new Chart({
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
            for(var chartKey in lightfish.view.charts._charts){
                if(chartKey=="pool"){
                    continue;
                }
                allCharts.push(lightfish.view.charts._charts[chartKey]);
            }
        
            for(var poolKey in lightfish.view.charts._charts.pool){
                var pool = lightfish.view.charts._charts.pool[poolKey];
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
            _Chart.maxDataLength = lightfish.view.config.charts.maxDataLength;
            $("#tabs").tabs({
                activate: lightfish.view.charts.redrawVisibleCharts
            });
        
            lightfish.view.charts._charts.heap = new Chart({
                element: '#heapChart',
                chartOptions: {
                    yaxis: {
                        min: 0,
                        tickFormatter: function(val,axis){
                            return formatBytes(val);
                        }
                    }
                }
            });
    
            lightfish.view.charts._charts.threads = new Chart({
                element: '#threadChart',
                labels:['thread count', 'peak thread count'],
                chartOptions: {
                    lines: {
                        fill:null
                    },
                    legend: {
                        position: "nw"
                    }
                }
            });
        
            lightfish.view.charts._charts.txCommit = new Chart({
                element: '#txCommitChart',
                chartOptions: {
                    yaxis: {
                        min:null
                    }
                }
            });
        
            lightfish.view.charts._charts.txRollback = new Chart({
                element: '#txRollbackChart'
            });
        
            lightfish.view.charts._charts.queuedConnections = new Chart({
                element: '#queuedConnectionChart',
                chartOptions: {
                    yaxis: {
                        min:0
                    }
                }
            });
        
            lightfish.view.charts._charts.errors = new Chart({
                element: '#errorChart',
                chartOptions: {
                    yaxis: {
                        minTickSize:1
                    }
                }
            });
        
            lightfish.view.charts._charts.errorsPerSecond = new Chart({
                element: '#errorPerSecondChart',
                chartOptions: {
                    yaxis: {
                        minTickSize:null,
                        min:0
                    }
                }
            });
        
            lightfish.view.charts._charts.busyThreads = new Chart({
                element: '#busyThreadChart'
            });
        
            lightfish.view.charts._charts.commitsPerSecond = new Chart({
                element: '#commitsPerSecondChart',
                chartOptions: {
                    yaxis: {
                        minTickSize:null,
                        min:0
                    }
                }
            });
        
            lightfish.view.charts._charts.rollbacksPerSecond = new Chart({
                element: '#rollbacksPerSecondChart',
                chartOptions: {
                    yaxis: {
                        minTickSize:null,
                        min:0
                    }
                }
            });
        
            lightfish.view.charts._charts.sessions = new Chart({
                element: '#sessionsChart'
            });
        
            lightfish.view.charts._charts.expiredSessions = new Chart({
                element: '#expiredSessionsChart'
            });
    
            lightfish.view.charts._charts.pool = {};
    
            lightfish.view._snapshotPoll();
        }
    }
}

$(function(){
    lightfish.view.setup();
});

