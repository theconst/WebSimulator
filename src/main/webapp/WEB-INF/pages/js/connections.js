'use strict'

var connections = (function() {

    //web-socket promise wrapper in request-response fashion
    function WebSocketP(url) {
        this._url = url;
    }
    
    WebSocketP.prototype.Connect = function() {
        this._endpoint = new WebSocket(this._url);
        var endpoint = this._endpoint;
        return new Promise(function(resolve, reject) {
            endpoint.onopen = function() {
                resolve();
            }
        });
    }

    WebSocketP.prototype.Request = function(request) {
        var endpoint = this._endpoint;
        return new Promise(function(resolve, reject) {

            //setup callbacks
            endpoint.onmessage = function(response) {
                resolve(JSON.parse(response.data)); //parse the response json
            }

            endpoint.onerror = function(error) {
                reject(error);
            }

            endpoint.onclose = function(reason) {
                reject(reason);
            }

            endpoint.send(typeof request == 'string' ? request : JSON.stringify(request));
        });
    }

    WebSocketP.prototype.Close = function(reason) { //closes websocket in right fashion
        var endpoint = this._endpoint;
        return new Promise(function(resolve, reject) {
            endpoint.onclose = function(reason) {
                resolve(reason);
            }

            (endpoint.readyState == 3) && resolve("already closed");

            endpoint.close();
        });
    }


    function Heartbeat(heartbeatURL, heartbeatPeriod) {
        this._heartbeatURL = heartbeatURL;
        this._heartbeatPeriod = heartbeatPeriod;
        this._heartbeatHandle = undefined;
    }

    Heartbeat.prototype.start = function() {
        var self = this;
        var withCredentials = {
                    credentials: "same-origin"
        };
        return new Promise(function(resolve, reject) {
            self._heartbeatHandle = setInterval(function() {
                fetch(self._heartbeatURL, withCredentials)
                        .then(function(response) {
                    response.ok || reject(response.status);
                }).catch(function(error) {
                    window.clearInterval(self._heartbeatHandle);
                    self._heartbeatHandle = undefined;
                    reject(error);
                });
            }, self._heartbeatPeriod);
        });
    }

    Heartbeat.prototype.stop = function() {
        this._heartbeatHandle && window.clearInterval(this._heartbeatHandle);
    }

    return {
        WebSocketP: WebSocketP,
        Heartbeat: Heartbeat
    }
})();