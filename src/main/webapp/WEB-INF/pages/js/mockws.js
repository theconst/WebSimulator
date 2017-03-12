/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * Comment
 */
;
var mock = (function mock() {


    /**
     * Comment
     */
    function mockfetch(url) {
        if (url.indexOf("initSimulation") != -1) {
            return Promise.resolve({
                json: function() {
                    return {
                        "heartbeatURL": "heartbeat",
                        "heartbeatPeriod": "5",
                        "minMessagePeriod": "1000",
                    };
                }
            });
            console.log("simulation initialized");
        } else if (url.indexOf("heartbeat") != -1) {
            return Promise.resolve({
                ok: true
            });
            console.log("heartbeat received");
        } else if (url.indexOf("history") != -1) {
            return Promise.resolve({
                ok: true,
                blob: () => Promise.resolve(new Blob(["whatever"]))
            });
        }
    }

    function MockWebSocket(URL) {
        var self = this;
        this.URL = URL;

        this.READY_STATE = 1;
        this.receivedMessage = false;

        /**
         * Comment
         */
        function* Ticker(initial) {
            var time = initial;
            while (true) {
                yield time;
                time += 1;
            }
        }

        //wait until defined
        this.onmessage = function(message) {

        };

        this.onclose = function(reason) {

        }

        this.ticker = Ticker(0);

        this.tick = function() {
            return self.ticker.next().value;
        }

        this.send = function(message) {
            console.log("Sent: " + message);
            this.inputs = JSON.parse(message);
            self.receivedMessage = true;
        }

        this.generateMockMessage = function() {

            self.receivedMessage = false;
            var message = JSON.stringify(!self.inputs["action"] ? {
                param1: new Array(10).fill(300),
                param2: new Array(10).fill(310),
                param3: new Array(10).fill(320),
                param4: new Array(10).fill(330),
                param5: new Array(10).fill(340),
                param6: new Array(10).fill(350),
                ticks: new Array(10).fill(0).map(function(elem) {
                    return self.tick();
                }) } : {
                    names : ["input1" , "input2", "input3"],
                    initial: ["50", "60" , "70"],
                    min: ["30", "30", "30"],
                    max: ["70", "70", "70"]
            });
            self.onmessage(message);

            console.log("Message sent " + JSON.stringify(message));
        }
        var handle = setInterval(function() {
            self.receivedMessage && self.generateMockMessage();
        }, 250);

        this.close = function() {
            clearInterval(handle);
            console.log("WebSocket closed");
            self.onclose(1000);
        }
    }

    return {
        WebSocket: MockWebSocket,
        fetch: mockfetch
    }
})();