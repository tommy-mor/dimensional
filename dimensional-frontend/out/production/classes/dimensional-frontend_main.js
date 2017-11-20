if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'dimensional-frontend_main'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'dimensional-frontend_main'.");
}
this['dimensional-frontend_main'] = function (_, Kotlin) {
  'use strict';
  var throwCCE = Kotlin.throwCCE;
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var Unit = Kotlin.kotlin.Unit;
  function main$lambda$lambda(closure$input) {
    return function (event) {
      println(closure$input.value);
      fetch(closure$input.value);
    };
  }
  function main$lambda(it) {
    var tmp$;
    fetch('thing');
    var input = Kotlin.isType(tmp$ = document.getElementById('count_id'), HTMLInputElement) ? tmp$ : throwCCE();
    var button = document.getElementById('button_id');
    return button != null ? (button.addEventListener('click', main$lambda$lambda(input)), Unit) : null;
  }
  function main(args) {
    window.onload = main$lambda;
  }
  function fetch(count) {
    var url = 'https://localhost:8080/api/ping/' + count;
    var req = new XMLHttpRequest();
  }
  _.main_kand9s$ = main;
  _.fetch_61zpoe$ = fetch;
  main([]);
  Kotlin.defineModule('dimensional-frontend_main', _);
  return _;
}(typeof this['dimensional-frontend_main'] === 'undefined' ? {} : this['dimensional-frontend_main'], kotlin);

//# sourceMappingURL=dimensional-frontend_main.js.map
