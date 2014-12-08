(function(define){
define(function(){return function(vars){
with(vars||{}) {
return "<div class=\"overlay\"><div id=\"splash\"><img src=\"img/logo.png\" id=\"splash-logo\" /><h1>${project.version}</h1></div></div>"; 
}};
});})(typeof define=="function"?
define:
function(factory){module.exports=factory.apply(this, deps.map(require));});
