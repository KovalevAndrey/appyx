!function(e,t){"object"==typeof exports&&"object"==typeof module?module.exports=t():"function"==typeof define&&define.amd?define([],t):"object"==typeof exports?exports.web=t():e.web=t()}(this,(()=>(()=>{"use strict";var e,t,r,n,o,a,i={541:(e,t,r)=>{r.a(e,(async(e,n)=>{try{r.r(t),r.d(t,{default:()=>e});var o=r(702);const e=(await(0,o._)()).exports;n()}catch(e){n(e)}}),1)},702:(e,t,r)=>{async function n(e={},t=!0){const n=new WeakMap,o=e["./skiko.mjs"]??await r.e(273).then(r.bind(r,273)),a=e.uuid??await r.e(311).then(r.bind(r,311)),i={"kotlin.captureStackTrace":()=>(new Error).stack,"kotlin.wasm.internal.throwJsError":(e,t,r)=>{const n=new Error;throw n.message=e,n.name=t,n.stack=r,n},"kotlin.wasm.internal.stringLength":e=>e.length,"kotlin.wasm.internal.jsExportStringToWasm":(e,t,r,n)=>{const o=new Uint16Array(c.memory.buffer,n,r);let a=0,i=t;for(;a<r;)o.set([e.charCodeAt(i)],a),i++,a++},"kotlin.wasm.internal.externrefToInt":e=>Number(e),"kotlin.wasm.internal.importStringFromWasm":(e,t,r)=>{const n=new Uint16Array(c.memory.buffer,e,t),o=String.fromCharCode.apply(null,n);return null==r?o:r+o},"kotlin.wasm.internal.getJsEmptyString":()=>"","kotlin.wasm.internal.externrefToString":e=>String(e),"kotlin.wasm.internal.externrefEquals":(e,t)=>e===t,"kotlin.wasm.internal.externrefHashCode":(()=>{const e=new DataView(new ArrayBuffer(8)),t=new WeakMap;return r=>{if(null==r)return 0;switch(typeof r){case"object":case"function":return function(e){const r=t.get(e);if(void 0===r){const r=4294967296,n=Math.random()*r|0;return t.set(e,n),n}return r}(r);case"number":return function(t){return(0|t)===t?0|t:(e.setFloat64(0,t,!0),(31*e.getInt32(0,!0)|0)+e.getInt32(4,!0)|0)}(r);case"boolean":return r?1231:1237;default:return function(e){for(var t=0,r=0;r<e.length;r++)t=31*t+e.charCodeAt(r)|0;return t}(String(r))}}})(),"kotlin.wasm.internal.isNullish":e=>null==e,"kotlin.wasm.internal.getJsTrue":()=>!0,"kotlin.wasm.internal.getJsFalse":()=>!1,"kotlin.wasm.internal.tryGetOrSetExternrefBox_$external_fun":(e,t)=>function(e,t){if("object"!=typeof e)return t;const r=n.get(e);return void 0!==r?r:(n.set(e,t),t)}(e,t),"kotlin.io.printError":e=>console.error(e),"kotlin.io.printlnImpl":e=>console.log(e),"kotlin.js.jsArrayGet":(e,t)=>e[t],"kotlin.js.length_$external_prop_getter":e=>e.length,"kotlin.js.__convertKotlinClosureToJsClosure_(()->Unit)":e=>()=>c["__callFunction_(()->Unit)"](e),"kotlin.random.initialSeed":()=>Math.random()*Math.pow(2,32)|0,"kotlinx.browser.window_$external_prop_getter":()=>window,"kotlinx.browser.document_$external_prop_getter":()=>document,"org.w3c.dom.length_$external_prop_getter":e=>e.length,"org.w3c.dom.item_$external_fun":(e,t)=>e.item(t),"org.w3c.dom.encryptedmedia.__convertKotlinClosureToJsClosure_((Js)->Unit)":e=>t=>c["__callFunction_((Js)->Unit)"](e,t),"org.w3c.dom.events.addEventListener_$external_fun":(e,t,r,n)=>e.addEventListener(t,r,n),"org.w3c.dom.events.addEventListener_$external_fun_1":(e,t,r)=>e.addEventListener(t,r),"org.w3c.dom.events.timeStamp_$external_prop_getter":e=>e.timeStamp,"org.w3c.dom.events.preventDefault_$external_fun":e=>e.preventDefault(),"org.w3c.dom.events.Event_$external_class_instanceof":e=>e instanceof Event,"org.w3c.dom.events.ctrlKey_$external_prop_getter":e=>e.ctrlKey,"org.w3c.dom.events.shiftKey_$external_prop_getter":e=>e.shiftKey,"org.w3c.dom.events.altKey_$external_prop_getter":e=>e.altKey,"org.w3c.dom.events.metaKey_$external_prop_getter":e=>e.metaKey,"org.w3c.dom.events.button_$external_prop_getter":e=>e.button,"org.w3c.dom.events.offsetX_$external_prop_getter":e=>e.offsetX,"org.w3c.dom.events.offsetY_$external_prop_getter":e=>e.offsetY,"org.w3c.dom.events.MouseEvent_$external_class_instanceof":e=>e instanceof MouseEvent,"org.w3c.dom.events.location_$external_prop_getter":e=>e.location,"org.w3c.dom.events.ctrlKey_$external_prop_getter_1":e=>e.ctrlKey,"org.w3c.dom.events.shiftKey_$external_prop_getter_1":e=>e.shiftKey,"org.w3c.dom.events.altKey_$external_prop_getter_1":e=>e.altKey,"org.w3c.dom.events.metaKey_$external_prop_getter_1":e=>e.metaKey,"org.w3c.dom.events.keyCode_$external_prop_getter":e=>e.keyCode,"org.w3c.dom.events.DOM_KEY_LOCATION_RIGHT_$external_prop_getter":e=>e.DOM_KEY_LOCATION_RIGHT,"org.w3c.dom.events.Companion_$external_object_getInstance":()=>KeyboardEvent,"org.w3c.dom.events.KeyboardEvent_$external_class_instanceof":e=>e instanceof KeyboardEvent,"org.w3c.dom.events.deltaX_$external_prop_getter":e=>e.deltaX,"org.w3c.dom.events.deltaY_$external_prop_getter":e=>e.deltaY,"org.w3c.dom.events.WheelEvent_$external_class_instanceof":e=>e instanceof WheelEvent,"org.w3c.dom.getMethodImplForHTMLCollection":(e,t)=>e[t],"org.w3c.dom.devicePixelRatio_$external_prop_getter":e=>e.devicePixelRatio,"org.w3c.dom.requestAnimationFrame_$external_fun":(e,t)=>e.requestAnimationFrame(t),"org.w3c.dom.__convertKotlinClosureToJsClosure_((Double)->Unit)":e=>t=>c["__callFunction_((Double)->Unit)"](e,t),"org.w3c.dom.matchMedia_$external_fun":(e,t)=>e.matchMedia(t),"org.w3c.dom.clearTimeout_$external_fun":(e,t,r)=>e.clearTimeout(r?void 0:t),"org.w3c.dom.documentElement_$external_prop_getter":e=>e.documentElement,"org.w3c.dom.head_$external_prop_getter":e=>e.head,"org.w3c.dom.getElementsByTagName_$external_fun":(e,t)=>e.getElementsByTagName(t),"org.w3c.dom.getElementsByClassName_$external_fun":(e,t)=>e.getElementsByClassName(t),"org.w3c.dom.createElement_$external_fun":(e,t,r,n)=>e.createElement(t,n?void 0:r),"org.w3c.dom.createTextNode_$external_fun":(e,t)=>e.createTextNode(t),"org.w3c.dom.getElementById_$external_fun":(e,t)=>e.getElementById(t),"org.w3c.dom.className_$external_prop_setter":(e,t)=>e.className=t,"org.w3c.dom.clientWidth_$external_prop_getter":e=>e.clientWidth,"org.w3c.dom.clientHeight_$external_prop_getter":e=>e.clientHeight,"org.w3c.dom.setAttribute_$external_fun":(e,t,r)=>e.setAttribute(t,r),"org.w3c.dom.getElementsByTagName_$external_fun_1":(e,t)=>e.getElementsByTagName(t),"org.w3c.dom.getBoundingClientRect_$external_fun":e=>e.getBoundingClientRect(),"org.w3c.dom.matches_$external_prop_getter":e=>e.matches,"org.w3c.dom.addListener_$external_fun":(e,t)=>e.addListener(t),"org.w3c.dom.parentNode_$external_prop_getter":e=>e.parentNode,"org.w3c.dom.textContent_$external_prop_setter":(e,t)=>e.textContent=t,"org.w3c.dom.appendChild_$external_fun":(e,t)=>e.appendChild(t),"org.w3c.dom.removeChild_$external_fun":(e,t)=>e.removeChild(t),"org.w3c.dom.item_$external_fun_1":(e,t)=>e.item(t),"org.w3c.dom.identifier_$external_prop_getter":e=>e.identifier,"org.w3c.dom.clientX_$external_prop_getter":e=>e.clientX,"org.w3c.dom.clientY_$external_prop_getter":e=>e.clientY,"org.w3c.dom.top_$external_prop_getter":e=>e.top,"org.w3c.dom.left_$external_prop_getter":e=>e.left,"org.w3c.dom.HTMLTitleElement_$external_class_instanceof":e=>e instanceof HTMLTitleElement,"org.w3c.dom.type_$external_prop_setter":(e,t)=>e.type=t,"org.w3c.dom.HTMLStyleElement_$external_class_instanceof":e=>e instanceof HTMLStyleElement,"org.w3c.dom.width_$external_prop_setter":(e,t)=>e.width=t,"org.w3c.dom.height_$external_prop_setter":(e,t)=>e.height=t,"org.w3c.dom.HTMLCanvasElement_$external_class_instanceof":e=>e instanceof HTMLCanvasElement,"org.w3c.dom.changedTouches_$external_prop_getter":e=>e.changedTouches,"org.w3c.dom.TouchEvent_$external_class_instanceof":e=>e instanceof TouchEvent,"org.w3c.dom.matches_$external_prop_getter_1":e=>e.matches,"org.w3c.dom.MediaQueryListEvent_$external_class_instanceof":e=>e instanceof MediaQueryListEvent,"org.w3c.performance.now_$external_fun":e=>e.now(),"org.w3c.performance.performance_$external_prop_getter":e=>e.performance,"kotlinx.coroutines.tryGetProcess":()=>"undefined"!=typeof process&&"function"==typeof process.nextTick?process:null,"kotlinx.coroutines.tryGetWindow":()=>"undefined"!=typeof window&&null!=window&&"function"==typeof window.addEventListener?window:null,"kotlinx.coroutines.nextTick_$external_fun":(e,t)=>e.nextTick(t),"kotlinx.coroutines.error_$external_fun":(e,t)=>e.error(t),"kotlinx.coroutines.console_$external_prop_getter":()=>console,"kotlinx.coroutines.createScheduleMessagePoster":e=>()=>Promise.resolve(0).then(e),"kotlinx.coroutines.__callJsClosure_(()->Unit)":e=>e(),"kotlinx.coroutines.createRescheduleMessagePoster":e=>()=>e.postMessage("dispatchCoroutine","*"),"kotlinx.coroutines.subscribeToWindowMessages":(e,t)=>{e.addEventListener("message",(r=>{r.source==e&&"dispatchCoroutine"==r.data&&(r.stopPropagation(),t())}),!0)},"kotlinx.coroutines.setTimeout":(e,t,r)=>e.setTimeout(t,r),"kotlinx.coroutines.clearTimeout":e=>{"undefined"!=typeof clearTimeout&&clearTimeout(e)},"kotlinx.coroutines.setTimeout_$external_fun":(e,t)=>setTimeout(e,t),"org.jetbrains.skiko.w3c.language_$external_prop_getter":e=>e.language,"org.jetbrains.skiko.w3c.userAgent_$external_prop_getter":e=>e.userAgent,"org.jetbrains.skiko.w3c.devicePixelRatio_$external_prop_getter":e=>e.devicePixelRatio,"org.jetbrains.skiko.w3c.navigator_$external_prop_getter":e=>e.navigator,"org.jetbrains.skiko.w3c.performance_$external_prop_getter":e=>e.performance,"org.jetbrains.skiko.w3c.requestAnimationFrame_$external_fun":(e,t)=>e.requestAnimationFrame(t),"org.jetbrains.skiko.w3c.window_$external_object_getInstance":()=>window,"org.jetbrains.skiko.w3c.now_$external_fun":e=>e.now(),"org.jetbrains.skiko.w3c.width_$external_prop_getter":e=>e.width,"org.jetbrains.skiko.w3c.width_$external_prop_setter":(e,t)=>e.width=t,"org.jetbrains.skiko.w3c.height_$external_prop_getter":e=>e.height,"org.jetbrains.skiko.w3c.height_$external_prop_setter":(e,t)=>e.height=t,"org.jetbrains.skiko.w3c.style_$external_prop_getter":e=>e.style,"org.jetbrains.skiko.w3c.HTMLCanvasElement_$external_class_instanceof":e=>e instanceof HTMLCanvasElement,"org.jetbrains.skiko.w3c.width_$external_prop_setter_1":(e,t)=>e.width=t,"org.jetbrains.skiko.w3c.height_$external_prop_setter_1":(e,t)=>e.height=t,"org.jetbrains.skia.impl.FinalizationRegistry_$external_fun":e=>new FinalizationRegistry(e),"org.jetbrains.skia.impl.register_$external_fun":(e,t,r)=>e.register(t,r),"org.jetbrains.skia.impl.unregister_$external_fun":(e,t)=>e.unregister(t),"org.jetbrains.skia.impl._releaseLocalCallbackScope_$external_fun":()=>o._releaseLocalCallbackScope(),"org.jetbrains.skiko.getNavigatorInfo":()=>navigator.userAgentData?navigator.userAgentData.platform:navigator.platform,"org.jetbrains.skiko.wasm.createContext_$external_fun":(e,t,r)=>e.createContext(t,r),"org.jetbrains.skiko.wasm.makeContextCurrent_$external_fun":(e,t)=>e.makeContextCurrent(t),"org.jetbrains.skiko.wasm.GL_$external_object_getInstance":()=>o.GL,"org.jetbrains.skiko.wasm.createDefaultContextAttributes":()=>({alpha:1,depth:1,stencil:8,antialias:0,premultipliedAlpha:1,preserveDrawingBuffer:0,preferLowPowerToHighPerformance:0,failIfMajorPerformanceCaveat:0,enableExtensionsByDefault:1,explicitSwapControl:0,renderViaOffscreenBackBuffer:0,majorVersion:2}),"androidx.compose.ui.text.intl.parseLanguageTagToIntlLocale":e=>new Intl.Locale(e),"androidx.compose.ui.text.intl.language_$external_prop_getter":e=>e.language,"androidx.compose.ui.text.intl.baseName_$external_prop_getter":e=>e.baseName,"androidx.compose.ui.text.intl.getUserPreferredLanguagesAsArray":()=>window.navigator.languages,"androidx.compose.ui.events.force_$external_prop_getter":e=>e.force,"androidx.compose.ui.window.setCursor":(e,t)=>document.getElementById(e).style.cursor=t,"androidx.compose.ui.window.isMatchMediaSupported":()=>null!=window.matchMedia,"com.bumble.appyx.utils.multiplatform.log_$external_fun":(e,t)=>e.log(t),"com.bumble.appyx.utils.multiplatform.console_$external_prop_getter":()=>console,"com.bumble.appyx.interactions.v4_$external_fun":e=>e.v4(),"com.bumble.appyx.interactions.Uuid_$external_object_getInstance":()=>a,"com.bumble.appyx.demos.onWasmReady_$external_fun":e=>onWasmReady(e)};let s,l,c;const _="undefined"!=typeof process&&"node"===process.release.name,p=!_&&("undefined"!=typeof d8||"undefined"!=typeof inIon||"undefined"!=typeof jscOptions),d=!_&&!p&&"undefined"!=typeof window;if(!_&&!p&&!d)throw"Supported JS engine not detected";const m="./appyx-demos-experimental-puzzle15-web-wa.wasm",u={js_code:i,"./skiko.mjs":e["./skiko.mjs"]??await r.e(273).then(r.bind(r,273))};try{if(_){l=(await import("node:module")).default.createRequire("file:///home/runner/work/appyx/appyx/build/js/packages/appyx-demos-experimental-puzzle15-web-wa/kotlin/appyx-demos-experimental-puzzle15-web-wa.uninstantiated.mjs");const e=l("fs"),t=l("path"),r=l("url").fileURLToPath("file:///home/runner/work/appyx/appyx/build/js/packages/appyx-demos-experimental-puzzle15-web-wa/kotlin/appyx-demos-experimental-puzzle15-web-wa.uninstantiated.mjs"),n=t.dirname(r),o=e.readFileSync(t.resolve(n,m)),a=new WebAssembly.Module(o);s=new WebAssembly.Instance(a,u)}if(p){const e=read(m,"binary"),t=new WebAssembly.Module(e);s=new WebAssembly.Instance(t,u)}d&&(s=(await WebAssembly.instantiateStreaming(fetch(m),u)).instance)}catch(e){if(e instanceof WebAssembly.CompileError){let e="Please make sure that your runtime environment supports the latest version of Wasm GC and Exception-Handling proposals.\nFor more information, see https://kotl.in/wasm-help\n";if(d)console.error(e);else{const t="\n"+e;"undefined"!=typeof console&&void 0!==console.log?console.log(t):print(t)}}throw e}return c=s.exports,t&&c._initialize(),{instance:s,exports:c}}r.d(t,{_:()=>n})}},s={};function l(e){var t=s[e];if(void 0!==t)return t.exports;var r=s[e]={exports:{}};return i[e](r,r.exports,l),r.exports}return l.m=i,e="function"==typeof Symbol?Symbol("webpack queues"):"__webpack_queues__",t="function"==typeof Symbol?Symbol("webpack exports"):"__webpack_exports__",r="function"==typeof Symbol?Symbol("webpack error"):"__webpack_error__",n=e=>{e&&!e.d&&(e.d=1,e.forEach((e=>e.r--)),e.forEach((e=>e.r--?e.r++:e())))},l.a=(o,a,i)=>{var s;i&&((s=[]).d=1);var l,c,_,p=new Set,d=o.exports,m=new Promise(((e,t)=>{_=t,c=e}));m[t]=d,m[e]=e=>(s&&e(s),p.forEach(e),m.catch((e=>{}))),o.exports=m,a((o=>{var a;l=(o=>o.map((o=>{if(null!==o&&"object"==typeof o){if(o[e])return o;if(o.then){var a=[];a.d=0,o.then((e=>{i[t]=e,n(a)}),(e=>{i[r]=e,n(a)}));var i={};return i[e]=e=>e(a),i}}var s={};return s[e]=e=>{},s[t]=o,s})))(o);var i=()=>l.map((e=>{if(e[r])throw e[r];return e[t]})),c=new Promise((t=>{(a=()=>t(i)).r=0;var r=e=>e!==s&&!p.has(e)&&(p.add(e),e&&!e.d&&(a.r++,e.push(a)));l.map((t=>t[e](r)))}));return a.r?c:i()}),(e=>(e?_(m[r]=e):c(d),n(s)))),s&&(s.d=0)},l.d=(e,t)=>{for(var r in t)l.o(t,r)&&!l.o(e,r)&&Object.defineProperty(e,r,{enumerable:!0,get:t[r]})},l.f={},l.e=e=>Promise.all(Object.keys(l.f).reduce(((t,r)=>(l.f[r](e,t),t)),[])),l.u=e=>e+".js",l.g=function(){if("object"==typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"==typeof window)return window}}(),l.o=(e,t)=>Object.prototype.hasOwnProperty.call(e,t),o={},a="web:",l.l=(e,t,r,n)=>{if(o[e])o[e].push(t);else{var i,s;if(void 0!==r)for(var c=document.getElementsByTagName("script"),_=0;_<c.length;_++){var p=c[_];if(p.getAttribute("src")==e||p.getAttribute("data-webpack")==a+r){i=p;break}}i||(s=!0,(i=document.createElement("script")).charset="utf-8",i.timeout=120,l.nc&&i.setAttribute("nonce",l.nc),i.setAttribute("data-webpack",a+r),i.src=e),o[e]=[t];var d=(t,r)=>{i.onerror=i.onload=null,clearTimeout(m);var n=o[e];if(delete o[e],i.parentNode&&i.parentNode.removeChild(i),n&&n.forEach((e=>e(r))),t)return t(r)},m=setTimeout(d.bind(null,void 0,{type:"timeout",target:i}),12e4);i.onerror=d.bind(null,i.onerror),i.onload=d.bind(null,i.onload),s&&document.head.appendChild(i)}},l.r=e=>{"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},(()=>{var e;l.g.importScripts&&(e=l.g.location+"");var t=l.g.document;if(!e&&t&&(t.currentScript&&(e=t.currentScript.src),!e)){var r=t.getElementsByTagName("script");if(r.length)for(var n=r.length-1;n>-1&&!e;)e=r[n--].src}if(!e)throw new Error("Automatic publicPath is not supported in this browser");e=e.replace(/#.*$/,"").replace(/\?.*$/,"").replace(/\/[^\/]+$/,"/"),l.p=e})(),(()=>{l.b=document.baseURI||self.location.href;var e={179:0};l.f.j=(t,r)=>{var n=l.o(e,t)?e[t]:void 0;if(0!==n)if(n)r.push(n[2]);else{var o=new Promise(((r,o)=>n=e[t]=[r,o]));r.push(n[2]=o);var a=l.p+l.u(t),i=new Error;l.l(a,(r=>{if(l.o(e,t)&&(0!==(n=e[t])&&(e[t]=void 0),n)){var o=r&&("load"===r.type?"missing":r.type),a=r&&r.target&&r.target.src;i.message="Loading chunk "+t+" failed.\n("+o+": "+a+")",i.name="ChunkLoadError",i.type=o,i.request=a,n[1](i)}}),"chunk-"+t,t)}};var t=(t,r)=>{var n,o,[a,i,s]=r,c=0;if(a.some((t=>0!==e[t]))){for(n in i)l.o(i,n)&&(l.m[n]=i[n]);s&&s(l)}for(t&&t(r);c<a.length;c++)o=a[c],l.o(e,o)&&e[o]&&e[o][0](),e[o]=0},r=this.webpackChunkweb=this.webpackChunkweb||[];r.forEach(t.bind(null,0)),r.push=t.bind(null,r.push.bind(r))})(),l(541)})()));
//# sourceMappingURL=web.js.map