/* 
 * Simulation.js
 */

'use strict';

$(document).ready(main);

/* applicaton function */
function main() {
    //function that runs the application
    function run(app, dataSource, period) {
        if (app.isFinished()) {
            return Promise.resolve("finished");
        } else if (app.isPaused()) {
            
            
            //delay and then loop
            return delay("ignored", period).then(function() {
                return run(app, dataSource, period);
            });
            
        } else {
            var request = dataSource.Request(app.getUserInput());
            var wait = delay("ignored", period);
            return Promise.all([request, wait]).then(function(plotData) {
                app.plot(plotData[0]); //ignore delay

                return run(app, dataSource, period);
            });
        }
    }
    
    var historyURL;
    var dataSource;
    var app;
    var heartbeat;
    var minMessagePeriod;
    
    var withCredentials = {
        credentials: "same-origin"
    };
    fetch("/WebSimulator/simulate", withCredentials)
        .then(function(response) {
            return response.json();
        }).then(function(initializationData) {
            var heartbeatURL = initializationData["heartbeatURL"];
            var heartbeatPeriod = initializationData["heartbeatPeriod"];
            var websocketURL = initializationData["websocketURL"];
            historyURL = initializationData["historyURL"];
            
            minMessagePeriod = initializationData["minMessagePeriod"];

            heartbeat = new connections.Heartbeat(heartbeatURL, toMillis(heartbeatPeriod));   
            dataSource = new connections.WebSocketP(websocketURL);
            
            heartbeat.start(); //start heartbeat to keep the session alive
            
            return dataSource.Connect();
        }).then(function() {
            return dataSource.Request({"action" : "initial" });
        }).then(function(response) {
            //parse response with initial data on client
            var names = response["names"];
            var initials = response["initial"];
            var mins = response["min"];
            var maxs = response["max"];
            
            var valveInitialPositions = Object.create(null); 
            names.forEach(function(name, i) {
                valveInitialPositions[name] = {value : initials[i], 
                    min : mins[i], max : maxs[i]}
            });
            
            app = new view.View(valveInitialPositions, minMessagePeriod,
                        speedCalculationStrategy);
            app.showAll(); //show the display

            return run(app, dataSource, minMessagePeriod); //run the app with the specified datasource and period
        }).then(function() {
            return dataSource.Request({"action" : "finish"});
        }).then(function() {
             return dataSource.Close(1000);     //close the connection
        }).then(function() {
            return fetch(historyURL, withCredentials);
        }).then(function(response) {
            //name and content
            var disposition = response.headers.get('Content-Disposition');
            var name = disposition.match(/filename="(.+)"/)[1];
            
            if (!name) {
                //no file name supplied!
                throw new Error("filename must be passed");
            }
            
            //return file and its name in the array (is it ok ?)
            return Promise.all([response.blob(), name]);  
        }).then(function(blob) {
            download(blob[0], prettyPrintDate(new Date()) + blob[1]);    
        }).catch(function(error) {

            /* just log the ugly things */
            console.log(error);
//            main();                             //retry the steps
//            window.location.reload();             //less user-friendly but more secure
        }).then(function() {
             /* Perform clenup and other */
            app && app.hideAll();
            heartbeat && heartbeat.stop();
//            goHome();
        }) ; //fetch history
}


function goHome() {
    window.location.href = "home.html"; //quick fix to redirect home
}

//helper functions
function speedCalculationStrategy(speed, sampling) {
    return Math.max(sampling, Math.exp(speed));
};

function toMillis(minutes) {
    return minutes * 3600 * 1000;
}

function delay(value, millis) {
    return new Promise(function(resolve, reject) {
        setTimeout(function() {
            typeof value === "function" ? resolve(value()) : resolve(value);
        }, millis);
    });
}


//copied from http://www.alexhadik.com/blog/2016/7/7/l8ztp8kr5lbctf5qns4l8t3646npqh
function download(blob, fname) {
    var url = window.URL.createObjectURL(blob); //create reference to blob
    let a = document.createElement("a");
    a.style = "display: none";
    document.body.appendChild(a);
    a.href = url;
    a.download = fname;
    a.click();

    document.body.removeChild(a); //remove the child back
    window.URL.revokeObjectURL(url); //release the reference to the file by revoking the Object URL
}

function prettyPrintDate(today) {
    var date = today.getDate() + "." + (today.getMonth() + 1) + "." + today.getFullYear();
    var time = today.getHours() + "_" + today.getMinutes() + "_" + today.getSeconds();
    return date + '(' + time + ')';
}