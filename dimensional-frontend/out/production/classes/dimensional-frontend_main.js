if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'dimensional-frontend_main'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'dimensional-frontend_main'.");
}
if (typeof this['kotlinx-html-js'] === 'undefined') {
  throw new Error("Error loading module 'dimensional-frontend_main'. Its dependency 'kotlinx-html-js' was not found. Please, check whether 'kotlinx-html-js' is loaded prior to 'dimensional-frontend_main'.");
}
this['dimensional-frontend_main'] = function (_, Kotlin, $module$kotlinx_html_js) {
  'use strict';
  var throwNPE = Kotlin.throwNPE;
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var Unit = Kotlin.kotlin.Unit;
  var listOf = Kotlin.kotlin.collections.listOf_mh5how$;
  var Kind_INTERFACE = Kotlin.Kind.INTERFACE;
  var get_create = $module$kotlinx_html_js.kotlinx.html.dom.get_create_4wc2mh$;
  var div = $module$kotlinx_html_js.kotlinx.html.js.div_wkomt5$;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var split = Kotlin.kotlin.text.split_ip8yn$;
  var toDoubleOrNull = Kotlin.kotlin.text.toDoubleOrNull_pdl1vz$;
  var IllegalArgumentException = Kotlin.kotlin.IllegalArgumentException;
  function main$lambda$lambda(event) {
    println(event);
  }
  function main$lambda(it) {
    var tmp$, tmp$_0;
    fetch('thing');
    var input = (tmp$ = document.getElementById('count_id')) != null ? tmp$ : throwNPE();
    var button = (tmp$_0 = document.getElementById('button_id')) != null ? tmp$_0 : throwNPE();
    button.addEventListener('click', main$lambda$lambda);
    return Unit;
  }
  function main$lambda_0(it) {
    var tmp$, tmp$_0;
    ((tmp$ = document.body) != null ? tmp$ : throwNPE()).append((new Table(listOf(new Plate(new Segment(14.0, 'cm', 'Ca'), new Segment())))).render_hizait$((tmp$_0 = document.body) != null ? tmp$_0 : throwNPE(), document));
    return Unit;
  }
  function main(args) {
    window.onload = main$lambda;
    window.onload = main$lambda_0;
  }
  function fetch(count) {
    var url = 'https://localhost:8080/api/ping/' + count;
  }
  function Renderable() {
  }
  Renderable.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'Renderable',
    interfaces: []
  };
  function Table(plates) {
    this.plates = plates;
  }
  Table.prototype.wrap = function () {
    var forEach$result;
    var tmp$;
    tmp$ = this.plates.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      element.wrap();
    }
    return 'latexthing ' + forEach$result;
  };
  function Table$render$lambda($receiver) {
    $receiver.unaryPlus_pdl1vz$('inside the table');
    return Unit;
  }
  Table.prototype.render_hizait$ = function (parent, document) {
    var div_0 = div(get_create(document), 'table', Table$render$lambda);
    var tmp$;
    tmp$ = this.plates.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      div_0.appendChild(element.render_hizait$(parent, document));
    }
    return div_0;
  };
  Table.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Table',
    interfaces: [Renderable]
  };
  Table.prototype.component1 = function () {
    return this.plates;
  };
  Table.prototype.copy_20c1z9$ = function (plates) {
    return new Table(plates === void 0 ? this.plates : plates);
  };
  Table.prototype.toString = function () {
    return 'Table(plates=' + Kotlin.toString(this.plates) + ')';
  };
  Table.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.plates) | 0;
    return result;
  };
  Table.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && Kotlin.equals(this.plates, other.plates))));
  };
  function Plate(top, bottom) {
    this.top = top;
    this.bottom = bottom;
  }
  Plate.prototype.wrap = function () {
    return 'latex dthing ' + this.top.wrap() + ', ' + this.bottom.wrap();
  };
  function Plate$render$lambda($receiver) {
    $receiver.unaryPlus_pdl1vz$('plate');
    return Unit;
  }
  Plate.prototype.render_hizait$ = function (parent, document) {
    var div_0 = div(get_create(document), 'plate', Plate$render$lambda);
    div_0.appendChild(this.top.render_hizait$(parent, document));
    div_0.appendChild(this.bottom.render_hizait$(parent, document));
    return div_0;
  };
  Plate.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Plate',
    interfaces: [Renderable]
  };
  function Segment(number, unit, element) {
    if (number === void 0)
      number = 0.0;
    if (unit === void 0)
      unit = 'cm';
    if (element === void 0)
      element = 'Na';
    this.number = number;
    this.unit = unit;
    this.element = element;
  }
  function Segment$render$lambda(this$Segment) {
    return function ($receiver) {
      $receiver.unaryPlus_pdl1vz$(this$Segment.number.toString() + ' , ' + this$Segment.unit + ' , and ' + this$Segment.element);
      return Unit;
    };
  }
  Segment.prototype.render_hizait$ = function (parent, document) {
    return div(get_create(document), 'segment', Segment$render$lambda(this));
  };
  Segment.prototype.wrap = function () {
    return 'latext thing 1 ' + this.number + ' ' + this.unit + ' ' + this.element;
  };
  Segment.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Segment',
    interfaces: [Renderable]
  };
  function Segment_init(input, $this) {
    $this = $this || Object.create(Segment.prototype);
    Segment.call($this);
    var tmp$;
    var all = split(input, ['']);
    tmp$ = toDoubleOrNull(all.get_za3lpa$(0));
    if (tmp$ == null) {
      throw new IllegalArgumentException('not a double');
    }
    $this.number = tmp$;
    $this.unit = all.get_za3lpa$(1);
    $this.element = all.get_za3lpa$(2);
    return $this;
  }
  _.main_kand9s$ = main;
  _.fetch_61zpoe$ = fetch;
  _.Renderable = Renderable;
  _.Table = Table;
  _.Plate = Plate;
  _.Segment_init_61zpoe$ = Segment_init;
  _.Segment = Segment;
  main([]);
  Kotlin.defineModule('dimensional-frontend_main', _);
  return _;
}(typeof this['dimensional-frontend_main'] === 'undefined' ? {} : this['dimensional-frontend_main'], kotlin, this['kotlinx-html-js']);

//# sourceMappingURL=dimensional-frontend_main.js.map
