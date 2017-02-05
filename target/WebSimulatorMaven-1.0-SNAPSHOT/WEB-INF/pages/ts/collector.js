"use strict";
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var React = require("react");
var ModelProps = (function () {
    function ModelProps(inputs, outputs) {
        this.inputs = inputs.slice();
        this.outputs = outputs.slice();
    }
    return ModelProps;
}());
var VaporHeaterInputs = (function (_super) {
    __extends(VaporHeaterInputs, _super);
    function VaporHeaterInputs() {
        return _super.call(this, ['valve1', 'valve2', 'valve3'], ['param1', 'param2', 'param3', 'param4', 'param5', 'param6']) || this;
    }
    return VaporHeaterInputs;
}(ModelProps));
var ModelViewer = (function (_super) {
    __extends(ModelViewer, _super);
    function ModelViewer() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    return ModelViewer;
}(React.Component));
exports.ModelViewer = ModelViewer;
//# sourceMappingURL=collector.js.map