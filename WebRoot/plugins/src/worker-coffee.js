"no use strict";
!(function(window) {
if (typeof window.window != "undefined" && window.document)
    return;
if (window.require && window.define)
    return;

if (!window.console) {
    window.console = function() {
        var msgs = Array.prototype.slice.call(arguments, 0);
        postMessage({type: "log", data: msgs});
    };
    window.console.error =
    window.console.warn = 
    window.console.log =
    window.console.trace = window.console;
}
window.window = window;
window.ace = window;

window.onerror = function(message, file, line, col, err) {
    postMessage({type: "error", data: {
        message: message,
        data: err.data,
        file: file,
        line: line, 
        col: col,
        stack: err.stack
    }});
};

window.normalizeModule = function(parentId, moduleName) {
    // normalize plugin requires
    if (moduleName.indexOf("!") !== -1) {
        var chunks = moduleName.split("!");
        return window.normalizeModule(parentId, chunks[0]) + "!" + window.normalizeModule(parentId, chunks[1]);
    }
    // normalize relative requires
    if (moduleName.charAt(0) == ".") {
        var base = parentId.split("/").slice(0, -1).join("/");
        moduleName = (base ? base + "/" : "") + moduleName;
        
        while (moduleName.indexOf(".") !== -1 && previous != moduleName) {
            var previous = moduleName;
            moduleName = moduleName.replace(/^\.\//, "").replace(/\/\.\//, "/").replace(/[^\/]+\/\.\.\//, "");
        }
    }
    
    return moduleName;
};

window.require = function require(parentId, id) {
    if (!id) {
        id = parentId;
        parentId = null;
    }
    if (!id.charAt)
        throw new Error("worker.js require() accepts only (parentId, id) as arguments");

    id = window.normalizeModule(parentId, id);

    var module = window.require.modules[id];
    if (module) {
        if (!module.initialized) {
            module.initialized = true;
            module.exports = module.factory().exports;
        }
        return module.exports;
    }
   
    if (!window.require.tlns)
        return console.log("unable to load " + id);
    
    var path = resolveModuleId(id, window.require.tlns);
    if (path.slice(-3) != ".js") path += ".js";
    
    window.require.id = id;
    window.require.modules[id] = {}; // prevent infinite loop on broken modules
    importScripts(path);
    return window.require(parentId, id);
};
function resolveModuleId(id, paths) {
    var testPath = id, tail = "";
    while (testPath) {
        var alias = paths[testPath];
        if (typeof alias == "string") {
            return alias + tail;
        } else if (alias) {
            return  alias.location.replace(/\/*$/, "/") + (tail || alias.main || alias.name);
        } else if (alias === false) {
            return "";
        }
        var i = testPath.lastIndexOf("/");
        if (i === -1) break;
        tail = testPath.substr(i) + tail;
        testPath = testPath.slice(0, i);
    }
    return id;
}
window.require.modules = {};
window.require.tlns = {};

window.define = function(id, deps, factory) {
    if (arguments.length == 2) {
        factory = deps;
        if (typeof id != "string") {
            deps = id;
            id = window.require.id;
        }
    } else if (arguments.length == 1) {
        factory = id;
        deps = [];
        id = window.require.id;
    }
    
    if (typeof factory != "function") {
        window.require.modules[id] = {
            exports: factory,
            initialized: true
        };
        return;
    }

    if (!deps.length)
        // If there is no dependencies, we inject "require", "exports" and
        // "module" as dependencies, to provide CommonJS compatibility.
        deps = ["require", "exports", "module"];

    var req = function(childId) {
        return window.require(id, childId);
    };

    window.require.modules[id] = {
        exports: {},
        factory: function() {
            var module = this;
            var returnExports = factory.apply(this, deps.map(function(dep) {
                switch (dep) {
                    // Because "require", "exports" and "module" aren't actual
                    // dependencies, we must handle them seperately.
                    case "require": return req;
                    case "exports": return module.exports;
                    case "module":  return module;
                    // But for all other dependencies, we can just go ahead and
                    // require them.
                    default:        return req(dep);
                }
            }));
            if (returnExports)
                module.exports = returnExports;
            return module;
        }
    };
};
window.define.amd = {};
require.tlns = {};
window.initBaseUrls  = function initBaseUrls(topLevelNamespaces) {
    for (var i in topLevelNamespaces)
        require.tlns[i] = topLevelNamespaces[i];
};

window.initSender = function initSender() {

    var EventEmitter = window.require("ace/lib/event_emitter").EventEmitter;
    var oop = window.require("ace/lib/oop");
    
    var Sender = function() {};
    
    (function() {
        
        oop.implement(this, EventEmitter);
                
        this.callback = function(data, callbackId) {
            postMessage({
                type: "call",
                id: callbackId,
                data: data
            });
        };
    
        this.emit = function(name, data) {
            postMessage({
                type: "event",
                name: name,
                data: data
            });
        };
        
    }).call(Sender.prototype);
    
    return new Sender();
};

var main = window.main = null;
var sender = window.sender = null;

window.onmessage = function(e) {
    var msg = e.data;
    if (msg.event && sender) {
        sender._signal(msg.event, msg.data);
    }
    else if (msg.command) {
        if (main[msg.command])
            main[msg.command].apply(main, msg.args);
        else if (window[msg.command])
            window[msg.command].apply(window, msg.args);
        else
            throw new Error("Unknown command:" + msg.command);
    }
    else if (msg.init) {
        window.initBaseUrls(msg.tlns);
        require("ace/lib/es5-shim");
        sender = window.sender = window.initSender();
        var clazz = require(msg.module)[msg.classname];
        main = window.main = new clazz(sender);
    }
};
})(this);

define("ace/lib/oop",["require","exports","module"], function(require, exports, module) {
"use strict";

exports.inherits = function(ctor, superCtor) {
    ctor.super_ = superCtor;
    ctor.prototype = Object.create(superCtor.prototype, {
        constructor: {
            value: ctor,
            enumerable: false,
            writable: true,
            configurable: true
        }
    });
};

exports.mixin = function(obj, mixin) {
    for (var key in mixin) {
        obj[key] = mixin[key];
    }
    return obj;
};

exports.implement = function(proto, mixin) {
    exports.mixin(proto, mixin);
};

});

define("ace/range",["require","exports","module"], function(require, exports, module) {
"use strict";
var comparePoints = function(p1, p2) {
    return p1.row - p2.row || p1.column - p2.column;
};
var Range = function(startRow, startColumn, endRow, endColumn) {
    this.start = {
        row: startRow,
        column: startColumn
    };

    this.end = {
        row: endRow,
        column: endColumn
    };
};

(function() {
    this.isEqual = function(range) {
        return this.start.row === range.start.row &&
            this.end.row === range.end.row &&
            this.start.column === range.start.column &&
            this.end.column === range.end.column;
    };
    this.toString = function() {
        return ("Range: [" + this.start.row + "/" + this.start.column +
            "] -> [" + this.end.row + "/" + this.end.column + "]");
    };

    this.contains = function(row, column) {
        return this.compare(row, column) == 0;
    };
    this.compareRange = function(range) {
        var cmp,
            end = range.end,
            start = range.start;

        cmp = this.compare(end.row, end.column);
        if (cmp == 1) {
            cmp = this.compare(start.row, start.column);
            if (cmp == 1) {
                return 2;
            } else if (cmp == 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (cmp == -1) {
            return -2;
        } else {
            cmp = this.compare(start.row, start.column);
            if (cmp == -1) {
                return -1;
            } else if (cmp == 1) {
                return 42;
            } else {
                return 0;
            }
        }
    };
    this.comparePoint = function(p) {
        return this.compare(p.row, p.column);
    };
    this.containsRange = function(range) {
        return this.comparePoint(range.start) == 0 && this.comparePoint(range.end) == 0;
    };
    this.intersects = function(range) {
        var cmp = this.compareRange(range);
        return (cmp == -1 || cmp == 0 || cmp == 1);
    };
    this.isEnd = function(row, column) {
        return this.end.row == row && this.end.column == column;
    };
    this.isStart = function(row, column) {
        return this.start.row == row && this.start.column == column;
    };
    this.setStart = function(row, column) {
        if (typeof row == "object") {
            this.start.column = row.column;
            this.start.row = row.row;
        } else {
            this.start.row = row;
            this.start.column = column;
        }
    };
    this.setEnd = function(row, column) {
        if (typeof row == "object") {
            this.end.column = row.column;
            this.end.row = row.row;
        } else {
            this.end.row = row;
            this.end.column = column;
        }
    };
    this.inside = function(row, column) {
        if (this.compare(row, column) == 0) {
            if (this.isEnd(row, column) || this.isStart(row, column)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    };
    this.insideStart = function(row, column) {
        if (this.compare(row, column) == 0) {
            if (this.isEnd(row, column)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    };
    this.insideEnd = function(row, column) {
        if (this.compare(row, column) == 0) {
            if (this.isStart(row, column)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    };
    this.compare = function(row, column) {
        if (!this.isMultiLine()) {
            if (row === this.start.row) {
                return column < this.start.column ? -1 : (column > this.end.column ? 1 : 0);
            }
        }

        if (row < this.start.row)
            return -1;

        if (row > this.end.row)
            return 1;

        if (this.start.row === row)
            return column >= this.start.column ? 0 : -1;

        if (this.end.row === row)
            return column <= this.end.column ? 0 : 1;

        return 0;
    };
    this.compareStart = function(row, column) {
        if (this.start.row == row && this.start.column == column) {
            return -1;
        } else {
            return this.compare(row, column);
        }
    };
    this.compareEnd = function(row, column) {
        if (this.end.row == row && this.end.column == column) {
            return 1;
        } else {
            return this.compare(row, column);
        }
    };
    this.compareInside = function(row, column) {
        if (this.end.row == row && this.end.column == column) {
            return 1;
        } else if (this.start.row == row && this.start.column == column) {
            return -1;
        } else {
            return this.compare(row, column);
        }
    };
    this.clipRows = function(firstRow, lastRow) {
        if (this.end.row > lastRow)
            var end = {row: lastRow + 1, column: 0};
        else if (this.end.row < firstRow)
            var end = {row: firstRow, column: 0};

        if (this.start.row > lastRow)
            var start = {row: lastRow + 1, column: 0};
        else if (this.start.row < firstRow)
            var start = {row: firstRow, column: 0};

        return Range.fromPoints(start || this.start, end || this.end);
    };
    this.extend = function(row, column) {
        var cmp = this.compare(row, column);

        if (cmp == 0)
            return this;
        else if (cmp == -1)
            var start = {row: row, column: column};
        else
            var end = {row: row, column: column};

        return Range.fromPoints(start || this.start, end || this.end);
    };

    this.isEmpty = function() {
        return (this.start.row === this.end.row && this.start.column === this.end.column);
    };
    this.isMultiLine = function() {
        return (this.start.row !== this.end.row);
    };
    this.clone = function() {
        return Range.fromPoints(this.start, this.end);
    };
    this.collapseRows = function() {
        if (this.end.column == 0)
            return new Range(this.start.row, 0, Math.max(this.start.row, this.end.row-1), 0);
        else
            return new Range(this.start.row, 0, this.end.row, 0);
    };
    this.toScreenRange = function(session) {
        var screenPosStart = session.documentToScreenPosition(this.start);
        var screenPosEnd = session.documentToScreenPosition(this.end);

        return new Range(
            screenPosStart.row, screenPosStart.column,
            screenPosEnd.row, screenPosEnd.column
        );
    };
    this.moveBy = function(row, column) {
        this.start.row += row;
        this.start.column += column;
        this.end.row += row;
        this.end.column += column;
    };

}).call(Range.prototype);
Range.fromPoints = function(start, end) {
    return new Range(start.row, start.column, end.row, end.column);
};
Range.comparePoints = comparePoints;

Range.comparePoints = function(p1, p2) {
    return p1.row - p2.row || p1.column - p2.column;
};


exports.Range = Range;
});

define("ace/apply_delta",["require","exports","module"], function(require, exports, module) {
"use strict";

function throwDeltaError(delta, errorText){
    console.log("Invalid Delta:", delta);
    throw "Invalid Delta: " + errorText;
}

function positionInDocument(docLines, position) {
    return position.row    >= 0 && position.row    <  docLines.length &&
           position.column >= 0 && position.column <= docLines[position.row].length;
}

function validateDelta(docLines, delta) {
    if (delta.action != "insert" && delta.action != "remove")
        throwDeltaError(delta, "delta.action must be 'insert' or 'remove'");
    if (!(delta.lines instanceof Array))
        throwDeltaError(delta, "delta.lines must be an Array");
    if (!delta.start || !delta.end)
       throwDeltaError(delta, "delta.start/end must be an present");
    var start = delta.start;
    if (!positionInDocument(docLines, delta.start))
        throwDeltaError(delta, "delta.start must be contained in document");
    var end = delta.end;
    if (delta.action == "remove" && !positionInDocument(docLines, end))
        throwDeltaError(delta, "delta.end must contained in document for 'remove' actions");
    var numRangeRows = end.row - start.row;
    var numRangeLastLineChars = (end.column - (numRangeRows == 0 ? start.column : 0));
    if (numRangeRows != delta.lines.length - 1 || delta.lines[numRangeRows].length != numRangeLastLineChars)
        throwDeltaError(delta, "delta.range must match delta lines");
}

exports.applyDelta = function(docLines, delta, doNotValidate) {
    
    var row = delta.start.row;
    var startColumn = delta.start.column;
    var line = docLines[row] || "";
    switch (delta.action) {
        case "insert":
            var lines = delta.lines;
            if (lines.length === 1) {
                docLines[row] = line.substring(0, startColumn) + delta.lines[0] + line.substring(startColumn);
            } else {
                var args = [row, 1].concat(delta.lines);
                docLines.splice.apply(docLines, args);
                docLines[row] = line.substring(0, startColumn) + docLines[row];
                docLines[row + delta.lines.length - 1] += line.substring(startColumn);
            }
            break;
        case "remove":
            var endColumn = delta.end.column;
            var endRow = delta.end.row;
            if (row === endRow) {
                docLines[row] = line.substring(0, startColumn) + line.substring(endColumn);
            } else {
                docLines.splice(
                    row, endRow - row + 1,
                    line.substring(0, startColumn) + docLines[endRow].substring(endColumn)
                );
            }
            break;
    }
};
});

define("ace/lib/event_emitter",["require","exports","module"], function(require, exports, module) {
"use strict";

var EventEmitter = {};
var stopPropagation = function() { this.propagationStopped = true; };
var preventDefault = function() { this.defaultPrevented = true; };

EventEmitter._emit =
EventEmitter._dispatchEvent = function(eventName, e) {
    this._eventRegistry || (this._eventRegistry = {});
    this._defaultHandlers || (this._defaultHandlers = {});

    var listeners = this._eventRegistry[eventName] || [];
    var defaultHandler = this._defaultHandlers[eventName];
    if (!listeners.length && !defaultHandler)
        return;

    if (typeof e != "object" || !e)
        e = {};

    if (!e.type)
        e.type = eventName;
    if (!e.stopPropagation)
        e.stopPropagation = stopPropagation;
    if (!e.preventDefault)
        e.preventDefault = preventDefault;

    listeners = listeners.slice();
    for (var i=0; i<listeners.length; i++) {
        listeners[i](e, this);
        if (e.propagationStopped)
            break;
    }
    
    if (defaultHandler && !e.defaultPrevented)
        return defaultHandler(e, this);
};


EventEmitter._signal = function(eventName, e) {
    var listeners = (this._eventRegistry || {})[eventName];
    if (!listeners)
        return;
    listeners = listeners.slice();
    for (var i=0; i<listeners.length; i++)
        listeners[i](e, this);
};

EventEmitter.once = function(eventName, callback) {
    var _self = this;
    callback && this.addEventListener(eventName, function newCallback() {
        _self.removeEventListener(eventName, newCallback);
        callback.apply(null, arguments);
    });
};


EventEmitter.setDefaultHandler = function(eventName, callback) {
    var handlers = this._defaultHandlers;
    if (!handlers)
        handlers = this._defaultHandlers = {_disabled_: {}};
    
    if (handlers[eventName]) {
        var old = handlers[eventName];
        var disabled = handlers._disabled_[eventName];
        if (!disabled)
            handlers._disabled_[eventName] = disabled = [];
        disabled.push(old);
        var i = disabled.indexOf(callback);
        if (i != -1) 
            disabled.splice(i, 1);
    }
    handlers[eventName] = callback;
};
EventEmitter.removeDefaultHandler = function(eventName, callback) {
    var handlers = this._defaultHandlers;
    if (!handlers)
        return;
    var disabled = handlers._disabled_[eventName];
    
    if (handlers[eventName] == callback) {
        var old = handlers[eventName];
        if (disabled)
            this.setDefaultHandler(eventName, disabled.pop());
    } else if (disabled) {
        var i = disabled.indexOf(callback);
        if (i != -1)
            disabled.splice(i, 1);
    }
};

EventEmitter.on =
EventEmitter.addEventListener = function(eventName, callback, capturing) {
    this._eventRegistry = this._eventRegistry || {};

    var listeners = this._eventRegistry[eventName];
    if (!listeners)
        listeners = this._eventRegistry[eventName] = [];

    if (listeners.indexOf(callback) == -1)
        listeners[capturing ? "unshift" : "push"](callback);
    return callback;
};

EventEmitter.off =
EventEmitter.removeListener =
EventEmitter.removeEventListener = function(eventName, callback) {
    this._eventRegistry = this._eventRegistry || {};

    var listeners = this._eventRegistry[eventName];
    if (!listeners)
        return;

    var index = listeners.indexOf(callback);
    if (index !== -1)
        listeners.splice(index, 1);
};

EventEmitter.removeAllListeners = function(eventName) {
    if (this._eventRegistry) this._eventRegistry[eventName] = [];
};

exports.EventEmitter = EventEmitter;

});

define("ace/anchor",["require","exports","module","ace/lib/oop","ace/lib/event_emitter"], function(require, exports, module) {
"use strict";

var oop = require("./lib/oop");
var EventEmitter = require("./lib/event_emitter").EventEmitter;

var Anchor = exports.Anchor = function(doc, row, column) {
    this.$onChange = this.onChange.bind(this);
    this.attach(doc);
    
    if (typeof column == "undefined")
        this.setPosition(row.row, row.column);
    else
        this.setPosition(row, column);
};

(function() {

    oop.implement(this, EventEmitter);
    this.getPosition = function() {
        return this.$clipPositionToDocument(this.row, this.column);
    };
    this.getDocument = function() {
        return this.document;
    };
    this.$insertRight = false;
    this.onChange = function(delta) {
        if (delta.start.row == delta.end.row && delta.start.row != this.row)
            return;

        if (delta.start.row > this.row)
            return;
            
        var point = $getTransformedPoint(delta, {row: this.row, column: this.column}, this.$insertRight);
        this.setPosition(point.row, point.column, true);
    };
    
    function $pointsInOrder(point1, point2, equalPointsInOrder) {
        var bColIsAfter = equalPointsInOrder ? point1.column <= point2.column : point1.column < point2.column;
        return (point1.row < point2.row) || (point1.row == point2.row && bColIsAfter);
    }
            
    function $getTransformedPoint(delta, point, moveIfEqual) {
        var deltaIsInsert = delta.action == "insert";
        var deltaRowShift = (deltaIsInsert ? 1 : -1) * (delta.end.row    - delta.start.row);
        var deltaColShift = (deltaIsInsert ? 1 : -1) * (delta.end.column - delta.start.column);
        var deltaStart = delta.start;
        var deltaEnd = deltaIsInsert ? deltaStart : delta.end; // Collapse insert range.
        if ($pointsInOrder(point, deltaStart, moveIfEqual)) {
            return {
                row: point.row,
                column: point.column
            };
        }
        if ($pointsInOrder(deltaEnd, point, !moveIfEqual)) {
            return {
                row: point.row + deltaRowShift,
                column: point.column + (point.row == deltaEnd.row ? deltaColShift : 0)
            };
        }
        
        return {
            row: deltaStart.row,
            column: deltaStart.column
        };
    }
    this.setPosition = function(row, column, noClip) {
        var pos;
        if (noClip) {
            pos = {
                row: row,
                column: column
            };
        } else {
            pos = this.$clipPositionToDocument(row, column);
        }

        if (this.row == pos.row && this.column == pos.column)
            return;

        var old = {
            row: this.row,
            column: this.column
        };

        this.row = pos.row;
        this.column = pos.column;
        this._signal("change", {
            old: old,
            value: pos
        });
    };
    this.detach = function() {
        this.document.removeEventListener("change", this.$onChange);
    };
    this.attach = function(doc) {
        this.document = doc || this.document;
        this.document.on("change", this.$onChange);
    };
    this.$clipPositionToDocument = function(row, column) {
        var pos = {};

        if (row >= this.document.getLength()) {
            pos.row = Math.max(0, this.document.getLength() - 1);
            pos.column = this.document.getLine(pos.row).length;
        }
        else if (row < 0) {
            pos.row = 0;
            pos.column = 0;
        }
        else {
            pos.row = row;
            pos.column = Math.min(this.document.getLine(pos.row).length, Math.max(0, column));
        }

        if (column < 0)
            pos.column = 0;

        return pos;
    };

}).call(Anchor.prototype);

});

define("ace/document",["require","exports","module","ace/lib/oop","ace/apply_delta","ace/lib/event_emitter","ace/range","ace/anchor"], function(require, exports, module) {
"use strict";

var oop = require("./lib/oop");
var applyDelta = require("./apply_delta").applyDelta;
var EventEmitter = require("./lib/event_emitter").EventEmitter;
var Range = require("./range").Range;
var Anchor = require("./anchor").Anchor;

var Document = function(textOrLines) {
    this.$lines = [""];
    if (textOrLines.length === 0) {
        this.$lines = [""];
    } else if (Array.isArray(textOrLines)) {
        this.insertMergedLines({row: 0, column: 0}, textOrLines);
    } else {
        this.insert({row: 0, column:0}, textOrLines);
    }
};

(function() {

    oop.implement(this, EventEmitter);
    this.setValue = function(text) {
        var len = this.getLength() - 1;
        this.remove(new Range(0, 0, len, this.getLine(len).length));
        this.insert({row: 0, column: 0}, text);
    };
    this.getValue = function() {
        return this.getAllLines().join(this.getNewLineCharacter());
    };
    this.createAnchor = function(row, column) {
        return new Anchor(this, row, column);
    };
    if ("aaa".split(/a/).length === 0) {
        this.$split = function(text) {
            return text.replace(/\r\n|\r/g, "\n").split("\n");
        };
    } else {
        this.$split = function(text) {
            return text.split(/\r\n|\r|\n/);
        };
    }


    this.$detectNewLine = function(text) {
        var match = text.match(/^.*?(\r\n|\r|\n)/m);
        this.$autoNewLine = match ? match[1] : "\n";
        this._signal("changeNewLineMode");
    };
    this.getNewLineCharacter = function() {
        switch (this.$newLineMode) {
          case "windows":
            return "\r\n";
          case "unix":
            return "\n";
          default:
            return this.$autoNewLine || "\n";
        }
    };

    this.$autoNewLine = "";
    this.$newLineMode = "auto";
    this.setNewLineMode = function(newLineMode) {
        if (this.$newLineMode === newLineMode)
            return;

        this.$newLineMode = newLineMode;
        this._signal("changeNewLineMode");
    };
    this.getNewLineMode = function() {
        return this.$newLineMode;
    };
    this.isNewLine = function(text) {
        return (text == "\r\n" || text == "\r" || text == "\n");
    };
    this.getLine = function(row) {
        return this.$lines[row] || "";
    };
    this.getLines = function(firstRow, lastRow) {
        return this.$lines.slice(firstRow, lastRow + 1);
    };
    this.getAllLines = function() {
        return this.getLines(0, this.getLength());
    };
    this.getLength = function() {
        return this.$lines.length;
    };
    this.getTextRange = function(range) {
        return this.getLinesForRange(range).join(this.getNewLineCharacter());
    };
    this.getLinesForRange = function(range) {
        var lines;
        if (range.start.row === range.end.row) {
            lines = [this.getLine(range.start.row).substring(range.start.column, range.end.column)];
        } else {
            lines = this.getLines(range.start.row, range.end.row);
            lines[0] = (lines[0] || "").substring(range.start.column);
            var l = lines.length - 1;
            if (range.end.row - range.start.row == l)
                lines[l] = lines[l].substring(0, range.end.column);
        }
        return lines;
    };
    this.insertLines = function(row, lines) {
        console.warn("Use of document.insertLines is deprecated. Use the insertFullLines method instead.");
        return this.insertFullLines(row, lines);
    };
    this.removeLines = function(firstRow, lastRow) {
        console.warn("Use of document.removeLines is deprecated. Use the removeFullLines method instead.");
        return this.removeFullLines(firstRow, lastRow);
    };
    this.insertNewLine = function(position) {
        console.warn("Use of document.insertNewLine is deprecated. Use insertMergedLines(position, ['', '']) instead.");
        return this.insertMergedLines(position, ["", ""]);
    };
    this.insert = function(position, text) {
        if (this.getLength() <= 1)
            this.$detectNewLine(text);
        
        return this.insertMergedLines(position, this.$split(text));
    };
    this.insertInLine = function(position, text) {
        var start = this.clippedPos(position.row, position.column);
        var end = this.pos(position.row, position.column + text.length);
        
        this.applyDelta({
            start: start,
            end: end,
            action: "insert",
            lines: [text]
        }, true);
        
        return this.clonePos(end);
    };
    
    this.clippedPos = function(row, column) {
        var length = this.getLength();
        if (row === undefined) {
            row = length;
        } else if (row < 0) {
            row = 0;
        } else if (row >= length) {
            row = length - 1;
            column = undefined;
        }
        var line = this.getLine(row);
        if (column == undefined)
            column = line.length;
        column = Math.min(Math.max(column, 0), line.length);
        return {row: row, column: column};
    };
    
    this.clonePos = function(pos) {
        return {row: pos.row, column: pos.column};
    };
    
    this.pos = function(row, column) {
        return {row: row, column: column};
    };
    
    this.$clipPosition = function(position) {
        var length = this.getLength();
        if (position.row >= length) {
            position.row = Math.max(0, length - 1);
            position.column = this.getLine(length - 1).length;
        } else {
            position.row = Math.max(0, position.row);
            position.column = Math.min(Math.max(position.column, 0), this.getLine(position.row).length);
        }
        return position;
    };
    this.insertFullLines = function(row, lines) {
        row = Math.min(Math.max(row, 0), this.getLength());
        var column = 0;
        if (row < this.getLength()) {
            lines = lines.concat([""]);
            column = 0;
        } else {
            lines = [""].concat(lines);
            row--;
            column = this.$lines[row].length;
        }
        this.insertMergedLines({row: row, column: column}, lines);
    };    
    this.insertMergedLines = function(position, lines) {
        var start = this.clippedPos(position.row, position.column);
        var end = {
            row: start.row + lines.length - 1,
            column: (lines.length == 1 ? start.column : 0) + lines[lines.length - 1].length
        };
        
        this.applyDelta({
            start: start,
            end: end,
            action: "insert",
            lines: lines
        });
        
        return this.clonePos(end);
    };
    this.remove = function(range) {
        var start = this.clippedPos(range.start.row, range.start.column);
        var end = this.clippedPos(range.end.row, range.end.column);
        this.applyDelta({
            start: start,
            end: end,
            action: "remove",
            lines: this.getLinesForRange({start: start, end: end})
        });
        return this.clonePos(start);
    };
    this.removeInLine = function(row, startColumn, endColumn) {
        var start = this.clippedPos(row, startColumn);
        var end = this.clippedPos(row, endColumn);
        
        this.applyDelta({
            start: start,
            end: end,
            action: "remove",
            lines: this.getLinesForRange({start: start, end: end})
        }, true);
        
        return this.clonePos(start);
    };
    this.removeFullLines = function(firstRow, lastRow) {
        firstRow = Math.min(Math.max(0, firstRow), this.getLength() - 1);
        lastRow  = Math.min(Math.max(0, lastRow ), this.getLength() - 1);
        var deleteFirstNewLine = lastRow == this.getLength() - 1 && firstRow > 0;
        var deleteLastNewLine  = lastRow  < this.getLength() - 1;
        var startRow = ( deleteFirstNewLine ? firstRow - 1                  : firstRow                    );
        var startCol = ( deleteFirstNewLine ? this.getLine(startRow).length : 0                           );
        var endRow   = ( deleteLastNewLine  ? lastRow + 1                   : lastRow                     );
        var endCol   = ( deleteLastNewLine  ? 0                             : this.getLine(endRow).length ); 
        var range = new Range(startRow, startCol, endRow, endCol);
        var deletedLines = this.$lines.slice(firstRow, lastRow + 1);
        
        this.applyDelta({
            start: range.start,
            end: range.end,
            action: "remove",
            lines: this.getLinesForRange(range)
        });
        return deletedLines;
    };
    this.removeNewLine = function(row) {
        if (row < this.getLength() - 1 && row >= 0) {
            this.applyDelta({
                start: this.pos(row, this.getLine(row).length),
                end: this.pos(row + 1, 0),
                action: "remove",
                lines: ["", ""]
            });
        }
    };
    this.replace = function(range, text) {
        if (!(range instanceof Range))
            range = Range.fromPoints(range.start, range.end);
        if (text.length === 0 && range.isEmpty())
            return range.start;
        if (text == this.getTextRange(range))
            return range.end;

        this.remove(range);
        var end;
        if (text) {
            end = this.insert(range.start, text);
        }
        else {
            end = range.start;
        }
        
        return end;
    };
    this.applyDeltas = function(deltas) {
        for (var i=0; i<deltas.length; i++) {
            this.applyDelta(deltas[i]);
        }
    };
    this.revertDeltas = function(deltas) {
        for (var i=deltas.length-1; i>=0; i--) {
            this.revertDelta(deltas[i]);
        }
    };
    this.applyDelta = function(delta, doNotValidate) {
        var isInsert = delta.action == "insert";
        if (isInsert ? delta.lines.length <= 1 && !delta.lines[0]
            : !Range.comparePoints(delta.start, delta.end)) {
            return;
        }
        
        if (isInsert && delta.lines.length > 20000)
            this.$splitAndapplyLargeDelta(delta, 20000);
        applyDelta(this.$lines, delta, doNotValidate);
        this._signal("change", delta);
    };
    
    this.$splitAndapplyLargeDelta = function(delta, MAX) {
        var lines = delta.lines;
        var l = lines.length;
        var row = delta.start.row; 
        var column = delta.start.column;
        var from = 0, to = 0;
        do {
            from = to;
            to += MAX - 1;
            var chunk = lines.slice(from, to);
            if (to > l) {
                delta.lines = chunk;
                delta.start.row = row + from;
                delta.start.column = column;
                break;
            }
            chunk.push("");
            this.applyDelta({
                start: this.pos(row + from, column),
                end: this.pos(row + to, column = 0),
                action: delta.action,
                lines: chunk
            }, true);
        } while(true);
    };
    this.revertDelta = function(delta) {
        this.applyDelta({
            start: this.clonePos(delta.start),
            end: this.clonePos(delta.end),
            action: (delta.action == "insert" ? "remove" : "insert"),
            lines: delta.lines.slice()
        });
    };
    this.indexToPosition = function(index, startRow) {
        var lines = this.$lines || this.getAllLines();
        var newlineLength = this.getNewLineCharacter().length;
        for (var i = startRow || 0, l = lines.length; i < l; i++) {
            index -= lines[i].length + newlineLength;
            if (index < 0)
                return {row: i, column: index + lines[i].length + newlineLength};
        }
        return {row: l-1, column: lines[l-1].length};
    };
    this.positionToIndex = function(pos, startRow) {
        var lines = this.$lines || this.getAllLines();
        var newlineLength = this.getNewLineCharacter().length;
        var index = 0;
        var row = Math.min(pos.row, lines.length);
        for (var i = startRow || 0; i < row; ++i)
            index += lines[i].length + newlineLength;

        return index + pos.column;
    };

}).call(Document.prototype);

exports.Document = Document;
});

define("ace/lib/lang",["require","exports","module"], function(require, exports, module) {
"use strict";

exports.last = function(a) {
    return a[a.length - 1];
};

exports.stringReverse = function(string) {
    return string.split("").reverse().join("");
};

exports.stringRepeat = function (string, count) {
    var result = '';
    while (count > 0) {
        if (count & 1)
            result += string;

        if (count >>= 1)
            string += string;
    }
    return result;
};

var trimBeginRegexp = /^\s\s*/;
var trimEndRegexp = /\s\s*$/;

exports.stringTrimLeft = function (string) {
    return string.replace(trimBeginRegexp, '');
};

exports.stringTrimRight = function (string) {
    return string.replace(trimEndRegexp, '');
};

exports.copyObject = function(obj) {
    var copy = {};
    for (var key in obj) {
        copy[key] = obj[key];
    }
    return copy;
};

exports.copyArray = function(array){
    var copy = [];
    for (var i=0, l=array.length; i<l; i++) {
        if (array[i] && typeof array[i] == "object")
            copy[i] = this.copyObject(array[i]);
        else 
            copy[i] = array[i];
    }
    return copy;
};

exports.deepCopy = function deepCopy(obj) {
    if (typeof obj !== "object" || !obj)
        return obj;
    var copy;
    if (Array.isArray(obj)) {
        copy = [];
        for (var key = 0; key < obj.length; key++) {
            copy[key] = deepCopy(obj[key]);
        }
        return copy;
    }
    if (Object.prototype.toString.call(obj) !== "[object Object]")
        return obj;
    
    copy = {};
    for (var key in obj)
        copy[key] = deepCopy(obj[key]);
    return copy;
};

exports.arrayToMap = function(arr) {
    var map = {};
    for (var i=0; i<arr.length; i++) {
        map[arr[i]] = 1;
    }
    return map;

};

exports.createMap = function(props) {
    var map = Object.create(null);
    for (var i in props) {
        map[i] = props[i];
    }
    return map;
};
exports.arrayRemove = function(array, value) {
  for (var i = 0; i <= array.length; i++) {
    if (value === array[i]) {
      array.splice(i, 1);
    }
  }
};

exports.escapeRegExp = function(str) {
    return str.replace(/([.*+?^${}()|[\]\/\\])/g, '\\$1');
};

exports.escapeHTML = function(str) {
    return str.replace(/&/g, "&#38;").replace(/"/g, "&#34;").replace(/'/g, "&#39;").replace(/</g, "&#60;");
};

exports.getMatchOffsets = function(string, regExp) {
    var matches = [];

    string.replace(regExp, function(str) {
        matches.push({
            offset: arguments[arguments.length-2],
            length: str.length
        });
    });

    return matches;
};
exports.deferredCall = function(fcn) {
    var timer = null;
    var callback = function() {
        timer = null;
        fcn();
    };

    var deferred = function(timeout) {
        deferred.cancel();
        timer = setTimeout(callback, timeout || 0);
        return deferred;
    };

    deferred.schedule = deferred;

    deferred.call = function() {
        this.cancel();
        fcn();
        return deferred;
    };

    deferred.cancel = function() {
        clearTimeout(timer);
        timer = null;
        return deferred;
    };
    
    deferred.isPending = function() {
        return timer;
    };

    return deferred;
};


exports.delayedCall = function(fcn, defaultTimeout) {
    var timer = null;
    var callback = function() {
        timer = null;
        fcn();
    };

    var _self = function(timeout) {
        if (timer == null)
            timer = setTimeout(callback, timeout || defaultTimeout);
    };

    _self.delay = function(timeout) {
        timer && clearTimeout(timer);
        timer = setTimeout(callback, timeout || defaultTimeout);
    };
    _self.schedule = _self;

    _self.call = function() {
        this.cancel();
        fcn();
    };

    _self.cancel = function() {
        timer && clearTimeout(timer);
        timer = null;
    };

    _self.isPending = function() {
        return timer;
    };

    return _self;
};
});

define("ace/worker/mirror",["require","exports","module","ace/range","ace/document","ace/lib/lang"], function(require, exports, module) {
"use strict";

var Range = require("../range").Range;
var Document = require("../document").Document;
var lang = require("../lib/lang");
    
var Mirror = exports.Mirror = function(sender) {
    this.sender = sender;
    var doc = this.doc = new Document("");
    
    var deferredUpdate = this.deferredUpdate = lang.delayedCall(this.onUpdate.bind(this));
    
    var _self = this;
    sender.on("change", function(e) {
        var data = e.data;
        if (data[0].start) {
            doc.applyDeltas(data);
        } else {
            for (var i = 0; i < data.length; i += 2) {
                if (Array.isArray(data[i+1])) {
                    var d = {action: "insert", start: data[i], lines: data[i+1]};
                } else {
                    var d = {action: "remove", start: data[i], end: data[i+1]};
                }
                doc.applyDelta(d, true);
            }
        }
        if (_self.$timeout)
            return deferredUpdate.schedule(_self.$timeout);
        _self.onUpdate();
    });
};

(function() {
    
    this.$timeout = 500;
    
    this.setTimeout = function(timeout) {
        this.$timeout = timeout;
    };
    
    this.setValue = function(value) {
        this.doc.setValue(value);
        this.deferredUpdate.schedule(this.$timeout);
    };
    
    this.getValue = function(callbackId) {
        this.sender.callback(this.doc.getValue(), callbackId);
    };
    
    this.onUpdate = function() {
    };
    
    this.isPending = function() {
        return this.deferredUpdate.isPending();
    };
    
}).call(Mirror.prototype);

});

define("ace/mode/coffee/coffee",["require","exports","module"], function(require, exports, module) {
function define(f) { module.exports = f() }; define.amd = {};
(function(root){var CoffeeScript=function(){function _dereq_(e){return _dereq_[e]}return _dereq_["./helpers"]=function(){var e={},t={exports:e};return function(){var t,n,i,r,s,o;e.starts=function(e,t,n){return t===e.substr(n,t.length)},e.ends=function(e,t,n){var i;return i=t.length,t===e.substr(e.length-i-(n||0),i)},e.repeat=s=function(e,t){var n;for(n="";t>0;)1&t&&(n+=e),t>>>=1,e+=e;return n},e.compact=function(e){var t,n,i,r;for(r=[],t=0,i=e.length;i>t;t++)n=e[t],n&&r.push(n);return r},e.count=function(e,t){var n,i;if(n=i=0,!t.length)return 1/0;for(;i=1+e.indexOf(t,i);)n++;return n},e.merge=function(e,t){return n(n({},e),t)},n=e.extend=function(e,t){var n,i;for(n in t)i=t[n],e[n]=i;return e},e.flatten=i=function(e){var t,n,r,s;for(n=[],r=0,s=e.length;s>r;r++)t=e[r],t instanceof Array?n=n.concat(i(t)):n.push(t);return n},e.del=function(e,t){var n;return n=e[t],delete e[t],n},e.some=null!=(r=Array.prototype.some)?r:function(e){var t,n,i;for(n=0,i=this.length;i>n;n++)if(t=this[n],e(t))return!0;return!1},e.invertLiterate=function(e){var t,n,i;return i=!0,n=function(){var n,r,s,o;for(s=e.split("\n"),o=[],n=0,r=s.length;r>n;n++)t=s[n],i&&/^([ ]{4}|[ ]{0,3}\t)/.test(t)?o.push(t):(i=/^\s*$/.test(t))?o.push(t):o.push("# "+t);return o}(),n.join("\n")},t=function(e,t){return t?{first_line:e.first_line,first_column:e.first_column,last_line:t.last_line,last_column:t.last_column}:e},e.addLocationDataFn=function(e,n){return function(i){return"object"==typeof i&&i.updateLocationDataIfMissing&&i.updateLocationDataIfMissing(t(e,n)),i}},e.locationDataToString=function(e){var t;return"2"in e&&"first_line"in e[2]?t=e[2]:"first_line"in e&&(t=e),t?t.first_line+1+":"+(t.first_column+1)+"-"+(t.last_line+1+":"+(t.last_column+1)):"No location data"},e.baseFileName=function(e,t,n){var i,r;return null==t&&(t=!1),null==n&&(n=!1),r=n?/\\|\//:/\//,i=e.split(r),e=i[i.length-1],t&&e.indexOf(".")>=0?(i=e.split("."),i.pop(),"coffee"===i[i.length-1]&&i.length>1&&i.pop(),i.join(".")):e},e.isCoffee=function(e){return/\.((lit)?coffee|coffee\.md)$/.test(e)},e.isLiterate=function(e){return/\.(litcoffee|coffee\.md)$/.test(e)},e.throwSyntaxError=function(e,t){var n;throw n=new SyntaxError(e),n.location=t,n.toString=o,n.stack=""+n,n},e.updateSyntaxError=function(e,t,n){return e.toString===o&&(e.code||(e.code=t),e.filename||(e.filename=n),e.stack=""+e),e},o=function(){var e,t,n,i,r,o,a,c,h,l,u,p,d,f,m;return this.code&&this.location?(u=this.location,a=u.first_line,o=u.first_column,h=u.last_line,c=u.last_column,null==h&&(h=a),null==c&&(c=o),r=this.filename||"[stdin]",e=this.code.split("\n")[a],m=o,i=a===h?c+1:e.length,l=e.slice(0,m).replace(/[^\s]/g," ")+s("^",i-m),"undefined"!=typeof process&&null!==process&&(n=(null!=(p=process.stdout)?p.isTTY:void 0)&&!(null!=(d=process.env)?d.NODE_DISABLE_COLORS:void 0)),(null!=(f=this.colorful)?f:n)&&(t=function(e){return"[1;31m"+e+"[0m"},e=e.slice(0,m)+t(e.slice(m,i))+e.slice(i),l=t(l)),r+":"+(a+1)+":"+(o+1)+": error: "+this.message+"\n"+e+"\n"+l):Error.prototype.toString.call(this)},e.nameWhitespaceCharacter=function(e){switch(e){case" ":return"space";case"\n":return"newline";case"\r":return"carriage return";case"	":return"tab";default:return e}}}.call(this),t.exports}(),_dereq_["./rewriter"]=function(){var e={},t={exports:e};return function(){var t,n,i,r,s,o,a,c,h,l,u,p,d,f,m,g,v,y,b,k=[].indexOf||function(e){for(var t=0,n=this.length;n>t;t++)if(t in this&&this[t]===e)return t;return-1},w=[].slice;for(f=function(e,t,n){var i;return i=[e,t],i.generated=!0,n&&(i.origin=n),i},e.Rewriter=function(){function e(){}return e.prototype.rewrite=function(e){return this.tokens=e,this.removeLeadingNewlines(),this.closeOpenCalls(),this.closeOpenIndexes(),this.normalizeLines(),this.tagPostfixConditionals(),this.addImplicitBracesAndParens(),this.addLocationDataToGeneratedTokens(),this.tokens},e.prototype.scanTokens=function(e){var t,n,i;for(i=this.tokens,t=0;n=i[t];)t+=e.call(this,n,t,i);return!0},e.prototype.detectEnd=function(e,t,n){var i,o,a,c,h;for(h=this.tokens,i=0;c=h[e];){if(0===i&&t.call(this,c,e))return n.call(this,c,e);if(!c||0>i)return n.call(this,c,e-1);o=c[0],k.call(s,o)>=0?i+=1:(a=c[0],k.call(r,a)>=0&&(i-=1)),e+=1}return e-1},e.prototype.removeLeadingNewlines=function(){var e,t,n,i,r;for(i=this.tokens,e=t=0,n=i.length;n>t&&(r=i[e][0],"TERMINATOR"===r);e=++t);return e?this.tokens.splice(0,e):void 0},e.prototype.closeOpenCalls=function(){var e,t;return t=function(e,t){var n;return")"===(n=e[0])||"CALL_END"===n||"OUTDENT"===e[0]&&")"===this.tag(t-1)},e=function(e,t){return this.tokens["OUTDENT"===e[0]?t-1:t][0]="CALL_END"},this.scanTokens(function(n,i){return"CALL_START"===n[0]&&this.detectEnd(i+1,t,e),1})},e.prototype.closeOpenIndexes=function(){var e,t;return t=function(e){var t;return"]"===(t=e[0])||"INDEX_END"===t},e=function(e){return e[0]="INDEX_END"},this.scanTokens(function(n,i){return"INDEX_START"===n[0]&&this.detectEnd(i+1,t,e),1})},e.prototype.indexOfTag=function(){var e,t,n,i,r,s,o;for(t=arguments[0],r=arguments.length>=2?w.call(arguments,1):[],e=0,n=i=0,s=r.length;s>=0?s>i:i>s;n=s>=0?++i:--i){for(;"HERECOMMENT"===this.tag(t+n+e);)e+=2;if(null!=r[n]&&("string"==typeof r[n]&&(r[n]=[r[n]]),o=this.tag(t+n+e),0>k.call(r[n],o)))return-1}return t+n+e-1},e.prototype.looksObjectish=function(e){var t,n;return this.indexOfTag(e,"@",null,":")>-1||this.indexOfTag(e,null,":")>-1?!0:(n=this.indexOfTag(e,s),n>-1&&(t=null,this.detectEnd(n+1,function(e){var t;return t=e[0],k.call(r,t)>=0},function(e,n){return t=n}),":"===this.tag(t+1))?!0:!1)},e.prototype.findTagsBackwards=function(e,t){var n,i,o,a,c,h,l;for(n=[];e>=0&&(n.length||(a=this.tag(e),0>k.call(t,a)&&(c=this.tag(e),0>k.call(s,c)||this.tokens[e].generated)&&(h=this.tag(e),0>k.call(u,h))));)i=this.tag(e),k.call(r,i)>=0&&n.push(this.tag(e)),o=this.tag(e),k.call(s,o)>=0&&n.length&&n.pop(),e-=1;return l=this.tag(e),k.call(t,l)>=0},e.prototype.addImplicitBracesAndParens=function(){var e,t;return e=[],t=null,this.scanTokens(function(i,l,p){var d,m,g,v,y,b,w,T,C,E,F,N,L,x,S,D,R,A,I,_,O,$,j,M,B,V,P,U;if(U=i[0],F=(N=l>0?p[l-1]:[])[0],C=(p.length-1>l?p[l+1]:[])[0],j=function(){return e[e.length-1]},M=l,g=function(e){return l-M+e},v=function(){var e,t;return null!=(e=j())?null!=(t=e[2])?t.ours:void 0:void 0},y=function(){var e;return v()&&"("===(null!=(e=j())?e[0]:void 0)},w=function(){var e;return v()&&"{"===(null!=(e=j())?e[0]:void 0)},b=function(){var e;return v&&"CONTROL"===(null!=(e=j())?e[0]:void 0)},B=function(t){var n;return n=null!=t?t:l,e.push(["(",n,{ours:!0}]),p.splice(n,0,f("CALL_START","(")),null==t?l+=1:void 0},d=function(){return e.pop(),p.splice(l,0,f("CALL_END",")",["","end of input",i[2]])),l+=1},V=function(t,n){var r,s;return null==n&&(n=!0),r=null!=t?t:l,e.push(["{",r,{sameLine:!0,startsLine:n,ours:!0}]),s=new String("{"),s.generated=!0,p.splice(r,0,f("{",s,i)),null==t?l+=1:void 0},m=function(t){return t=null!=t?t:l,e.pop(),p.splice(t,0,f("}","}",i)),l+=1},y()&&("IF"===U||"TRY"===U||"FINALLY"===U||"CATCH"===U||"CLASS"===U||"SWITCH"===U))return e.push(["CONTROL",l,{ours:!0}]),g(1);if("INDENT"===U&&v()){if("=>"!==F&&"->"!==F&&"["!==F&&"("!==F&&","!==F&&"{"!==F&&"TRY"!==F&&"ELSE"!==F&&"="!==F)for(;y();)d();return b()&&e.pop(),e.push([U,l]),g(1)}if(k.call(s,U)>=0)return e.push([U,l]),g(1);if(k.call(r,U)>=0){for(;v();)y()?d():w()?m():e.pop();t=e.pop()}if((k.call(c,U)>=0&&i.spaced||"?"===U&&l>0&&!p[l-1].spaced)&&(k.call(o,C)>=0||k.call(h,C)>=0&&!(null!=(L=p[l+1])?L.spaced:void 0)&&!(null!=(x=p[l+1])?x.newLine:void 0)))return"?"===U&&(U=i[0]="FUNC_EXIST"),B(l+1),g(2);if(k.call(c,U)>=0&&this.indexOfTag(l+1,"INDENT")>-1&&this.looksObjectish(l+2)&&!this.findTagsBackwards(l,["CLASS","EXTENDS","IF","CATCH","SWITCH","LEADING_WHEN","FOR","WHILE","UNTIL"]))return B(l+1),e.push(["INDENT",l+2]),g(3);if(":"===U){for(I=function(){var e;switch(!1){case e=this.tag(l-1),0>k.call(r,e):return t[1];case"@"!==this.tag(l-2):return l-2;default:return l-1}}.call(this);"HERECOMMENT"===this.tag(I-2);)I-=2;return this.insideForDeclaration="FOR"===C,P=0===I||(S=this.tag(I-1),k.call(u,S)>=0)||p[I-1].newLine,j()&&(D=j(),$=D[0],O=D[1],("{"===$||"INDENT"===$&&"{"===this.tag(O-1))&&(P||","===this.tag(I-1)||"{"===this.tag(I-1)))?g(1):(V(I,!!P),g(2))}if(w()&&k.call(u,U)>=0&&(j()[2].sameLine=!1),T="OUTDENT"===F||N.newLine,k.call(a,U)>=0||k.call(n,U)>=0&&T)for(;v();)if(R=j(),$=R[0],O=R[1],A=R[2],_=A.sameLine,P=A.startsLine,y()&&","!==F)d();else if(w()&&!this.insideForDeclaration&&_&&"TERMINATOR"!==U&&":"!==F)m();else{if(!w()||"TERMINATOR"!==U||","===F||P&&this.looksObjectish(l+1))break;if("HERECOMMENT"===C)return g(1);m()}if(!(","!==U||this.looksObjectish(l+1)||!w()||this.insideForDeclaration||"TERMINATOR"===C&&this.looksObjectish(l+2)))for(E="OUTDENT"===C?1:0;w();)m(l+E);return g(1)})},e.prototype.addLocationDataToGeneratedTokens=function(){return this.scanTokens(function(e,t,n){var i,r,s,o,a,c;return e[2]?1:e.generated||e.explicit?("{"===e[0]&&(s=null!=(a=n[t+1])?a[2]:void 0)?(r=s.first_line,i=s.first_column):(o=null!=(c=n[t-1])?c[2]:void 0)?(r=o.last_line,i=o.last_column):r=i=0,e[2]={first_line:r,first_column:i,last_line:r,last_column:i},1):1})},e.prototype.normalizeLines=function(){var e,t,r,s,o;return o=r=s=null,t=function(e,t){var r,s,a,c;return";"!==e[1]&&(r=e[0],k.call(p,r)>=0)&&!("TERMINATOR"===e[0]&&(s=this.tag(t+1),k.call(i,s)>=0))&&!("ELSE"===e[0]&&"THEN"!==o)&&!!("CATCH"!==(a=e[0])&&"FINALLY"!==a||"->"!==o&&"=>"!==o)||(c=e[0],k.call(n,c)>=0&&this.tokens[t-1].newLine)},e=function(e,t){return this.tokens.splice(","===this.tag(t-1)?t-1:t,0,s)},this.scanTokens(function(n,a,c){var h,l,u,p,f,m;if(m=n[0],"TERMINATOR"===m){if("ELSE"===this.tag(a+1)&&"OUTDENT"!==this.tag(a-1))return c.splice.apply(c,[a,1].concat(w.call(this.indentation()))),1;if(u=this.tag(a+1),k.call(i,u)>=0)return c.splice(a,1),0}if("CATCH"===m)for(h=l=1;2>=l;h=++l)if("OUTDENT"===(p=this.tag(a+h))||"TERMINATOR"===p||"FINALLY"===p)return c.splice.apply(c,[a+h,0].concat(w.call(this.indentation()))),2+h;return k.call(d,m)>=0&&"INDENT"!==this.tag(a+1)&&("ELSE"!==m||"IF"!==this.tag(a+1))?(o=m,f=this.indentation(c[a]),r=f[0],s=f[1],"THEN"===o&&(r.fromThen=!0),c.splice(a+1,0,r),this.detectEnd(a+2,t,e),"THEN"===m&&c.splice(a,1),1):1})},e.prototype.tagPostfixConditionals=function(){var e,t,n;return n=null,t=function(e,t){var n,i;return i=e[0],n=this.tokens[t-1][0],"TERMINATOR"===i||"INDENT"===i&&0>k.call(d,n)},e=function(e){return"INDENT"!==e[0]||e.generated&&!e.fromThen?n[0]="POST_"+n[0]:void 0},this.scanTokens(function(i,r){return"IF"!==i[0]?1:(n=i,this.detectEnd(r+1,t,e),1)})},e.prototype.indentation=function(e){var t,n;return t=["INDENT",2],n=["OUTDENT",2],e?(t.generated=n.generated=!0,t.origin=n.origin=e):t.explicit=n.explicit=!0,[t,n]},e.prototype.generate=f,e.prototype.tag=function(e){var t;return null!=(t=this.tokens[e])?t[0]:void 0},e}(),t=[["(",")"],["[","]"],["{","}"],["INDENT","OUTDENT"],["CALL_START","CALL_END"],["PARAM_START","PARAM_END"],["INDEX_START","INDEX_END"],["STRING_START","STRING_END"],["REGEX_START","REGEX_END"]],e.INVERSES=l={},s=[],r=[],m=0,v=t.length;v>m;m++)y=t[m],g=y[0],b=y[1],s.push(l[b]=g),r.push(l[g]=b);i=["CATCH","THEN","ELSE","FINALLY"].concat(r),c=["IDENTIFIER","SUPER",")","CALL_END","]","INDEX_END","@","THIS"],o=["IDENTIFIER","NUMBER","STRING","STRING_START","JS","REGEX","REGEX_START","NEW","PARAM_START","CLASS","IF","TRY","SWITCH","THIS","BOOL","NULL","UNDEFINED","UNARY","YIELD","UNARY_MATH","SUPER","THROW","@","->","=>","[","(","{","--","++"],h=["+","-"],a=["POST_IF","FOR","WHILE","UNTIL","WHEN","BY","LOOP","TERMINATOR"],d=["ELSE","->","=>","TRY","FINALLY","THEN"],p=["TERMINATOR","CATCH","FINALLY","ELSE","OUTDENT","LEADING_WHEN"],u=["TERMINATOR","INDENT","OUTDENT"],n=[".","?.","::","?::"]}.call(this),t.exports}(),_dereq_["./lexer"]=function(){var e={},t={exports:e};return function(){var t,n,i,r,s,o,a,c,h,l,u,p,d,f,m,g,v,y,b,k,w,T,C,E,F,N,L,x,S,D,R,A,I,_,O,$,j,M,B,V,P,U,G,H,q,X,W,Y,K,z,J,Q,Z,et,tt,nt,it,rt,st,ot,at,ct,ht,lt,ut=[].indexOf||function(e){for(var t=0,n=this.length;n>t;t++)if(t in this&&this[t]===e)return t;return-1};ot=_dereq_("./rewriter"),P=ot.Rewriter,w=ot.INVERSES,at=_dereq_("./helpers"),nt=at.count,ht=at.starts,tt=at.compact,ct=at.repeat,it=at.invertLiterate,st=at.locationDataToString,lt=at.throwSyntaxError,e.Lexer=S=function(){function e(){}return e.prototype.tokenize=function(e,t){var n,i,r,s;for(null==t&&(t={}),this.literate=t.literate,this.indent=0,this.baseIndent=0,this.indebt=0,this.outdebt=0,this.indents=[],this.ends=[],this.tokens=[],this.chunkLine=t.line||0,this.chunkColumn=t.column||0,e=this.clean(e),r=0;this.chunk=e.slice(r);)if(n=this.identifierToken()||this.commentToken()||this.whitespaceToken()||this.lineToken()||this.stringToken()||this.numberToken()||this.regexToken()||this.jsToken()||this.literalToken(),s=this.getLineAndColumnFromChunk(n),this.chunkLine=s[0],this.chunkColumn=s[1],r+=n,t.untilBalanced&&0===this.ends.length)return{tokens:this.tokens,index:r};return this.closeIndentation(),(i=this.ends.pop())&&this.error("missing "+i.tag,i.origin[2]),t.rewrite===!1?this.tokens:(new P).rewrite(this.tokens)},e.prototype.clean=function(e){return e.charCodeAt(0)===t&&(e=e.slice(1)),e=e.replace(/\r/g,"").replace(z,""),et.test(e)&&(e="\n"+e,this.chunkLine--),this.literate&&(e=it(e)),e},e.prototype.identifierToken=function(){var e,t,n,i,r,c,h,l,u,p,d,f,m,g,y,b;return(l=v.exec(this.chunk))?(h=l[0],r=l[1],t=l[2],c=r.length,u=void 0,"own"===r&&"FOR"===this.tag()?(this.token("OWN",r),r.length):"from"===r&&"YIELD"===this.tag()?(this.token("FROM",r),r.length):(d=this.tokens,p=d[d.length-1],i=t||null!=p&&("."===(f=p[0])||"?."===f||"::"===f||"?::"===f||!p.spaced&&"@"===p[0]),y="IDENTIFIER",!i&&(ut.call(E,r)>=0||ut.call(a,r)>=0)&&(y=r.toUpperCase(),"WHEN"===y&&(m=this.tag(),ut.call(N,m)>=0)?y="LEADING_WHEN":"FOR"===y?this.seenFor=!0:"UNLESS"===y?y="IF":ut.call(J,y)>=0?y="UNARY":ut.call(B,y)>=0&&("INSTANCEOF"!==y&&this.seenFor?(y="FOR"+y,this.seenFor=!1):(y="RELATION","!"===this.value()&&(u=this.tokens.pop(),r="!"+r)))),ut.call(C,r)>=0&&(i?(y="IDENTIFIER",r=new String(r),r.reserved=!0):ut.call(V,r)>=0&&this.error("reserved word '"+r+"'",{length:r.length})),i||(ut.call(s,r)>=0&&(e=r,r=o[r]),y=function(){switch(r){case"!":return"UNARY";case"==":case"!=":return"COMPARE";case"&&":case"||":return"LOGIC";case"true":case"false":return"BOOL";case"break":case"continue":return"STATEMENT";default:return y}}()),b=this.token(y,r,0,c),e&&(b.origin=[y,e,b[2]]),b.variable=!i,u&&(g=[u[2].first_line,u[2].first_column],b[2].first_line=g[0],b[2].first_column=g[1]),t&&(n=h.lastIndexOf(":"),this.token(":",":",n,t.length)),h.length)):0},e.prototype.numberToken=function(){var e,t,n,i,r;return(n=I.exec(this.chunk))?(i=n[0],t=i.length,/^0[BOX]/.test(i)?this.error("radix prefix in '"+i+"' must be lowercase",{offset:1}):/E/.test(i)&&!/^0x/.test(i)?this.error("exponential notation in '"+i+"' must be indicated with a lowercase 'e'",{offset:i.indexOf("E")}):/^0\d*[89]/.test(i)?this.error("decimal literal '"+i+"' must not be prefixed with '0'",{length:t}):/^0\d+/.test(i)&&this.error("octal literal '"+i+"' must be prefixed with '0o'",{length:t}),(r=/^0o([0-7]+)/.exec(i))&&(i="0x"+parseInt(r[1],8).toString(16)),(e=/^0b([01]+)/.exec(i))&&(i="0x"+parseInt(e[1],2).toString(16)),this.token("NUMBER",i,0,t),t):0},e.prototype.stringToken=function(){var e,t,n,i,r,s,o,a,c,h,l,u,m,g,v,y;if(l=(Y.exec(this.chunk)||[])[0],!l)return 0;if(g=function(){switch(l){case"'":return W;case'"':return q;case"'''":return f;case'"""':return p}}(),s=3===l.length,u=this.matchWithInterpolations(g,l),y=u.tokens,r=u.index,e=y.length-1,n=l.charAt(0),s){for(a=null,i=function(){var e,t,n;for(n=[],o=e=0,t=y.length;t>e;o=++e)v=y[o],"NEOSTRING"===v[0]&&n.push(v[1]);return n}().join("#{}");h=d.exec(i);)t=h[1],(null===a||(m=t.length)>0&&a.length>m)&&(a=t);a&&(c=RegExp("^"+a,"gm")),this.mergeInterpolationTokens(y,{delimiter:n},function(t){return function(n,i){return n=t.formatString(n),0===i&&(n=n.replace(F,"")),i===e&&(n=n.replace(K,"")),c&&(n=n.replace(c,"")),n}}(this))}else this.mergeInterpolationTokens(y,{delimiter:n},function(t){return function(n,i){return n=t.formatString(n),n=n.replace(G,function(t,r){return 0===i&&0===r||i===e&&r+t.length===n.length?"":" "})}}(this));return r},e.prototype.commentToken=function(){var e,t,n;return(n=this.chunk.match(c))?(e=n[0],t=n[1],t&&((n=u.exec(e))&&this.error("block comments cannot contain "+n[0],{offset:n.index,length:n[0].length}),t.indexOf("\n")>=0&&(t=t.replace(RegExp("\\n"+ct(" ",this.indent),"g"),"\n")),this.token("HERECOMMENT",t,0,e.length)),e.length):0},e.prototype.jsToken=function(){var e,t;return"`"===this.chunk.charAt(0)&&(e=T.exec(this.chunk))?(this.token("JS",(t=e[0]).slice(1,-1),0,t.length),t.length):0},e.prototype.regexToken=function(){var e,t,n,r,s,o,a,c,h,l,u,p,d;switch(!1){case!(o=M.exec(this.chunk)):this.error("regular expressions cannot begin with "+o[2],{offset:o.index+o[1].length});break;case!(o=this.matchWithInterpolations(m,"///")):d=o.tokens,s=o.index;break;case!(o=$.exec(this.chunk)):if(p=o[0],e=o[1],t=o[2],this.validateEscapes(e,{isRegex:!0,offsetInChunk:1}),s=p.length,h=this.tokens,c=h[h.length-1],c)if(c.spaced&&(l=c[0],ut.call(i,l)>=0)){if(!t||O.test(p))return 0}else if(u=c[0],ut.call(A,u)>=0)return 0;t||this.error("missing / (unclosed regex)");break;default:return 0}switch(r=j.exec(this.chunk.slice(s))[0],n=s+r.length,a=this.makeToken("REGEX",null,0,n),!1){case!!Z.test(r):this.error("invalid regular expression flags "+r,{offset:s,length:r.length});break;case!(p||1===d.length):null==e&&(e=this.formatHeregex(d[0][1])),this.token("REGEX",""+this.makeDelimitedLiteral(e,{delimiter:"/"})+r,0,n,a);break;default:this.token("REGEX_START","(",0,0,a),this.token("IDENTIFIER","RegExp",0,0),this.token("CALL_START","(",0,0),this.mergeInterpolationTokens(d,{delimiter:'"',"double":!0},this.formatHeregex),r&&(this.token(",",",",s,0),this.token("STRING",'"'+r+'"',s,r.length)),this.token(")",")",n,0),this.token("REGEX_END",")",n,0)}return n},e.prototype.lineToken=function(){var e,t,n,i,r;if(!(n=R.exec(this.chunk)))return 0;if(t=n[0],this.seenFor=!1,r=t.length-1-t.lastIndexOf("\n"),i=this.unfinished(),r-this.indebt===this.indent)return i?this.suppressNewlines():this.newlineToken(0),t.length;if(r>this.indent){if(i)return this.indebt=r-this.indent,this.suppressNewlines(),t.length;if(!this.tokens.length)return this.baseIndent=this.indent=r,t.length;e=r-this.indent+this.outdebt,this.token("INDENT",e,t.length-r,r),this.indents.push(e),this.ends.push({tag:"OUTDENT"}),this.outdebt=this.indebt=0,this.indent=r}else this.baseIndent>r?this.error("missing indentation",{offset:t.length}):(this.indebt=0,this.outdentToken(this.indent-r,i,t.length));return t.length},e.prototype.outdentToken=function(e,t,n){var i,r,s,o;for(i=this.indent-e;e>0;)s=this.indents[this.indents.length-1],s?s===this.outdebt?(e-=this.outdebt,this.outdebt=0):this.outdebt>s?(this.outdebt-=s,e-=s):(r=this.indents.pop()+this.outdebt,n&&(o=this.chunk[n],ut.call(y,o)>=0)&&(i-=r-e,e=r),this.outdebt=0,this.pair("OUTDENT"),this.token("OUTDENT",e,0,n),e-=r):e=0;for(r&&(this.outdebt-=e);";"===this.value();)this.tokens.pop();return"TERMINATOR"===this.tag()||t||this.token("TERMINATOR","\n",n,0),this.indent=i,this},e.prototype.whitespaceToken=function(){var e,t,n,i;return(e=et.exec(this.chunk))||(t="\n"===this.chunk.charAt(0))?(i=this.tokens,n=i[i.length-1],n&&(n[e?"spaced":"newLine"]=!0),e?e[0].length:0):0},e.prototype.newlineToken=function(e){for(;";"===this.value();)this.tokens.pop();return"TERMINATOR"!==this.tag()&&this.token("TERMINATOR","\n",e,0),this},e.prototype.suppressNewlines=function(){return"\\"===this.value()&&this.tokens.pop(),this},e.prototype.literalToken=function(){var e,t,n,s,o,a,c,u,p,d;if((e=_.exec(this.chunk))?(d=e[0],r.test(d)&&this.tagParameters()):d=this.chunk.charAt(0),u=d,n=this.tokens,t=n[n.length-1],"="===d&&t&&(!t[1].reserved&&(s=t[1],ut.call(C,s)>=0)&&(t.origin&&(t=t.origin),this.error("reserved word '"+t[1]+"' can't be assigned",t[2])),"||"===(o=t[1])||"&&"===o))return t[0]="COMPOUND_ASSIGN",t[1]+="=",d.length;if(";"===d)this.seenFor=!1,u="TERMINATOR";else if(ut.call(D,d)>=0)u="MATH";else if(ut.call(h,d)>=0)u="COMPARE";else if(ut.call(l,d)>=0)u="COMPOUND_ASSIGN";else if(ut.call(J,d)>=0)u="UNARY";else if(ut.call(Q,d)>=0)u="UNARY_MATH";else if(ut.call(U,d)>=0)u="SHIFT";else if(ut.call(x,d)>=0||"?"===d&&(null!=t?t.spaced:void 0))u="LOGIC";else if(t&&!t.spaced)if("("===d&&(a=t[0],ut.call(i,a)>=0))"?"===t[0]&&(t[0]="FUNC_EXIST"),u="CALL_START";else if("["===d&&(c=t[0],ut.call(b,c)>=0))switch(u="INDEX_START",t[0]){case"?":t[0]="INDEX_SOAK"}switch(p=this.makeToken(u,d),d){case"(":case"{":case"[":this.ends.push({tag:w[d],origin:p});break;case")":case"}":case"]":this.pair(d)}return this.tokens.push(p),d.length},e.prototype.tagParameters=function(){var e,t,n,i;if(")"!==this.tag())return this;for(t=[],i=this.tokens,e=i.length,i[--e][0]="PARAM_END";n=i[--e];)switch(n[0]){case")":t.push(n);break;case"(":case"CALL_START":if(!t.length)return"("===n[0]?(n[0]="PARAM_START",this):this;t.pop()}return this},e.prototype.closeIndentation=function(){return this.outdentToken(this.indent)},e.prototype.matchWithInterpolations=function(t,n){var i,r,s,o,a,c,h,l,u,p,d,f,m,g,v;if(v=[],l=n.length,this.chunk.slice(0,l)!==n)return null;for(m=this.chunk.slice(l);;){if(g=t.exec(m)[0],this.validateEscapes(g,{isRegex:"/"===n.charAt(0),offsetInChunk:l}),v.push(this.makeToken("NEOSTRING",g,l)),m=m.slice(g.length),l+=g.length,"#{"!==m.slice(0,2))break;p=this.getLineAndColumnFromChunk(l+1),c=p[0],r=p[1],d=(new e).tokenize(m.slice(1),{line:c,column:r,untilBalanced:!0}),h=d.tokens,o=d.index,o+=1,u=h[0],i=h[h.length-1],u[0]=u[1]="(",i[0]=i[1]=")",i.origin=["","end of interpolation",i[2]],"TERMINATOR"===(null!=(f=h[1])?f[0]:void 0)&&h.splice(1,1),v.push(["TOKENS",h]),m=m.slice(o),l+=o}return m.slice(0,n.length)!==n&&this.error("missing "+n,{length:n.length}),s=v[0],a=v[v.length-1],s[2].first_column-=n.length,a[2].last_column+=n.length,0===a[1].length&&(a[2].last_column-=1),{tokens:v,index:l+n.length}},e.prototype.mergeInterpolationTokens=function(e,t,n){var i,r,s,o,a,c,h,l,u,p,d,f,m,g,v,y;for(e.length>1&&(u=this.token("STRING_START","(",0,0)),s=this.tokens.length,o=a=0,h=e.length;h>a;o=++a){switch(g=e[o],m=g[0],y=g[1],m){case"TOKENS":if(2===y.length)continue;l=y[0],v=y;break;case"NEOSTRING":if(i=n(g[1],o),0===i.length){if(0!==o)continue;r=this.tokens.length}2===o&&null!=r&&this.tokens.splice(r,2),g[0]="STRING",g[1]=this.makeDelimitedLiteral(i,t),l=g,v=[g]}this.tokens.length>s&&(p=this.token("+","+"),p[2]={first_line:l[2].first_line,first_column:l[2].first_column,last_line:l[2].first_line,last_column:l[2].first_column}),(d=this.tokens).push.apply(d,v)}return u?(c=e[e.length-1],u.origin=["STRING",null,{first_line:u[2].first_line,first_column:u[2].first_column,last_line:c[2].last_line,last_column:c[2].last_column}],f=this.token("STRING_END",")"),f[2]={first_line:c[2].last_line,first_column:c[2].last_column,last_line:c[2].last_line,last_column:c[2].last_column}):void 0},e.prototype.pair=function(e){var t,n,i,r,s;return i=this.ends,n=i[i.length-1],e!==(s=null!=n?n.tag:void 0)?("OUTDENT"!==s&&this.error("unmatched "+e),r=this.indents,t=r[r.length-1],this.outdentToken(t,!0),this.pair(e)):this.ends.pop()},e.prototype.getLineAndColumnFromChunk=function(e){var t,n,i,r,s;return 0===e?[this.chunkLine,this.chunkColumn]:(s=e>=this.chunk.length?this.chunk:this.chunk.slice(0,+(e-1)+1||9e9),i=nt(s,"\n"),t=this.chunkColumn,i>0?(r=s.split("\n"),n=r[r.length-1],t=n.length):t+=s.length,[this.chunkLine+i,t])},e.prototype.makeToken=function(e,t,n,i){var r,s,o,a,c;return null==n&&(n=0),null==i&&(i=t.length),s={},o=this.getLineAndColumnFromChunk(n),s.first_line=o[0],s.first_column=o[1],r=Math.max(0,i-1),a=this.getLineAndColumnFromChunk(n+r),s.last_line=a[0],s.last_column=a[1],c=[e,t,s]},e.prototype.token=function(e,t,n,i,r){var s;return s=this.makeToken(e,t,n,i),r&&(s.origin=r),this.tokens.push(s),s},e.prototype.tag=function(){var e,t;return e=this.tokens,t=e[e.length-1],null!=t?t[0]:void 0},e.prototype.value=function(){var e,t;return e=this.tokens,t=e[e.length-1],null!=t?t[1]:void 0},e.prototype.unfinished=function(){var e;return L.test(this.chunk)||"\\"===(e=this.tag())||"."===e||"?."===e||"?::"===e||"UNARY"===e||"MATH"===e||"UNARY_MATH"===e||"+"===e||"-"===e||"YIELD"===e||"**"===e||"SHIFT"===e||"RELATION"===e||"COMPARE"===e||"LOGIC"===e||"THROW"===e||"EXTENDS"===e},e.prototype.formatString=function(e){return e.replace(X,"$1")},e.prototype.formatHeregex=function(e){return e.replace(g,"$1$2")},e.prototype.validateEscapes=function(e,t){var n,i,r,s,o,a,c,h;return null==t&&(t={}),s=k.exec(e),!s||(s[0],n=s[1],a=s[2],i=s[3],h=s[4],t.isRegex&&a&&"0"!==a.charAt(0))?void 0:(o=a?"octal escape sequences are not allowed":"invalid escape sequence",r="\\"+(a||i||h),this.error(o+" "+r,{offset:(null!=(c=t.offsetInChunk)?c:0)+s.index+n.length,length:r.length}))},e.prototype.makeDelimitedLiteral=function(e,t){var n;return null==t&&(t={}),""===e&&"/"===t.delimiter&&(e="(?:)"),n=RegExp("(\\\\\\\\)|(\\\\0(?=[1-7]))|\\\\?("+t.delimiter+")|\\\\?(?:(\\n)|(\\r)|(\\u2028)|(\\u2029))|(\\\\.)","g"),e=e.replace(n,function(e,n,i,r,s,o,a,c,h){switch(!1){case!n:return t.double?n+n:n;case!i:return"\\x00";case!r:return"\\"+r;case!s:return"\\n";case!o:return"\\r";case!a:return"\\u2028";case!c:return"\\u2029";case!h:return t.double?"\\"+h:h}}),""+t.delimiter+e+t.delimiter},e.prototype.error=function(e,t){var n,i,r,s,o,a;return null==t&&(t={}),r="first_line"in t?t:(o=this.getLineAndColumnFromChunk(null!=(s=t.offset)?s:0),i=o[0],n=o[1],o,{first_line:i,first_column:n,last_column:n+(null!=(a=t.length)?a:1)-1}),lt(e,r)},e}(),E=["true","false","null","this","new","delete","typeof","in","instanceof","return","throw","break","continue","debugger","yield","if","else","switch","for","while","do","try","catch","finally","class","extends","super"],a=["undefined","then","unless","until","loop","of","by","when"],o={and:"&&",or:"||",is:"==",isnt:"!=",not:"!",yes:"true",no:"false",on:"true",off:"false"},s=function(){var e;e=[];for(rt in o)e.push(rt);return e}(),a=a.concat(s),V=["case","default","function","var","void","with","const","let","enum","export","import","native","implements","interface","package","private","protected","public","static"],H=["arguments","eval","yield*"],C=E.concat(V).concat(H),e.RESERVED=V.concat(E).concat(a).concat(H),e.STRICT_PROSCRIBED=H,t=65279,v=/^(?!\d)((?:(?!\s)[$\w\x7f-\uffff])+)([^\n\S]*:(?!:))?/,I=/^0b[01]+|^0o[0-7]+|^0x[\da-f]+|^\d*\.?\d+(?:e[+-]?\d+)?/i,_=/^(?:[-=]>|[-+*\/%<>&|^!?=]=|>>>=?|([-+:])\1|([&|<>*\/%])\2=?|\?(\.|::)|\.{2,3})/,et=/^[^\n\S]+/,c=/^###([^#][\s\S]*?)(?:###[^\n\S]*|###$)|^(?:\s*#(?!##[^#]).*)+/,r=/^[-=]>/,R=/^(?:\n[^\n\S]*)+/,T=/^`[^\\`]*(?:\\.[^\\`]*)*`/,Y=/^(?:'''|"""|'|")/,W=/^(?:[^\\']|\\[\s\S])*/,q=/^(?:[^\\"#]|\\[\s\S]|\#(?!\{))*/,f=/^(?:[^\\']|\\[\s\S]|'(?!''))*/,p=/^(?:[^\\"#]|\\[\s\S]|"(?!"")|\#(?!\{))*/,X=/((?:\\\\)+)|\\[^\S\n]*\n\s*/g,G=/\s*\n\s*/g,d=/\n+([^\n\S]*)(?=\S)/g,$=/^\/(?!\/)((?:[^[\/\n\\]|\\[^\n]|\[(?:\\[^\n]|[^\]\n\\])*\])*)(\/)?/,j=/^\w*/,Z=/^(?!.*(.).*\1)[imgy]*$/,m=/^(?:[^\\\/#]|\\[\s\S]|\/(?!\/\/)|\#(?!\{))*/,g=/((?:\\\\)+)|\\(\s)|\s+(?:#.*)?/g,M=/^(\/|\/{3}\s*)(\*)/,O=/^\/=?\s/,u=/\*\//,L=/^\s*(?:,|\??\.(?![.\d])|::)/,k=/((?:^|[^\\])(?:\\\\)*)\\(?:(0[0-7]|[1-7])|(x(?![\da-fA-F]{2}).{0,2})|(u(?![\da-fA-F]{4}).{0,4}))/,F=/^[^\n\S]*\n/,K=/\n[^\n\S]*$/,z=/\s+$/,l=["-=","+=","/=","*=","%=","||=","&&=","?=","<<=",">>=",">>>=","&=","^=","|=","**=","//=","%%="],J=["NEW","TYPEOF","DELETE","DO"],Q=["!","~"],x=["&&","||","&","|","^"],U=["<<",">>",">>>"],h=["==","!=","<",">","<=",">="],D=["*","/","%","//","%%"],B=["IN","OF","INSTANCEOF"],n=["TRUE","FALSE"],i=["IDENTIFIER",")","]","?","@","THIS","SUPER"],b=i.concat(["NUMBER","STRING","STRING_END","REGEX","REGEX_END","BOOL","NULL","UNDEFINED","}","::"]),A=b.concat(["++","--"]),N=["INDENT","OUTDENT","TERMINATOR"],y=[")","}","]"]}.call(this),t.exports}(),_dereq_["./parser"]=function(){var e={},t={exports:e},n=function(){function e(){this.yy={}}var t=function(e,t,n,i){for(n=n||{},i=e.length;i--;n[e[i]]=t);return n},n=[1,20],i=[1,75],r=[1,71],s=[1,76],o=[1,77],a=[1,73],c=[1,74],h=[1,50],l=[1,52],u=[1,53],p=[1,54],d=[1,55],f=[1,45],m=[1,46],g=[1,27],v=[1,60],y=[1,61],b=[1,70],k=[1,43],w=[1,26],T=[1,58],C=[1,59],E=[1,57],F=[1,38],N=[1,44],L=[1,56],x=[1,65],S=[1,66],D=[1,67],R=[1,68],A=[1,42],I=[1,64],_=[1,29],O=[1,30],$=[1,31],j=[1,32],M=[1,33],B=[1,34],V=[1,35],P=[1,78],U=[1,6,26,34,108],G=[1,88],H=[1,81],q=[1,80],X=[1,79],W=[1,82],Y=[1,83],K=[1,84],z=[1,85],J=[1,86],Q=[1,87],Z=[1,91],et=[1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,135,136,141,142,143,144,145,146,147],tt=[1,97],nt=[1,98],it=[1,99],rt=[1,100],st=[1,102],ot=[1,103],at=[1,96],ct=[2,112],ht=[1,6,25,26,34,55,60,63,72,73,74,75,77,79,80,84,90,91,92,97,99,108,110,111,112,116,117,132,135,136,141,142,143,144,145,146,147],lt=[2,79],ut=[1,108],pt=[2,58],dt=[1,112],ft=[1,117],mt=[1,118],gt=[1,120],vt=[1,6,25,26,34,46,55,60,63,72,73,74,75,77,79,80,84,90,91,92,97,99,108,110,111,112,116,117,132,135,136,141,142,143,144,145,146,147],yt=[2,76],bt=[1,6,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,135,136,141,142,143,144,145,146,147],kt=[1,155],wt=[1,157],Tt=[1,152],Ct=[1,6,25,26,34,46,55,60,63,72,73,74,75,77,79,80,84,86,90,91,92,97,99,108,110,111,112,116,117,132,135,136,139,140,141,142,143,144,145,146,147,148],Et=[2,95],Ft=[1,6,25,26,34,49,55,60,63,72,73,74,75,77,79,80,84,90,91,92,97,99,108,110,111,112,116,117,132,135,136,141,142,143,144,145,146,147],Nt=[1,6,25,26,34,46,49,55,60,63,72,73,74,75,77,79,80,84,86,90,91,92,97,99,108,110,111,112,116,117,123,124,132,135,136,139,140,141,142,143,144,145,146,147,148],Lt=[1,206],xt=[1,205],St=[1,6,25,26,34,38,55,60,63,72,73,74,75,77,79,80,84,90,91,92,97,99,108,110,111,112,116,117,132,135,136,141,142,143,144,145,146,147],Dt=[2,56],Rt=[1,216],At=[6,25,26,55,60],It=[6,25,26,46,55,60,63],_t=[1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,135,136,142,144,145,146,147],Ot=[1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132],$t=[72,73,74,75,77,80,90,91],jt=[1,235],Mt=[2,133],Bt=[1,6,25,26,34,46,55,60,63,72,73,74,75,77,79,80,84,90,91,92,97,99,108,110,111,112,116,117,123,124,132,135,136,141,142,143,144,145,146,147],Vt=[1,244],Pt=[6,25,26,60,92,97],Ut=[1,6,25,26,34,55,60,63,79,84,92,97,99,108,117,132],Gt=[1,6,25,26,34,55,60,63,79,84,92,97,99,108,111,117,132],Ht=[123,124],qt=[60,123,124],Xt=[1,255],Wt=[6,25,26,60,84],Yt=[6,25,26,49,60,84],Kt=[1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,135,136,144,145,146,147],zt=[11,28,30,32,33,36,37,40,41,42,43,44,51,52,53,57,58,79,82,85,89,94,95,96,102,106,107,110,112,114,116,125,131,133,134,135,136,137,139,140],Jt=[2,122],Qt=[6,25,26],Zt=[2,57],en=[1,268],tn=[1,269],nn=[1,6,25,26,34,55,60,63,79,84,92,97,99,104,105,108,110,111,112,116,117,127,129,132,135,136,141,142,143,144,145,146,147],rn=[26,127,129],sn=[1,6,26,34,55,60,63,79,84,92,97,99,108,111,117,132],on=[2,71],an=[1,291],cn=[1,292],hn=[1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,127,132,135,136,141,142,143,144,145,146,147],ln=[1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,112,116,117,132],un=[1,303],pn=[1,304],dn=[6,25,26,60],fn=[1,6,25,26,34,55,60,63,79,84,92,97,99,104,108,110,111,112,116,117,132,135,136,141,142,143,144,145,146,147],mn=[25,60],gn={trace:function(){},yy:{},symbols_:{error:2,Root:3,Body:4,Line:5,TERMINATOR:6,Expression:7,Statement:8,Return:9,Comment:10,STATEMENT:11,Value:12,Invocation:13,Code:14,Operation:15,Assign:16,If:17,Try:18,While:19,For:20,Switch:21,Class:22,Throw:23,Block:24,INDENT:25,OUTDENT:26,Identifier:27,IDENTIFIER:28,AlphaNumeric:29,NUMBER:30,String:31,STRING:32,STRING_START:33,STRING_END:34,Regex:35,REGEX:36,REGEX_START:37,REGEX_END:38,Literal:39,JS:40,DEBUGGER:41,UNDEFINED:42,NULL:43,BOOL:44,Assignable:45,"=":46,AssignObj:47,ObjAssignable:48,":":49,ThisProperty:50,RETURN:51,HERECOMMENT:52,PARAM_START:53,ParamList:54,PARAM_END:55,FuncGlyph:56,"->":57,"=>":58,OptComma:59,",":60,Param:61,ParamVar:62,"...":63,Array:64,Object:65,Splat:66,SimpleAssignable:67,Accessor:68,Parenthetical:69,Range:70,This:71,".":72,"?.":73,"::":74,"?::":75,Index:76,INDEX_START:77,IndexValue:78,INDEX_END:79,INDEX_SOAK:80,Slice:81,"{":82,AssignList:83,"}":84,CLASS:85,EXTENDS:86,OptFuncExist:87,Arguments:88,SUPER:89,FUNC_EXIST:90,CALL_START:91,CALL_END:92,ArgList:93,THIS:94,"@":95,"[":96,"]":97,RangeDots:98,"..":99,Arg:100,SimpleArgs:101,TRY:102,Catch:103,FINALLY:104,CATCH:105,THROW:106,"(":107,")":108,WhileSource:109,WHILE:110,WHEN:111,UNTIL:112,Loop:113,LOOP:114,ForBody:115,FOR:116,BY:117,ForStart:118,ForSource:119,ForVariables:120,OWN:121,ForValue:122,FORIN:123,FOROF:124,SWITCH:125,Whens:126,ELSE:127,When:128,LEADING_WHEN:129,IfBlock:130,IF:131,POST_IF:132,UNARY:133,UNARY_MATH:134,"-":135,"+":136,YIELD:137,FROM:138,"--":139,"++":140,"?":141,MATH:142,"**":143,SHIFT:144,COMPARE:145,LOGIC:146,RELATION:147,COMPOUND_ASSIGN:148,$accept:0,$end:1},terminals_:{2:"error",6:"TERMINATOR",11:"STATEMENT",25:"INDENT",26:"OUTDENT",28:"IDENTIFIER",30:"NUMBER",32:"STRING",33:"STRING_START",34:"STRING_END",36:"REGEX",37:"REGEX_START",38:"REGEX_END",40:"JS",41:"DEBUGGER",42:"UNDEFINED",43:"NULL",44:"BOOL",46:"=",49:":",51:"RETURN",52:"HERECOMMENT",53:"PARAM_START",55:"PARAM_END",57:"->",58:"=>",60:",",63:"...",72:".",73:"?.",74:"::",75:"?::",77:"INDEX_START",79:"INDEX_END",80:"INDEX_SOAK",82:"{",84:"}",85:"CLASS",86:"EXTENDS",89:"SUPER",90:"FUNC_EXIST",91:"CALL_START",92:"CALL_END",94:"THIS",95:"@",96:"[",97:"]",99:"..",102:"TRY",104:"FINALLY",105:"CATCH",106:"THROW",107:"(",108:")",110:"WHILE",111:"WHEN",112:"UNTIL",114:"LOOP",116:"FOR",117:"BY",121:"OWN",123:"FORIN",124:"FOROF",125:"SWITCH",127:"ELSE",129:"LEADING_WHEN",131:"IF",132:"POST_IF",133:"UNARY",134:"UNARY_MATH",135:"-",136:"+",137:"YIELD",138:"FROM",139:"--",140:"++",141:"?",142:"MATH",143:"**",144:"SHIFT",145:"COMPARE",146:"LOGIC",147:"RELATION",148:"COMPOUND_ASSIGN"},productions_:[0,[3,0],[3,1],[4,1],[4,3],[4,2],[5,1],[5,1],[8,1],[8,1],[8,1],[7,1],[7,1],[7,1],[7,1],[7,1],[7,1],[7,1],[7,1],[7,1],[7,1],[7,1],[7,1],[24,2],[24,3],[27,1],[29,1],[29,1],[31,1],[31,3],[35,1],[35,3],[39,1],[39,1],[39,1],[39,1],[39,1],[39,1],[39,1],[16,3],[16,4],[16,5],[47,1],[47,3],[47,5],[47,1],[48,1],[48,1],[48,1],[9,2],[9,1],[10,1],[14,5],[14,2],[56,1],[56,1],[59,0],[59,1],[54,0],[54,1],[54,3],[54,4],[54,6],[61,1],[61,2],[61,3],[61,1],[62,1],[62,1],[62,1],[62,1],[66,2],[67,1],[67,2],[67,2],[67,1],[45,1],[45,1],[45,1],[12,1],[12,1],[12,1],[12,1],[12,1],[68,2],[68,2],[68,2],[68,2],[68,1],[68,1],[76,3],[76,2],[78,1],[78,1],[65,4],[83,0],[83,1],[83,3],[83,4],[83,6],[22,1],[22,2],[22,3],[22,4],[22,2],[22,3],[22,4],[22,5],[13,3],[13,3],[13,1],[13,2],[87,0],[87,1],[88,2],[88,4],[71,1],[71,1],[50,2],[64,2],[64,4],[98,1],[98,1],[70,5],[81,3],[81,2],[81,2],[81,1],[93,1],[93,3],[93,4],[93,4],[93,6],[100,1],[100,1],[100,1],[101,1],[101,3],[18,2],[18,3],[18,4],[18,5],[103,3],[103,3],[103,2],[23,2],[69,3],[69,5],[109,2],[109,4],[109,2],[109,4],[19,2],[19,2],[19,2],[19,1],[113,2],[113,2],[20,2],[20,2],[20,2],[115,2],[115,4],[115,2],[118,2],[118,3],[122,1],[122,1],[122,1],[122,1],[120,1],[120,3],[119,2],[119,2],[119,4],[119,4],[119,4],[119,6],[119,6],[21,5],[21,7],[21,4],[21,6],[126,1],[126,2],[128,3],[128,4],[130,3],[130,5],[17,1],[17,3],[17,3],[17,3],[15,2],[15,2],[15,2],[15,2],[15,2],[15,2],[15,3],[15,2],[15,2],[15,2],[15,2],[15,2],[15,3],[15,3],[15,3],[15,3],[15,3],[15,3],[15,3],[15,3],[15,3],[15,5],[15,4],[15,3]],performAction:function(e,t,n,i,r,s,o){var a=s.length-1;
switch(r){case 1:return this.$=i.addLocationDataFn(o[a],o[a])(new i.Block);case 2:return this.$=s[a];case 3:this.$=i.addLocationDataFn(o[a],o[a])(i.Block.wrap([s[a]]));break;case 4:this.$=i.addLocationDataFn(o[a-2],o[a])(s[a-2].push(s[a]));break;case 5:this.$=s[a-1];break;case 6:case 7:case 8:case 9:case 11:case 12:case 13:case 14:case 15:case 16:case 17:case 18:case 19:case 20:case 21:case 22:case 27:case 32:case 34:case 45:case 46:case 47:case 48:case 56:case 57:case 67:case 68:case 69:case 70:case 75:case 76:case 79:case 83:case 89:case 133:case 134:case 136:case 166:case 167:case 183:case 189:this.$=s[a];break;case 10:case 25:case 26:case 28:case 30:case 33:case 35:this.$=i.addLocationDataFn(o[a],o[a])(new i.Literal(s[a]));break;case 23:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Block);break;case 24:case 31:case 90:this.$=i.addLocationDataFn(o[a-2],o[a])(s[a-1]);break;case 29:case 146:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Parens(s[a-1]));break;case 36:this.$=i.addLocationDataFn(o[a],o[a])(new i.Undefined);break;case 37:this.$=i.addLocationDataFn(o[a],o[a])(new i.Null);break;case 38:this.$=i.addLocationDataFn(o[a],o[a])(new i.Bool(s[a]));break;case 39:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Assign(s[a-2],s[a]));break;case 40:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.Assign(s[a-3],s[a]));break;case 41:this.$=i.addLocationDataFn(o[a-4],o[a])(new i.Assign(s[a-4],s[a-1]));break;case 42:case 72:case 77:case 78:case 80:case 81:case 82:case 168:case 169:this.$=i.addLocationDataFn(o[a],o[a])(new i.Value(s[a]));break;case 43:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Assign(i.addLocationDataFn(o[a-2])(new i.Value(s[a-2])),s[a],"object"));break;case 44:this.$=i.addLocationDataFn(o[a-4],o[a])(new i.Assign(i.addLocationDataFn(o[a-4])(new i.Value(s[a-4])),s[a-1],"object"));break;case 49:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Return(s[a]));break;case 50:this.$=i.addLocationDataFn(o[a],o[a])(new i.Return);break;case 51:this.$=i.addLocationDataFn(o[a],o[a])(new i.Comment(s[a]));break;case 52:this.$=i.addLocationDataFn(o[a-4],o[a])(new i.Code(s[a-3],s[a],s[a-1]));break;case 53:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Code([],s[a],s[a-1]));break;case 54:this.$=i.addLocationDataFn(o[a],o[a])("func");break;case 55:this.$=i.addLocationDataFn(o[a],o[a])("boundfunc");break;case 58:case 95:this.$=i.addLocationDataFn(o[a],o[a])([]);break;case 59:case 96:case 128:case 170:this.$=i.addLocationDataFn(o[a],o[a])([s[a]]);break;case 60:case 97:case 129:this.$=i.addLocationDataFn(o[a-2],o[a])(s[a-2].concat(s[a]));break;case 61:case 98:case 130:this.$=i.addLocationDataFn(o[a-3],o[a])(s[a-3].concat(s[a]));break;case 62:case 99:case 132:this.$=i.addLocationDataFn(o[a-5],o[a])(s[a-5].concat(s[a-2]));break;case 63:this.$=i.addLocationDataFn(o[a],o[a])(new i.Param(s[a]));break;case 64:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Param(s[a-1],null,!0));break;case 65:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Param(s[a-2],s[a]));break;case 66:case 135:this.$=i.addLocationDataFn(o[a],o[a])(new i.Expansion);break;case 71:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Splat(s[a-1]));break;case 73:this.$=i.addLocationDataFn(o[a-1],o[a])(s[a-1].add(s[a]));break;case 74:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Value(s[a-1],[].concat(s[a])));break;case 84:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Access(s[a]));break;case 85:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Access(s[a],"soak"));break;case 86:this.$=i.addLocationDataFn(o[a-1],o[a])([i.addLocationDataFn(o[a-1])(new i.Access(new i.Literal("prototype"))),i.addLocationDataFn(o[a])(new i.Access(s[a]))]);break;case 87:this.$=i.addLocationDataFn(o[a-1],o[a])([i.addLocationDataFn(o[a-1])(new i.Access(new i.Literal("prototype"),"soak")),i.addLocationDataFn(o[a])(new i.Access(s[a]))]);break;case 88:this.$=i.addLocationDataFn(o[a],o[a])(new i.Access(new i.Literal("prototype")));break;case 91:this.$=i.addLocationDataFn(o[a-1],o[a])(i.extend(s[a],{soak:!0}));break;case 92:this.$=i.addLocationDataFn(o[a],o[a])(new i.Index(s[a]));break;case 93:this.$=i.addLocationDataFn(o[a],o[a])(new i.Slice(s[a]));break;case 94:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.Obj(s[a-2],s[a-3].generated));break;case 100:this.$=i.addLocationDataFn(o[a],o[a])(new i.Class);break;case 101:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Class(null,null,s[a]));break;case 102:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Class(null,s[a]));break;case 103:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.Class(null,s[a-1],s[a]));break;case 104:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Class(s[a]));break;case 105:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Class(s[a-1],null,s[a]));break;case 106:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.Class(s[a-2],s[a]));break;case 107:this.$=i.addLocationDataFn(o[a-4],o[a])(new i.Class(s[a-3],s[a-1],s[a]));break;case 108:case 109:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Call(s[a-2],s[a],s[a-1]));break;case 110:this.$=i.addLocationDataFn(o[a],o[a])(new i.Call("super",[new i.Splat(new i.Literal("arguments"))]));break;case 111:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Call("super",s[a]));break;case 112:this.$=i.addLocationDataFn(o[a],o[a])(!1);break;case 113:this.$=i.addLocationDataFn(o[a],o[a])(!0);break;case 114:this.$=i.addLocationDataFn(o[a-1],o[a])([]);break;case 115:case 131:this.$=i.addLocationDataFn(o[a-3],o[a])(s[a-2]);break;case 116:case 117:this.$=i.addLocationDataFn(o[a],o[a])(new i.Value(new i.Literal("this")));break;case 118:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Value(i.addLocationDataFn(o[a-1])(new i.Literal("this")),[i.addLocationDataFn(o[a])(new i.Access(s[a]))],"this"));break;case 119:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Arr([]));break;case 120:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.Arr(s[a-2]));break;case 121:this.$=i.addLocationDataFn(o[a],o[a])("inclusive");break;case 122:this.$=i.addLocationDataFn(o[a],o[a])("exclusive");break;case 123:this.$=i.addLocationDataFn(o[a-4],o[a])(new i.Range(s[a-3],s[a-1],s[a-2]));break;case 124:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Range(s[a-2],s[a],s[a-1]));break;case 125:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Range(s[a-1],null,s[a]));break;case 126:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Range(null,s[a],s[a-1]));break;case 127:this.$=i.addLocationDataFn(o[a],o[a])(new i.Range(null,null,s[a]));break;case 137:this.$=i.addLocationDataFn(o[a-2],o[a])([].concat(s[a-2],s[a]));break;case 138:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Try(s[a]));break;case 139:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Try(s[a-1],s[a][0],s[a][1]));break;case 140:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.Try(s[a-2],null,null,s[a]));break;case 141:this.$=i.addLocationDataFn(o[a-4],o[a])(new i.Try(s[a-3],s[a-2][0],s[a-2][1],s[a]));break;case 142:this.$=i.addLocationDataFn(o[a-2],o[a])([s[a-1],s[a]]);break;case 143:this.$=i.addLocationDataFn(o[a-2],o[a])([i.addLocationDataFn(o[a-1])(new i.Value(s[a-1])),s[a]]);break;case 144:this.$=i.addLocationDataFn(o[a-1],o[a])([null,s[a]]);break;case 145:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Throw(s[a]));break;case 147:this.$=i.addLocationDataFn(o[a-4],o[a])(new i.Parens(s[a-2]));break;case 148:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.While(s[a]));break;case 149:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.While(s[a-2],{guard:s[a]}));break;case 150:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.While(s[a],{invert:!0}));break;case 151:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.While(s[a-2],{invert:!0,guard:s[a]}));break;case 152:this.$=i.addLocationDataFn(o[a-1],o[a])(s[a-1].addBody(s[a]));break;case 153:case 154:this.$=i.addLocationDataFn(o[a-1],o[a])(s[a].addBody(i.addLocationDataFn(o[a-1])(i.Block.wrap([s[a-1]]))));break;case 155:this.$=i.addLocationDataFn(o[a],o[a])(s[a]);break;case 156:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.While(i.addLocationDataFn(o[a-1])(new i.Literal("true"))).addBody(s[a]));break;case 157:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.While(i.addLocationDataFn(o[a-1])(new i.Literal("true"))).addBody(i.addLocationDataFn(o[a])(i.Block.wrap([s[a]]))));break;case 158:case 159:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.For(s[a-1],s[a]));break;case 160:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.For(s[a],s[a-1]));break;case 161:this.$=i.addLocationDataFn(o[a-1],o[a])({source:i.addLocationDataFn(o[a])(new i.Value(s[a]))});break;case 162:this.$=i.addLocationDataFn(o[a-3],o[a])({source:i.addLocationDataFn(o[a-2])(new i.Value(s[a-2])),step:s[a]});break;case 163:this.$=i.addLocationDataFn(o[a-1],o[a])(function(){return s[a].own=s[a-1].own,s[a].name=s[a-1][0],s[a].index=s[a-1][1],s[a]}());break;case 164:this.$=i.addLocationDataFn(o[a-1],o[a])(s[a]);break;case 165:this.$=i.addLocationDataFn(o[a-2],o[a])(function(){return s[a].own=!0,s[a]}());break;case 171:this.$=i.addLocationDataFn(o[a-2],o[a])([s[a-2],s[a]]);break;case 172:this.$=i.addLocationDataFn(o[a-1],o[a])({source:s[a]});break;case 173:this.$=i.addLocationDataFn(o[a-1],o[a])({source:s[a],object:!0});break;case 174:this.$=i.addLocationDataFn(o[a-3],o[a])({source:s[a-2],guard:s[a]});break;case 175:this.$=i.addLocationDataFn(o[a-3],o[a])({source:s[a-2],guard:s[a],object:!0});break;case 176:this.$=i.addLocationDataFn(o[a-3],o[a])({source:s[a-2],step:s[a]});break;case 177:this.$=i.addLocationDataFn(o[a-5],o[a])({source:s[a-4],guard:s[a-2],step:s[a]});break;case 178:this.$=i.addLocationDataFn(o[a-5],o[a])({source:s[a-4],step:s[a-2],guard:s[a]});break;case 179:this.$=i.addLocationDataFn(o[a-4],o[a])(new i.Switch(s[a-3],s[a-1]));break;case 180:this.$=i.addLocationDataFn(o[a-6],o[a])(new i.Switch(s[a-5],s[a-3],s[a-1]));break;case 181:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.Switch(null,s[a-1]));break;case 182:this.$=i.addLocationDataFn(o[a-5],o[a])(new i.Switch(null,s[a-3],s[a-1]));break;case 184:this.$=i.addLocationDataFn(o[a-1],o[a])(s[a-1].concat(s[a]));break;case 185:this.$=i.addLocationDataFn(o[a-2],o[a])([[s[a-1],s[a]]]);break;case 186:this.$=i.addLocationDataFn(o[a-3],o[a])([[s[a-2],s[a-1]]]);break;case 187:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.If(s[a-1],s[a],{type:s[a-2]}));break;case 188:this.$=i.addLocationDataFn(o[a-4],o[a])(s[a-4].addElse(i.addLocationDataFn(o[a-2],o[a])(new i.If(s[a-1],s[a],{type:s[a-2]}))));break;case 190:this.$=i.addLocationDataFn(o[a-2],o[a])(s[a-2].addElse(s[a]));break;case 191:case 192:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.If(s[a],i.addLocationDataFn(o[a-2])(i.Block.wrap([s[a-2]])),{type:s[a-1],statement:!0}));break;case 193:case 194:case 197:case 198:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Op(s[a-1],s[a]));break;case 195:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Op("-",s[a]));break;case 196:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Op("+",s[a]));break;case 199:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Op(s[a-2].concat(s[a-1]),s[a]));break;case 200:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Op("--",s[a]));break;case 201:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Op("++",s[a]));break;case 202:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Op("--",s[a-1],null,!0));break;case 203:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Op("++",s[a-1],null,!0));break;case 204:this.$=i.addLocationDataFn(o[a-1],o[a])(new i.Existence(s[a-1]));break;case 205:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Op("+",s[a-2],s[a]));break;case 206:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Op("-",s[a-2],s[a]));break;case 207:case 208:case 209:case 210:case 211:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Op(s[a-1],s[a-2],s[a]));break;case 212:this.$=i.addLocationDataFn(o[a-2],o[a])(function(){return"!"===s[a-1].charAt(0)?new i.Op(s[a-1].slice(1),s[a-2],s[a]).invert():new i.Op(s[a-1],s[a-2],s[a])}());break;case 213:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Assign(s[a-2],s[a],s[a-1]));break;case 214:this.$=i.addLocationDataFn(o[a-4],o[a])(new i.Assign(s[a-4],s[a-1],s[a-3]));break;case 215:this.$=i.addLocationDataFn(o[a-3],o[a])(new i.Assign(s[a-3],s[a],s[a-2]));break;case 216:this.$=i.addLocationDataFn(o[a-2],o[a])(new i.Extends(s[a-2],s[a]))}},table:[{1:[2,1],3:1,4:2,5:3,7:4,8:5,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{1:[3]},{1:[2,2],6:P},t(U,[2,3]),t(U,[2,6],{118:69,109:89,115:90,110:x,112:S,116:R,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(U,[2,7],{118:69,109:92,115:93,110:x,112:S,116:R,132:Z}),t(et,[2,11],{87:94,68:95,76:101,72:tt,73:nt,74:it,75:rt,77:st,80:ot,90:at,91:ct}),t(et,[2,12],{76:101,87:104,68:105,72:tt,73:nt,74:it,75:rt,77:st,80:ot,90:at,91:ct}),t(et,[2,13]),t(et,[2,14]),t(et,[2,15]),t(et,[2,16]),t(et,[2,17]),t(et,[2,18]),t(et,[2,19]),t(et,[2,20]),t(et,[2,21]),t(et,[2,22]),t(et,[2,8]),t(et,[2,9]),t(et,[2,10]),t(ht,lt,{46:[1,106]}),t(ht,[2,80]),t(ht,[2,81]),t(ht,[2,82]),t(ht,[2,83]),t([1,6,25,26,34,38,55,60,63,72,73,74,75,77,79,80,84,90,92,97,99,108,110,111,112,116,117,132,135,136,141,142,143,144,145,146,147],[2,110],{88:107,91:ut}),t([6,25,55,60],pt,{54:109,61:110,62:111,27:113,50:114,64:115,65:116,28:i,63:dt,82:b,95:ft,96:mt}),{24:119,25:gt},{7:121,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:123,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:124,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:125,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:127,8:126,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,138:[1,128],139:B,140:V},{12:130,13:131,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:132,50:63,64:47,65:48,67:129,69:23,70:24,71:25,82:b,89:w,94:T,95:C,96:E,107:L},{12:130,13:131,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:132,50:63,64:47,65:48,67:133,69:23,70:24,71:25,82:b,89:w,94:T,95:C,96:E,107:L},t(vt,yt,{86:[1,137],139:[1,134],140:[1,135],148:[1,136]}),t(et,[2,189],{127:[1,138]}),{24:139,25:gt},{24:140,25:gt},t(et,[2,155]),{24:141,25:gt},{7:142,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:[1,143],27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(bt,[2,100],{39:22,69:23,70:24,71:25,64:47,65:48,29:49,35:51,27:62,50:63,31:72,12:130,13:131,45:132,24:144,67:146,25:gt,28:i,30:r,32:s,33:o,36:a,37:c,40:h,41:l,42:u,43:p,44:d,82:b,86:[1,145],89:w,94:T,95:C,96:E,107:L}),{7:147,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t([1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,141,142,143,144,145,146,147],[2,50],{12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,9:18,10:19,45:21,39:22,69:23,70:24,71:25,56:28,67:36,130:37,109:39,113:40,115:41,64:47,65:48,29:49,35:51,27:62,50:63,118:69,31:72,8:122,7:148,11:n,28:i,30:r,32:s,33:o,36:a,37:c,40:h,41:l,42:u,43:p,44:d,51:f,52:m,53:g,57:v,58:y,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,114:D,125:A,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V}),t(et,[2,51]),t(vt,[2,77]),t(vt,[2,78]),t(ht,[2,32]),t(ht,[2,33]),t(ht,[2,34]),t(ht,[2,35]),t(ht,[2,36]),t(ht,[2,37]),t(ht,[2,38]),{4:149,5:3,7:4,8:5,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:[1,150],27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:151,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:kt,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,63:wt,64:47,65:48,66:156,67:36,69:23,70:24,71:25,82:b,85:k,89:w,93:153,94:T,95:C,96:E,97:Tt,100:154,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(ht,[2,116]),t(ht,[2,117],{27:158,28:i}),{25:[2,54]},{25:[2,55]},t(Ct,[2,72]),t(Ct,[2,75]),{7:159,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:160,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:161,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:163,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,24:162,25:gt,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{27:168,28:i,50:169,64:170,65:171,70:164,82:b,95:ft,96:E,120:165,121:[1,166],122:167},{119:172,123:[1,173],124:[1,174]},t([6,25,60,84],Et,{31:72,83:175,47:176,48:177,10:178,27:179,29:180,50:181,28:i,30:r,32:s,33:o,52:m,95:ft}),t(Ft,[2,26]),t(Ft,[2,27]),t(ht,[2,30]),{12:130,13:182,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:132,50:63,64:47,65:48,67:183,69:23,70:24,71:25,82:b,89:w,94:T,95:C,96:E,107:L},t(Nt,[2,25]),t(Ft,[2,28]),{4:184,5:3,7:4,8:5,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(U,[2,5],{7:4,8:5,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,9:18,10:19,45:21,39:22,69:23,70:24,71:25,56:28,67:36,130:37,109:39,113:40,115:41,64:47,65:48,29:49,35:51,27:62,50:63,118:69,31:72,5:185,11:n,28:i,30:r,32:s,33:o,36:a,37:c,40:h,41:l,42:u,43:p,44:d,51:f,52:m,53:g,57:v,58:y,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,110:x,112:S,114:D,116:R,125:A,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V}),t(et,[2,204]),{7:186,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:187,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:188,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:189,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:190,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:191,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:192,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:193,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:194,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(et,[2,154]),t(et,[2,159]),{7:195,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(et,[2,153]),t(et,[2,158]),{88:196,91:ut},t(Ct,[2,73]),{91:[2,113]},{27:197,28:i},{27:198,28:i},t(Ct,[2,88],{27:199,28:i}),{27:200,28:i},t(Ct,[2,89]),{7:202,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,63:Lt,64:47,65:48,67:36,69:23,70:24,71:25,78:201,81:203,82:b,85:k,89:w,94:T,95:C,96:E,98:204,99:xt,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{76:207,77:st,80:ot},{88:208,91:ut},t(Ct,[2,74]),{6:[1,210],7:209,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:[1,211],27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(St,[2,111]),{7:214,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:kt,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,63:wt,64:47,65:48,66:156,67:36,69:23,70:24,71:25,82:b,85:k,89:w,92:[1,212],93:213,94:T,95:C,96:E,100:154,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t([6,25],Dt,{59:217,55:[1,215],60:Rt}),t(At,[2,59]),t(At,[2,63],{46:[1,219],63:[1,218]}),t(At,[2,66]),t(It,[2,67]),t(It,[2,68]),t(It,[2,69]),t(It,[2,70]),{27:158,28:i},{7:214,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:kt,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,63:wt,64:47,65:48,66:156,67:36,69:23,70:24,71:25,82:b,85:k,89:w,93:153,94:T,95:C,96:E,97:Tt,100:154,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(et,[2,53]),{4:221,5:3,7:4,8:5,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,26:[1,220],27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t([1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,135,136,142,143,144,145,146,147],[2,193],{118:69,109:89,115:90,141:X}),{109:92,110:x,112:S,115:93,116:R,118:69,132:Z},t(_t,[2,194],{118:69,109:89,115:90,141:X,143:Y}),t(_t,[2,195],{118:69,109:89,115:90,141:X,143:Y}),t(_t,[2,196],{118:69,109:89,115:90,141:X,143:Y}),t(et,[2,197],{118:69,109:92,115:93}),t(Ot,[2,198],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{7:222,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(et,[2,200],{72:yt,73:yt,74:yt,75:yt,77:yt,80:yt,90:yt,91:yt}),{68:95,72:tt,73:nt,74:it,75:rt,76:101,77:st,80:ot,87:94,90:at,91:ct},{68:105,72:tt,73:nt,74:it,75:rt,76:101,77:st,80:ot,87:104,90:at,91:ct},t($t,lt),t(et,[2,201],{72:yt,73:yt,74:yt,75:yt,77:yt,80:yt,90:yt,91:yt}),t(et,[2,202]),t(et,[2,203]),{6:[1,225],7:223,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:[1,224],27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:226,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{24:227,25:gt,131:[1,228]},t(et,[2,138],{103:229,104:[1,230],105:[1,231]}),t(et,[2,152]),t(et,[2,160]),{25:[1,232],109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},{126:233,128:234,129:jt},t(et,[2,101]),{7:236,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(bt,[2,104],{24:237,25:gt,72:yt,73:yt,74:yt,75:yt,77:yt,80:yt,90:yt,91:yt,86:[1,238]}),t(Ot,[2,145],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Ot,[2,49],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{6:P,108:[1,239]},{4:240,5:3,7:4,8:5,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t([6,25,60,97],Mt,{118:69,109:89,115:90,98:241,63:[1,242],99:xt,110:x,112:S,116:R,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Bt,[2,119]),t([6,25,97],Dt,{59:243,60:Vt}),t(Pt,[2,128]),{7:214,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:kt,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,63:wt,64:47,65:48,66:156,67:36,69:23,70:24,71:25,82:b,85:k,89:w,93:245,94:T,95:C,96:E,100:154,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(Pt,[2,134]),t(Pt,[2,135]),t(Nt,[2,118]),{24:246,25:gt,109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},t(Ut,[2,148],{118:69,109:89,115:90,110:x,111:[1,247],112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Ut,[2,150],{118:69,109:89,115:90,110:x,111:[1,248],112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(et,[2,156]),t(Gt,[2,157],{118:69,109:89,115:90,110:x,112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t([1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,132,135,136,141,142,143,144,145,146,147],[2,161],{117:[1,249]}),t(Ht,[2,164]),{27:168,28:i,50:169,64:170,65:171,82:b,95:ft,96:mt,120:250,122:167},t(Ht,[2,170],{60:[1,251]}),t(qt,[2,166]),t(qt,[2,167]),t(qt,[2,168]),t(qt,[2,169]),t(et,[2,163]),{7:252,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:253,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t([6,25,84],Dt,{59:254,60:Xt}),t(Wt,[2,96]),t(Wt,[2,42],{49:[1,256]}),t(Wt,[2,45]),t(Yt,[2,46]),t(Yt,[2,47]),t(Yt,[2,48]),{38:[1,257],68:105,72:tt,73:nt,74:it,75:rt,76:101,77:st,80:ot,87:104,90:at,91:ct},t($t,yt),{6:P,34:[1,258]},t(U,[2,4]),t(Kt,[2,205],{118:69,109:89,115:90,141:X,142:W,143:Y}),t(Kt,[2,206],{118:69,109:89,115:90,141:X,142:W,143:Y}),t(_t,[2,207],{118:69,109:89,115:90,141:X,143:Y}),t(_t,[2,208],{118:69,109:89,115:90,141:X,143:Y}),t([1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,144,145,146,147],[2,209],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y}),t([1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,145,146],[2,210],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,147:Q}),t([1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,146],[2,211],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,147:Q}),t([1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,117,132,145,146,147],[2,212],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K}),t(Gt,[2,192],{118:69,109:89,115:90,110:x,112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Gt,[2,191],{118:69,109:89,115:90,110:x,112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(St,[2,108]),t(Ct,[2,84]),t(Ct,[2,85]),t(Ct,[2,86]),t(Ct,[2,87]),{79:[1,259]},{63:Lt,79:[2,92],98:260,99:xt,109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},{79:[2,93]},{7:261,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,79:[2,127],82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(zt,[2,121]),t(zt,Jt),t(Ct,[2,91]),t(St,[2,109]),t(Ot,[2,39],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{7:262,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:263,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(St,[2,114]),t([6,25,92],Dt,{59:264,60:Vt}),t(Pt,Mt,{118:69,109:89,115:90,63:[1,265],110:x,112:S,116:R,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{56:266,57:v,58:y},t(Qt,Zt,{62:111,27:113,50:114,64:115,65:116,61:267,28:i,63:dt,82:b,95:ft,96:mt}),{6:en,25:tn},t(At,[2,64]),{7:270,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(nn,[2,23]),{6:P,26:[1,271]},t(Ot,[2,199],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Ot,[2,213],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{7:272,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:273,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(Ot,[2,216],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(et,[2,190]),{7:274,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(et,[2,139],{104:[1,275]}),{24:276,25:gt},{24:279,25:gt,27:277,28:i,65:278,82:b},{126:280,128:234,129:jt},{26:[1,281],127:[1,282],128:283,129:jt},t(rn,[2,183]),{7:285,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,101:284,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(sn,[2,102],{118:69,109:89,115:90,24:286,25:gt,110:x,112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(et,[2,105]),{7:287,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(ht,[2,146]),{6:P,26:[1,288]},{7:289,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t([11,28,30,32,33,36,37,40,41,42,43,44,51,52,53,57,58,82,85,89,94,95,96,102,106,107,110,112,114,116,125,131,133,134,135,136,137,139,140],Jt,{6:on,25:on,60:on,97:on}),{6:an,25:cn,97:[1,290]},t([6,25,26,92,97],Zt,{12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,9:18,10:19,45:21,39:22,69:23,70:24,71:25,56:28,67:36,130:37,109:39,113:40,115:41,64:47,65:48,29:49,35:51,27:62,50:63,118:69,31:72,8:122,66:156,7:214,100:293,11:n,28:i,30:r,32:s,33:o,36:a,37:c,40:h,41:l,42:u,43:p,44:d,51:f,52:m,53:g,57:v,58:y,63:wt,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,110:x,112:S,114:D,116:R,125:A,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V}),t(Qt,Dt,{59:294,60:Vt}),t(hn,[2,187]),{7:295,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:296,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:297,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(Ht,[2,165]),{27:168,28:i,50:169,64:170,65:171,82:b,95:ft,96:mt,122:298},t([1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,112,116,132],[2,172],{118:69,109:89,115:90,111:[1,299],117:[1,300],135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(ln,[2,173],{118:69,109:89,115:90,111:[1,301],135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{6:un,25:pn,84:[1,302]},t([6,25,26,84],Zt,{31:72,48:177,10:178,27:179,29:180,50:181,47:305,28:i,30:r,32:s,33:o,52:m,95:ft}),{7:306,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:[1,307],27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(ht,[2,31]),t(Ft,[2,29]),t(Ct,[2,90]),{7:308,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,79:[2,125],82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{79:[2,126],109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},t(Ot,[2,40],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{26:[1,309],109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},{6:an,25:cn,92:[1,310]},t(Pt,on),{24:311,25:gt},t(At,[2,60]),{27:113,28:i,50:114,61:312,62:111,63:dt,64:115,65:116,82:b,95:ft,96:mt},t(dn,pt,{61:110,62:111,27:113,50:114,64:115,65:116,54:313,28:i,63:dt,82:b,95:ft,96:mt}),t(At,[2,65],{118:69,109:89,115:90,110:x,112:S,116:R,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(nn,[2,24]),{26:[1,314],109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},t(Ot,[2,215],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{24:315,25:gt,109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},{24:316,25:gt},t(et,[2,140]),{24:317,25:gt},{24:318,25:gt},t(fn,[2,144]),{26:[1,319],127:[1,320],128:283,129:jt},t(et,[2,181]),{24:321,25:gt},t(rn,[2,184]),{24:322,25:gt,60:[1,323]},t(mn,[2,136],{118:69,109:89,115:90,110:x,112:S,116:R,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(et,[2,103]),t(sn,[2,106],{118:69,109:89,115:90,24:324,25:gt,110:x,112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{108:[1,325]},{97:[1,326],109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},t(Bt,[2,120]),{7:214,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,63:wt,64:47,65:48,66:156,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,100:327,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:214,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,25:kt,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,63:wt,64:47,65:48,66:156,67:36,69:23,70:24,71:25,82:b,85:k,89:w,93:328,94:T,95:C,96:E,100:154,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(Pt,[2,129]),{6:an,25:cn,26:[1,329]},t(Gt,[2,149],{118:69,109:89,115:90,110:x,112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Gt,[2,151],{118:69,109:89,115:90,110:x,112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Gt,[2,162],{118:69,109:89,115:90,110:x,112:S,116:R,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Ht,[2,171]),{7:330,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:331,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:332,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(Bt,[2,94]),{10:178,27:179,28:i,29:180,30:r,31:72,32:s,33:o,47:333,48:177,50:181,52:m,95:ft},t(dn,Et,{31:72,47:176,48:177,10:178,27:179,29:180,50:181,83:334,28:i,30:r,32:s,33:o,52:m,95:ft}),t(Wt,[2,97]),t(Wt,[2,43],{118:69,109:89,115:90,110:x,112:S,116:R,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{7:335,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{79:[2,124],109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},t(et,[2,41]),t(St,[2,115]),t(et,[2,52]),t(At,[2,61]),t(Qt,Dt,{59:336,60:Rt}),t(et,[2,214]),t(hn,[2,188]),t(et,[2,141]),t(fn,[2,142]),t(fn,[2,143]),t(et,[2,179]),{24:337,25:gt},{26:[1,338]},t(rn,[2,185],{6:[1,339]}),{7:340,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},t(et,[2,107]),t(ht,[2,147]),t(ht,[2,123]),t(Pt,[2,130]),t(Qt,Dt,{59:341,60:Vt}),t(Pt,[2,131]),t([1,6,25,26,34,55,60,63,79,84,92,97,99,108,110,111,112,116,132],[2,174],{118:69,109:89,115:90,117:[1,342],135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(ln,[2,176],{118:69,109:89,115:90,111:[1,343],135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Ot,[2,175],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Wt,[2,98]),t(Qt,Dt,{59:344,60:Xt}),{26:[1,345],109:89,110:x,112:S,115:90,116:R,118:69,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q},{6:en,25:tn,26:[1,346]},{26:[1,347]},t(et,[2,182]),t(rn,[2,186]),t(mn,[2,137],{118:69,109:89,115:90,110:x,112:S,116:R,132:G,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),{6:an,25:cn,26:[1,348]},{7:349,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{7:350,8:122,9:18,10:19,11:n,12:6,13:7,14:8,15:9,16:10,17:11,18:12,19:13,20:14,21:15,22:16,23:17,27:62,28:i,29:49,30:r,31:72,32:s,33:o,35:51,36:a,37:c,39:22,40:h,41:l,42:u,43:p,44:d,45:21,50:63,51:f,52:m,53:g,56:28,57:v,58:y,64:47,65:48,67:36,69:23,70:24,71:25,82:b,85:k,89:w,94:T,95:C,96:E,102:F,106:N,107:L,109:39,110:x,112:S,113:40,114:D,115:41,116:R,118:69,125:A,130:37,131:I,133:_,134:O,135:$,136:j,137:M,139:B,140:V},{6:un,25:pn,26:[1,351]},t(Wt,[2,44]),t(At,[2,62]),t(et,[2,180]),t(Pt,[2,132]),t(Ot,[2,177],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Ot,[2,178],{118:69,109:89,115:90,135:H,136:q,141:X,142:W,143:Y,144:K,145:z,146:J,147:Q}),t(Wt,[2,99])],defaultActions:{60:[2,54],61:[2,55],96:[2,113],203:[2,93]},parseError:function(e,t){if(!t.recoverable)throw Error(e);
this.trace(e)},parse:function(e){function t(){var e;return e=f.lex()||p,"number"!=typeof e&&(e=n.symbols_[e]||e),e}var n=this,i=[0],r=[null],s=[],o=this.table,a="",c=0,h=0,l=0,u=2,p=1,d=s.slice.call(arguments,1),f=Object.create(this.lexer),m={yy:{}};for(var g in this.yy)Object.prototype.hasOwnProperty.call(this.yy,g)&&(m.yy[g]=this.yy[g]);f.setInput(e,m.yy),m.yy.lexer=f,m.yy.parser=this,f.yylloc===void 0&&(f.yylloc={});var v=f.yylloc;s.push(v);var y=f.options&&f.options.ranges;this.parseError="function"==typeof m.yy.parseError?m.yy.parseError:Object.getPrototypeOf(this).parseError;for(var b,k,w,T,C,E,F,N,L,x={};;){if(w=i[i.length-1],this.defaultActions[w]?T=this.defaultActions[w]:((null===b||b===void 0)&&(b=t()),T=o[w]&&o[w][b]),T===void 0||!T.length||!T[0]){var S="";L=[];for(E in o[w])this.terminals_[E]&&E>u&&L.push("'"+this.terminals_[E]+"'");S=f.showPosition?"Parse error on line "+(c+1)+":\n"+f.showPosition()+"\nExpecting "+L.join(", ")+", got '"+(this.terminals_[b]||b)+"'":"Parse error on line "+(c+1)+": Unexpected "+(b==p?"end of input":"'"+(this.terminals_[b]||b)+"'"),this.parseError(S,{text:f.match,token:this.terminals_[b]||b,line:f.yylineno,loc:v,expected:L})}if(T[0]instanceof Array&&T.length>1)throw Error("Parse Error: multiple actions possible at state: "+w+", token: "+b);switch(T[0]){case 1:i.push(b),r.push(f.yytext),s.push(f.yylloc),i.push(T[1]),b=null,k?(b=k,k=null):(h=f.yyleng,a=f.yytext,c=f.yylineno,v=f.yylloc,l>0&&l--);break;case 2:if(F=this.productions_[T[1]][1],x.$=r[r.length-F],x._$={first_line:s[s.length-(F||1)].first_line,last_line:s[s.length-1].last_line,first_column:s[s.length-(F||1)].first_column,last_column:s[s.length-1].last_column},y&&(x._$.range=[s[s.length-(F||1)].range[0],s[s.length-1].range[1]]),C=this.performAction.apply(x,[a,h,c,m.yy,T[1],r,s].concat(d)),C!==void 0)return C;F&&(i=i.slice(0,2*-1*F),r=r.slice(0,-1*F),s=s.slice(0,-1*F)),i.push(this.productions_[T[1]][0]),r.push(x.$),s.push(x._$),N=o[i[i.length-2]][i[i.length-1]],i.push(N);break;case 3:return!0}}return!0}};return e.prototype=gn,gn.Parser=e,new e}();return _dereq_!==void 0&&e!==void 0&&(e.parser=n,e.Parser=n.Parser,e.parse=function(){return n.parse.apply(n,arguments)},e.main=function(t){t[1]||(console.log("Usage: "+t[0]+" FILE"),process.exit(1));var n=_dereq_("fs").readFileSync(_dereq_("path").normalize(t[1]),"utf8");return e.parser.parse(n)},t!==void 0&&_dereq_.main===t&&e.main(process.argv.slice(1))),t.exports}(),_dereq_["./scope"]=function(){var e={},t={exports:e};return function(){var t,n=[].indexOf||function(e){for(var t=0,n=this.length;n>t;t++)if(t in this&&this[t]===e)return t;return-1};e.Scope=t=function(){function e(e,t,n,i){var r,s;this.parent=e,this.expressions=t,this.method=n,this.referencedVars=i,this.variables=[{name:"arguments",type:"arguments"}],this.positions={},this.parent||(this.utilities={}),this.root=null!=(r=null!=(s=this.parent)?s.root:void 0)?r:this}return e.prototype.add=function(e,t,n){return this.shared&&!n?this.parent.add(e,t,n):Object.prototype.hasOwnProperty.call(this.positions,e)?this.variables[this.positions[e]].type=t:this.positions[e]=this.variables.push({name:e,type:t})-1},e.prototype.namedMethod=function(){var e;return(null!=(e=this.method)?e.name:void 0)||!this.parent?this.method:this.parent.namedMethod()},e.prototype.find=function(e){return this.check(e)?!0:(this.add(e,"var"),!1)},e.prototype.parameter=function(e){return this.shared&&this.parent.check(e,!0)?void 0:this.add(e,"param")},e.prototype.check=function(e){var t;return!!(this.type(e)||(null!=(t=this.parent)?t.check(e):void 0))},e.prototype.temporary=function(e,t,n){return null==n&&(n=!1),n?(t+parseInt(e,36)).toString(36).replace(/\d/g,"a"):e+(t||"")},e.prototype.type=function(e){var t,n,i,r;for(i=this.variables,t=0,n=i.length;n>t;t++)if(r=i[t],r.name===e)return r.type;return null},e.prototype.freeVariable=function(e,t){var i,r,s;for(null==t&&(t={}),i=0;;){if(s=this.temporary(e,i,t.single),!(this.check(s)||n.call(this.root.referencedVars,s)>=0))break;i++}return(null!=(r=t.reserve)?r:!0)&&this.add(s,"var",!0),s},e.prototype.assign=function(e,t){return this.add(e,{value:t,assigned:!0},!0),this.hasAssignments=!0},e.prototype.hasDeclarations=function(){return!!this.declaredVariables().length},e.prototype.declaredVariables=function(){var e;return function(){var t,n,i,r;for(i=this.variables,r=[],t=0,n=i.length;n>t;t++)e=i[t],"var"===e.type&&r.push(e.name);return r}.call(this).sort()},e.prototype.assignedVariables=function(){var e,t,n,i,r;for(n=this.variables,i=[],e=0,t=n.length;t>e;e++)r=n[e],r.type.assigned&&i.push(r.name+" = "+r.type.value);return i},e}()}.call(this),t.exports}(),_dereq_["./nodes"]=function(){var e={},t={exports:e};return function(){var t,n,i,r,s,o,a,c,h,l,u,p,d,f,m,g,v,y,b,k,w,T,C,E,F,N,L,x,S,D,R,A,I,_,O,$,j,M,B,V,P,U,G,H,q,X,W,Y,K,z,J,Q,Z,et,tt,nt,it,rt,st,ot,at,ct,ht,lt,ut,pt,dt,ft,mt,gt,vt,yt,bt,kt=function(e,t){function n(){this.constructor=e}for(var i in t)wt.call(t,i)&&(e[i]=t[i]);return n.prototype=t.prototype,e.prototype=new n,e.__super__=t.prototype,e},wt={}.hasOwnProperty,Tt=[].indexOf||function(e){for(var t=0,n=this.length;n>t;t++)if(t in this&&this[t]===e)return t;return-1},Ct=[].slice;Error.stackTraceLimit=1/0,P=_dereq_("./scope").Scope,dt=_dereq_("./lexer"),$=dt.RESERVED,V=dt.STRICT_PROSCRIBED,ft=_dereq_("./helpers"),et=ft.compact,rt=ft.flatten,it=ft.extend,lt=ft.merge,tt=ft.del,gt=ft.starts,nt=ft.ends,mt=ft.some,Z=ft.addLocationDataFn,ht=ft.locationDataToString,vt=ft.throwSyntaxError,e.extend=it,e.addLocationDataFn=Z,Q=function(){return!0},D=function(){return!1},X=function(){return this},S=function(){return this.negated=!this.negated,this},e.CodeFragment=h=function(){function e(e,t){var n;this.code=""+t,this.locationData=null!=e?e.locationData:void 0,this.type=(null!=e?null!=(n=e.constructor)?n.name:void 0:void 0)||"unknown"}return e.prototype.toString=function(){return""+this.code+(this.locationData?": "+ht(this.locationData):"")},e}(),st=function(e){var t;return function(){var n,i,r;for(r=[],n=0,i=e.length;i>n;n++)t=e[n],r.push(t.code);return r}().join("")},e.Base=r=function(){function e(){}return e.prototype.compile=function(e,t){return st(this.compileToFragments(e,t))},e.prototype.compileToFragments=function(e,t){var n;return e=it({},e),t&&(e.level=t),n=this.unfoldSoak(e)||this,n.tab=e.indent,e.level!==L&&n.isStatement(e)?n.compileClosure(e):n.compileNode(e)},e.prototype.compileClosure=function(e){var n,i,r,a,h,l,u;return(a=this.jumps())&&a.error("cannot use a pure statement in an expression"),e.sharedScope=!0,r=new c([],s.wrap([this])),n=[],((i=this.contains(at))||this.contains(ct))&&(n=[new x("this")],i?(h="apply",n.push(new x("arguments"))):h="call",r=new z(r,[new t(new x(h))])),l=new o(r,n).compileNode(e),(r.isGenerator||(null!=(u=r.base)?u.isGenerator:void 0))&&(l.unshift(this.makeCode("(yield* ")),l.push(this.makeCode(")"))),l},e.prototype.cache=function(e,t,n){var r,s,o;return r=null!=n?n(this):this.isComplex(),r?(s=new x(e.scope.freeVariable("ref")),o=new i(s,this),t?[o.compileToFragments(e,t),[this.makeCode(s.value)]]:[o,s]):(s=t?this.compileToFragments(e,t):this,[s,s])},e.prototype.cacheToCodeFragments=function(e){return[st(e[0]),st(e[1])]},e.prototype.makeReturn=function(e){var t;return t=this.unwrapAll(),e?new o(new x(e+".push"),[t]):new M(t)},e.prototype.contains=function(e){var t;return t=void 0,this.traverseChildren(!1,function(n){return e(n)?(t=n,!1):void 0}),t},e.prototype.lastNonComment=function(e){var t;for(t=e.length;t--;)if(!(e[t]instanceof l))return e[t];return null},e.prototype.toString=function(e,t){var n;return null==e&&(e=""),null==t&&(t=this.constructor.name),n="\n"+e+t,this.soak&&(n+="?"),this.eachChild(function(t){return n+=t.toString(e+q)}),n},e.prototype.eachChild=function(e){var t,n,i,r,s,o,a,c;if(!this.children)return this;for(a=this.children,i=0,s=a.length;s>i;i++)if(t=a[i],this[t])for(c=rt([this[t]]),r=0,o=c.length;o>r;r++)if(n=c[r],e(n)===!1)return this;return this},e.prototype.traverseChildren=function(e,t){return this.eachChild(function(n){var i;return i=t(n),i!==!1?n.traverseChildren(e,t):void 0})},e.prototype.invert=function(){return new I("!",this)},e.prototype.unwrapAll=function(){var e;for(e=this;e!==(e=e.unwrap()););return e},e.prototype.children=[],e.prototype.isStatement=D,e.prototype.jumps=D,e.prototype.isComplex=Q,e.prototype.isChainable=D,e.prototype.isAssignable=D,e.prototype.unwrap=X,e.prototype.unfoldSoak=D,e.prototype.assigns=D,e.prototype.updateLocationDataIfMissing=function(e){return this.locationData?this:(this.locationData=e,this.eachChild(function(t){return t.updateLocationDataIfMissing(e)}))},e.prototype.error=function(e){return vt(e,this.locationData)},e.prototype.makeCode=function(e){return new h(this,e)},e.prototype.wrapInBraces=function(e){return[].concat(this.makeCode("("),e,this.makeCode(")"))},e.prototype.joinFragmentArrays=function(e,t){var n,i,r,s,o;for(n=[],r=s=0,o=e.length;o>s;r=++s)i=e[r],r&&n.push(this.makeCode(t)),n=n.concat(i);return n},e}(),e.Block=s=function(e){function t(e){this.expressions=et(rt(e||[]))}return kt(t,e),t.prototype.children=["expressions"],t.prototype.push=function(e){return this.expressions.push(e),this},t.prototype.pop=function(){return this.expressions.pop()},t.prototype.unshift=function(e){return this.expressions.unshift(e),this},t.prototype.unwrap=function(){return 1===this.expressions.length?this.expressions[0]:this},t.prototype.isEmpty=function(){return!this.expressions.length},t.prototype.isStatement=function(e){var t,n,i,r;for(r=this.expressions,n=0,i=r.length;i>n;n++)if(t=r[n],t.isStatement(e))return!0;return!1},t.prototype.jumps=function(e){var t,n,i,r,s;for(s=this.expressions,n=0,r=s.length;r>n;n++)if(t=s[n],i=t.jumps(e))return i},t.prototype.makeReturn=function(e){var t,n;for(n=this.expressions.length;n--;)if(t=this.expressions[n],!(t instanceof l)){this.expressions[n]=t.makeReturn(e),t instanceof M&&!t.expression&&this.expressions.splice(n,1);break}return this},t.prototype.compileToFragments=function(e,n){return null==e&&(e={}),e.scope?t.__super__.compileToFragments.call(this,e,n):this.compileRoot(e)},t.prototype.compileNode=function(e){var n,i,r,s,o,a,c,h,l;for(this.tab=e.indent,l=e.level===L,i=[],h=this.expressions,s=o=0,a=h.length;a>o;s=++o)c=h[s],c=c.unwrapAll(),c=c.unfoldSoak(e)||c,c instanceof t?i.push(c.compileNode(e)):l?(c.front=!0,r=c.compileToFragments(e),c.isStatement(e)||(r.unshift(this.makeCode(""+this.tab)),r.push(this.makeCode(";"))),i.push(r)):i.push(c.compileToFragments(e,E));return l?this.spaced?[].concat(this.joinFragmentArrays(i,"\n\n"),this.makeCode("\n")):this.joinFragmentArrays(i,"\n"):(n=i.length?this.joinFragmentArrays(i,", "):[this.makeCode("void 0")],i.length>1&&e.level>=E?this.wrapInBraces(n):n)},t.prototype.compileRoot=function(e){var t,n,i,r,s,o,a,c,h,u,p;for(e.indent=e.bare?"":q,e.level=L,this.spaced=!0,e.scope=new P(null,this,null,null!=(h=e.referencedVars)?h:[]),u=e.locals||[],r=0,s=u.length;s>r;r++)o=u[r],e.scope.parameter(o);return a=[],e.bare||(c=function(){var e,n,r,s;for(r=this.expressions,s=[],i=e=0,n=r.length;n>e&&(t=r[i],t.unwrap()instanceof l);i=++e)s.push(t);return s}.call(this),p=this.expressions.slice(c.length),this.expressions=c,c.length&&(a=this.compileNode(lt(e,{indent:""})),a.push(this.makeCode("\n"))),this.expressions=p),n=this.compileWithDeclarations(e),e.bare?n:[].concat(a,this.makeCode("(function() {\n"),n,this.makeCode("\n}).call(this);\n"))},t.prototype.compileWithDeclarations=function(e){var t,n,i,r,s,o,a,c,h,u,p,d,f,m;for(r=[],c=[],h=this.expressions,s=o=0,a=h.length;a>o&&(i=h[s],i=i.unwrap(),i instanceof l||i instanceof x);s=++o);return e=lt(e,{level:L}),s&&(d=this.expressions.splice(s,9e9),u=[this.spaced,!1],m=u[0],this.spaced=u[1],p=[this.compileNode(e),m],r=p[0],this.spaced=p[1],this.expressions=d),c=this.compileNode(e),f=e.scope,f.expressions===this&&(n=e.scope.hasDeclarations(),t=f.hasAssignments,n||t?(s&&r.push(this.makeCode("\n")),r.push(this.makeCode(this.tab+"var ")),n&&r.push(this.makeCode(f.declaredVariables().join(", "))),t&&(n&&r.push(this.makeCode(",\n"+(this.tab+q))),r.push(this.makeCode(f.assignedVariables().join(",\n"+(this.tab+q))))),r.push(this.makeCode(";\n"+(this.spaced?"\n":"")))):r.length&&c.length&&r.push(this.makeCode("\n"))),r.concat(c)},t.wrap=function(e){return 1===e.length&&e[0]instanceof t?e[0]:new t(e)},t}(r),e.Literal=x=function(e){function t(e){this.value=e}return kt(t,e),t.prototype.makeReturn=function(){return this.isStatement()?this:t.__super__.makeReturn.apply(this,arguments)},t.prototype.isAssignable=function(){return g.test(this.value)},t.prototype.isStatement=function(){var e;return"break"===(e=this.value)||"continue"===e||"debugger"===e},t.prototype.isComplex=D,t.prototype.assigns=function(e){return e===this.value},t.prototype.jumps=function(e){return"break"!==this.value||(null!=e?e.loop:void 0)||(null!=e?e.block:void 0)?"continue"!==this.value||(null!=e?e.loop:void 0)?void 0:this:this},t.prototype.compileNode=function(e){var t,n,i;return n="this"===this.value?(null!=(i=e.scope.method)?i.bound:void 0)?e.scope.method.context:this.value:this.value.reserved?'"'+this.value+'"':this.value,t=this.isStatement()?""+this.tab+n+";":n,[this.makeCode(t)]},t.prototype.toString=function(){return' "'+this.value+'"'},t}(r),e.Undefined=function(e){function t(){return t.__super__.constructor.apply(this,arguments)}return kt(t,e),t.prototype.isAssignable=D,t.prototype.isComplex=D,t.prototype.compileNode=function(e){return[this.makeCode(e.level>=T?"(void 0)":"void 0")]},t}(r),e.Null=function(e){function t(){return t.__super__.constructor.apply(this,arguments)}return kt(t,e),t.prototype.isAssignable=D,t.prototype.isComplex=D,t.prototype.compileNode=function(){return[this.makeCode("null")]},t}(r),e.Bool=function(e){function t(e){this.val=e}return kt(t,e),t.prototype.isAssignable=D,t.prototype.isComplex=D,t.prototype.compileNode=function(){return[this.makeCode(this.val)]},t}(r),e.Return=M=function(e){function t(e){this.expression=e}return kt(t,e),t.prototype.children=["expression"],t.prototype.isStatement=Q,t.prototype.makeReturn=X,t.prototype.jumps=X,t.prototype.compileToFragments=function(e,n){var i,r;return i=null!=(r=this.expression)?r.makeReturn():void 0,!i||i instanceof t?t.__super__.compileToFragments.call(this,e,n):i.compileToFragments(e,n)},t.prototype.compileNode=function(e){var t,n,i;return t=[],n=null!=(i=this.expression)?"function"==typeof i.isYieldReturn?i.isYieldReturn():void 0:void 0,n||t.push(this.makeCode(this.tab+("return"+(this.expression?" ":"")))),this.expression&&(t=t.concat(this.expression.compileToFragments(e,N))),n||t.push(this.makeCode(";")),t},t}(r),e.Value=z=function(e){function t(e,n,i){return!n&&e instanceof t?e:(this.base=e,this.properties=n||[],i&&(this[i]=!0),this)}return kt(t,e),t.prototype.children=["base","properties"],t.prototype.add=function(e){return this.properties=this.properties.concat(e),this},t.prototype.hasProperties=function(){return!!this.properties.length},t.prototype.bareLiteral=function(e){return!this.properties.length&&this.base instanceof e},t.prototype.isArray=function(){return this.bareLiteral(n)},t.prototype.isRange=function(){return this.bareLiteral(j)},t.prototype.isComplex=function(){return this.hasProperties()||this.base.isComplex()},t.prototype.isAssignable=function(){return this.hasProperties()||this.base.isAssignable()},t.prototype.isSimpleNumber=function(){return this.bareLiteral(x)&&B.test(this.base.value)},t.prototype.isString=function(){return this.bareLiteral(x)&&y.test(this.base.value)},t.prototype.isRegex=function(){return this.bareLiteral(x)&&v.test(this.base.value)},t.prototype.isAtomic=function(){var e,t,n,i;for(i=this.properties.concat(this.base),e=0,t=i.length;t>e;e++)if(n=i[e],n.soak||n instanceof o)return!1;return!0},t.prototype.isNotCallable=function(){return this.isSimpleNumber()||this.isString()||this.isRegex()||this.isArray()||this.isRange()||this.isSplice()||this.isObject()},t.prototype.isStatement=function(e){return!this.properties.length&&this.base.isStatement(e)},t.prototype.assigns=function(e){return!this.properties.length&&this.base.assigns(e)},t.prototype.jumps=function(e){return!this.properties.length&&this.base.jumps(e)},t.prototype.isObject=function(e){return this.properties.length?!1:this.base instanceof A&&(!e||this.base.generated)},t.prototype.isSplice=function(){var e,t;return t=this.properties,e=t[t.length-1],e instanceof U},t.prototype.looksStatic=function(e){var t;return this.base.value===e&&1===this.properties.length&&"prototype"!==(null!=(t=this.properties[0].name)?t.value:void 0)},t.prototype.unwrap=function(){return this.properties.length?this:this.base},t.prototype.cacheReference=function(e){var n,r,s,o,a;return a=this.properties,s=a[a.length-1],2>this.properties.length&&!this.base.isComplex()&&!(null!=s?s.isComplex():void 0)?[this,this]:(n=new t(this.base,this.properties.slice(0,-1)),n.isComplex()&&(r=new x(e.scope.freeVariable("base")),n=new t(new O(new i(r,n)))),s?(s.isComplex()&&(o=new x(e.scope.freeVariable("name")),s=new w(new i(o,s.index)),o=new w(o)),[n.add(s),new t(r||n.base,[o||s])]):[n,r])},t.prototype.compileNode=function(e){var t,n,i,r,s;for(this.base.front=this.front,s=this.properties,t=this.base.compileToFragments(e,s.length?T:null),(this.base instanceof O||s.length)&&B.test(st(t))&&t.push(this.makeCode(".")),n=0,i=s.length;i>n;n++)r=s[n],t.push.apply(t,r.compileToFragments(e));return t},t.prototype.unfoldSoak=function(e){return null!=this.unfoldedSoak?this.unfoldedSoak:this.unfoldedSoak=function(n){return function(){var r,s,o,a,c,h,l,p,d,f;if(o=n.base.unfoldSoak(e))return(p=o.body.properties).push.apply(p,n.properties),o;for(d=n.properties,s=a=0,c=d.length;c>a;s=++a)if(h=d[s],h.soak)return h.soak=!1,r=new t(n.base,n.properties.slice(0,s)),f=new t(n.base,n.properties.slice(s)),r.isComplex()&&(l=new x(e.scope.freeVariable("ref")),r=new O(new i(l,r)),f.base=l),new b(new u(r),f,{soak:!0});return!1}}(this)()},t}(r),e.Comment=l=function(e){function t(e){this.comment=e}return kt(t,e),t.prototype.isStatement=Q,t.prototype.makeReturn=X,t.prototype.compileNode=function(e,t){var n,i;return i=this.comment.replace(/^(\s*)#(?=\s)/gm,"$1 *"),n="/*"+ut(i,this.tab)+(Tt.call(i,"\n")>=0?"\n"+this.tab:"")+" */",(t||e.level)===L&&(n=e.indent+n),[this.makeCode("\n"),this.makeCode(n)]},t}(r),e.Call=o=function(e){function n(e,t,n){this.args=null!=t?t:[],this.soak=n,this.isNew=!1,this.isSuper="super"===e,this.variable=this.isSuper?null:e,e instanceof z&&e.isNotCallable()&&e.error("literal is not a function")}return kt(n,e),n.prototype.children=["variable","args"],n.prototype.newInstance=function(){var e,t;return e=(null!=(t=this.variable)?t.base:void 0)||this.variable,e instanceof n&&!e.isNew?e.newInstance():this.isNew=!0,this},n.prototype.superReference=function(e){var n,r,s,o,a,c,h,l;return a=e.scope.namedMethod(),(null!=a?a.klass:void 0)?(o=a.klass,c=a.name,l=a.variable,o.isComplex()&&(s=new x(e.scope.parent.freeVariable("base")),r=new z(new O(new i(s,o))),l.base=r,l.properties.splice(0,o.properties.length)),(c.isComplex()||c instanceof w&&c.index.isAssignable())&&(h=new x(e.scope.parent.freeVariable("name")),c=new w(new i(h,c.index)),l.properties.pop(),l.properties.push(c)),n=[new t(new x("__super__"))],a["static"]&&n.push(new t(new x("constructor"))),n.push(null!=h?new w(h):c),new z(null!=s?s:o,n).compile(e)):(null!=a?a.ctor:void 0)?a.name+".__super__.constructor":this.error("cannot call super outside of an instance method.")},n.prototype.superThis=function(e){var t;return t=e.scope.method,t&&!t.klass&&t.context||"this"},n.prototype.unfoldSoak=function(e){var t,i,r,s,o,a,c,h,l;if(this.soak){if(this.variable){if(i=yt(e,this,"variable"))return i;c=new z(this.variable).cacheReference(e),s=c[0],l=c[1]}else s=new x(this.superReference(e)),l=new z(s);return l=new n(l,this.args),l.isNew=this.isNew,s=new x("typeof "+s.compile(e)+' === "function"'),new b(s,new z(l),{soak:!0})}for(t=this,a=[];;)if(t.variable instanceof n)a.push(t),t=t.variable;else{if(!(t.variable instanceof z))break;if(a.push(t),!((t=t.variable.base)instanceof n))break}for(h=a.reverse(),r=0,o=h.length;o>r;r++)t=h[r],i&&(t.variable instanceof n?t.variable=i:t.variable.base=i),i=yt(e,t,"variable");return i},n.prototype.compileNode=function(e){var t,n,i,r,s,o,a,c,h,l;if(null!=(h=this.variable)&&(h.front=this.front),r=G.compileSplattedArray(e,this.args,!0),r.length)return this.compileSplat(e,r);for(i=[],l=this.args,n=o=0,a=l.length;a>o;n=++o)t=l[n],n&&i.push(this.makeCode(", ")),i.push.apply(i,t.compileToFragments(e,E));return s=[],this.isSuper?(c=this.superReference(e)+(".call("+this.superThis(e)),i.length&&(c+=", "),s.push(this.makeCode(c))):(this.isNew&&s.push(this.makeCode("new ")),s.push.apply(s,this.variable.compileToFragments(e,T)),s.push(this.makeCode("("))),s.push.apply(s,i),s.push(this.makeCode(")")),s},n.prototype.compileSplat=function(e,t){var n,i,r,s,o,a;return this.isSuper?[].concat(this.makeCode(this.superReference(e)+".apply("+this.superThis(e)+", "),t,this.makeCode(")")):this.isNew?(s=this.tab+q,[].concat(this.makeCode("(function(func, args, ctor) {\n"+s+"ctor.prototype = func.prototype;\n"+s+"var child = new ctor, result = func.apply(child, args);\n"+s+"return Object(result) === result ? result : child;\n"+this.tab+"})("),this.variable.compileToFragments(e,E),this.makeCode(", "),t,this.makeCode(", function(){})"))):(n=[],i=new z(this.variable),(o=i.properties.pop())&&i.isComplex()?(a=e.scope.freeVariable("ref"),n=n.concat(this.makeCode("("+a+" = "),i.compileToFragments(e,E),this.makeCode(")"),o.compileToFragments(e))):(r=i.compileToFragments(e,T),B.test(st(r))&&(r=this.wrapInBraces(r)),o?(a=st(r),r.push.apply(r,o.compileToFragments(e))):a="null",n=n.concat(r)),n=n.concat(this.makeCode(".apply("+a+", "),t,this.makeCode(")")))},n}(r),e.Extends=d=function(e){function t(e,t){this.child=e,this.parent=t}return kt(t,e),t.prototype.children=["child","parent"],t.prototype.compileToFragments=function(e){return new o(new z(new x(bt("extend",e))),[this.child,this.parent]).compileToFragments(e)},t}(r),e.Access=t=function(e){function t(e,t){this.name=e,this.name.asKey=!0,this.soak="soak"===t}return kt(t,e),t.prototype.children=["name"],t.prototype.compileToFragments=function(e){var t;return t=this.name.compileToFragments(e),g.test(st(t))?t.unshift(this.makeCode(".")):(t.unshift(this.makeCode("[")),t.push(this.makeCode("]"))),t},t.prototype.isComplex=D,t}(r),e.Index=w=function(e){function t(e){this.index=e}return kt(t,e),t.prototype.children=["index"],t.prototype.compileToFragments=function(e){return[].concat(this.makeCode("["),this.index.compileToFragments(e,N),this.makeCode("]"))},t.prototype.isComplex=function(){return this.index.isComplex()},t}(r),e.Range=j=function(e){function t(e,t,n){this.from=e,this.to=t,this.exclusive="exclusive"===n,this.equals=this.exclusive?"":"="}return kt(t,e),t.prototype.children=["from","to"],t.prototype.compileVariables=function(e){var t,n,i,r,s,o;return e=lt(e,{top:!0}),t=tt(e,"isComplex"),n=this.cacheToCodeFragments(this.from.cache(e,E,t)),this.fromC=n[0],this.fromVar=n[1],i=this.cacheToCodeFragments(this.to.cache(e,E,t)),this.toC=i[0],this.toVar=i[1],(o=tt(e,"step"))&&(r=this.cacheToCodeFragments(o.cache(e,E,t)),this.step=r[0],this.stepVar=r[1]),s=[this.fromVar.match(R),this.toVar.match(R)],this.fromNum=s[0],this.toNum=s[1],this.stepVar?this.stepNum=this.stepVar.match(R):void 0},t.prototype.compileNode=function(e){var t,n,i,r,s,o,a,c,h,l,u,p,d,f;return this.fromVar||this.compileVariables(e),e.index?(a=this.fromNum&&this.toNum,s=tt(e,"index"),o=tt(e,"name"),h=o&&o!==s,f=s+" = "+this.fromC,this.toC!==this.toVar&&(f+=", "+this.toC),this.step!==this.stepVar&&(f+=", "+this.step),l=[s+" <"+this.equals,s+" >"+this.equals],c=l[0],r=l[1],n=this.stepNum?pt(this.stepNum[0])>0?c+" "+this.toVar:r+" "+this.toVar:a?(u=[pt(this.fromNum[0]),pt(this.toNum[0])],i=u[0],d=u[1],u,d>=i?c+" "+d:r+" "+d):(t=this.stepVar?this.stepVar+" > 0":this.fromVar+" <= "+this.toVar,t+" ? "+c+" "+this.toVar+" : "+r+" "+this.toVar),p=this.stepVar?s+" += "+this.stepVar:a?h?d>=i?"++"+s:"--"+s:d>=i?s+"++":s+"--":h?t+" ? ++"+s+" : --"+s:t+" ? "+s+"++ : "+s+"--",h&&(f=o+" = "+f),h&&(p=o+" = "+p),[this.makeCode(f+"; "+n+"; "+p)]):this.compileArray(e)},t.prototype.compileArray=function(e){var t,n,i,r,s,o,a,c,h,l,u,p,d;return this.fromNum&&this.toNum&&20>=Math.abs(this.fromNum-this.toNum)?(h=function(){p=[];for(var e=l=+this.fromNum,t=+this.toNum;t>=l?t>=e:e>=t;t>=l?e++:e--)p.push(e);return p}.apply(this),this.exclusive&&h.pop(),[this.makeCode("["+h.join(", ")+"]")]):(o=this.tab+q,s=e.scope.freeVariable("i",{single:!0}),u=e.scope.freeVariable("results"),c="\n"+o+u+" = [];",this.fromNum&&this.toNum?(e.index=s,n=st(this.compileNode(e))):(d=s+" = "+this.fromC+(this.toC!==this.toVar?", "+this.toC:""),i=this.fromVar+" <= "+this.toVar,n="var "+d+"; "+i+" ? "+s+" <"+this.equals+" "+this.toVar+" : "+s+" >"+this.equals+" "+this.toVar+"; "+i+" ? "+s+"++ : "+s+"--"),a="{ "+u+".push("+s+"); }\n"+o+"return "+u+";\n"+e.indent,r=function(e){return null!=e?e.contains(at):void 0},(r(this.from)||r(this.to))&&(t=", arguments"),[this.makeCode("(function() {"+c+"\n"+o+"for ("+n+")"+a+"}).apply(this"+(null!=t?t:"")+")")])},t}(r),e.Slice=U=function(e){function t(e){this.range=e,t.__super__.constructor.call(this)}return kt(t,e),t.prototype.children=["range"],t.prototype.compileNode=function(e){var t,n,i,r,s,o,a;return s=this.range,o=s.to,i=s.from,r=i&&i.compileToFragments(e,N)||[this.makeCode("0")],o&&(t=o.compileToFragments(e,N),n=st(t),(this.range.exclusive||-1!==+n)&&(a=", "+(this.range.exclusive?n:B.test(n)?""+(+n+1):(t=o.compileToFragments(e,T),"+"+st(t)+" + 1 || 9e9")))),[this.makeCode(".slice("+st(r)+(a||"")+")")]},t}(r),e.Obj=A=function(e){function n(e,t){this.generated=null!=t?t:!1,this.objects=this.properties=e||[]}return kt(n,e),n.prototype.children=["properties"],n.prototype.compileNode=function(e){var n,r,s,o,a,c,h,u,p,d,f,m,g,v,y,b,k,w,T,C,E;if(T=this.properties,this.generated)for(h=0,g=T.length;g>h;h++)b=T[h],b instanceof z&&b.error("cannot have an implicit value in an implicit object");for(r=p=0,v=T.length;v>p&&(w=T[r],!((w.variable||w).base instanceof O));r=++p);for(s=T.length>r,a=e.indent+=q,m=this.lastNonComment(this.properties),n=[],s&&(k=e.scope.freeVariable("obj"),n.push(this.makeCode("(\n"+a+k+" = "))),n.push(this.makeCode("{"+(0===T.length||0===r?"}":"\n"))),o=f=0,y=T.length;y>f;o=++f)w=T[o],o===r&&(0!==o&&n.push(this.makeCode("\n"+a+"}")),n.push(this.makeCode(",\n"))),u=o===T.length-1||o===r-1?"":w===m||w instanceof l?"\n":",\n",c=w instanceof l?"":a,s&&r>o&&(c+=q),w instanceof i&&w.variable instanceof z&&w.variable.hasProperties()&&w.variable.error("invalid object key"),w instanceof z&&w["this"]&&(w=new i(w.properties[0].name,w,"object")),w instanceof l||(r>o?(w instanceof i||(w=new i(w,w,"object")),(w.variable.base||w.variable).asKey=!0):(w instanceof i?(d=w.variable,E=w.value):(C=w.base.cache(e),d=C[0],E=C[1]),w=new i(new z(new x(k),[new t(d)]),E))),c&&n.push(this.makeCode(c)),n.push.apply(n,w.compileToFragments(e,L)),u&&n.push(this.makeCode(u));return s?n.push(this.makeCode(",\n"+a+k+"\n"+this.tab+")")):0!==T.length&&n.push(this.makeCode("\n"+this.tab+"}")),this.front&&!s?this.wrapInBraces(n):n},n.prototype.assigns=function(e){var t,n,i,r;for(r=this.properties,t=0,n=r.length;n>t;t++)if(i=r[t],i.assigns(e))return!0;return!1},n}(r),e.Arr=n=function(e){function t(e){this.objects=e||[]}return kt(t,e),t.prototype.children=["objects"],t.prototype.compileNode=function(e){var t,n,i,r,s,o,a;if(!this.objects.length)return[this.makeCode("[]")];if(e.indent+=q,t=G.compileSplattedArray(e,this.objects),t.length)return t;for(t=[],n=function(){var t,n,i,r;for(i=this.objects,r=[],t=0,n=i.length;n>t;t++)a=i[t],r.push(a.compileToFragments(e,E));return r}.call(this),r=s=0,o=n.length;o>s;r=++s)i=n[r],r&&t.push(this.makeCode(", ")),t.push.apply(t,i);return st(t).indexOf("\n")>=0?(t.unshift(this.makeCode("[\n"+e.indent)),t.push(this.makeCode("\n"+this.tab+"]"))):(t.unshift(this.makeCode("[")),t.push(this.makeCode("]"))),t},t.prototype.assigns=function(e){var t,n,i,r;for(r=this.objects,t=0,n=r.length;n>t;t++)if(i=r[t],i.assigns(e))return!0;return!1},t}(r),e.Class=a=function(e){function n(e,t,n){this.variable=e,this.parent=t,this.body=null!=n?n:new s,this.boundFuncs=[],this.body.classBody=!0}return kt(n,e),n.prototype.children=["variable","parent","body"],n.prototype.determineName=function(){var e,n,i;return this.variable?(n=this.variable.properties,i=n[n.length-1],e=i?i instanceof t&&i.name.value:this.variable.base.value,Tt.call(V,e)>=0&&this.variable.error("class variable name may not be "+e),e&&(e=g.test(e)&&e)):null},n.prototype.setContext=function(e){return this.body.traverseChildren(!1,function(t){return t.classBody?!1:t instanceof x&&"this"===t.value?t.value=e:t instanceof c&&t.bound?t.context=e:void 0})},n.prototype.addBoundFunctions=function(e){var n,i,r,s,o;for(o=this.boundFuncs,i=0,r=o.length;r>i;i++)n=o[i],s=new z(new x("this"),[new t(n)]).compile(e),this.ctor.body.unshift(new x(s+" = "+bt("bind",e)+"("+s+", this)"))},n.prototype.addProperties=function(e,n,r){var s,o,a,h,l,u;return u=e.base.properties.slice(0),h=function(){var e;for(e=[];o=u.shift();)o instanceof i&&(a=o.variable.base,delete o.context,l=o.value,"constructor"===a.value?(this.ctor&&o.error("cannot define more than one constructor in a class"),l.bound&&o.error("cannot define a constructor as a bound function"),l instanceof c?o=this.ctor=l:(this.externalCtor=r.classScope.freeVariable("class"),o=new i(new x(this.externalCtor),l))):o.variable["this"]?l["static"]=!0:(s=a.isComplex()?new w(a):new t(a),o.variable=new z(new x(n),[new t(new x("prototype")),s]),l instanceof c&&l.bound&&(this.boundFuncs.push(a),l.bound=!1))),e.push(o);return e}.call(this),et(h)},n.prototype.walkBody=function(e,t){return this.traverseChildren(!1,function(r){return function(o){var a,c,h,l,u,p,d;if(a=!0,o instanceof n)return!1;if(o instanceof s){for(d=c=o.expressions,h=l=0,u=d.length;u>l;h=++l)p=d[h],p instanceof i&&p.variable.looksStatic(e)?p.value["static"]=!0:p instanceof z&&p.isObject(!0)&&(a=!1,c[h]=r.addProperties(p,e,t));o.expressions=c=rt(c)}return a&&!(o instanceof n)}}(this))},n.prototype.hoistDirectivePrologue=function(){var e,t,n;for(t=0,e=this.body.expressions;(n=e[t])&&n instanceof l||n instanceof z&&n.isString();)++t;return this.directives=e.splice(0,t)},n.prototype.ensureConstructor=function(e){return this.ctor||(this.ctor=new c,this.externalCtor?this.ctor.body.push(new x(this.externalCtor+".apply(this, arguments)")):this.parent&&this.ctor.body.push(new x(e+".__super__.constructor.apply(this, arguments)")),this.ctor.body.makeReturn(),this.body.expressions.unshift(this.ctor)),this.ctor.ctor=this.ctor.name=e,this.ctor.klass=null,this.ctor.noReturn=!0},n.prototype.compileNode=function(e){var t,n,r,a,h,l,u,p,f;return(a=this.body.jumps())&&a.error("Class bodies cannot contain pure statements"),(n=this.body.contains(at))&&n.error("Class bodies shouldn't reference arguments"),u=this.determineName()||"_Class",u.reserved&&(u="_"+u),l=new x(u),r=new c([],s.wrap([this.body])),t=[],e.classScope=r.makeScope(e.scope),this.hoistDirectivePrologue(),this.setContext(u),this.walkBody(u,e),this.ensureConstructor(u),this.addBoundFunctions(e),this.body.spaced=!0,this.body.expressions.push(l),this.parent&&(f=new x(e.classScope.freeVariable("superClass",{reserve:!1})),this.body.expressions.unshift(new d(l,f)),r.params.push(new _(f)),t.push(this.parent)),(p=this.body.expressions).unshift.apply(p,this.directives),h=new O(new o(r,t)),this.variable&&(h=new i(this.variable,h)),h.compileToFragments(e)},n}(r),e.Assign=i=function(e){function n(e,t,n,i){var r,s,o;this.variable=e,this.value=t,this.context=n,this.param=i&&i.param,this.subpattern=i&&i.subpattern,o=s=this.variable.unwrapAll().value,r=Tt.call(V,o)>=0,r&&"object"!==this.context&&this.variable.error('variable name may not be "'+s+'"')}return kt(n,e),n.prototype.children=["variable","value"],n.prototype.isStatement=function(e){return(null!=e?e.level:void 0)===L&&null!=this.context&&Tt.call(this.context,"?")>=0
},n.prototype.assigns=function(e){return this["object"===this.context?"value":"variable"].assigns(e)},n.prototype.unfoldSoak=function(e){return yt(e,this,"variable")},n.prototype.compileNode=function(e){var t,n,i,r,s,o,a,h,l,u,p,d,f,m;if(i=this.variable instanceof z){if(this.variable.isArray()||this.variable.isObject())return this.compilePatternMatch(e);if(this.variable.isSplice())return this.compileSplice(e);if("||="===(h=this.context)||"&&="===h||"?="===h)return this.compileConditional(e);if("**="===(l=this.context)||"//="===l||"%%="===l)return this.compileSpecialMath(e)}return this.value instanceof c&&(this.value["static"]?(this.value.klass=this.variable.base,this.value.name=this.variable.properties[0],this.value.variable=this.variable):(null!=(u=this.variable.properties)?u.length:void 0)>=2&&(p=this.variable.properties,o=p.length>=3?Ct.call(p,0,r=p.length-2):(r=0,[]),a=p[r++],s=p[r++],"prototype"===(null!=(d=a.name)?d.value:void 0)&&(this.value.klass=new z(this.variable.base,o),this.value.name=s,this.value.variable=this.variable))),this.context||(m=this.variable.unwrapAll(),m.isAssignable()||this.variable.error('"'+this.variable.compile(e)+'" cannot be assigned'),("function"==typeof m.hasProperties?m.hasProperties():void 0)||(this.param?e.scope.add(m.value,"var"):e.scope.find(m.value))),f=this.value.compileToFragments(e,E),n=this.variable.compileToFragments(e,E),"object"===this.context?n.concat(this.makeCode(": "),f):(t=n.concat(this.makeCode(" "+(this.context||"=")+" "),f),E>=e.level?t:this.wrapInBraces(t))},n.prototype.compilePatternMatch=function(e){var i,r,s,o,a,c,h,l,u,d,f,m,v,y,b,k,T,C,N,S,D,R,A,I,_,j,M,B;if(I=e.level===L,j=this.value,y=this.variable.base.objects,!(b=y.length))return s=j.compileToFragments(e),e.level>=F?this.wrapInBraces(s):s;if(l=this.variable.isObject(),I&&1===b&&!((v=y[0])instanceof G))return v instanceof n?(T=v,C=T.variable,h=C.base,v=T.value):h=l?v["this"]?v.properties[0].name:v:new x(0),i=g.test(h.unwrap().value||0),j=new z(j),j.properties.push(new(i?t:w)(h)),N=v.unwrap().value,Tt.call($,N)>=0&&v.error("assignment to a reserved word: "+v.compile(e)),new n(v,j,null,{param:this.param}).compileToFragments(e,L);for(M=j.compileToFragments(e,E),B=st(M),r=[],o=!1,(!g.test(B)||this.variable.assigns(B))&&(r.push([this.makeCode((k=e.scope.freeVariable("ref"))+" = ")].concat(Ct.call(M))),M=[this.makeCode(k)],B=k),c=d=0,f=y.length;f>d;c=++d){if(v=y[c],h=c,l&&(v instanceof n?(S=v,D=S.variable,h=D.base,v=S.value):v.base instanceof O?(R=new z(v.unwrapAll()).cacheReference(e),v=R[0],h=R[1]):h=v["this"]?v.properties[0].name:v),!o&&v instanceof G)m=v.name.unwrap().value,v=v.unwrap(),_=b+" <= "+B+".length ? "+bt("slice",e)+".call("+B+", "+c,(A=b-c-1)?(u=e.scope.freeVariable("i",{single:!0}),_+=", "+u+" = "+B+".length - "+A+") : ("+u+" = "+c+", [])"):_+=") : []",_=new x(_),o=u+"++";else{if(!o&&v instanceof p){(A=b-c-1)&&(1===A?o=B+".length - 1":(u=e.scope.freeVariable("i",{single:!0}),_=new x(u+" = "+B+".length - "+A),o=u+"++",r.push(_.compileToFragments(e,E))));continue}m=v.unwrap().value,(v instanceof G||v instanceof p)&&v.error("multiple splats/expansions are disallowed in an assignment"),"number"==typeof h?(h=new x(o||h),i=!1):i=l&&g.test(h.unwrap().value||0),_=new z(new x(B),[new(i?t:w)(h)])}null!=m&&Tt.call($,m)>=0&&v.error("assignment to a reserved word: "+v.compile(e)),r.push(new n(v,_,null,{param:this.param,subpattern:!0}).compileToFragments(e,E))}return I||this.subpattern||r.push(M),a=this.joinFragmentArrays(r,", "),E>e.level?a:this.wrapInBraces(a)},n.prototype.compileConditional=function(e){var t,i,r,s;return r=this.variable.cacheReference(e),i=r[0],s=r[1],!i.properties.length&&i.base instanceof x&&"this"!==i.base.value&&!e.scope.check(i.base.value)&&this.variable.error('the variable "'+i.base.value+"\" can't be assigned with "+this.context+" because it has not been declared before"),Tt.call(this.context,"?")>=0?(e.isExistentialEquals=!0,new b(new u(i),s,{type:"if"}).addElse(new n(s,this.value,"=")).compileToFragments(e)):(t=new I(this.context.slice(0,-1),i,new n(s,this.value,"=")).compileToFragments(e),E>=e.level?t:this.wrapInBraces(t))},n.prototype.compileSpecialMath=function(e){var t,i,r;return i=this.variable.cacheReference(e),t=i[0],r=i[1],new n(t,new I(this.context.slice(0,-1),r,this.value)).compileToFragments(e)},n.prototype.compileSplice=function(e){var t,n,i,r,s,o,a,c,h,l,u,p;return a=this.variable.properties.pop().range,i=a.from,l=a.to,n=a.exclusive,o=this.variable.compile(e),i?(c=this.cacheToCodeFragments(i.cache(e,F)),r=c[0],s=c[1]):r=s="0",l?i instanceof z&&i.isSimpleNumber()&&l instanceof z&&l.isSimpleNumber()?(l=l.compile(e)-s,n||(l+=1)):(l=l.compile(e,T)+" - "+s,n||(l+=" + 1")):l="9e9",h=this.value.cache(e,E),u=h[0],p=h[1],t=[].concat(this.makeCode("[].splice.apply("+o+", ["+r+", "+l+"].concat("),u,this.makeCode(")), "),p),e.level>L?this.wrapInBraces(t):t},n}(r),e.Code=c=function(e){function t(e,t,n){this.params=e||[],this.body=t||new s,this.bound="boundfunc"===n,this.isGenerator=!!this.body.contains(function(e){var t;return e instanceof I&&("yield"===(t=e.operator)||"yield*"===t)})}return kt(t,e),t.prototype.children=["params","body"],t.prototype.isStatement=function(){return!!this.ctor},t.prototype.jumps=D,t.prototype.makeScope=function(e){return new P(e,this.body,this)},t.prototype.compileNode=function(e){var r,a,c,h,l,u,d,f,m,g,v,y,k,w,C,E,F,N,L,S,D,R,A,O,$,j,M,B,V,P,U,G,H;if(this.bound&&(null!=(A=e.scope.method)?A.bound:void 0)&&(this.context=e.scope.method.context),this.bound&&!this.context)return this.context="_this",H=new t([new _(new x(this.context))],new s([this])),a=new o(H,[new x("this")]),a.updateLocationDataIfMissing(this.locationData),a.compileNode(e);for(e.scope=tt(e,"classScope")||this.makeScope(e.scope),e.scope.shared=tt(e,"sharedScope"),e.indent+=q,delete e.bare,delete e.isExistentialEquals,L=[],h=[],O=this.params,u=0,m=O.length;m>u;u++)N=O[u],N instanceof p||e.scope.parameter(N.asReference(e));for($=this.params,d=0,g=$.length;g>d;d++)if(N=$[d],N.splat||N instanceof p){for(j=this.params,f=0,v=j.length;v>f;f++)F=j[f],F instanceof p||!F.name.value||e.scope.add(F.name.value,"var",!0);V=new i(new z(new n(function(){var t,n,i,r;for(i=this.params,r=[],n=0,t=i.length;t>n;n++)F=i[n],r.push(F.asReference(e));return r}.call(this))),new z(new x("arguments")));break}for(M=this.params,E=0,y=M.length;y>E;E++)N=M[E],N.isComplex()?(U=R=N.asReference(e),N.value&&(U=new I("?",R,N.value)),h.push(new i(new z(N.name),U,"=",{param:!0}))):(R=N,N.value&&(C=new x(R.name.value+" == null"),U=new i(new z(N.name),N.value,"="),h.push(new b(C,U)))),V||L.push(R);for(G=this.body.isEmpty(),V&&h.unshift(V),h.length&&(B=this.body.expressions).unshift.apply(B,h),l=S=0,k=L.length;k>S;l=++S)F=L[l],L[l]=F.compileToFragments(e),e.scope.parameter(st(L[l]));for(P=[],this.eachParamName(function(e,t){return Tt.call(P,e)>=0&&t.error("multiple parameters named "+e),P.push(e)}),G||this.noReturn||this.body.makeReturn(),c="function",this.isGenerator&&(c+="*"),this.ctor&&(c+=" "+this.name),c+="(",r=[this.makeCode(c)],l=D=0,w=L.length;w>D;l=++D)F=L[l],l&&r.push(this.makeCode(", ")),r.push.apply(r,F);return r.push(this.makeCode(") {")),this.body.isEmpty()||(r=r.concat(this.makeCode("\n"),this.body.compileWithDeclarations(e),this.makeCode("\n"+this.tab))),r.push(this.makeCode("}")),this.ctor?[this.makeCode(this.tab)].concat(Ct.call(r)):this.front||e.level>=T?this.wrapInBraces(r):r},t.prototype.eachParamName=function(e){var t,n,i,r,s;for(r=this.params,s=[],t=0,n=r.length;n>t;t++)i=r[t],s.push(i.eachName(e));return s},t.prototype.traverseChildren=function(e,n){return e?t.__super__.traverseChildren.call(this,e,n):void 0},t}(r),e.Param=_=function(e){function t(e,t,n){var i,r;this.name=e,this.value=t,this.splat=n,r=i=this.name.unwrapAll().value,Tt.call(V,r)>=0&&this.name.error('parameter name "'+i+'" is not allowed')}return kt(t,e),t.prototype.children=["name","value"],t.prototype.compileToFragments=function(e){return this.name.compileToFragments(e,E)},t.prototype.asReference=function(e){var t,n;return this.reference?this.reference:(n=this.name,n["this"]?(t=n.properties[0].name.value,t.reserved&&(t="_"+t),n=new x(e.scope.freeVariable(t))):n.isComplex()&&(n=new x(e.scope.freeVariable("arg"))),n=new z(n),this.splat&&(n=new G(n)),n.updateLocationDataIfMissing(this.locationData),this.reference=n)},t.prototype.isComplex=function(){return this.name.isComplex()},t.prototype.eachName=function(e,t){var n,r,s,o,a,c;if(null==t&&(t=this.name),n=function(t){return e("@"+t.properties[0].name.value,t)},t instanceof x)return e(t.value,t);if(t instanceof z)return n(t);for(c=t.objects,r=0,s=c.length;s>r;r++)a=c[r],a instanceof i?this.eachName(e,a.value.unwrap()):a instanceof G?(o=a.name.unwrap(),e(o.value,o)):a instanceof z?a.isArray()||a.isObject()?this.eachName(e,a.base):a["this"]?n(a):e(a.base.value,a.base):a instanceof p||a.error("illegal parameter "+a.compile())},t}(r),e.Splat=G=function(e){function t(e){this.name=e.compile?e:new x(e)}return kt(t,e),t.prototype.children=["name"],t.prototype.isAssignable=Q,t.prototype.assigns=function(e){return this.name.assigns(e)},t.prototype.compileToFragments=function(e){return this.name.compileToFragments(e)},t.prototype.unwrap=function(){return this.name},t.compileSplattedArray=function(e,n,i){var r,s,o,a,c,h,l,u,p,d,f;for(l=-1;(f=n[++l])&&!(f instanceof t););if(l>=n.length)return[];if(1===n.length)return f=n[0],c=f.compileToFragments(e,E),i?c:[].concat(f.makeCode(bt("slice",e)+".call("),c,f.makeCode(")"));for(r=n.slice(l),h=u=0,d=r.length;d>u;h=++u)f=r[h],o=f.compileToFragments(e,E),r[h]=f instanceof t?[].concat(f.makeCode(bt("slice",e)+".call("),o,f.makeCode(")")):[].concat(f.makeCode("["),o,f.makeCode("]"));return 0===l?(f=n[0],a=f.joinFragmentArrays(r.slice(1),", "),r[0].concat(f.makeCode(".concat("),a,f.makeCode(")"))):(s=function(){var t,i,r,s;for(r=n.slice(0,l),s=[],t=0,i=r.length;i>t;t++)f=r[t],s.push(f.compileToFragments(e,E));return s}(),s=n[0].joinFragmentArrays(s,", "),a=n[l].joinFragmentArrays(r,", "),p=n[n.length-1],[].concat(n[0].makeCode("["),s,n[l].makeCode("].concat("),a,p.makeCode(")")))},t}(r),e.Expansion=p=function(e){function t(){return t.__super__.constructor.apply(this,arguments)}return kt(t,e),t.prototype.isComplex=D,t.prototype.compileNode=function(){return this.error("Expansion must be used inside a destructuring assignment or parameter list")},t.prototype.asReference=function(){return this},t.prototype.eachName=function(){},t}(r),e.While=J=function(e){function t(e,t){this.condition=(null!=t?t.invert:void 0)?e.invert():e,this.guard=null!=t?t.guard:void 0}return kt(t,e),t.prototype.children=["condition","guard","body"],t.prototype.isStatement=Q,t.prototype.makeReturn=function(e){return e?t.__super__.makeReturn.apply(this,arguments):(this.returns=!this.jumps({loop:!0}),this)},t.prototype.addBody=function(e){return this.body=e,this},t.prototype.jumps=function(){var e,t,n,i,r;if(e=this.body.expressions,!e.length)return!1;for(t=0,i=e.length;i>t;t++)if(r=e[t],n=r.jumps({loop:!0}))return n;return!1},t.prototype.compileNode=function(e){var t,n,i,r;return e.indent+=q,r="",n=this.body,n.isEmpty()?n=this.makeCode(""):(this.returns&&(n.makeReturn(i=e.scope.freeVariable("results")),r=""+this.tab+i+" = [];\n"),this.guard&&(n.expressions.length>1?n.expressions.unshift(new b(new O(this.guard).invert(),new x("continue"))):this.guard&&(n=s.wrap([new b(this.guard,n)]))),n=[].concat(this.makeCode("\n"),n.compileToFragments(e,L),this.makeCode("\n"+this.tab))),t=[].concat(this.makeCode(r+this.tab+"while ("),this.condition.compileToFragments(e,N),this.makeCode(") {"),n,this.makeCode("}")),this.returns&&t.push(this.makeCode("\n"+this.tab+"return "+i+";")),t},t}(r),e.Op=I=function(e){function n(e,t,n,i){if("in"===e)return new k(t,n);if("do"===e)return this.generateDo(t);if("new"===e){if(t instanceof o&&!t["do"]&&!t.isNew)return t.newInstance();(t instanceof c&&t.bound||t["do"])&&(t=new O(t))}return this.operator=r[e]||e,this.first=t,this.second=n,this.flip=!!i,this}var r,s;return kt(n,e),r={"==":"===","!=":"!==",of:"in",yieldfrom:"yield*"},s={"!==":"===","===":"!=="},n.prototype.children=["first","second"],n.prototype.isSimpleNumber=D,n.prototype.isYield=function(){var e;return"yield"===(e=this.operator)||"yield*"===e},n.prototype.isYieldReturn=function(){return this.isYield()&&this.first instanceof M},n.prototype.isUnary=function(){return!this.second},n.prototype.isComplex=function(){var e;return!(this.isUnary()&&("+"===(e=this.operator)||"-"===e)&&this.first instanceof z&&this.first.isSimpleNumber())},n.prototype.isChainable=function(){var e;return"<"===(e=this.operator)||">"===e||">="===e||"<="===e||"==="===e||"!=="===e},n.prototype.invert=function(){var e,t,i,r,o;if(this.isChainable()&&this.first.isChainable()){for(e=!0,t=this;t&&t.operator;)e&&(e=t.operator in s),t=t.first;if(!e)return new O(this).invert();for(t=this;t&&t.operator;)t.invert=!t.invert,t.operator=s[t.operator],t=t.first;return this}return(r=s[this.operator])?(this.operator=r,this.first.unwrap()instanceof n&&this.first.invert(),this):this.second?new O(this).invert():"!"===this.operator&&(i=this.first.unwrap())instanceof n&&("!"===(o=i.operator)||"in"===o||"instanceof"===o)?i:new n("!",this)},n.prototype.unfoldSoak=function(e){var t;return("++"===(t=this.operator)||"--"===t||"delete"===t)&&yt(e,this,"first")},n.prototype.generateDo=function(e){var t,n,r,s,a,h,l,u;for(h=[],n=e instanceof i&&(l=e.value.unwrap())instanceof c?l:e,u=n.params||[],r=0,s=u.length;s>r;r++)a=u[r],a.value?(h.push(a.value),delete a.value):h.push(a);return t=new o(e,h),t["do"]=!0,t},n.prototype.compileNode=function(e){var t,n,i,r,s,o;if(n=this.isChainable()&&this.first.isChainable(),n||(this.first.front=this.front),"delete"===this.operator&&e.scope.check(this.first.unwrapAll().value)&&this.error("delete operand may not be argument or var"),("--"===(r=this.operator)||"++"===r)&&(s=this.first.unwrapAll().value,Tt.call(V,s)>=0)&&this.error('cannot increment/decrement "'+this.first.unwrapAll().value+'"'),this.isYield())return this.compileYield(e);if(this.isUnary())return this.compileUnary(e);if(n)return this.compileChain(e);switch(this.operator){case"?":return this.compileExistence(e);case"**":return this.compilePower(e);case"//":return this.compileFloorDivision(e);case"%%":return this.compileModulo(e);default:return i=this.first.compileToFragments(e,F),o=this.second.compileToFragments(e,F),t=[].concat(i,this.makeCode(" "+this.operator+" "),o),F>=e.level?t:this.wrapInBraces(t)}},n.prototype.compileChain=function(e){var t,n,i,r;return i=this.first.second.cache(e),this.first.second=i[0],r=i[1],n=this.first.compileToFragments(e,F),t=n.concat(this.makeCode(" "+(this.invert?"&&":"||")+" "),r.compileToFragments(e),this.makeCode(" "+this.operator+" "),this.second.compileToFragments(e,F)),this.wrapInBraces(t)},n.prototype.compileExistence=function(e){var t,n;return this.first.isComplex()?(n=new x(e.scope.freeVariable("ref")),t=new O(new i(n,this.first))):(t=this.first,n=t),new b(new u(t),n,{type:"if"}).addElse(this.second).compileToFragments(e)},n.prototype.compileUnary=function(e){var t,i,r;return i=[],t=this.operator,i.push([this.makeCode(t)]),"!"===t&&this.first instanceof u?(this.first.negated=!this.first.negated,this.first.compileToFragments(e)):e.level>=T?new O(this).compileToFragments(e):(r="+"===t||"-"===t,("new"===t||"typeof"===t||"delete"===t||r&&this.first instanceof n&&this.first.operator===t)&&i.push([this.makeCode(" ")]),(r&&this.first instanceof n||"new"===t&&this.first.isStatement(e))&&(this.first=new O(this.first)),i.push(this.first.compileToFragments(e,F)),this.flip&&i.reverse(),this.joinFragmentArrays(i,""))},n.prototype.compileYield=function(e){var t,n;return n=[],t=this.operator,null==e.scope.parent&&this.error("yield statements must occur within a function generator."),Tt.call(Object.keys(this.first),"expression")>=0&&!(this.first instanceof W)?this.isYieldReturn()?n.push(this.first.compileToFragments(e,L)):null!=this.first.expression&&n.push(this.first.expression.compileToFragments(e,F)):(n.push([this.makeCode("("+t+" ")]),n.push(this.first.compileToFragments(e,F)),n.push([this.makeCode(")")])),this.joinFragmentArrays(n,"")},n.prototype.compilePower=function(e){var n;return n=new z(new x("Math"),[new t(new x("pow"))]),new o(n,[this.first,this.second]).compileToFragments(e)},n.prototype.compileFloorDivision=function(e){var i,r;return r=new z(new x("Math"),[new t(new x("floor"))]),i=new n("/",this.first,this.second),new o(r,[i]).compileToFragments(e)},n.prototype.compileModulo=function(e){var t;return t=new z(new x(bt("modulo",e))),new o(t,[this.first,this.second]).compileToFragments(e)},n.prototype.toString=function(e){return n.__super__.toString.call(this,e,this.constructor.name+" "+this.operator)},n}(r),e.In=k=function(e){function t(e,t){this.object=e,this.array=t}return kt(t,e),t.prototype.children=["object","array"],t.prototype.invert=S,t.prototype.compileNode=function(e){var t,n,i,r,s;if(this.array instanceof z&&this.array.isArray()&&this.array.base.objects.length){for(s=this.array.base.objects,n=0,i=s.length;i>n;n++)if(r=s[n],r instanceof G){t=!0;break}if(!t)return this.compileOrTest(e)}return this.compileLoopTest(e)},t.prototype.compileOrTest=function(e){var t,n,i,r,s,o,a,c,h,l,u,p;for(c=this.object.cache(e,F),u=c[0],a=c[1],h=this.negated?[" !== "," && "]:[" === "," || "],t=h[0],n=h[1],p=[],l=this.array.base.objects,i=s=0,o=l.length;o>s;i=++s)r=l[i],i&&p.push(this.makeCode(n)),p=p.concat(i?a:u,this.makeCode(t),r.compileToFragments(e,T));return F>e.level?p:this.wrapInBraces(p)},t.prototype.compileLoopTest=function(e){var t,n,i,r;return i=this.object.cache(e,E),r=i[0],n=i[1],t=[].concat(this.makeCode(bt("indexOf",e)+".call("),this.array.compileToFragments(e,E),this.makeCode(", "),n,this.makeCode(") "+(this.negated?"< 0":">= 0"))),st(r)===st(n)?t:(t=r.concat(this.makeCode(", "),t),E>e.level?t:this.wrapInBraces(t))},t.prototype.toString=function(e){return t.__super__.toString.call(this,e,this.constructor.name+(this.negated?"!":""))},t}(r),e.Try=Y=function(e){function t(e,t,n,i){this.attempt=e,this.errorVariable=t,this.recovery=n,this.ensure=i}return kt(t,e),t.prototype.children=["attempt","recovery","ensure"],t.prototype.isStatement=Q,t.prototype.jumps=function(e){var t;return this.attempt.jumps(e)||(null!=(t=this.recovery)?t.jumps(e):void 0)},t.prototype.makeReturn=function(e){return this.attempt&&(this.attempt=this.attempt.makeReturn(e)),this.recovery&&(this.recovery=this.recovery.makeReturn(e)),this},t.prototype.compileNode=function(e){var t,n,r,s;return e.indent+=q,s=this.attempt.compileToFragments(e,L),t=this.recovery?(r=new x("_error"),this.errorVariable?this.recovery.unshift(new i(this.errorVariable,r)):void 0,[].concat(this.makeCode(" catch ("),r.compileToFragments(e),this.makeCode(") {\n"),this.recovery.compileToFragments(e,L),this.makeCode("\n"+this.tab+"}"))):this.ensure||this.recovery?[]:[this.makeCode(" catch (_error) {}")],n=this.ensure?[].concat(this.makeCode(" finally {\n"),this.ensure.compileToFragments(e,L),this.makeCode("\n"+this.tab+"}")):[],[].concat(this.makeCode(this.tab+"try {\n"),s,this.makeCode("\n"+this.tab+"}"),t,n)},t}(r),e.Throw=W=function(e){function t(e){this.expression=e}return kt(t,e),t.prototype.children=["expression"],t.prototype.isStatement=Q,t.prototype.jumps=D,t.prototype.makeReturn=X,t.prototype.compileNode=function(e){return[].concat(this.makeCode(this.tab+"throw "),this.expression.compileToFragments(e),this.makeCode(";"))},t}(r),e.Existence=u=function(e){function t(e){this.expression=e}return kt(t,e),t.prototype.children=["expression"],t.prototype.invert=S,t.prototype.compileNode=function(e){var t,n,i,r;return this.expression.front=this.front,i=this.expression.compile(e,F),g.test(i)&&!e.scope.check(i)?(r=this.negated?["===","||"]:["!==","&&"],t=r[0],n=r[1],i="typeof "+i+" "+t+' "undefined" '+n+" "+i+" "+t+" null"):i=i+" "+(this.negated?"==":"!=")+" null",[this.makeCode(C>=e.level?i:"("+i+")")]},t}(r),e.Parens=O=function(e){function t(e){this.body=e}return kt(t,e),t.prototype.children=["body"],t.prototype.unwrap=function(){return this.body},t.prototype.isComplex=function(){return this.body.isComplex()},t.prototype.compileNode=function(e){var t,n,i;return n=this.body.unwrap(),n instanceof z&&n.isAtomic()?(n.front=this.front,n.compileToFragments(e)):(i=n.compileToFragments(e,N),t=F>e.level&&(n instanceof I||n instanceof o||n instanceof f&&n.returns),t?i:this.wrapInBraces(i))},t}(r),e.For=f=function(e){function t(e,t){var n;this.source=t.source,this.guard=t.guard,this.step=t.step,this.name=t.name,this.index=t.index,this.body=s.wrap([e]),this.own=!!t.own,this.object=!!t.object,this.object&&(n=[this.index,this.name],this.name=n[0],this.index=n[1]),this.index instanceof z&&this.index.error("index cannot be a pattern matching expression"),this.range=this.source instanceof z&&this.source.base instanceof j&&!this.source.properties.length,this.pattern=this.name instanceof z,this.range&&this.index&&this.index.error("indexes do not apply to range loops"),this.range&&this.pattern&&this.name.error("cannot pattern match over range loops"),this.own&&!this.object&&this.name.error("cannot use own with for-in"),this.returns=!1}return kt(t,e),t.prototype.children=["body","source","guard","step"],t.prototype.compileNode=function(e){var t,n,r,o,a,c,h,l,u,p,d,f,m,v,y,k,w,T,C,F,N,S,D,A,I,_,$,j,B,V,P,U,G,H;return t=s.wrap([this.body]),D=t.expressions,T=D[D.length-1],(null!=T?T.jumps():void 0)instanceof M&&(this.returns=!1),B=this.range?this.source.base:this.source,j=e.scope,this.pattern||(F=this.name&&this.name.compile(e,E)),v=this.index&&this.index.compile(e,E),F&&!this.pattern&&j.find(F),v&&j.find(v),this.returns&&($=j.freeVariable("results")),y=this.object&&v||j.freeVariable("i",{single:!0}),k=this.range&&F||v||y,w=k!==y?k+" = ":"",this.step&&!this.range&&(A=this.cacheToCodeFragments(this.step.cache(e,E,ot)),V=A[0],U=A[1],P=U.match(R)),this.pattern&&(F=y),H="",d="",h="",f=this.tab+q,this.range?p=B.compileToFragments(lt(e,{index:y,name:F,step:this.step,isComplex:ot})):(G=this.source.compile(e,E),!F&&!this.own||g.test(G)||(h+=""+this.tab+(S=j.freeVariable("ref"))+" = "+G+";\n",G=S),F&&!this.pattern&&(N=F+" = "+G+"["+k+"]"),this.object||(V!==U&&(h+=""+this.tab+V+";\n"),this.step&&P&&(u=0>pt(P[0]))||(C=j.freeVariable("len")),a=""+w+y+" = 0, "+C+" = "+G+".length",c=""+w+y+" = "+G+".length - 1",r=y+" < "+C,o=y+" >= 0",this.step?(P?u&&(r=o,a=c):(r=U+" > 0 ? "+r+" : "+o,a="("+U+" > 0 ? ("+a+") : "+c+")"),m=y+" += "+U):m=""+(k!==y?"++"+y:y+"++"),p=[this.makeCode(a+"; "+r+"; "+w+m)])),this.returns&&(I=""+this.tab+$+" = [];\n",_="\n"+this.tab+"return "+$+";",t.makeReturn($)),this.guard&&(t.expressions.length>1?t.expressions.unshift(new b(new O(this.guard).invert(),new x("continue"))):this.guard&&(t=s.wrap([new b(this.guard,t)]))),this.pattern&&t.expressions.unshift(new i(this.name,new x(G+"["+k+"]"))),l=[].concat(this.makeCode(h),this.pluckDirectCall(e,t)),N&&(H="\n"+f+N+";"),this.object&&(p=[this.makeCode(k+" in "+G)],this.own&&(d="\n"+f+"if (!"+bt("hasProp",e)+".call("+G+", "+k+")) continue;")),n=t.compileToFragments(lt(e,{indent:f}),L),n&&n.length>0&&(n=[].concat(this.makeCode("\n"),n,this.makeCode("\n"))),[].concat(l,this.makeCode(""+(I||"")+this.tab+"for ("),p,this.makeCode(") {"+d+H),n,this.makeCode(this.tab+"}"+(_||"")))},t.prototype.pluckDirectCall=function(e,t){var n,r,s,a,h,l,u,p,d,f,m,g,v,y,b,k;for(r=[],d=t.expressions,h=l=0,u=d.length;u>l;h=++l)s=d[h],s=s.unwrapAll(),s instanceof o&&(k=null!=(f=s.variable)?f.unwrapAll():void 0,(k instanceof c||k instanceof z&&(null!=(m=k.base)?m.unwrapAll():void 0)instanceof c&&1===k.properties.length&&("call"===(g=null!=(v=k.properties[0].name)?v.value:void 0)||"apply"===g))&&(a=(null!=(y=k.base)?y.unwrapAll():void 0)||k,p=new x(e.scope.freeVariable("fn")),n=new z(p),k.base&&(b=[n,k],k.base=b[0],n=b[1]),t.expressions[h]=new o(n,s.args),r=r.concat(this.makeCode(this.tab),new i(p,a).compileToFragments(e,L),this.makeCode(";\n"))));return r},t}(J),e.Switch=H=function(e){function t(e,t,n){this.subject=e,this.cases=t,this.otherwise=n}return kt(t,e),t.prototype.children=["subject","cases","otherwise"],t.prototype.isStatement=Q,t.prototype.jumps=function(e){var t,n,i,r,s,o,a,c;for(null==e&&(e={block:!0}),o=this.cases,i=0,s=o.length;s>i;i++)if(a=o[i],n=a[0],t=a[1],r=t.jumps(e))return r;return null!=(c=this.otherwise)?c.jumps(e):void 0},t.prototype.makeReturn=function(e){var t,n,i,r,o;for(r=this.cases,t=0,n=r.length;n>t;t++)i=r[t],i[1].makeReturn(e);return e&&(this.otherwise||(this.otherwise=new s([new x("void 0")]))),null!=(o=this.otherwise)&&o.makeReturn(e),this},t.prototype.compileNode=function(e){var t,n,i,r,s,o,a,c,h,l,u,p,d,f,m,g;for(c=e.indent+q,h=e.indent=c+q,o=[].concat(this.makeCode(this.tab+"switch ("),this.subject?this.subject.compileToFragments(e,N):this.makeCode("false"),this.makeCode(") {\n")),f=this.cases,a=l=0,p=f.length;p>l;a=++l){for(m=f[a],r=m[0],t=m[1],g=rt([r]),u=0,d=g.length;d>u;u++)i=g[u],this.subject||(i=i.invert()),o=o.concat(this.makeCode(c+"case "),i.compileToFragments(e,N),this.makeCode(":\n"));if((n=t.compileToFragments(e,L)).length>0&&(o=o.concat(n,this.makeCode("\n"))),a===this.cases.length-1&&!this.otherwise)break;s=this.lastNonComment(t.expressions),s instanceof M||s instanceof x&&s.jumps()&&"debugger"!==s.value||o.push(i.makeCode(h+"break;\n"))}return this.otherwise&&this.otherwise.expressions.length&&o.push.apply(o,[this.makeCode(c+"default:\n")].concat(Ct.call(this.otherwise.compileToFragments(e,L)),[this.makeCode("\n")])),o.push(this.makeCode(this.tab+"}")),o},t}(r),e.If=b=function(e){function t(e,t,n){this.body=t,null==n&&(n={}),this.condition="unless"===n.type?e.invert():e,this.elseBody=null,this.isChain=!1,this.soak=n.soak}return kt(t,e),t.prototype.children=["condition","body","elseBody"],t.prototype.bodyNode=function(){var e;return null!=(e=this.body)?e.unwrap():void 0},t.prototype.elseBodyNode=function(){var e;return null!=(e=this.elseBody)?e.unwrap():void 0},t.prototype.addElse=function(e){return this.isChain?this.elseBodyNode().addElse(e):(this.isChain=e instanceof t,this.elseBody=this.ensureBlock(e),this.elseBody.updateLocationDataIfMissing(e.locationData)),this},t.prototype.isStatement=function(e){var t;return(null!=e?e.level:void 0)===L||this.bodyNode().isStatement(e)||(null!=(t=this.elseBodyNode())?t.isStatement(e):void 0)},t.prototype.jumps=function(e){var t;return this.body.jumps(e)||(null!=(t=this.elseBody)?t.jumps(e):void 0)},t.prototype.compileNode=function(e){return this.isStatement(e)?this.compileStatement(e):this.compileExpression(e)},t.prototype.makeReturn=function(e){return e&&(this.elseBody||(this.elseBody=new s([new x("void 0")]))),this.body&&(this.body=new s([this.body.makeReturn(e)])),this.elseBody&&(this.elseBody=new s([this.elseBody.makeReturn(e)])),this},t.prototype.ensureBlock=function(e){return e instanceof s?e:new s([e])},t.prototype.compileStatement=function(e){var n,i,r,s,o,a,c;return r=tt(e,"chainChild"),(o=tt(e,"isExistentialEquals"))?new t(this.condition.invert(),this.elseBodyNode(),{type:"if"}).compileToFragments(e):(c=e.indent+q,s=this.condition.compileToFragments(e,N),i=this.ensureBlock(this.body).compileToFragments(lt(e,{indent:c})),a=[].concat(this.makeCode("if ("),s,this.makeCode(") {\n"),i,this.makeCode("\n"+this.tab+"}")),r||a.unshift(this.makeCode(this.tab)),this.elseBody?(n=a.concat(this.makeCode(" else ")),this.isChain?(e.chainChild=!0,n=n.concat(this.elseBody.unwrap().compileToFragments(e,L))):n=n.concat(this.makeCode("{\n"),this.elseBody.compileToFragments(lt(e,{indent:c}),L),this.makeCode("\n"+this.tab+"}")),n):a)},t.prototype.compileExpression=function(e){var t,n,i,r;return i=this.condition.compileToFragments(e,C),n=this.bodyNode().compileToFragments(e,E),t=this.elseBodyNode()?this.elseBodyNode().compileToFragments(e,E):[this.makeCode("void 0")],r=i.concat(this.makeCode(" ? "),n,this.makeCode(" : "),t),e.level>=C?this.wrapInBraces(r):r},t.prototype.unfoldSoak=function(){return this.soak&&this},t}(r),K={extend:function(e){return"function(child, parent) { for (var key in parent) { if ("+bt("hasProp",e)+".call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; }"},bind:function(){return"function(fn, me){ return function(){ return fn.apply(me, arguments); }; }"},indexOf:function(){return"[].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; }"},modulo:function(){return"function(a, b) { return (+a % (b = +b) + b) % b; }"},hasProp:function(){return"{}.hasOwnProperty"},slice:function(){return"[].slice"}},L=1,N=2,E=3,C=4,F=5,T=6,q="  ",g=/^(?!\d)[$\w\x7f-\uffff]+$/,B=/^[+-]?\d+$/,m=/^[+-]?0x[\da-f]+/i,R=/^[+-]?(?:0x[\da-f]+|\d*\.?\d+(?:e[+-]?\d+)?)$/i,y=/^['"]/,v=/^\//,bt=function(e,t){var n,i;return i=t.scope.root,e in i.utilities?i.utilities[e]:(n=i.freeVariable(e),i.assign(n,K[e](t)),i.utilities[e]=n)},ut=function(e,t){return e=e.replace(/\n/g,"$&"+t),e.replace(/\s+$/,"")},pt=function(e){return null==e?0:e.match(m)?parseInt(e,16):parseFloat(e)},at=function(e){return e instanceof x&&"arguments"===e.value&&!e.asKey},ct=function(e){return e instanceof x&&"this"===e.value&&!e.asKey||e instanceof c&&e.bound||e instanceof o&&e.isSuper},ot=function(e){return e.isComplex()||("function"==typeof e.isAssignable?e.isAssignable():void 0)},yt=function(e,t,n){var i;if(i=t[n].unfoldSoak(e))return t[n]=i.body,i.body=new z(t),i}}.call(this),t.exports}(),_dereq_["./sourcemap"]=function(){var e={},t={exports:e};return function(){var e,n;e=function(){function e(e){this.line=e,this.columns=[]}return e.prototype.add=function(e,t,n){var i,r;return r=t[0],i=t[1],null==n&&(n={}),this.columns[e]&&n.noReplace?void 0:this.columns[e]={line:this.line,column:e,sourceLine:r,sourceColumn:i}},e.prototype.sourceLocation=function(e){for(var t;!((t=this.columns[e])||0>=e);)e--;return t&&[t.sourceLine,t.sourceColumn]},e}(),n=function(){function t(){this.lines=[]}var n,i,r,s;return t.prototype.add=function(t,n,i){var r,s,o,a;return null==i&&(i={}),o=n[0],s=n[1],a=(r=this.lines)[o]||(r[o]=new e(o)),a.add(s,t,i)},t.prototype.sourceLocation=function(e){var t,n,i;for(n=e[0],t=e[1];!((i=this.lines[n])||0>=n);)n--;return i&&i.sourceLocation(t)},t.prototype.generate=function(e,t){var n,i,r,s,o,a,c,h,l,u,p,d,f,m,g,v;for(null==e&&(e={}),null==t&&(t=null),v=0,s=0,a=0,o=0,d=!1,n="",f=this.lines,u=i=0,c=f.length;c>i;u=++i)if(l=f[u])for(m=l.columns,r=0,h=m.length;h>r;r++)if(p=m[r]){for(;p.line>v;)s=0,d=!1,n+=";",v++;d&&(n+=",",d=!1),n+=this.encodeVlq(p.column-s),s=p.column,n+=this.encodeVlq(0),n+=this.encodeVlq(p.sourceLine-a),a=p.sourceLine,n+=this.encodeVlq(p.sourceColumn-o),o=p.sourceColumn,d=!0}return g={version:3,file:e.generatedFile||"",sourceRoot:e.sourceRoot||"",sources:e.sourceFiles||[""],names:[],mappings:n},e.inline&&(g.sourcesContent=[t]),JSON.stringify(g,null,2)},r=5,i=1<<r,s=i-1,t.prototype.encodeVlq=function(e){var t,n,o,a;for(t="",o=0>e?1:0,a=(Math.abs(e)<<1)+o;a||!t;)n=a&s,a>>=r,a&&(n|=i),t+=this.encodeBase64(n);return t},n="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",t.prototype.encodeBase64=function(e){return n[e]||function(){throw Error("Cannot Base64 encode value: "+e)}()},t}(),t.exports=n}.call(this),t.exports}(),_dereq_["./coffee-script"]=function(){var e={},t={exports:e};return function(){var t,n,i,r,s,o,a,c,h,l,u,p,d,f,m,g,v,y,b={}.hasOwnProperty,k=[].indexOf||function(e){for(var t=0,n=this.length;n>t;t++)if(t in this&&this[t]===e)return t;return-1};if(a=_dereq_("fs"),v=_dereq_("vm"),f=_dereq_("path"),t=_dereq_("./lexer").Lexer,d=_dereq_("./parser").parser,h=_dereq_("./helpers"),n=_dereq_("./sourcemap"),e.VERSION="1.9.3",e.FILE_EXTENSIONS=[".coffee",".litcoffee",".coffee.md"],e.helpers=h,y=function(e){return function(t,n){var i;null==n&&(n={});try{return e.call(this,t,n)
}catch(r){if(i=r,"string"!=typeof t)throw i;throw h.updateSyntaxError(i,t,n.filename)}}},e.compile=r=y(function(e,t){var i,r,s,o,a,c,l,u,f,m,g,v,y,b,k;for(v=h.merge,o=h.extend,t=o({},t),t.sourceMap&&(g=new n),k=p.tokenize(e,t),t.referencedVars=function(){var e,t,n;for(n=[],e=0,t=k.length;t>e;e++)b=k[e],b.variable&&n.push(b[1]);return n}(),c=d.parse(k).compileToFragments(t),s=0,t.header&&(s+=1),t.shiftLine&&(s+=1),r=0,f="",u=0,m=c.length;m>u;u++)a=c[u],t.sourceMap&&(a.locationData&&!/^[;\s]*$/.test(a.code)&&g.add([a.locationData.first_line,a.locationData.first_column],[s,r],{noReplace:!0}),y=h.count(a.code,"\n"),s+=y,y?r=a.code.length-(a.code.lastIndexOf("\n")+1):r+=a.code.length),f+=a.code;return t.header&&(l="Generated by CoffeeScript "+this.VERSION,f="// "+l+"\n"+f),t.sourceMap?(i={js:f},i.sourceMap=g,i.v3SourceMap=g.generate(t,e),i):f}),e.tokens=y(function(e,t){return p.tokenize(e,t)}),e.nodes=y(function(e,t){return"string"==typeof e?d.parse(p.tokenize(e,t)):d.parse(e)}),e.run=function(e,t){var n,i,s,o;return null==t&&(t={}),s=_dereq_.main,s.filename=process.argv[1]=t.filename?a.realpathSync(t.filename):".",s.moduleCache&&(s.moduleCache={}),i=t.filename?f.dirname(a.realpathSync(t.filename)):a.realpathSync("."),s.paths=_dereq_("module")._nodeModulePaths(i),(!h.isCoffee(s.filename)||_dereq_.extensions)&&(n=r(e,t),e=null!=(o=n.js)?o:n),s._compile(e,s.filename)},e.eval=function(e,t){var n,i,s,o,a,c,h,l,u,p,d,m,g,y,k,w,T;if(null==t&&(t={}),e=e.trim()){if(o=null!=(m=v.Script.createContext)?m:v.createContext,c=null!=(g=v.isContext)?g:function(){return t.sandbox instanceof o().constructor},o){if(null!=t.sandbox){if(c(t.sandbox))w=t.sandbox;else{w=o(),y=t.sandbox;for(l in y)b.call(y,l)&&(T=y[l],w[l]=T)}w.global=w.root=w.GLOBAL=w}else w=global;if(w.__filename=t.filename||"eval",w.__dirname=f.dirname(w.__filename),w===global&&!w.module&&!w.require){for(n=_dereq_("module"),w.module=i=new n(t.modulename||"eval"),w.require=s=function(e){return n._load(e,i,!0)},i.filename=w.__filename,k=Object.getOwnPropertyNames(_dereq_),a=0,u=k.length;u>a;a++)d=k[a],"paths"!==d&&(s[d]=_dereq_[d]);s.paths=i.paths=n._nodeModulePaths(process.cwd()),s.resolve=function(e){return n._resolveFilename(e,i)}}}p={};for(l in t)b.call(t,l)&&(T=t[l],p[l]=T);return p.bare=!0,h=r(e,p),w===global?v.runInThisContext(h):v.runInContext(h,w)}},e.register=function(){return _dereq_("./register")},_dereq_.extensions)for(m=this.FILE_EXTENSIONS,l=0,u=m.length;u>l;l++)s=m[l],null==(i=_dereq_.extensions)[s]&&(i[s]=function(){throw Error("Use CoffeeScript.register() or require the coffee-script/register module to require "+s+" files.")});e._compileFile=function(e,t){var n,i,s,o;null==t&&(t=!1),s=a.readFileSync(e,"utf8"),o=65279===s.charCodeAt(0)?s.substring(1):s;try{n=r(o,{filename:e,sourceMap:t,literate:h.isLiterate(e)})}catch(c){throw i=c,h.updateSyntaxError(i,o,e)}return n},p=new t,d.lexer={lex:function(){var e,t;return t=d.tokens[this.pos++],t?(e=t[0],this.yytext=t[1],this.yylloc=t[2],d.errorToken=t.origin||t,this.yylineno=this.yylloc.first_line):e="",e},setInput:function(e){return d.tokens=e,this.pos=0},upcomingInput:function(){return""}},d.yy=_dereq_("./nodes"),d.yy.parseError=function(e,t){var n,i,r,s,o,a;return o=t.token,s=d.errorToken,a=d.tokens,i=s[0],r=s[1],n=s[2],r=function(){switch(!1){case s!==a[a.length-1]:return"end of input";case"INDENT"!==i&&"OUTDENT"!==i:return"indentation";case"IDENTIFIER"!==i&&"NUMBER"!==i&&"STRING"!==i&&"STRING_START"!==i&&"REGEX"!==i&&"REGEX_START"!==i:return i.replace(/_START$/,"").toLowerCase();default:return h.nameWhitespaceCharacter(r)}}(),h.throwSyntaxError("unexpected "+r,n)},o=function(e,t){var n,i,r,s,o,a,c,h,l,u,p,d;return s=void 0,r="",e.isNative()?r="native":(e.isEval()?(s=e.getScriptNameOrSourceURL(),s||(r=e.getEvalOrigin()+", ")):s=e.getFileName(),s||(s="<anonymous>"),h=e.getLineNumber(),i=e.getColumnNumber(),u=t(s,h,i),r=u?s+":"+u[0]+":"+u[1]:s+":"+h+":"+i),o=e.getFunctionName(),a=e.isConstructor(),c=!(e.isToplevel()||a),c?(l=e.getMethodName(),d=e.getTypeName(),o?(p=n="",d&&o.indexOf(d)&&(p=d+"."),l&&o.indexOf("."+l)!==o.length-l.length-1&&(n=" [as "+l+"]"),""+p+o+n+" ("+r+")"):d+"."+(l||"<anonymous>")+" ("+r+")"):a?"new "+(o||"<anonymous>")+" ("+r+")":o?o+" ("+r+")":r},g={},c=function(t){var n,i;if(g[t])return g[t];if(i=null!=f?f.extname(t):void 0,!(0>k.call(e.FILE_EXTENSIONS,i)))return n=e._compileFile(t,!0),g[t]=n.sourceMap},Error.prepareStackTrace=function(t,n){var i,r,s;return s=function(e,t,n){var i,r;return r=c(e),r&&(i=r.sourceLocation([t-1,n-1])),i?[i[0]+1,i[1]+1]:null},r=function(){var t,r,a;for(a=[],t=0,r=n.length;r>t&&(i=n[t],i.getFunction()!==e.run);t++)a.push("  at "+o(i,s));return a}(),""+t+"\n"+r.join("\n")+"\n"}}.call(this),t.exports}(),_dereq_["./browser"]=function(){var exports={},module={exports:exports};return function(){var CoffeeScript,compile,runScripts,indexOf=[].indexOf||function(e){for(var t=0,n=this.length;n>t;t++)if(t in this&&this[t]===e)return t;return-1};CoffeeScript=_dereq_("./coffee-script"),CoffeeScript.require=_dereq_,compile=CoffeeScript.compile,CoffeeScript.eval=function(code,options){return null==options&&(options={}),null==options.bare&&(options.bare=!0),eval(compile(code,options))},CoffeeScript.run=function(e,t){return null==t&&(t={}),t.bare=!0,t.shiftLine=!0,Function(compile(e,t))()},"undefined"!=typeof window&&null!==window&&("undefined"!=typeof btoa&&null!==btoa&&"undefined"!=typeof JSON&&null!==JSON&&"undefined"!=typeof unescape&&null!==unescape&&"undefined"!=typeof encodeURIComponent&&null!==encodeURIComponent&&(compile=function(e,t){var n,i,r;return null==t&&(t={}),t.sourceMap=!0,t.inline=!0,i=CoffeeScript.compile(e,t),n=i.js,r=i.v3SourceMap,n+"\n//# sourceMappingURL=data:application/json;base64,"+btoa(unescape(encodeURIComponent(r)))+"\n//# sourceURL=coffeescript"}),CoffeeScript.load=function(e,t,n,i){var r;return null==n&&(n={}),null==i&&(i=!1),n.sourceFiles=[e],r=window.ActiveXObject?new window.ActiveXObject("Microsoft.XMLHTTP"):new window.XMLHttpRequest,r.open("GET",e,!0),"overrideMimeType"in r&&r.overrideMimeType("text/plain"),r.onreadystatechange=function(){var s,o;if(4===r.readyState){if(0!==(o=r.status)&&200!==o)throw Error("Could not load "+e);if(s=[r.responseText,n],i||CoffeeScript.run.apply(CoffeeScript,s),t)return t(s)}},r.send(null)},runScripts=function(){var e,t,n,i,r,s,o,a,c,h,l;for(l=window.document.getElementsByTagName("script"),t=["text/coffeescript","text/literate-coffeescript"],e=function(){var e,n,i,r;for(r=[],e=0,n=l.length;n>e;e++)c=l[e],i=c.type,indexOf.call(t,i)>=0&&r.push(c);return r}(),s=0,n=function(){var t;return t=e[s],t instanceof Array?(CoffeeScript.run.apply(CoffeeScript,t),s++,n()):void 0},i=function(i,r){var s,o;return s={literate:i.type===t[1]},o=i.src||i.getAttribute("data-src"),o?CoffeeScript.load(o,function(t){return e[r]=t,n()},s,!0):(s.sourceFiles=["embedded"],e[r]=[i.innerHTML,s])},r=o=0,a=e.length;a>o;r=++o)h=e[r],i(h,r);return n()},window.addEventListener?window.addEventListener("DOMContentLoaded",runScripts,!1):window.attachEvent("onload",runScripts))}.call(this),module.exports}(),_dereq_["./coffee-script"]}();"function"==typeof define&&define.amd?define(function(){return CoffeeScript}):root.CoffeeScript=CoffeeScript})(this);
});

define("ace/mode/coffee_worker",["require","exports","module","ace/lib/oop","ace/worker/mirror","ace/mode/coffee/coffee"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var Mirror = require("../worker/mirror").Mirror;
var coffee = require("../mode/coffee/coffee");

window.addEventListener = function() {};


var Worker = exports.Worker = function(sender) {
    Mirror.call(this, sender);
    this.setTimeout(250);
};

oop.inherits(Worker, Mirror);

(function() {

    this.onUpdate = function() {
        var value = this.doc.getValue();
        var errors = [];
        try {
            coffee.compile(value);
        } catch(e) {
            var loc = e.location;
            if (loc) {
                errors.push({
                    row: loc.first_line,
                    column: loc.first_column,
                    endRow: loc.last_line,
                    endColumn: loc.last_column,
                    text: e.message,
                    type: "error"
                });
            }
        }
        this.sender.emit("annotate", errors);
    };

}).call(Worker.prototype);

});

define("ace/lib/es5-shim",["require","exports","module"], function(require, exports, module) {

function Empty() {}

if (!Function.prototype.bind) {
    Function.prototype.bind = function bind(that) { // .length is 1
        var target = this;
        if (typeof target != "function") {
            throw new TypeError("Function.prototype.bind called on incompatible " + target);
        }
        var args = slice.call(arguments, 1); // for normal call
        var bound = function () {

            if (this instanceof bound) {

                var result = target.apply(
                    this,
                    args.concat(slice.call(arguments))
                );
                if (Object(result) === result) {
                    return result;
                }
                return this;

            } else {
                return target.apply(
                    that,
                    args.concat(slice.call(arguments))
                );

            }

        };
        if(target.prototype) {
            Empty.prototype = target.prototype;
            bound.prototype = new Empty();
            Empty.prototype = null;
        }
        return bound;
    };
}
var call = Function.prototype.call;
var prototypeOfArray = Array.prototype;
var prototypeOfObject = Object.prototype;
var slice = prototypeOfArray.slice;
var _toString = call.bind(prototypeOfObject.toString);
var owns = call.bind(prototypeOfObject.hasOwnProperty);
var defineGetter;
var defineSetter;
var lookupGetter;
var lookupSetter;
var supportsAccessors;
if ((supportsAccessors = owns(prototypeOfObject, "__defineGetter__"))) {
    defineGetter = call.bind(prototypeOfObject.__defineGetter__);
    defineSetter = call.bind(prototypeOfObject.__defineSetter__);
    lookupGetter = call.bind(prototypeOfObject.__lookupGetter__);
    lookupSetter = call.bind(prototypeOfObject.__lookupSetter__);
}
if ([1,2].splice(0).length != 2) {
    if(function() { // test IE < 9 to splice bug - see issue #138
        function makeArray(l) {
            var a = new Array(l+2);
            a[0] = a[1] = 0;
            return a;
        }
        var array = [], lengthBefore;
        
        array.splice.apply(array, makeArray(20));
        array.splice.apply(array, makeArray(26));

        lengthBefore = array.length; //46
        array.splice(5, 0, "XXX"); // add one element

        lengthBefore + 1 == array.length

        if (lengthBefore + 1 == array.length) {
            return true;// has right splice implementation without bugs
        }
    }()) {//IE 6/7
        var array_splice = Array.prototype.splice;
        Array.prototype.splice = function(start, deleteCount) {
            if (!arguments.length) {
                return [];
            } else {
                return array_splice.apply(this, [
                    start === void 0 ? 0 : start,
                    deleteCount === void 0 ? (this.length - start) : deleteCount
                ].concat(slice.call(arguments, 2)))
            }
        };
    } else {//IE8
        Array.prototype.splice = function(pos, removeCount){
            var length = this.length;
            if (pos > 0) {
                if (pos > length)
                    pos = length;
            } else if (pos == void 0) {
                pos = 0;
            } else if (pos < 0) {
                pos = Math.max(length + pos, 0);
            }

            if (!(pos+removeCount < length))
                removeCount = length - pos;

            var removed = this.slice(pos, pos+removeCount);
            var insert = slice.call(arguments, 2);
            var add = insert.length;            
            if (pos === length) {
                if (add) {
                    this.push.apply(this, insert);
                }
            } else {
                var remove = Math.min(removeCount, length - pos);
                var tailOldPos = pos + remove;
                var tailNewPos = tailOldPos + add - remove;
                var tailCount = length - tailOldPos;
                var lengthAfterRemove = length - remove;

                if (tailNewPos < tailOldPos) { // case A
                    for (var i = 0; i < tailCount; ++i) {
                        this[tailNewPos+i] = this[tailOldPos+i];
                    }
                } else if (tailNewPos > tailOldPos) { // case B
                    for (i = tailCount; i--; ) {
                        this[tailNewPos+i] = this[tailOldPos+i];
                    }
                } // else, add == remove (nothing to do)

                if (add && pos === lengthAfterRemove) {
                    this.length = lengthAfterRemove; // truncate array
                    this.push.apply(this, insert);
                } else {
                    this.length = lengthAfterRemove + add; // reserves space
                    for (i = 0; i < add; ++i) {
                        this[pos+i] = insert[i];
                    }
                }
            }
            return removed;
        };
    }
}
if (!Array.isArray) {
    Array.isArray = function isArray(obj) {
        return _toString(obj) == "[object Array]";
    };
}
var boxedString = Object("a"),
    splitString = boxedString[0] != "a" || !(0 in boxedString);

if (!Array.prototype.forEach) {
    Array.prototype.forEach = function forEach(fun /*, thisp*/) {
        var object = toObject(this),
            self = splitString && _toString(this) == "[object String]" ?
                this.split("") :
                object,
            thisp = arguments[1],
            i = -1,
            length = self.length >>> 0;
        if (_toString(fun) != "[object Function]") {
            throw new TypeError(); // TODO message
        }

        while (++i < length) {
            if (i in self) {
                fun.call(thisp, self[i], i, object);
            }
        }
    };
}
if (!Array.prototype.map) {
    Array.prototype.map = function map(fun /*, thisp*/) {
        var object = toObject(this),
            self = splitString && _toString(this) == "[object String]" ?
                this.split("") :
                object,
            length = self.length >>> 0,
            result = Array(length),
            thisp = arguments[1];
        if (_toString(fun) != "[object Function]") {
            throw new TypeError(fun + " is not a function");
        }

        for (var i = 0; i < length; i++) {
            if (i in self)
                result[i] = fun.call(thisp, self[i], i, object);
        }
        return result;
    };
}
if (!Array.prototype.filter) {
    Array.prototype.filter = function filter(fun /*, thisp */) {
        var object = toObject(this),
            self = splitString && _toString(this) == "[object String]" ?
                this.split("") :
                    object,
            length = self.length >>> 0,
            result = [],
            value,
            thisp = arguments[1];
        if (_toString(fun) != "[object Function]") {
            throw new TypeError(fun + " is not a function");
        }

        for (var i = 0; i < length; i++) {
            if (i in self) {
                value = self[i];
                if (fun.call(thisp, value, i, object)) {
                    result.push(value);
                }
            }
        }
        return result;
    };
}
if (!Array.prototype.every) {
    Array.prototype.every = function every(fun /*, thisp */) {
        var object = toObject(this),
            self = splitString && _toString(this) == "[object String]" ?
                this.split("") :
                object,
            length = self.length >>> 0,
            thisp = arguments[1];
        if (_toString(fun) != "[object Function]") {
            throw new TypeError(fun + " is not a function");
        }

        for (var i = 0; i < length; i++) {
            if (i in self && !fun.call(thisp, self[i], i, object)) {
                return false;
            }
        }
        return true;
    };
}
if (!Array.prototype.some) {
    Array.prototype.some = function some(fun /*, thisp */) {
        var object = toObject(this),
            self = splitString && _toString(this) == "[object String]" ?
                this.split("") :
                object,
            length = self.length >>> 0,
            thisp = arguments[1];
        if (_toString(fun) != "[object Function]") {
            throw new TypeError(fun + " is not a function");
        }

        for (var i = 0; i < length; i++) {
            if (i in self && fun.call(thisp, self[i], i, object)) {
                return true;
            }
        }
        return false;
    };
}
if (!Array.prototype.reduce) {
    Array.prototype.reduce = function reduce(fun /*, initial*/) {
        var object = toObject(this),
            self = splitString && _toString(this) == "[object String]" ?
                this.split("") :
                object,
            length = self.length >>> 0;
        if (_toString(fun) != "[object Function]") {
            throw new TypeError(fun + " is not a function");
        }
        if (!length && arguments.length == 1) {
            throw new TypeError("reduce of empty array with no initial value");
        }

        var i = 0;
        var result;
        if (arguments.length >= 2) {
            result = arguments[1];
        } else {
            do {
                if (i in self) {
                    result = self[i++];
                    break;
                }
                if (++i >= length) {
                    throw new TypeError("reduce of empty array with no initial value");
                }
            } while (true);
        }

        for (; i < length; i++) {
            if (i in self) {
                result = fun.call(void 0, result, self[i], i, object);
            }
        }

        return result;
    };
}
if (!Array.prototype.reduceRight) {
    Array.prototype.reduceRight = function reduceRight(fun /*, initial*/) {
        var object = toObject(this),
            self = splitString && _toString(this) == "[object String]" ?
                this.split("") :
                object,
            length = self.length >>> 0;
        if (_toString(fun) != "[object Function]") {
            throw new TypeError(fun + " is not a function");
        }
        if (!length && arguments.length == 1) {
            throw new TypeError("reduceRight of empty array with no initial value");
        }

        var result, i = length - 1;
        if (arguments.length >= 2) {
            result = arguments[1];
        } else {
            do {
                if (i in self) {
                    result = self[i--];
                    break;
                }
                if (--i < 0) {
                    throw new TypeError("reduceRight of empty array with no initial value");
                }
            } while (true);
        }

        do {
            if (i in this) {
                result = fun.call(void 0, result, self[i], i, object);
            }
        } while (i--);

        return result;
    };
}
if (!Array.prototype.indexOf || ([0, 1].indexOf(1, 2) != -1)) {
    Array.prototype.indexOf = function indexOf(sought /*, fromIndex */ ) {
        var self = splitString && _toString(this) == "[object String]" ?
                this.split("") :
                toObject(this),
            length = self.length >>> 0;

        if (!length) {
            return -1;
        }

        var i = 0;
        if (arguments.length > 1) {
            i = toInteger(arguments[1]);
        }
        i = i >= 0 ? i : Math.max(0, length + i);
        for (; i < length; i++) {
            if (i in self && self[i] === sought) {
                return i;
            }
        }
        return -1;
    };
}
if (!Array.prototype.lastIndexOf || ([0, 1].lastIndexOf(0, -3) != -1)) {
    Array.prototype.lastIndexOf = function lastIndexOf(sought /*, fromIndex */) {
        var self = splitString && _toString(this) == "[object String]" ?
                this.split("") :
                toObject(this),
            length = self.length >>> 0;

        if (!length) {
            return -1;
        }
        var i = length - 1;
        if (arguments.length > 1) {
            i = Math.min(i, toInteger(arguments[1]));
        }
        i = i >= 0 ? i : length - Math.abs(i);
        for (; i >= 0; i--) {
            if (i in self && sought === self[i]) {
                return i;
            }
        }
        return -1;
    };
}
if (!Object.getPrototypeOf) {
    Object.getPrototypeOf = function getPrototypeOf(object) {
        return object.__proto__ || (
            object.constructor ?
            object.constructor.prototype :
            prototypeOfObject
        );
    };
}
if (!Object.getOwnPropertyDescriptor) {
    var ERR_NON_OBJECT = "Object.getOwnPropertyDescriptor called on a " +
                         "non-object: ";
    Object.getOwnPropertyDescriptor = function getOwnPropertyDescriptor(object, property) {
        if ((typeof object != "object" && typeof object != "function") || object === null)
            throw new TypeError(ERR_NON_OBJECT + object);
        if (!owns(object, property))
            return;

        var descriptor, getter, setter;
        descriptor =  { enumerable: true, configurable: true };
        if (supportsAccessors) {
            var prototype = object.__proto__;
            object.__proto__ = prototypeOfObject;

            var getter = lookupGetter(object, property);
            var setter = lookupSetter(object, property);
            object.__proto__ = prototype;

            if (getter || setter) {
                if (getter) descriptor.get = getter;
                if (setter) descriptor.set = setter;
                return descriptor;
            }
        }
        descriptor.value = object[property];
        return descriptor;
    };
}
if (!Object.getOwnPropertyNames) {
    Object.getOwnPropertyNames = function getOwnPropertyNames(object) {
        return Object.keys(object);
    };
}
if (!Object.create) {
    var createEmpty;
    if (Object.prototype.__proto__ === null) {
        createEmpty = function () {
            return { "__proto__": null };
        };
    } else {
        createEmpty = function () {
            var empty = {};
            for (var i in empty)
                empty[i] = null;
            empty.constructor =
            empty.hasOwnProperty =
            empty.propertyIsEnumerable =
            empty.isPrototypeOf =
            empty.toLocaleString =
            empty.toString =
            empty.valueOf =
            empty.__proto__ = null;
            return empty;
        }
    }

    Object.create = function create(prototype, properties) {
        var object;
        if (prototype === null) {
            object = createEmpty();
        } else {
            if (typeof prototype != "object")
                throw new TypeError("typeof prototype["+(typeof prototype)+"] != 'object'");
            var Type = function () {};
            Type.prototype = prototype;
            object = new Type();
            object.__proto__ = prototype;
        }
        if (properties !== void 0)
            Object.defineProperties(object, properties);
        return object;
    };
}

function doesDefinePropertyWork(object) {
    try {
        Object.defineProperty(object, "sentinel", {});
        return "sentinel" in object;
    } catch (exception) {
    }
}
if (Object.defineProperty) {
    var definePropertyWorksOnObject = doesDefinePropertyWork({});
    var definePropertyWorksOnDom = typeof document == "undefined" ||
        doesDefinePropertyWork(document.createElement("div"));
    if (!definePropertyWorksOnObject || !definePropertyWorksOnDom) {
        var definePropertyFallback = Object.defineProperty;
    }
}

if (!Object.defineProperty || definePropertyFallback) {
    var ERR_NON_OBJECT_DESCRIPTOR = "Property description must be an object: ";
    var ERR_NON_OBJECT_TARGET = "Object.defineProperty called on non-object: "
    var ERR_ACCESSORS_NOT_SUPPORTED = "getters & setters can not be defined " +
                                      "on this javascript engine";

    Object.defineProperty = function defineProperty(object, property, descriptor) {
        if ((typeof object != "object" && typeof object != "function") || object === null)
            throw new TypeError(ERR_NON_OBJECT_TARGET + object);
        if ((typeof descriptor != "object" && typeof descriptor != "function") || descriptor === null)
            throw new TypeError(ERR_NON_OBJECT_DESCRIPTOR + descriptor);
        if (definePropertyFallback) {
            try {
                return definePropertyFallback.call(Object, object, property, descriptor);
            } catch (exception) {
            }
        }
        if (owns(descriptor, "value")) {

            if (supportsAccessors && (lookupGetter(object, property) ||
                                      lookupSetter(object, property)))
            {
                var prototype = object.__proto__;
                object.__proto__ = prototypeOfObject;
                delete object[property];
                object[property] = descriptor.value;
                object.__proto__ = prototype;
            } else {
                object[property] = descriptor.value;
            }
        } else {
            if (!supportsAccessors)
                throw new TypeError(ERR_ACCESSORS_NOT_SUPPORTED);
            if (owns(descriptor, "get"))
                defineGetter(object, property, descriptor.get);
            if (owns(descriptor, "set"))
                defineSetter(object, property, descriptor.set);
        }

        return object;
    };
}
if (!Object.defineProperties) {
    Object.defineProperties = function defineProperties(object, properties) {
        for (var property in properties) {
            if (owns(properties, property))
                Object.defineProperty(object, property, properties[property]);
        }
        return object;
    };
}
if (!Object.seal) {
    Object.seal = function seal(object) {
        return object;
    };
}
if (!Object.freeze) {
    Object.freeze = function freeze(object) {
        return object;
    };
}
try {
    Object.freeze(function () {});
} catch (exception) {
    Object.freeze = (function freeze(freezeObject) {
        return function freeze(object) {
            if (typeof object == "function") {
                return object;
            } else {
                return freezeObject(object);
            }
        };
    })(Object.freeze);
}
if (!Object.preventExtensions) {
    Object.preventExtensions = function preventExtensions(object) {
        return object;
    };
}
if (!Object.isSealed) {
    Object.isSealed = function isSealed(object) {
        return false;
    };
}
if (!Object.isFrozen) {
    Object.isFrozen = function isFrozen(object) {
        return false;
    };
}
if (!Object.isExtensible) {
    Object.isExtensible = function isExtensible(object) {
        if (Object(object) === object) {
            throw new TypeError(); // TODO message
        }
        var name = '';
        while (owns(object, name)) {
            name += '?';
        }
        object[name] = true;
        var returnValue = owns(object, name);
        delete object[name];
        return returnValue;
    };
}
if (!Object.keys) {
    var hasDontEnumBug = true,
        dontEnums = [
            "toString",
            "toLocaleString",
            "valueOf",
            "hasOwnProperty",
            "isPrototypeOf",
            "propertyIsEnumerable",
            "constructor"
        ],
        dontEnumsLength = dontEnums.length;

    for (var key in {"toString": null}) {
        hasDontEnumBug = false;
    }

    Object.keys = function keys(object) {

        if (
            (typeof object != "object" && typeof object != "function") ||
            object === null
        ) {
            throw new TypeError("Object.keys called on a non-object");
        }

        var keys = [];
        for (var name in object) {
            if (owns(object, name)) {
                keys.push(name);
            }
        }

        if (hasDontEnumBug) {
            for (var i = 0, ii = dontEnumsLength; i < ii; i++) {
                var dontEnum = dontEnums[i];
                if (owns(object, dontEnum)) {
                    keys.push(dontEnum);
                }
            }
        }
        return keys;
    };

}
if (!Date.now) {
    Date.now = function now() {
        return new Date().getTime();
    };
}
var ws = "\x09\x0A\x0B\x0C\x0D\x20\xA0\u1680\u180E\u2000\u2001\u2002\u2003" +
    "\u2004\u2005\u2006\u2007\u2008\u2009\u200A\u202F\u205F\u3000\u2028" +
    "\u2029\uFEFF";
if (!String.prototype.trim || ws.trim()) {
    ws = "[" + ws + "]";
    var trimBeginRegexp = new RegExp("^" + ws + ws + "*"),
        trimEndRegexp = new RegExp(ws + ws + "*$");
    String.prototype.trim = function trim() {
        return String(this).replace(trimBeginRegexp, "").replace(trimEndRegexp, "");
    };
}

function toInteger(n) {
    n = +n;
    if (n !== n) { // isNaN
        n = 0;
    } else if (n !== 0 && n !== (1/0) && n !== -(1/0)) {
        n = (n > 0 || -1) * Math.floor(Math.abs(n));
    }
    return n;
}

function isPrimitive(input) {
    var type = typeof input;
    return (
        input === null ||
        type === "undefined" ||
        type === "boolean" ||
        type === "number" ||
        type === "string"
    );
}

function toPrimitive(input) {
    var val, valueOf, toString;
    if (isPrimitive(input)) {
        return input;
    }
    valueOf = input.valueOf;
    if (typeof valueOf === "function") {
        val = valueOf.call(input);
        if (isPrimitive(val)) {
            return val;
        }
    }
    toString = input.toString;
    if (typeof toString === "function") {
        val = toString.call(input);
        if (isPrimitive(val)) {
            return val;
        }
    }
    throw new TypeError();
}
var toObject = function (o) {
    if (o == null) { // this matches both null and undefined
        throw new TypeError("can't convert "+o+" to object");
    }
    return Object(o);
};

});
